package com.shop.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.banner.BannerService;
import com.shop.category.CategoryNotFoundException;
import com.shop.category.CategoryService;
import com.shop.common.entity.Banner;
import com.shop.common.entity.Brand;
import com.shop.common.entity.Category;
import com.shop.common.entity.Product;
import com.shop.common.entity.Review;
import com.shop.review.ReviewService;

@Controller
public class ProductController {

	@Autowired private ProductService productService;
	
	@Autowired private CategoryService categoryService;
	
	@Autowired private BannerService bannerService;
	
	@Autowired private ReviewService reviewService;
	
	
	@GetMapping("/c/{category_alias}")
	public String listFirstPage(@PathVariable(name = "category_alias") String alias, Model model) {
		return listByPage(alias, 1, "asc", "id", null, null ,model);
	}
	
	@GetMapping("/c/{category_alias}/{pageNum}")
	public String listByPage(@PathVariable(name = "category_alias") String alias,
			@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortDir") String sortDir,
			@Param("sortField") String sortField,
			@Param("brand[]") String brand,
			@Param("filterPrice") Integer filterPrice,
			Model model) {
		try {
			Category category = categoryService.getCategoryByAlias(alias);
			List<Integer> integerSelectedBrand = getListIntBrand(brand);
			Page<Product> page = productService.listByCategory(pageNum, category.getId(), sortDir, sortField, integerSelectedBrand, filterPrice);
			List<Product> products = page.getContent();
			List<Category> listCategoriesParent = categoryService.getCategoryParents(category);
			List<Category> listChildrenCategories = categoryService.listCategoriesNoChildren();
			List<Banner> banners = bannerService.findAll();
			Set<Brand> listBrandsByCategory = categoryService.getAllBrandByCategory(category);
			Category parentOfSubCategory = categoryService.getParentOfSubCategory(category);
			long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
			long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
			if(endCount > page.getTotalElements()) {
				endCount = page.getTotalElements();
			}
			model.addAttribute("products", products);
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalElements", page.getTotalElements());
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("alias", alias);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("sortField", sortField);
			model.addAttribute("listChildrenCategories", listChildrenCategories);
			model.addAttribute("listCategoriesParent",listCategoriesParent);
			model.addAttribute("banners", banners);
			model.addAttribute("parentOfSubCategory", parentOfSubCategory);
			model.addAttribute("listBrandsByCategory", listBrandsByCategory);
			model.addAttribute("integerSelectedBrand", integerSelectedBrand);
			model.addAttribute("brand", brand);
			model.addAttribute("filterPrice", filterPrice);
			return "products/products_by_category";
		}catch(CategoryNotFoundException ex) {
			return "error/404";
		}
	}
	
	private List<Integer> getListIntBrand(String brand) {
		if(brand != null) {
			String[] stringSelectedBrand = brand.split(",");
			List<Integer> integerSelectedBrand = new ArrayList<>();
			for(int i =0; i< stringSelectedBrand.length; i++) {
				integerSelectedBrand.add(Integer.parseInt(stringSelectedBrand[i]));
			}
			return integerSelectedBrand;
		}else {
			return null;
		}
	}
	
	@GetMapping("/p/{product_alias}")
	public String viewProductDetail(@PathVariable(name = "product_alias") String alias, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.findByAlias(alias);
			List<Product> relatedProduct = productService.findByCategory(product.getCategory());
			List<Review> reviews = reviewService.findAllEnabled(product);
			List<Category> listCategoriesParent = categoryService.getCategoryParents(product.getCategory());
			model.addAttribute("product", product);
			model.addAttribute("reviews", reviews);
			model.addAttribute("relatedProduct", relatedProduct);
			model.addAttribute("listCategoriesParent",listCategoriesParent);
			return "products/product_detail";
		}catch(ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/home";
		}
	}
	
}
