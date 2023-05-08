package com.shop.admin.review;

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

import com.shop.admin.order.OrderNotFoundException;
import com.shop.admin.order.OrderService;
import com.shop.admin.user.UserNotFoundException;
import com.shop.common.entity.Review;
import com.shop.common.entity.User;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@GetMapping("/reviews")
	public String listFirstPage(Model model) {
		return listByPage(1, "id", "asc", null, model);
	}

	@GetMapping("/reviews/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, @Param("sortField") String sortField,
			@Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {
		Page<Review> page = reviewService.findAll(pageNum, sortDir, sortField, keyword);
		List<Review> reviews = page.getContent();
		long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
		long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("reviews", reviews);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("endCount", endCount);
		return "reviews/reviews";
	}
	
	@GetMapping("/reviews/edit/{id}")
	public String edit(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		try {
			Review review = reviewService.get(id);
			model.addAttribute("review", review);
			return "reviews/review_form";
		}catch(ReviewNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/reviews";
		}
	}
	
	@PostMapping("/reviews/save")
	public String save(Review review, RedirectAttributes redirectAttributes) {
		try {
			reviewService.save(review);
			redirectAttributes.addFlashAttribute("message", "Update Success");
		}catch(ReviewNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/reviews";
	}
	
	@GetMapping("/reviews/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean status,
			RedirectAttributes redirectAttributes) {
		try {
			reviewService.updateEnabledStatus(id, status);
			redirectAttributes.addFlashAttribute("message", "User Status Update Successful");
		}catch(ReviewNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/reviews";
	}
	
	@GetMapping("/reviews/detail/{id}")
	public String viewDetailUser(@PathVariable(name = "id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Review review =reviewService.get(id);
			model.addAttribute("review", review);
			return "reviews/review_detail_modal";
		}catch(ReviewNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/reviews";
		}
	}
	
	@GetMapping("/reviews/delete/{id}")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			reviewService.delete(id);
			redirectAttributes.addFlashAttribute("message", "Delete Success");
		}catch(ReviewNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/reviews";
	}
}
