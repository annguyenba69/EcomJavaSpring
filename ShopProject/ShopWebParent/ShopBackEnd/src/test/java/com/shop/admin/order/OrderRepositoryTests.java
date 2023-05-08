package com.shop.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Customer;
import com.shop.common.entity.Order;
import com.shop.common.entity.OrderDetail;
import com.shop.common.entity.OrderStatus;
import com.shop.common.entity.PaymentMethod;
import com.shop.common.entity.Product;

import jakarta.persistence.EntityManager;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositoryTests {

	@Autowired private OrderRepository orderRepository;
	@Autowired private EntityManager entityManager;
	
	@Test
	public void testCreateNewOrderWithSingleProduct() {
		Customer customer = entityManager.find(Customer.class, 1);
		Product product = entityManager.find(Product.class, 1);
		Order order = new Order();
		order.setCustomer(customer);
		order.copyAddressFromCustomer();
		order.setOrderTime(new Date());
		order.setShippingCost(15);
		order.setProductCost(product.getCost());
		order.setTax(0);
		order.setSubtotal(product.getPrice());
		order.setTotal(product.getPrice() + 15);
		
		order.setPaymentMethod(PaymentMethod.COD);
		order.setStatus(OrderStatus.NEW);
		order.setDeliverDate(new Date());
		order.setDeliverDays(5);
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrder(order);
		orderDetail.setProductCost(product.getCost());
		orderDetail.setShippingCost(15);
		orderDetail.setQuantity(1);
		orderDetail.setSubTotal(product.getPrice());
		orderDetail.setUnitPrice(product.getPrice());
		
		order.getOrderDetails().add(orderDetail);
		Order savedOrder = orderRepository.save(order);
		
		assertThat(savedOrder.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateOrderWithTwoProduct() {
		Customer customer = entityManager.find(Customer.class, 2);
		Product product2 = entityManager.find(Product.class, 2);
		Product product3 = entityManager.find(Product.class, 3);
		Order order = new Order();
		order.setOrderTime(new Date());
		order.setCustomer(customer);
		order.copyAddressFromCustomer();
		
		OrderDetail orderDetail1 = new OrderDetail();
		orderDetail1.setQuantity(1);
		orderDetail1.setProductCost(product2.getCost());
		orderDetail1.setShippingCost(10);
		orderDetail1.setUnitPrice(product2.getPrice());
		orderDetail1.setSubTotal(product2.getPrice());
		orderDetail1.setProduct(product2);
		orderDetail1.setOrder(order);
		
		OrderDetail orderDetail2 = new OrderDetail();
		orderDetail2.setQuantity(2);
		orderDetail2.setProductCost(product3.getCost());
		orderDetail2.setShippingCost(20);
		orderDetail2.setUnitPrice(product3.getPrice());
		orderDetail2.setSubTotal(product3.getPrice() * 2);
		orderDetail2.setProduct(product3);
		orderDetail2.setOrder(order);
		
		order.getOrderDetails().addAll(List.of(orderDetail1, orderDetail2));
		order.setShippingCost(30);
		order.setProductCost(product2.getCost() + product3.getCost() * 2);
		order.setTax(0);
		float subTotal = product2.getPrice() + product3.getPrice() * 2;
		order.setSubtotal(subTotal);
		order.setTotal(subTotal + 30);	
		
		order.setPaymentMethod(PaymentMethod.COD);
		order.setStatus(OrderStatus.PROCESSING);
		order.setDeliverDate(new Date());
		order.setDeliverDays(3);
		
		Order savedOrder = orderRepository.save(order);
		assertThat(savedOrder.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListOrders() {
		Iterable<Order> iterable = orderRepository.findAll();
		iterable.forEach(System.out::println);
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testFindByOrderTimeBetween() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormat.parse("2021-08-01");
		Date endTime = dateFormat.parse("2021-08-31");
		
		List<Order> listOrders = orderRepository.findByOrderTimeBetween(startTime, endTime);
		assertThat(listOrders.size()).isGreaterThan(0);
		
		for(Order order : listOrders) {
			System.out.printf("%s | %s | %.2f | %.2f \n",
					order.getId(), order.getOrderTime(), order.getProductCost(),
					order.getSubtotal(), order.getTotal());
		}
	}
	
}
