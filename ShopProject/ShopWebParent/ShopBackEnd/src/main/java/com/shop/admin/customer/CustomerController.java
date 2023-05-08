package com.shop.admin.customer;

import java.util.Date;
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
import com.shop.admin.user.UserNotFoundException;
import com.shop.admin.user.UserService;
import com.shop.common.entity.Country;
import com.shop.common.entity.Customer;
import com.shop.common.entity.User;

@Controller
public class CustomerController {

	@Autowired private CustomerService customerService;
	
	@Autowired private CountryService countryService;
	
	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		return listByPage(1, "id", "asc", null, model);
	}
	
	@GetMapping("/customers/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			Model model) {
		Page<Customer> page = customerService.listByPage(pageNum, sortDir, sortField, keyword);
		List<Customer> customers = page.getContent();
		long startCount = (pageNum - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;
		long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("desc") ? "asc" : "desc";
		
		model.addAttribute("customers", customers);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("keyword", keyword);
		return "customers/customers";
	}
	
	@GetMapping("/customers/create")
	public String create(Model model) {
		Customer customer = new Customer();
		List<Country> countries = countryService.listAll();
		model.addAttribute("customer", customer);
		model.addAttribute("countries", countries);
		return "customers/customer_form";
	}
	
	@PostMapping("/customers/save")
	public String save(Customer customer, RedirectAttributes redirectAttributes) {
		customer.setCreatedTime(new Date());
		customerService.save(customer);
		redirectAttributes.addFlashAttribute("message", "Save Customer Successfull");
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/edit/{id}")
	public String edit(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Customer customer = customerService.get(id);
			List<Country> countries = countryService.listAll();
			model.addAttribute("customer", customer);
			model.addAttribute("countries", countries);
			return "customers/customer_form";
		}catch(CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/customers";
		}
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean status,
			RedirectAttributes redirectAttributes) {
		try {
			customerService.updateEnabledStatus(id, status);
			redirectAttributes.addFlashAttribute("message", "Update Enabled Status Success");
		}catch(CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewDetailUser(@PathVariable(name = "id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Customer customer = customerService.get(id);
			model.addAttribute("customer", customer);
			return "customers/customer_detail_modal";
		}catch(CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/customers";
		}
	}
	
	@GetMapping("/customers/delete/{id}")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			customerService.delete(id);
			redirectAttributes.addFlashAttribute("message", "Delete Customer Success");
		}catch(CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/customers";
	}
	
}
