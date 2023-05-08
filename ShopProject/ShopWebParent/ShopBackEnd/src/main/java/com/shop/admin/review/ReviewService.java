package com.shop.admin.review;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Review;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewService {
	public static final int REVIEWS_PER_PAGE = 7;
	
	@Autowired private ReviewRepository reviewRepository;
	
	public Page<Review> findAll(Integer pageNum, String sortDir, String sortField, String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);
		if(keyword != null) {
			return reviewRepository.findAll(keyword, pageable);
		}
		return reviewRepository.findAll(pageable);
	}
	
	public Review get(Integer id) throws ReviewNotFoundException {
		try {
			return reviewRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new ReviewNotFoundException("Could not find Review with id: "+id);
		}
	}
	
	public void delete(Integer id) throws ReviewNotFoundException {
		long countById = reviewRepository.countById(id);
		if(countById == 0) {
			throw new ReviewNotFoundException("Could not find Review with id: "+id);
		}
		reviewRepository.deleteById(id);
	}
	
	public void updateEnabledStatus(Integer id, boolean status) throws ReviewNotFoundException {
		long countById = reviewRepository.countById(id);
		if(countById == 0) {
			throw new ReviewNotFoundException("Could not find Review with id: "+id);
		}
		reviewRepository.updateEnabledStatus(id, status);
	}
	
	public void save(Review reviewInForm) throws ReviewNotFoundException {
		Review reviewInDb = reviewRepository.findById(reviewInForm.getId()).get();
		if(reviewInDb == null) {
			throw new ReviewNotFoundException("Could not find Review");
		}
		reviewInDb.setHeadline(reviewInForm.getHeadline());
		reviewInDb.setComment(reviewInForm.getComment());
		reviewInDb.setEnabled(reviewInForm.isEnabled());
		reviewRepository.save(reviewInDb);
	}
	
	
}
