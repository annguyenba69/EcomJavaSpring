package com.shop.category;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
	
	@Query("SELECT c FROM Category c WHERE c.enabled = true ORDER BY c.name ASC")
	public List<Category> findAllEnabled();
	
	@Query("SELECT c FROM Category c WHERE c.enabled = true AND c.alias = ?1")
	public Category findByAliasEnabled(String alias);
	
	@Query("SELECT c FROM Category c WHERE c.enabled = true AND c.allParentIDs LIKE %?1%")
	public List<Category> findChildrenCategories(String parentId);
	
	@Query("SELECT c FROM Category c WHERE c.enabled = true AND c.parent is NULL")
	public List<Category> findParentCategories();

}
