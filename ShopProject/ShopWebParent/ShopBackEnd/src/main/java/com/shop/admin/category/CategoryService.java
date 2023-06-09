package com.shop.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Category;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
	public static final int CATEGORIES_PER_PAGE = 2;

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> listByPage(CategoryPageInfo categoryPageInfo, Integer pageNum, String sortDir,
			String keyword){
		Sort sort = Sort.by("name");
		
		if(sortDir.equals("asc")) {
			sort = sort.ascending();
		}else {
			sort = sort.descending();
		}
		
		Pageable pageable = PageRequest.of(pageNum - 1, CATEGORIES_PER_PAGE, sort);
		Page<Category> page = null;
		if(keyword != null && !keyword.isEmpty()) {
			page = categoryRepository.search(keyword, pageable);
		}else {
			page = categoryRepository.findRootCategories(pageable);
		}
		List<Category> rootCategories = page.getContent();
		categoryPageInfo.setTotalPages(page.getTotalPages());
		categoryPageInfo.setTotalElements(page.getTotalElements());
		
		if(keyword != null && !keyword.isEmpty()) {
			List<Category> searchResult = page.getContent();
			for(Category category : searchResult) {
				category.setHasChildren(category.getChildren().size() > 0);
			}
			return searchResult;
		}else {
			return listHierarchicalCategories(rootCategories, sortDir);
		}
	}

	public List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
		List<Category> listHierarchicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {
			listHierarchicalCategories.add(Category.copyFull(rootCategory));

			Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

			for (Category subCategory : children) {
				String name = "--/" + subCategory.getName();
				listHierarchicalCategories.add(Category.copyFull(subCategory, name));

				listSubHierarchicalCategories(listHierarchicalCategories, subCategory, 1, sortDir);
			}
		}
		return listHierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> listHierarchicalCategories, Category parent, int subLevel,
			String sortDir) {
		Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
		int newSubLevel = subLevel + 1;
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--/";
			}
			name += subCategory.getName();
			listHierarchicalCategories.add(Category.copyFull(subCategory, name));
			listSubHierarchicalCategories(listHierarchicalCategories, subCategory, newSubLevel, sortDir);
		}
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> listCategoriesUsedInForm = new ArrayList<>();

		Iterable<Category> rootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
		for (Category category : rootCategories) {
			listCategoriesUsedInForm.add(Category.copyIdAndName(category));

			Set<Category> children = sortSubCategories(category.getChildren());
			for (Category subCategory : children) {
				String name = "--/" + subCategory.getName();
				listCategoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
				listChildren(listCategoriesUsedInForm, subCategory, 1);
			}
		}
		return listCategoriesUsedInForm;
	}

	private void listChildren(List<Category> listCategoriesUsedInForm, Category parent, int subLevel) {
		Set<Category> children = parent.getChildren();
		int newSubLevel = subLevel + 1;

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			listCategoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
			listChildren(listCategoriesUsedInForm, subCategory, newSubLevel);
		}
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children) {
		return sortSubCategories(children, "asc");
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
		Comparator<Category> comparator = new Comparator<Category>() {

			@Override
			public int compare(Category c1, Category c2) {
				if (sortDir.equals("asc")) {
					return c1.getName().compareTo(c2.getName());
				} else {
					return c2.getName().compareTo(c1.getName());
				}
			}
		};
		SortedSet<Category> sortedChildren = new TreeSet<>(comparator);
		sortedChildren.addAll(children);
		return sortedChildren;
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id ==0);
		Category categoryByName = categoryRepository.findByName(name);
		if(isCreatingNew) {
			if(categoryByName != null) {
				return "DuplicateName";
			}else {
				Category categoryByAlias = categoryRepository.findByAlias(alias);
				if(categoryByAlias != null) {
					return "DuplicateAlias";
				}
			}
		}else {
			if(categoryByName != null && categoryByName.getId() != id) {
				return "DuplicateName";
			}
			Category categoryByAlias = categoryRepository.findByAlias(alias);
			if(categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}
		return "Ok";
	}

	public Category save(Category category) {
		Category parent = category.getParent();
		if(parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId()) + "-";
			category.setAllParentIDs(allParentIds);
		}
		return categoryRepository.save(category);
	}
	
	public void delete(Integer id) throws CategoryNotFoundException, CategoryParentException {
		long countById = categoryRepository.countById(id);
		if(countById == 0) {
			throw new CategoryNotFoundException("Could not find category with id: " +id);
		}
		Category category = categoryRepository.findById(id).get();
		if(category.getChildren().size() > 0) {
			throw new CategoryParentException("Could not delete Parent Category");
		}
		categoryRepository.deleteById(id);
	}
	
	public void updateEnabledStatus(boolean status, Integer id) throws CategoryNotFoundException {
		long countById = categoryRepository.countById(id);
		if(countById == 0) {
			throw new CategoryNotFoundException("Could not find category with id: "+id);
		}
		categoryRepository.updateEnabledStatus(status, id);
	}
	
	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return categoryRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find category with id: " +id);
		}
	}
	
}
