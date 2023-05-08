package com.shop.admin.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.FileUploadUtil;
import com.shop.admin.security.ShopUserDetails;
import com.shop.common.entity.User;

@Controller
public class AccountController {
	
	@Autowired private UserService userService;
	
	@GetMapping("/account")
	public String account(@AuthenticationPrincipal ShopUserDetails loggedUser,
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			String email = loggedUser.getUsername();
			User user = userService.getByEmail(email);
			model.addAttribute("user", user);
			return "users/account_form";
		}catch(UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@PostMapping("/account/save")
	public String updateAccount(User user, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal ShopUserDetails loggedUser,
			@RequestParam(name = "image") MultipartFile multipartFile) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.updateAccount(user);
			
			String uploadDir = "user-photos/" +savedUser.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			userService.updateAccount(user);
		}
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		loggedUser.setPhotos(user.getPhotos());
		redirectAttributes.addFlashAttribute("message", "Your Account Details Have Been Updated");
		return "redirect:/account";
	}
}
