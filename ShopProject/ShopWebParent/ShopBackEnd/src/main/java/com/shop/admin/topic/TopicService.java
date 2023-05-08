package com.shop.admin.topic;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Topic;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TopicService {
	public static final int TOPICS_PER_PAGE = 10;

	@Autowired
	private TopicRepository topicRepository;
	
	public List<Topic> findAll(){
		return  (List<Topic>) topicRepository.findAll();
	}

	public Page<Topic> findAll(Integer pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, TOPICS_PER_PAGE, sort);
		if (keyword != null) {
			return topicRepository.findAll(keyword, pageable);
		}
		return topicRepository.findAll(pageable);
	}

	public Topic get(Integer id) throws TopicNotFoundException {
		try {
			return topicRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new TopicNotFoundException("Could not found Topic with id: " + id);
		}
	}

	public void delete(Integer id) throws TopicNotFoundException {
		long countById = topicRepository.countById(id);
		if (countById == 0) {
			throw new TopicNotFoundException("Could not found Topic with id: " + id);
		}
		topicRepository.deleteById(id);
	}

	public String checkUniqueNameAndAlias(Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);
		Topic topicByName = topicRepository.findByName(name);
		Topic topicByAlias = topicRepository.findByAlias(alias);
		if(isCreatingNew) {
			if(topicByName != null) {
				return "DuplicatedName";
			}else {
				if(topicByAlias != null) {
					return "DuplicatedAlias";
				}
			}
		}else {
			if(topicByName != null && topicByName.getId() != id) {
				return "DuplicatedName";
			}else {
				if(topicByAlias != null && topicByAlias.getId() != id) {
					return "DuplicatedAlias";
				}
			}
		}
		return "Ok";
	}
	
	public Topic save(Topic topic) {
		generateAliasByName(topic);
		return topicRepository.save(topic);
	}
	
	private void generateAliasByName(Topic topic) {
		if(topic.getAlias() == null || topic.getAlias().isEmpty()) {
			String defaultAlias = topic.getName().replace(" ", "-");
			topic.setAlias(defaultAlias);
		}
	}
	
	public void updateEnabledStatus(Integer id, boolean status) throws TopicNotFoundException {
		long countById = topicRepository.countById(id);
		if (countById == 0) {
			throw new TopicNotFoundException("Could not found Topic with id: " + id);
		}
		topicRepository.updateEnabledStatus(id, status);
	}
	
}
