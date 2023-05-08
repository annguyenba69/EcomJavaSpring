package com.shop.admin.report;

public class ReportInfo {

	private long totalOrders;
	
	private long totalProducts;
	
	private long totalArticles;
	
	private long totalUsers;

	public ReportInfo() {

	}

	public long getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(long totalOrders) {
		this.totalOrders = totalOrders;
	}

	public long getTotalProducts() {
		return totalProducts;
	}

	public void setTotalProducts(long totalProducts) {
		this.totalProducts = totalProducts;
	}

	public long getTotalArticles() {
		return totalArticles;
	}

	public void setTotalArticles(long totalArticles) {
		this.totalArticles = totalArticles;
	}

	public long getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(long totalUsers) {
		this.totalUsers = totalUsers;
	}

	@Override
	public String toString() {
		return "ReportInfo [totalOrders=" + totalOrders + ", totalProducts=" + totalProducts + ", totalArticles="
				+ totalArticles + ", totalUsers=" + totalUsers + "]";
	}

	
	
}
