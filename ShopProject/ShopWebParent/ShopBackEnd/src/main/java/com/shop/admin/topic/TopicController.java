package com.shop.admin.topic;

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

import com.shop.admin.article.ArticleService;
import com.shop.common.entity.Topic;

@Controller
public class TopicController {

	@Autowired private TopicService topicService;
	
	@GetMapping("/topics")
	public String listFirstPage(Model model) {
		return listByPage(1, "id", "asc", null, model);
	}
	
	@GetMapping("/topics/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			Model model) {
		Page<Topic> page = topicService.findAll(pageNum, sortField, sortDir, keyword);
		List<Topic> topics = page.getContent();
		long startCount = (pageNum - 1) * TopicService.TOPICS_PER_PAGE + 1;
		long endCount = startCount + TopicService.TOPICS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("topics", topics);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalElements", page.getTotalElements());
		
		return "topics/topics"; 
	}
	
	@GetMapping("/topics/create")
	public String create(Model model) {
		Topic topic = new Topic();
		model.addAttribute("topic", topic);
		return "topics/topic_form";
	}
	
	@GetMapping("/topics/edit/{id}")
	public String edit(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Topic topic = topicService.get(id);
			model.addAttribute("topic", topic);
			return "topics/topic_form"; 
		}catch(TopicNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/topics";
		}
	}
	
	@PostMapping("/topics/save")
	public String save(Topic topic, RedirectAttributes redirectAttributes) {
		topicService.save(topic);
		redirectAttributes.addFlashAttribute("message", "Save Topic Success");
		return "redirect:/topics";
	}
	
	@GetMapping("/topics/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean status,
			RedirectAttributes redirectAttributes) {
		try {
			topicService.updateEnabledStatus(id, status);
			redirectAttributes.addFlashAttribute("message", "Update Enabled Success");
		}catch(TopicNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/topics";
	}
	
	
	@GetMapping("/topics/delete/{id}")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			topicService.delete(id);
			redirectAttributes.addFlashAttribute("message", "Delete Success");
		}catch(TopicNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/topics";
	}
	
	@GetMapping("/topics/detail/{id}")
	public String viewDetail(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Topic topic = topicService.get(id);
			model.addAttribute("topic", topic);
			return "topics/topic_detail_modal";
		}catch(TopicNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/topics";
		}
		
	}
}
