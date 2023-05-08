package com.shop.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Setting;
import com.shop.common.entity.SettingCategory;

@Service
public class SettingService {

	@Autowired private SettingRepository settingRepository;
	
	public List<Setting> getGeneralSettings(){
		return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
	
	public EmailSettingBag getEmailSettings(){
		List<Setting> emailSettings = settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
		emailSettings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_SERVER));
		return new EmailSettingBag(emailSettings);
	}
}
