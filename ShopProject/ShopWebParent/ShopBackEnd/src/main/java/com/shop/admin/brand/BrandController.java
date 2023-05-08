package com.shop.admin.brand;

import java.io.IOException;
import java.util.List;

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
import com.shop.admin.category.CategoryService;
import com.shop.common.entity.Brand;
import com.shop.common.entity.Category;

@Controller
public class BrandController {

	@Autowired private BrandService brandService;

	@Autowired private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		return listByPage(1, "id", "asc", null, model);
	}
	
	@GetMapping("/brands/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			Model model) {
		Page<Brand> page = brandService.findAll(pageNum, sortField, sortDir, keyword);
		List<Brand> brands = page.getContent();
		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("brands", brands);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalElements", page.getTotalElements());
		
		return "brands/brands"; 
	}
	
	@GetMapping("/brands/create")
	public String createBrand(Model model) {
		Brand brand = new Brand();
		List<Category> listCategoriesUsedInForm = categoryService.listCategoriesUsedInForm();
		model.addAttribute("brand", brand);
		model.addAttribute("listCategoriesUsedInForm", listCategoriesUsedInForm);
		return "brands/brand_form";
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, @RequestParam("image") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			Brand savedBrand = brandService.save(brand);
			String uploadDir = "../brand-logos/" + savedBrand.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			brandService.save(brand);
		}
		redirectAttributes.addFlashAttribute("message", "The Brand has been saved successfully");
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategoriesUsedInForm = categoryService.listCategoriesUsedInForm();
			model.addAttribute("brand", brand);
			model.addAttribute("listCategoriesUsedInForm", listCategoriesUsedInForm);
			return "brands/brand_form";
		}catch(BrandNotFoundException ex) {
			redirectAttributes.addAttribute("message", ex.getMessage());
			return "redirect:/brands";
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			brandService.delete(id);
			String brandDir = "../brand-logos/" + id;
			FileUploadUtil.removeDir(brandDir);
			redirectAttributes.addFlashAttribute("message", "Delete Brand Successfull");
		}catch(BrandNotFoundException ex) {
			redirectAttributes.addAttribute("message", ex.getMessage());
		}
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/detail/{id}")
	public String viewDetail(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Brand brand = brandService.get(id);
			model.addAttribute("brand", brand);
			return "brands/brand_detail_modal";
		}catch(BrandNotFoundException ex) {
			redirectAttributes.addAttribute("message", ex.getMessage());
			return "redirect:/brands";
		}
	}
	
}
