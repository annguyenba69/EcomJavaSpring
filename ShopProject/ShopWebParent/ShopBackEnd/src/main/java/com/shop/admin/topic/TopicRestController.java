package com.shop.admin.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicRestController {

	@Autowired private TopicService topicService;
	
	@PostMapping("/topics/checkUnique")
	public String checkUniqueNameAndAlias(@Param("id") Integer id, @Param("name") String name, @Param("alias") String alias) {
		return topicService.checkUniqueNameAndAlias(id, name, alias);
	}
	
}
