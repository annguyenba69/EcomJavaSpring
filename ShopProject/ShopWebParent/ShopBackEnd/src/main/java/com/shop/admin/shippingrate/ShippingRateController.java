package com.shop.admin.shippingrate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.country.CountryService;
import com.shop.common.entity.Country;
import com.shop.common.entity.ShippingRate;

@Controller
public class ShippingRateController {

	@Autowired private ShippingRateService shippingRateService;
	
	@Autowired private CountryService countryService;
	
	@GetMapping("/shippingrates")
	public String listFirstPage(Model model) {
		return listByPage(1, "country", "asc", null, model);
	}
	
	@GetMapping("/shippingrates/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			Model model) {
		Page<ShippingRate> page = shippingRateService.listByPage(pageNum, sortField, sortDir, keyword);
		List<ShippingRate> shippingRates = page.getContent();
		long startCount = (pageNum - 1) * ShippingRateService.SHIPPINGRATES_PER_PAGE + 1;
		long endCount = startCount + ShippingRateService.SHIPPINGRATES_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("shippingRates", shippingRates);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("endCount", endCount);
		return "shippingrates/shippingrates";
	}
	
	@GetMapping("/shippingrates/create")
	public String create(Model model) {
		ShippingRate shippingRate = new ShippingRate();
		List<Country> countries = countryService.listAll();
		model.addAttribute("shippingrate", shippingRate);
		model.addAttribute("countries", countries);
		return "shippingrates/shippingrate_form";
	}
	
	@PostMapping("/shippingrates/save")
	public String save(ShippingRate shippingRate, Model model, RedirectAttributes redirectAttributes) {
		try {
			shippingRateService.save(shippingRate);
			redirectAttributes.addFlashAttribute("message", "Save Shipping Rates Successfull");
		}catch(ShippingRateAlreadyExistsException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/shippingrates";
	}
	
	@GetMapping("/shippingrates/edit/{id}")
	public String edit(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			ShippingRate shippingRate = shippingRateService.get(id);
			List<Country> countries = countryService.listAll();
			model.addAttribute("countries", countries);
			model.addAttribute("shippingrate", shippingRate);
			return "shippingrates/shippingrate_form";
		}catch(ShippingRateException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/shippingrates";
		}
	}
	
	@GetMapping("/shippingrates/{id}/enabled/{status}")
	public String updateCodSupported(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean status,
			RedirectAttributes redirectAttributes) {
		try {
			shippingRateService.updateCODSupported(id, status);
			redirectAttributes.addFlashAttribute("message", "Update Cod Supported Successfull");
		}catch(ShippingRateException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/shippingrates";
	}
	
	@GetMapping("/shippingrates/detail/{id}")
	public String viewDetail(@PathVariable(name = "id") Integer id, Model model ,RedirectAttributes redirectAttributes) {
		try {
			ShippingRate shippingRate = shippingRateService.get(id);
			model.addAttribute("shippingRate", shippingRate);
			return "shippingrates/shippingrate_detail_modal";
		}catch(ShippingRateException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/shippingrates";
		}
	}
	
	
}
