package com.shop.product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Retry.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Category;
import com.shop.common.entity.Product;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 8;

	@Autowired
	private ProductRepository productRepository;

	public Page<Product> listByCategory(Integer pageNum, Integer categoryId, String sortDir, String sortField,
			List<Integer> integerSelectedBrand, Integer filterPrice) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		String categoryIDMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);
		float startPrice = 0;
		float endPrice = 0;
		if(filterPrice != null) {
			if (filterPrice == 1) {
				startPrice = 0;
				endPrice = 100;
			} else if (filterPrice == 2) {
				startPrice = 100;
				endPrice = 200;
			} else if (filterPrice == 3) {
				startPrice = 200;
				endPrice = 500;
			} else if (filterPrice == 4) {
				startPrice = 500;
			}
		}

		if (filterPrice != null && integerSelectedBrand != null) {
			if(endPrice == 0) {
				return productRepository.listByCategory(categoryId, categoryIDMatch, integerSelectedBrand, startPrice, pageable);
			}
			return productRepository.listByCategory(categoryId, categoryIDMatch, integerSelectedBrand, startPrice,
					endPrice, pageable);
		} else if (filterPrice != null && integerSelectedBrand == null) {
			if(endPrice == 0) {
				return productRepository.listByCategory(categoryId, categoryIDMatch, startPrice, pageable); 
			}
			return productRepository.listByCategory(categoryId, categoryIDMatch, startPrice, endPrice, pageable);
		} else if (integerSelectedBrand != null && filterPrice == null) {
			return productRepository.listByCategory(categoryId, categoryIDMatch, integerSelectedBrand, pageable);
		} else
			return productRepository.listByCategory(categoryId, categoryIDMatch, pageable);

	}

	public Product findByAlias(String alias) throws ProductNotFoundException {
		try {
			return productRepository.findByAlias(alias);
		} catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not found product with alias :" + alias);
		}
	}

	public List<Product> findByCategory(Category category) {
		return productRepository.findByCategory(category);
	}

	public List<Product> getFiveProductsTopRates() {
		List<Product> topRates = productRepository.findProductWithReview();
		List<Product> listFiveProductsTopRates = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			if (topRates.size() > i) {
				listFiveProductsTopRates.add(topRates.get(i));
			}
		}
		return listFiveProductsTopRates;
	}

	public Product findById(Integer id) throws ProductNotFoundException {
		try {
			return productRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not found product with id :" + id);
		}
	}
}
