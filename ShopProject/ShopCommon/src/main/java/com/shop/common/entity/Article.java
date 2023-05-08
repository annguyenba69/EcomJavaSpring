package com.shop.common.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "articles")
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 256)
	private String title;

	@Column(name = "image", nullable = false)
	private String image;

	@Column(nullable = false, length = 4096)
	private String content;

	@Column(nullable = false, length = 256)
	private String alias;

	@Column(name = "created_time")
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
	private Date createdTime;
	
	@Column(name = "updated_time")
	private Date updatedTime;

	private boolean published;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "topic_id")
	private Topic topic;

	public Article() {

	}
	
	@Transient
	private Integer countTotalArticleByTopic;
	
	public Article(Topic topic, Integer countTotalArticleByTopic) {
		this.topic = topic;
		this.countTotalArticleByTopic = countTotalArticleByTopic;
	}

	public Article(Integer id, String title, String image, User user, Date updatedTime,
			boolean published, Date createdTime, Topic topic) {
		super();
		this.id = id;
		this.title = title;
		this.image = image;
		this.updatedTime = updatedTime;
		this.published = published;
		this.user = user;
		this.createdTime = createdTime;
		this.topic = topic;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	
	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	

	public Integer getCountTotalArticleByTopic() {
		return countTotalArticleByTopic;
	}

	public void setCountTotalArticleByTopic(Integer countTotalArticleByTopic) {
		this.countTotalArticleByTopic = countTotalArticleByTopic;
	}

	@Transient
	public String getImagePath() {
		if (this.id == null) return "/default-images/default-thumbnail.jpg";
		
		
		return "/article-image/" + this.id + "/" + this.image;		
	}
	
	@Transient
	public String getURL() {
		return "/articles/detail/" + this.alias;
	}
	
	@Transient
	public String getIntro() {
		if(content.length() > 300) {
			return content.substring(0, 300).concat("...");
		}
		return content;
	}
	
	@Transient
	public String getShortTitle() {
		if(title.length() > 50) {
			return title.substring(0, 50).concat("...");
		}
		return title;
	}

	public Article(Integer id, String title, String image, String content, String alias, Date createdTime,
			Date updatedTime, boolean published, Topic topic, Integer countTotalArticleByTopic) {
		super();
		this.id = id;
		this.title = title;
		this.image = image;
		this.content = content;
		this.alias = alias;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
		this.published = published;
		this.topic = topic;
		this.countTotalArticleByTopic = countTotalArticleByTopic;
	}
		
	
}
