package com.shop.admin.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.websocket.AsyncChannelWrapperNonSecure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.FileUploadUtil;
import com.shop.admin.currency.CurrencyNotFoundException;
import com.shop.admin.currency.CurrencyService;
import com.shop.common.entity.Currency;
import com.shop.common.entity.Setting;
import com.shop.common.entity.SettingCategory;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettingController {

	@Autowired private SettingService settingService;
	
	@Autowired private CurrencyService currencyService;
	
	@GetMapping("/settings")
	public String settings(Model model) {
		List<Setting> settings = settingService.listAll();
		List<Currency> currencies = currencyService.findAllByOrderByNameAsc();
		
		for(Setting setting : settings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		System.out.println(settings);
		model.addAttribute("currencies", currencies);
		return "settings/setting_form";
	}
	
	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam(name = "image") MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
		try {
			GeneralSettingBag generalSettingBag = settingService.getGeneralSettingBag();
			
			saveSiteLogo(multipartFile, generalSettingBag);
			saveCurrencySymbol(request, generalSettingBag);
			saveSettingValueFromForm(request, generalSettingBag.list());
			
			redirectAttributes.addFlashAttribute("message", "General settings have been saved.");
		}catch(CurrencyNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/settings";
	}
	
	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag generalSettingBag) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			generalSettingBag.updateSiteLogo(value);
			
			String uploadDir = "../site-logo/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
	}
	
	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag generalSettingBag) throws CurrencyNotFoundException {
		try {
			Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
			Currency currency = currencyService.get(currencyId);
			generalSettingBag.updateCurrencySymbol(currency.getSymbol());
		}catch(CurrencyNotFoundException ex) {
			throw new CurrencyNotFoundException(ex.getMessage());
		}	
	}
	
	private void saveSettingValueFromForm(HttpServletRequest request, List<Setting> settings) {
		for(Setting setting : settings) {
			String value = request.getParameter(setting.getKey());
			if(value != null) {
				setting.setValue(value);
			}
		}
		settingService.saveAll(settings);
	}
	
	@PostMapping("/settings/saveMailServer")
	public String saveMailServer(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<Setting> listSettingsMailServer = settingService.findByCategory(SettingCategory.MAIL_SERVER);
		saveSettingValueFromForm(request, listSettingsMailServer);
		
		redirectAttributes.addFlashAttribute("message", "Mail Server Settings Has Been Saved");
		return "redirect:/settings#mailserver";
	}
	
	@PostMapping("/settings/save_mail_template")
	public String saveGeneral(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<Setting> listMailTemplate = settingService.findByCategory(SettingCategory.MAIL_TEMPLATES);
		saveSettingValueFromForm(request, listMailTemplate);
		redirectAttributes.addFlashAttribute("message", "Mail Template Settings Has Been Saved");
		return "redirect:/settings";
	}
}
