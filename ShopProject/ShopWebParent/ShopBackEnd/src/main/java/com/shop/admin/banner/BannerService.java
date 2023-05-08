package com.shop.admin.banner;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Banner;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BannerService {
	public static final int BANNERS_PER_PAGE = 7;
	
	@Autowired private BannerRepository bannerRepository;
	
	public Page<Banner> findAll(Integer pageNum, String sortField, String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, BANNERS_PER_PAGE, sort);
		if(keyword != null) {
			return bannerRepository.findAll(keyword, pageable);
		}
		return bannerRepository.findAll(pageable);
	}
	
	public Banner get(Integer id) throws BannerNotFoundException {
		try {
			return bannerRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new BannerNotFoundException("Could not found banner with id: "+ id);
		}
	}
	
	public void delete(Integer id) throws BannerNotFoundException {
		long countById = bannerRepository.countById(id);
		if(countById == 0) {
			throw new BannerNotFoundException("Could not found banner with id: "+ id);
		}
		bannerRepository.deleteById(id);
	}
	
	public void updateEnabledStatus(Integer id, boolean status) throws BannerNotFoundException {
		long countById = bannerRepository.countById(id);
		if(countById == 0) {
			throw new BannerNotFoundException("Could not found banner with id: "+ id);
		}
		bannerRepository.updateEnabledStatus(id, status);
	}
	
	public boolean checkUniqueTitle(Integer id, String title) {
		Banner existingBanner = bannerRepository.findByTitle(title);
		if(existingBanner == null) return true;
		boolean isCreatingNew = (id == null || id ==0);
		if(isCreatingNew) {
			if(existingBanner != null) {
				return false;
			}
		}else {
			if(existingBanner != null && existingBanner.getId() != id) {
				return false;
			}
		}
		return true;
	}
	
	public Banner save(Banner banner) {
		return bannerRepository.save(banner);
	}
}
