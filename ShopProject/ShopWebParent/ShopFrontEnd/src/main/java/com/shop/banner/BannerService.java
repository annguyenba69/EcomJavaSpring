package com.shop.banner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Banner;

@Service
public class BannerService {

	@Autowired private BannerRepository bannerRepository;
	
	public List<Banner> findAll(){
		return bannerRepository.findAllWithEnabled();
	}
	
}
