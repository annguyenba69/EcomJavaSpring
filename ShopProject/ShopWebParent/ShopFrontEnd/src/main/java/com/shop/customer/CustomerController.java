package com.shop.customer;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.Utility;
import com.shop.address.AddressNotFoundException;
import com.shop.address.AddressService;
import com.shop.admin.country.CountryService;
import com.shop.common.entity.Address;
import com.shop.common.entity.Country;
import com.shop.common.entity.Customer;
import com.shop.common.entity.Order;
import com.shop.order.OrderNotFoundException;
import com.shop.order.OrderService;
import com.shop.security.CustomerUserDetails;
import com.shop.setting.EmailSettingBag;
import com.shop.setting.SettingService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomerController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private SettingService settingService;

	@GetMapping("/register")
	public String register(Model model) {
		List<Country> listCountries = countryService.listAll();
		Customer customer = new Customer();
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("customer", customer);
		return "register/register_form";
	}
	
	@PostMapping("/save_customer")
	public String saveCustomer(Customer customer, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		sendVerificationEmail(request, customer);
		return "register/register_success";
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		boolean isVerify = customerService.verify(code);
		if(isVerify) {
			return "register/register_verify_success";
		}else {
			return "register/register_verify_fail";
		}
	}

	private void sendVerificationEmail(HttpServletRequest request, Customer customer)
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", customer.getFullName());

		String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);
		mailSender.send(message);
	}

	@GetMapping("/account")
	public String viewDetailAccount(Model model, @AuthenticationPrincipal CustomerUserDetails logged) {
		return viewDetailAccountByPage(1, "asc", "id", logged, model);
	}

	@GetMapping("/account/{pageNum}")
	public String viewDetailAccountByPage(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortDir") String sortDir, @Param("sortField") String sortField,
			@AuthenticationPrincipal CustomerUserDetails logged, Model model) {
		Customer customer = logged.getCustomer();
		Page<Order> page = orderService.findByOrder(pageNum, sortDir, sortField, customer);
		List<Order> orders = page.getContent();
		List<Country> countries = countryService.listAll();
		List<Address> listAddresses = addressService.listAddress(customer);
		boolean usePrimaryAddressAsDefault = true;
		for (Address address : listAddresses) {
			if (address.isDefaultForShipping()) {
				usePrimaryAddressAsDefault = false;
				break;
			}
		}
		long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
		long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("customer", customer);
		model.addAttribute("orders", orders);
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		model.addAttribute("listAddresses", listAddresses);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("countries", countries);
		return "accounts/account";
	}

	@GetMapping("/account/order/detail/{orderId}")
	public String viewOrderDetail(@PathVariable(name = "orderId") Integer orderId, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Order order = orderService.get(orderId);
			model.addAttribute("order", order);
			return "accounts/order_detail_modal";
		} catch (OrderNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "errors/404";
		}
	}

	@PostMapping("/account/save_account")
	public String saveAccount(Customer customer, RedirectAttributes redirectAttributes) {
		customer.setCreatedTime(new Date());
		customerService.save(customer);
		redirectAttributes.addFlashAttribute("message", "Save Customer Successfull");
		return "redirect:/account";
	}

	@GetMapping("/account/add_new_address")
	public String addNewAddress(Model model) {
		Address address = new Address();
		List<Country> countries = countryService.listAll();
		model.addAttribute("address", address);
		model.addAttribute("countries", countries);
		return "accounts/add_new_address";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.findByEmail(email);
	}

	@PostMapping("/account/save_address")
	public String saveAddress(Address address, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws CustomerNotFoundException {
		Customer customer = getAuthenticatedCustomer(request);
		address.setCustomer(customer);
		addressService.save(address);
		redirectAttributes.addAttribute("message", "The Address Has Been Saved Successfully");
		return "redirect:/account";
	}

	@GetMapping("/account/set_address_default/{id}")
	public String setDefaultAddress(@PathVariable(name = "id") Integer id, RedirectAttributes attributes) {
		addressService.setDefaultAddress(id);
		return "redirect:/account";
	}
}
