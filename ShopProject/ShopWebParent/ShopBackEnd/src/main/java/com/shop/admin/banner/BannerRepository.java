package com.shop.admin.banner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Banner;

public interface BannerRepository extends CrudRepository<Banner, Integer>, PagingAndSortingRepository<Banner, Integer> {

	@Query("SELECT b FROM Banner b WHERE b.title LIKE %?1%")
	public Page<Banner> findAll(String keyword, Pageable pageable);

	public long countById(Integer id);

	@Query("UPDATE Banner b SET b.enabled = ?2 WHERE b.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
	
	public Banner findByTitle(String title);
}
