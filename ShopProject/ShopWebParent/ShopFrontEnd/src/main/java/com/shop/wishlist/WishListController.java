package com.shop.wishlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.Helper;
import com.shop.common.entity.Customer;
import com.shop.common.entity.Product;
import com.shop.common.entity.Wishlist;
import com.shop.customer.CustomerNotFoundException;
import com.shop.product.ProductNotFoundException;
import com.shop.product.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class WishListController {

	@Autowired private WishListService wishListService;
	@Autowired private Helper helper;
	@Autowired private ProductService productService;
	
	@GetMapping("/wishlists")
	public String listFirstPage(HttpServletRequest request, Model model) {
		return listByPage(1, "asc", "id", null, request, model);
	}
	
	@GetMapping("/wishlists/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortDir") String sortDir,
			@Param("sortField") String sortField,
			@Param("keyword") String keyword,
			HttpServletRequest request,
			Model model) {
		try {
			Customer customer = helper.getCustomer(request);
			Page<Wishlist> page = wishListService.findAll(pageNum, sortField, sortDir, keyword, customer);
			List<Wishlist> wishlists = page.getContent();
			long startCount = (pageNum - 1) * WishListService.WISHLISTS_PER_PAGE + 1;
			long endCount = startCount + WishListService.WISHLISTS_PER_PAGE - 1;
			if(endCount > page.getTotalElements()) {
				endCount = page.getTotalElements();
			}
			String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
			model.addAttribute("wishlists", wishlists);
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalElements", page.getTotalElements());
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("sortField", sortField);
			model.addAttribute("reverseSortDir", reverseSortDir);
			model.addAttribute("keyword", keyword);
			return "wishlist/wishlist";
		}catch(CustomerNotFoundException ex) {
			return "error/404";
		}
	}
	
	@GetMapping("/wishlists/delete/{productId}")
	public String deleteWishList(@PathVariable(name = "productId") Integer id, HttpServletRequest request, RedirectAttributes redirectAttributes)  {
		try {
			Customer customer = helper.getCustomer(request);
			Product product = productService.findById(id);
			wishListService.deleteByProductAndCustomer(product, customer);
			redirectAttributes.addFlashAttribute("message", "Successfully removed product from wishlist");
		}catch(CustomerNotFoundException ex) {
			return ex.getMessage();
		}catch(ProductNotFoundException ex) {
			return ex.getMessage();
		}
		return "redirect:/wishlists";
	}
	
}
