package com.shop.article;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Article;
import com.shop.common.entity.Topic;

@Service
public class ArticleService {
	public static final int ARTICLES_PER_PAGE = 4;
	@Autowired private ArticleRepository articleRepository;
	
	public Page<Article> listByPage(Integer pageNum, String sortDir, String sortField, String keyword){
		Sort sort = Sort.by(sortField);
		sort  = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, ARTICLES_PER_PAGE, sort);
		if(keyword != null) {
			return articleRepository.findAllWithPublished(keyword, pageable);
		}
		return articleRepository.findAllWithPublished(pageable);
	}
	
	public Article getByAlias(String alias) throws ArticleNotFoundException {
		try {
			return articleRepository.findByAlias(alias);
		}catch(NoSuchElementException ex) {
			throw new ArticleNotFoundException(ex.getMessage());
		}
	}
	
	public List<Article> findNextArticle(Integer id){
		Pageable pageable = PageRequest.of(0, 1);
		return articleRepository.findNextArticle(id, pageable);
	}
	
	public List<Article> findPreviousArticle(Integer id){
		Pageable pageable = PageRequest.of(0, 1);
		return articleRepository.findPreviousArticle(id, pageable);
	}
	
	public List<Article> findLimitArticleByTopic(Topic topic, Integer limit){
		Pageable pageable = PageRequest.of(0, limit);
		return articleRepository.findLimitArticleByTopic(topic, pageable);
	}
}
