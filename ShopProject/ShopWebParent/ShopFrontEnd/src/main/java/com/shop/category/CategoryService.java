package com.shop.category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Brand;
import com.shop.common.entity.Category;

@Service
public class CategoryService {

	@Autowired private CategoryRepository categoryRepository;
	
	public List<Category> listCategoriesNoChildren(){
		List<Category> listCategoriesNoChildren = new ArrayList<>();
		List<Category> listEnabledCategories = categoryRepository.findAllEnabled();
		for(Category category : listEnabledCategories) {
			Set<Category> children = category.getChildren();
			if(children == null || children.size() == 0) {
				listCategoriesNoChildren.add(category);
			}
		}
		return listCategoriesNoChildren;
	}
	
	public Category getCategoryByAlias(String alias) throws CategoryNotFoundException {
		Category category = categoryRepository.findByAliasEnabled(alias);
		if(category == null) {
			throw new CategoryNotFoundException("Could not found category with alias: "+ alias);
		}
		return category;
	}
	
	public List<Category> getCategoryParents(Category child){
		List<Category> listParents = new ArrayList<>();
		Category parent = child.getParent();
		
		while(parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent();
		}
		listParents.add(child);
		return listParents;
	}
	
	public List<Category> getCategoryChildren(Category category){
		String parentId = "-" + category.getId() + "-";
		List<Category> listChildrenCategories = categoryRepository.findChildrenCategories(parentId);
		listChildrenCategories.add(category);
		return listChildrenCategories;
	}
	
	public List<Category> getChildrenCategoryByAlias(String alias) throws CategoryNotFoundException{
		Category category = categoryRepository.findByAliasEnabled(alias);
		if(category == null) {
			throw new CategoryNotFoundException("Could not find category with alias: "+ alias);
		}
		String parentId = "-" + category.getId() + "-";
		return categoryRepository.findChildrenCategories(parentId);
	}
	
	public List<Category> findParentCategories(){
		return categoryRepository.findParentCategories();
	}
	
	public static Category getParentOfSubCategory(Category subCategory) {
		Category parentCategory = subCategory.getParent();
		if(parentCategory != null) {
			getParentOfSubCategory(parentCategory);
		}
		return parentCategory;
	}
	
	public Set<Brand> getAllBrandByCategory(Category category){
		Set<Brand> brands = new HashSet();
		List<Category> listChildrenCategories = getCategoryChildren(category);
		for(Category cat : listChildrenCategories) {
			brands.addAll(cat.getBrands());
		}
		return brands;
	}
	
	
}
