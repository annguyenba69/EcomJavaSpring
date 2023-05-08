package com.shop.banner;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.Banner;

public interface BannerRepository extends CrudRepository<Banner, Integer>{

	@Query("SELECT b FROM Banner b WHERE b.enabled = true")
	public List<Banner> findAllWithEnabled();
}
