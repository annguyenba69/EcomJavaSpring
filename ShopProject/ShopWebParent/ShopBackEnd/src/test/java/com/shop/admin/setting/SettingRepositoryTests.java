package com.shop.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Setting;
import com.shop.common.entity.SettingCategory;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {

	@Autowired private SettingRepository settingRepository;
	
	@Test
	public void testCreateGeneralSetting() {
		Setting siteName = new Setting("SITE_NAME", "Shop", SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO", "shop.png", SettingCategory.GENERAL);
		Setting copyRight = new Setting("COPYRIGHT", "Copyright (C) 2023 Shop ltd.", SettingCategory.GENERAL);
		
		Iterable<Setting> iterable = settingRepository.saveAll(List.of(siteName, siteLogo, copyRight));
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testCreateCurrencySetting() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);
		Iterable<Setting> iterable = settingRepository.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType, decimalDigits, thousandsPointType));
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testListSettingsByCategory() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.GENERAL);
		settings.forEach(System.out::println);
	}
}
