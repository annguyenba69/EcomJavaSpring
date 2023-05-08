package com.shop.common.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "first_name", nullable = false, length = 45)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length = 45)
	private String lastName;
	
	@Column(name = "phone_number", nullable = false, length = 15)
	private String phoneNumber;
	
	@Column(name = "address_line_1", nullable = false, length = 64)
	private String addressLine1;
	
	@Column(name = "address_line_2", length = 64)
	private String addressLine2;
	
	@Column(nullable = false, length = 45)
	private String city;
	
	@Column(nullable = false, length = 45)
	private String state;
	
	@Column(name = "postal_code", nullable = false, length = 10)
	private String postalCode;
	
	@Column(nullable = false, length = 45)
	private String country;
	
	private Date orderTime;
	
	private float shippingCost;
	
	private float productCost;
	
	private float subtotal;
	
	private float tax;
	
	private float total;
	
	private int deliverDays;
	
	private Date deliverDate;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderDetail> orderDetails = new HashSet<>();

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderTrack> orderTracks = new HashSet<>();
	
	public Order() {

	}
	
	public Order(Integer id, Date orderTime, float productCost, float subtotal, float total) {
		this.id = id;
		this.orderTime = orderTime;
		this.productCost = productCost;
		this.subtotal = subtotal;
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	public void copyAddressFromCustomer() {
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
		setPhoneNumber(customer.getPhoneNumber());
		setAddressLine1(customer.getAddressLine1());
		setAddressLine2(customer.getAddressLine2());
		setCity(customer.getCity());
		setCountry(customer.getCountry().getName());
		setPostalCode(customer.getPostalCode());
		setState(customer.getState());
	}
	
	public void copyShippingAddress(Address address) {
		setFirstName(address.getFirstName());
		setLastName(address.getLastName());
		setPhoneNumber(address.getPhoneNumber());
		setAddressLine1(address.getAddressLine1());
		setAddressLine2(address.getAddressLine2());
		setCity(address.getCity());
		setCountry(address.getCountry().getName());
		setPostalCode(address.getPostalCode());
		setState(address.getState());			
	}

	public Set<OrderTrack> getOrderTracks() {
		return orderTracks;
	}

	public void setOrderTracks(Set<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
				+ phoneNumber + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", city=" + city
				+ ", state=" + state + ", postalCode=" + postalCode + ", country=" + country + ", orderTime="
				+ orderTime + ", shippingCost=" + shippingCost + ", productCost=" + productCost + ", subtotal="
				+ subtotal + ", tax=" + tax + ", total=" + total + ", deliverDays=" + deliverDays + ", deliverDate="
				+ deliverDate + ", paymentMethod=" + paymentMethod + ", status=" + status + ", getId()=" + getId()
				+ ", getFirstName()=" + getFirstName() + ", getLastName()=" + getLastName() + ", getPhoneNumber()="
				+ getPhoneNumber() + ", getAddressLine1()=" + getAddressLine1() + ", getAddressLine2()="
				+ getAddressLine2() + ", getCity()=" + getCity() + ", getState()=" + getState() + ", getPostalCode()="
				+ getPostalCode() + ", getCountry()=" + getCountry() + ", getOrderTime()=" + getOrderTime()
				+ ", getShippingCost()=" + getShippingCost() + ", getProductCost()=" + getProductCost()
				+ ", getSubtotal()=" + getSubtotal() + ", getTax()=" + getTax() + ", getTotal()=" + getTotal()
				+ ", getDeliverDays()=" + getDeliverDays() + ", getDeliverDate()=" + getDeliverDate()
				+ ", getPaymentMethod()=" + getPaymentMethod() + ", getStatus()=" + getStatus() + ", getCustomer()="
				+ getCustomer() + ", getOrderDetails()=" + getOrderDetails() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	@Transient
	public String getDestination() {
		return city + ", " + state + ", " + country;
	}
	
	@Transient 
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}
}