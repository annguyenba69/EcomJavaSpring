package com.shop.topic;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.Topic;

public interface TopicRepository extends CrudRepository<Topic, Integer> {

	@Query("SELECT t FROM Topic t WHERE t.enabled = true")
	public List<Topic> findAll();
}
