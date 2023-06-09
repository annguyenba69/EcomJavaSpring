package com.shop.admin.report;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportRestController {

	@Autowired
	private MasterOrderReportService masterOrderReportService;

	@GetMapping("/reports/sales_by_date/{period}")
	public List<ReportItem> getReportDataByDatePeriod(@PathVariable("period") String period) {
		switch (period) {
		case "last_7_days":
			return masterOrderReportService.getReportDataLast7Days();
		case "last_28_days":
			return masterOrderReportService.getReportDataLast28Days();
		case "last_6_months":
			return masterOrderReportService.getReportDataLast6Months();
		case "last_year":
			return masterOrderReportService.getReportDataLastYear();
		default:
			return masterOrderReportService.getReportDataLast7Days();
		}
	}
	
	@GetMapping("/reports/sales_by_date/{startDate}/{endDate}")
	public List<ReportItem> getReportDataByDatePeriod(@PathVariable(name = "startDate") String startDate,
			@PathVariable(name = "endDate") String endDate) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormat.parse(startDate);
		Date endTime = dateFormat.parse(endDate);
		return masterOrderReportService.getReportDataByDateRange(startTime, endTime);
	}

}
