package com.shop.admin.banner;

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
import com.shop.admin.brand.BrandService;
import com.shop.common.entity.Banner;
import com.shop.common.entity.Brand;

@Controller
public class BannerController {
	
	@Autowired private BannerService bannerService;
	
	@GetMapping("/banners")
	public String listFirstPage(Model model) {
		return listByPage(1, "title", "asc", null, model);
	}
	
	@GetMapping("/banners/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			Model model) {
		Page<Banner> page = bannerService.findAll(pageNum, sortField, sortDir, keyword);
		List<Banner> banners = page.getContent();
		long startCount = (pageNum - 1) * BannerService.BANNERS_PER_PAGE + 1;
		long endCount = startCount + BannerService.BANNERS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("banners", banners);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalElements", page.getTotalElements());
		
		return "banners/banners"; 
	}
	
	@GetMapping("/banners/create")
	public String create(Model model) {
		Banner banner = new Banner();
		model.addAttribute("banner", banner);
		return "banners/banner_form";
	}
	
	@GetMapping("/banners/edit/{id}")
	public String edit(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Banner banner = bannerService.get(id);
			model.addAttribute("banner", banner);
			return "banners/banner_form";
		}catch(BannerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/banners";
		}
	}
	
	@PostMapping("/banners/save")
	public String saveBrand(Banner banner, @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			banner.setImage(fileName);
			Banner savedBanner = bannerService.save(banner);
			String uploadDir = "../banner/" + savedBanner.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			bannerService.save(banner);
		}
		redirectAttributes.addFlashAttribute("message", "The Banner has been saved successfully");
		return "redirect:/banners";
	}
	
	@GetMapping("/banners/delete/{id}")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			bannerService.delete(id);
		}catch(BannerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/banners";
	}
	
	@GetMapping("/banners/detail/{id}")
	public String viewDetail(@PathVariable(name = "id") Integer id, Model model,  RedirectAttributes redirectAttributes) {
		try {
			Banner banner = bannerService.get(id);
			model.addAttribute("banner", banner);
			return "banners/banner_detail_modal";
		}catch(BannerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/banners";
		}
	}
	
	@GetMapping("/banners/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean status, RedirectAttributes redirectAttributes) {
		try {
			bannerService.updateEnabledStatus(id, status);
			redirectAttributes.addFlashAttribute("message", "The Banner has been saved successfully");
		}catch(BannerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/banners";
	}
}
