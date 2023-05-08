package com.shop.admin.user;

import java.io.IOException;
import java.util.List;

import javax.swing.plaf.multi.MultiButtonUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.FileUploadUtil;
import com.shop.admin.role.RoleService;
import com.shop.common.entity.Role;
import com.shop.common.entity.User;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

	@Autowired private UserService userService;
	
	@Autowired private RoleService roleService;
	
	@GetMapping("/users")
	public String users(Model model) {
		return listByPage(1, "id", "asc", null,model);
	}
	
	@GetMapping("/users/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			Model model) {
		Page<User> page = userService.listAll(pageNum, sortField, sortDir, keyword);
		List<User> users = page.getContent();
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("desc") ? "asc" : "desc";
		
		model.addAttribute("users", users);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("keyword", keyword);
		return "users/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean Status,
			RedirectAttributes redirectAttributes) {
		try {
			userService.updateEnabledStatus(Status, id);
			redirectAttributes.addFlashAttribute("message", "User Status Update Successful");
		}catch(UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}
	
	@GetMapping("/users/create")
	public String createUser(Model model) {
		User user = new User();
		user.setEnabled(true);
		List<Role> roles = roleService.listAll();
		
		model.addAttribute("user", user);
		model.addAttribute("roles", roles);
		return "users/user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user,
			@RequestParam(name = "image") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			userService.save(user);
		}
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
		return "redirect:/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id,
			RedirectAttributes redirectAttributes,
			Model model) {
		try {
			User user = userService.get(id);
			List<Role> roles = roleService.listAll();
			model.addAttribute("user", user);
			model.addAttribute("roles", roles);
			return "users/user_form";
		}catch(UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id,
			RedirectAttributes redirectAttributes) {
		try {
			userService.delete(id);
			redirectAttributes.addFlashAttribute("message", "Delete User Successful");
		}catch(UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}
	
	@GetMapping("/users/detail/{id}")
	public String viewDetailUser(@PathVariable(name = "id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			User user = userService.get(id);
			model.addAttribute("user", user);
			return "users/user_detail_modal";
		}catch(UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<User> listUsers = userService.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = userService.listAll();
		
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = userService.listAll();
		
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}	
}
