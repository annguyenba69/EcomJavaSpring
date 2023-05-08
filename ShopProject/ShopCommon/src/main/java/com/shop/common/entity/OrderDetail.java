package com.shop.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_details")
public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer quantity;
	
	private float productCost;
	
	private float shippingCost;
	
	private float unitPrice;
	
	private float subTotal;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	public OrderDetail() {

	}
	
	public OrderDetail(String categoryName ,Integer quantity, float productCost, float shippingCost, float subTotal) {
		this.product = new Product();
		this.product.setCategory(new Category(categoryName));
		this.quantity = quantity;
		this.productCost = productCost;
		this.shippingCost = shippingCost;
		this.subTotal = subTotal;
	}



	public OrderDetail(Integer quantity, String productName, float productCost, float shippingCost, float subTotal) {
		this.product = new Product(productName);
		this.quantity = quantity;
		this.productCost = productCost;
		this.shippingCost = shippingCost;
		this.subTotal = subTotal;
	}

	public OrderDetail(Integer quantity, float productCost, float shippingCost, float subTotal, Product product) {
		this.quantity = quantity;
		this.productCost = productCost;
		this.shippingCost = shippingCost;
		this.subTotal = subTotal;
		this.product = product;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public float getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
}
