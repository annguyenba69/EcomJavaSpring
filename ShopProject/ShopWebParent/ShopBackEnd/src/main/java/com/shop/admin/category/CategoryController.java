	package com.shop.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.FileUploadUtil;
import com.shop.common.entity.Category;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public String listFirstPage(Model model) {
		return listByPage(1, "asc", null, "name", model);
	}

	@GetMapping("/categories/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword, @Param("sortField") String sortField, Model model) {
		CategoryPageInfo categoryPageInfo = new CategoryPageInfo();
		List<Category> categories = categoryService.listByPage(categoryPageInfo, pageNum, sortDir, keyword);
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		long startCount = (pageNum - 1) * CategoryService.CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.CATEGORIES_PER_PAGE - 1;
		if (endCount > categoryPageInfo.getTotalElements()) {
			endCount = categoryPageInfo.getTotalElements();
		}

		model.addAttribute("totalPages", categoryPageInfo.getTotalPages());
		model.addAttribute("totalElements", categoryPageInfo.getTotalElements());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("categories", categories);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);

		return "categories/categories";
	}

	@GetMapping("/categories/create")
	public String createCategory(Model model) {
		Category category = new Category();
		List<Category> listCategoriesUsedInForm = categoryService.listCategoriesUsedInForm();
		model.addAttribute("category", category);
		model.addAttribute("listCategoriesUsedInForm", listCategoriesUsedInForm);
		return "categories/category_form";
	}

	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam(name = "fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);

			Category savedCategory = categoryService.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			categoryService.save(category);
		}
		redirectAttributes.addFlashAttribute("message", "The Category has been saved successfully");
		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Category category = categoryService.get(id);
			List<Category> listCategoriesUsedInForm = categoryService.listCategoriesUsedInForm();
			model.addAttribute("category", category);
			model.addAttribute("listCategoriesUsedInForm", listCategoriesUsedInForm);
			return "categories/category_form";
		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories";
		}
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			categoryService.delete(id);
			String categoryDir = "../category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);
			redirectAttributes.addFlashAttribute("message", "Delete Category Successfull");
		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		} catch (CategoryParentException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/categories";
	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean status, RedirectAttributes redirectAttributes) {
		try {
			categoryService.updateEnabledStatus(status, id);
			redirectAttributes.addFlashAttribute("message", "The Category has been updated successfully");
		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/categories";
	}

	@GetMapping("/categories/detail/{id}")
	public String viewDetail(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes,
			Model model) {
		try {
			Category category = categoryService.get(id);
			model.addAttribute("category", category);
			return "categories/category_detail_modal";
		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories";
		}
	}
}
