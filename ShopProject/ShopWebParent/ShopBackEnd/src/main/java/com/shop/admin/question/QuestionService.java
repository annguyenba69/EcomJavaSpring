package com.shop.admin.question;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.shop.common.entity.Question;
import com.shop.common.entity.User;

import jakarta.transaction.Transactional;

import com.shop.common.entity.Question;

@Service
@Transactional
public class QuestionService {
	public static final int QUESTIONS_PER_PAGE = 10;
	@Autowired private QuestionRepository questionRepository;
	
	public Page<Question> listByPage(Integer pageNum, String sortDir, String sortField, String keyword){
		Sort sort = Sort.by(sortField);
		sort  = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, QUESTIONS_PER_PAGE, sort);
		if(keyword != null) {
			return questionRepository.findAll(keyword, pageable);
		}
		return questionRepository.findAll(pageable);
	}
	
	public Question get(Integer id) throws QuestionNotFoundException {
		try {
			return questionRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new QuestionNotFoundException("Could not find Question with id: "+ id);
		}
	}
	
	public void delete(Integer id) throws QuestionNotFoundException {
		long countById = questionRepository.countById(id);
		if(countById == 0) {
			throw new QuestionNotFoundException("Could not find Question with id: "+ id);
		}
		questionRepository.deleteById(id);
	}
	
	public void updateApprovedStatus(Integer id, boolean status) throws QuestionNotFoundException {
		long countById = questionRepository.countById(id);
		if(countById == 0) {
			throw new QuestionNotFoundException("Could not find Question with id: "+ id);
		}
		questionRepository.updateApprovedStatus(id, status);
	}
	
	public void save(Question questionInForm, User user) throws QuestionNotFoundException {
		try {
			Question questionInDb = questionRepository.findById(questionInForm.getId()).get();
			questionInDb.setQuestionContent(questionInForm.getQuestionContent());
			questionInDb.setApproved(questionInForm.isApproved());
			questionInDb.setAnswer(questionInForm.getAnswer());
			if(questionInDb.isAnswer()) {
				questionInDb.setAnswerTime(new Date());
				questionInDb.setAnswerer(user);
			}
			questionRepository.save(questionInDb);
		}catch(NoSuchElementException ex) {
			throw new QuestionNotFoundException("Could not find any question with ID " + questionInForm.getId());
		}
	}
	

}
