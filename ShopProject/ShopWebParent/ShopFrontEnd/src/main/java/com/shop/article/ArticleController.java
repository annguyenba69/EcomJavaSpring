package com.shop.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.common.entity.Article;
import com.shop.common.entity.Topic;
import com.shop.topic.TopicService;

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
		List<Topic> topics = topicService.findAll();
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
		model.addAttribute("topics", topics);
		
		return "articles/articles"; 
	}
	
	@GetMapping("/articles/detail/{alias}")
	public String detail(@PathVariable(name = "alias") String alias,
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Article article = articleService.getByAlias(alias);
			List<Article> nextArticle = articleService.findNextArticle(article.getId());
			List<Article> previousArticle = articleService.findPreviousArticle(article.getId());
			List<Article> listLimitByTopic = articleService.findLimitArticleByTopic(article.getTopic(), 2);
			model.addAttribute("article", article);
			model.addAttribute("nextArticle", nextArticle);
			model.addAttribute("previousArticle", previousArticle);
			model.addAttribute("listLimitByTopic", listLimitByTopic);
			return "articles/article_detail";
		}catch(ArticleNotFoundException ex) {
			return "redirect:/error/404";
		}
	}
}
