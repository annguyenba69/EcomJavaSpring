package com.shop.admin.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.order.OrderService;
import com.shop.admin.security.ShopUserDetails;
import com.shop.common.entity.Order;
import com.shop.common.entity.Question;

@Controller
public class QuestionController {

	@Autowired private QuestionService questionService;
	
	@GetMapping("/questions")
	public String listFirstPage(Model model) {
		return listByPage(1, "id", "asc", null, model);
	}
	
	@GetMapping("/questions/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, @Param("sortField") String sortField,
			@Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {
		Page<Question> page = questionService.listByPage(pageNum, sortDir, sortField, keyword);
		List<Question> questions = page.getContent();
		long startCount = (pageNum - 1) * QuestionService.QUESTIONS_PER_PAGE + 1;
		long endCount = startCount + QuestionService.QUESTIONS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";


		model.addAttribute("questions", questions);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("endCount", endCount);
		return "questions/questions";
	}
	
	@GetMapping("/questions/detail/{id}")
	public String viewDetail(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Question question = questionService.get(id);
			model.addAttribute("question", question);
			return "questions/question_detail_modal";
		}catch(QuestionNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/questions";
		}
	}
	
	@GetMapping("/questions/edit/{id}")
	public String edit(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Question question = questionService.get(id);
			model.addAttribute("question", question);
			return "questions/question_form";
		}catch(QuestionNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/questions";
		}
	}
	
	@PostMapping("/questions/save")
	public String save(Question question, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal ShopUserDetails userDetails) {
		try {
			questionService.save(question, userDetails.getUser());
			redirectAttributes.addFlashAttribute("message", "Save Success");
		}catch(QuestionNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/questions";
	}
	
	@GetMapping("/questions/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean status,
			RedirectAttributes redirectAttributes) {
		try {
			questionService.updateApprovedStatus(id, status);
			redirectAttributes.addFlashAttribute("message", "Save Success");
		}catch(QuestionNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/questions";
	}
	
	@GetMapping("/questions/delete/{id}")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			questionService.delete(id);
			redirectAttributes.addFlashAttribute("message", "Save Success");
		}catch(QuestionNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/questions";
	}
}
