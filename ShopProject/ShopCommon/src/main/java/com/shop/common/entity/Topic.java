package com.shop.common.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CollectionId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "topics")
public class Topic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 256, unique = true)
	private String name;
	
	@Column(nullable = false, length = 256, unique = true)
	private String alias;
	
	@OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
	private Set<Article> articles = new HashSet<>();
	
	private boolean enabled;

	public Topic() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "Topic [id=" + id + ", name=" + name + ", alias=" + alias + ", articles=" + articles + ", enabled="
				+ enabled + "]";
	}

	@Transient
	public String getURL() {
		return "/topics/" + this.alias;
	}
}
