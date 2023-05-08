package com.shop.admin.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.admin.order.OrderRepository;
import com.shop.common.entity.Order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class MasterOrderReportService {

	@Autowired
	private OrderRepository orderRepository;
	private DateFormat dateFormat;
	
	@Autowired
	private EntityManager entityManager;
	
	public ReportInfo loadSummary() {
		ReportInfo summary = new ReportInfo();
		Query query = entityManager.createQuery("SELECT "
				+ "(SELECT COUNT(DISTINCT o.id) AS totalOrders FROM Order o),"
				+ "(SELECT COUNT(DISTINCT p.id) AS totalProducts FROM Product p),"
				+ "(SELECT COUNT(DISTINCT a.id) AS totalArticles FROM Article a),"
				+ "(SELECT COUNT(DISTINCT u.id) AS totalUsers FROM User u)");
		List<Object[]> entityCounts = query.getResultList();
		Object[] arrayCounts = entityCounts.get(0);
		int count = 0;
		summary.setTotalOrders((Long) arrayCounts[count++]);
		summary.setTotalProducts((Long) arrayCounts[count++]);
		summary.setTotalArticles((Long) arrayCounts[count++]);
		summary.setTotalUsers((Long) arrayCounts[count++]);
		return summary;
	}

	public List<ReportItem> getReportDataLast7Days() {
		return getReportDataLastXDays(7);
	}

	public List<ReportItem> getReportDataLast28Days() {
		return getReportDataLastXDays(28);
	}

	private List<ReportItem> getReportDataLastXDays(int days) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
		Date startTime = cal.getTime();
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return getReportDataByDateRange(startTime, endTime, "days");
	}
	
	public List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime){
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return getReportDataByDateRange(startTime, endTime, "days");
	}

	private List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime, String period) {
		List<Order> listOrders = orderRepository.findByOrderTimeBetween(startTime, endTime);
		List<ReportItem> listReportItems = createReportData(startTime, endTime, period);
		calculateSalesForReportData(listOrders, listReportItems);
		printReportData(listReportItems);
		return listReportItems;
	}

	private void printReportData(List<ReportItem> listReportItems) {
		listReportItems.forEach(item -> {
			System.out.printf("%s, %10.2f, %10.2f, %d \n", item.getIdentifier(), item.getGrossSales(),
					item.getNetSales(), item.getOrdersCount());
		});

	}

	private void calculateSalesForReportData(List<Order> listOrders, List<ReportItem> listReportItems) {
		for (Order order : listOrders) {
			String orderDateString = dateFormat.format(order.getOrderTime());
			ReportItem reportItem = new ReportItem(orderDateString);
			int itemIndex = listReportItems.indexOf(reportItem);
			if (itemIndex >= 0) {
				reportItem = listReportItems.get(itemIndex);
				reportItem.addGrossSales(order.getTotal());
				reportItem.addNetSales(order.getSubtotal() - order.getProductCost());
				reportItem.increaseOrdersCount();
			}
		}
	};

	private List<ReportItem> createReportData(Date startTime, Date endTime, String period) {
		List<ReportItem> listReportItems = new ArrayList<>();

		Calendar startDate = Calendar.getInstance();
		startDate.setTime(startTime);

		Calendar endDate = Calendar.getInstance();
		endDate.setTime(endTime);

		Date currentDate = startDate.getTime();
		String dateString = dateFormat.format(currentDate);
		listReportItems.add(new ReportItem(dateString));
		do {
			if (period.equals("days")) {
				startDate.add(Calendar.DAY_OF_MONTH, 1);
			} else if (period.equals("months")) {
				startDate.add(Calendar.MONTH, 1);
			}

			currentDate = startDate.getTime();
			dateString = dateFormat.format(currentDate);
			listReportItems.add(new ReportItem(dateString));
		} while (startDate.before(endDate));
		return listReportItems;
	}

	public List<ReportItem> getReportDataLast6Months(){
		return getReportDataLastXMonths(6);
	}

	public List<ReportItem> getReportDataLastYear() {
		return getReportDataLastXMonths(12);
	}

	private List<ReportItem> getReportDataLastXMonths(int months) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -(months - 1));
		Date startTime = cal.getTime();

		dateFormat = new SimpleDateFormat("yyyy-MM");

		return getReportDataByDateRange(startTime, endTime, "months");
	}
}
