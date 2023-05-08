package com.shop.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Setting;
import com.shop.common.entity.SettingCategory;

@Service
public class SettingService {
	
	@Autowired private SettingRepository settingRepository;
	
	public List<Setting> listAll(){
		return (List<Setting>) settingRepository.findAll();
	}
	
	public GeneralSettingBag getGeneralSettingBag() {
		List<Setting> settings = new ArrayList<>();
		
		List<Setting> generalSetting = settingRepository.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySetting = settingRepository.findByCategory(SettingCategory.CURRENCY);
		settings.addAll(generalSetting);
		settings.addAll(currencySetting);
		return new GeneralSettingBag(settings);
	}

	
	public void saveAll(List<Setting> settings) {
		settingRepository.saveAll(settings);
	}
	 
	public List<Setting> findByCategory(SettingCategory settingCategory){
		return settingRepository.findByCategory(settingCategory);
	}
}
