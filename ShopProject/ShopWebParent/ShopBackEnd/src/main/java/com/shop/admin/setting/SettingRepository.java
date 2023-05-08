package com.shop.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Setting;
import com.shop.common.entity.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, Integer>, PagingAndSortingRepository<Setting, Integer> {

	public List<Setting> findByCategory(SettingCategory settingCategory);
	
}
