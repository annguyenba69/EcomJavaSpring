package com.shop.admin.article;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.shop.admin.security.ShopUserDetails;
import com.shop.admin.topic.TopicService;
import com.shop.common.entity.Article;
import com.shop.common.entity.Brand;
import com.shop.common.entity.Topic;
import com.shop.common.entity.User;

@Controller
public class ArticleController {

	@Autowired private ArticleService articleService;
	
	@Autowired private TopicService topicService;
	
	@GetMapping("/articles")
	public String listFirstPage(Model model) {
		return listByPage(1, "id", "asc", null, model);
	}
	
	@GetMapping("/articles/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortDir") String sortDir,
			@Param("sortField") String sortField,
			@Param("keyword") String keyword,
			Model model) {
		Page<Article> page = articleService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Article> articles = page.getContent();
		long startCount = (pageNum - 1) * ArticleService.ARTICLES_PER_PAGE + 1;
		long endCount = startCount + ArticleService.ARTICLES_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("articles", articles);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalElements", page.getTotalElements());
		
		return "articles/articles"; 
	}
	
	@GetMapping("/articles/create")
	public String create(Model model) {
		Article article = new Article();
		List<Topic> topics = topicService.findAll();
		model.addAttribute("article", article);
		model.addAttribute("topics", topics);
		return "articles/article_form";
	}
	
	@PostMapping("/articles/save")
	public String save(Article article, @AuthenticationPrincipal ShopUserDetails currentUser , @RequestParam("fileImage") MultipartFile multipartFile,RedirectAttributes redirectAttributes) throws IOException {
		User user = currentUser.getUser();
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			article.setImage(fileName);
			Article savedArticle = articleService.save(article, user);
			String uploadDir = "../article-image/" + savedArticle.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			articleService.save(article, user);
		}
		redirectAttributes.addFlashAttribute("message", "The Brand has been saved successfully");
		return "redirect:/articles";
	}
	
	@GetMapping("/articles/edit/{id}")
	public String edit(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		try {
			Article article = articleService.get(id);
			model.addAttribute("article", article);
			List<Topic> topics = topicService.findAll();
			model.addAttribute("topics", topics);
			return "articles/article_form";
		}catch(ArticleNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/articles";
		}
	}
	
	@GetMapping("/articles/{id}/enabled/{status}")
	public String updatePublishedStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean status,
			RedirectAttributes redirectAttributes) {
		try {
			articleService.updatePublishedStatus(id, status);
			redirectAttributes.addFlashAttribute("message", "Update Published Success");
		}catch(ArticleNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/articles";
	}
	
	@GetMapping("/articles/detail/{id}")
	public String viewDetail(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Article article = articleService.get(id);
			model.addAttribute("article", article);
			return "articles/article_detail_modal";	
		}catch(ArticleNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/articles";
	}
	
	@GetMapping("/articles/delete/{id}")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			articleService.delete(id);
			redirectAttributes.addFlashAttribute("message", "Delete Success");
		}catch(ArticleNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/articles";
	}
	
}
