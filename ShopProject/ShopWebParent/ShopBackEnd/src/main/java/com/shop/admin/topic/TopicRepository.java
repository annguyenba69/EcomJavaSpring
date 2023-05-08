package com.shop.admin.topic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Topic;

public interface TopicRepository extends CrudRepository<Topic, Integer>, PagingAndSortingRepository<Topic, Integer> {
	
	@Query("SELECT t FROM Topic t WHERE t.name LIKE %?1%")
	public Page<Topic> findAll(String keyword, Pageable pageable);
	
	public long countById(Integer id);
	
	public Topic findByName(String name);
	
	public Topic findByAlias(String alias);
	
	@Query("UPDATE Topic t SET t.enabled = ?2 WHERE t.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
}
