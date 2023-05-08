package com.shop.checkout;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.Utility;
import com.shop.address.AddressService;
import com.shop.admin.shoppingcart.ShoppingCartService;
import com.shop.common.entity.Address;
import com.shop.common.entity.CartItem;
import com.shop.common.entity.Customer;
import com.shop.common.entity.Order;
import com.shop.common.entity.PaymentMethod;
import com.shop.common.entity.ShippingRate;
import com.shop.customer.CustomerNotFoundException;
import com.shop.customer.CustomerService;
import com.shop.order.OrderService;
import com.shop.shippingrate.ShippingRateService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CheckoutController {

	@Autowired
	private CheckoutService checkoutService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private ShippingRateService rateService;
	@Autowired
	private ShoppingCartService cartService;
	@Autowired
	private OrderService orderService;

	@GetMapping("/checkout")
	public String viewCheckout(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws CustomerNotFoundException {
		Customer customer = getAuthenticatedCustomer(request);
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		if (defaultAddress != null) {
			model.addAttribute("shippingAddress", defaultAddress.toString());
			shippingRate = rateService.getShippingRateByAddress(defaultAddress);
		} else {
			model.addAttribute("shippingAddress", customer.toString());
			shippingRate = rateService.getShippingRateByCustomer(customer);
		}
		if (shippingRate == null) {
			redirectAttributes.addFlashAttribute("message",
					"Sorry we can't deliver to your address, please change your shipping address here:");
			return "redirect:/shoppingcarts";
		}
		List<CartItem> cartItems = cartService.listCartItem(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);

		return "checkout/checkout";
	}

	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			String paymentType = request.getParameter("paymentMethod");
			PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

			Customer customer = getAuthenticatedCustomer(request);
			Address defaultAddress = addressService.getDefaultAddress(customer);
			ShippingRate shippingRate = null;
			if (defaultAddress != null) {
				shippingRate = rateService.getShippingRateByAddress(defaultAddress);
			}else {
				shippingRate = rateService.getShippingRateByCustomer(customer);
			}
			List<CartItem>  cartItems = cartService.listCartItem(customer);
			CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
			Order newOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
			cartService.deleteByCustomer(customer);
			return "checkout/success";

		} catch (CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/login";
		}
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.findByEmail(email);
	}

}
