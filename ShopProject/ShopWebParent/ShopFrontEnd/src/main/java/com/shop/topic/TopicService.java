package com.shop.topic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Topic;

@Service
public class TopicService {

	@Autowired private TopicRepository topicRepository;
	
	public List<Topic> findAll(){
		return topicRepository.findAll();
	}
	
}
