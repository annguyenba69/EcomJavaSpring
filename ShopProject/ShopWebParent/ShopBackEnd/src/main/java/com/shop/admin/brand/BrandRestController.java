package com.shop.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.admin.category.CategoryDTO;
import com.shop.common.entity.Brand;
import com.shop.common.entity.Category;

@RestController
public class BrandRestController {

	@Autowired private BrandService brandService;
	
	@PostMapping("/brands/checkUnique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return brandService.checkUnique(id, name) ? "Ok" : "DuplicatedName";
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer id) throws BrandRestNotFoundException{
		List<CategoryDTO> dtos = new ArrayList<>();
		try {
			Brand brand = brandService.get(id);
			Set<Category> categories = brand.getCategories();
			for(Category category : categories) {
				dtos.add(new CategoryDTO(category.getId(), category.getName()));
			}
			return dtos;
		}catch(BrandNotFoundException ex) {
			throw new BrandRestNotFoundException();
		}
	}
	
}
