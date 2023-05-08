package com.shop.admin.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Question;

public interface QuestionRepository extends CrudRepository<Question, Integer>, PagingAndSortingRepository<Question, Integer>{

	
	@Query("SELECT q FROM Question q WHERE q.questionContent LIKE %?1% OR"
			+ " q.product.name LIKE %?1% OR"
			+ " q.answer LIKE %?1% OR"
			+ " CONCAT(q.asker.firstName, ' ', q.asker.lastName) LIKE %?1%")
	public Page<Question> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE Question q SET q.approved = ?2 WHERE q.id = ?1")
	@Modifying
	public void updateApprovedStatus(Integer id, boolean status);
	
	public long countById(Integer id);
}
