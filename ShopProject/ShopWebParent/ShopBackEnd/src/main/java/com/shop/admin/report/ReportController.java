package com.shop.admin.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

	@Autowired private MasterOrderReportService service;
	
	@GetMapping("/reports")
	public String viewSalesReportHome(Model model) {
		ReportInfo reportInfo = service.loadSummary();
		model.addAttribute("reportInfo", reportInfo);
		return "reports/reports";
	}
}
