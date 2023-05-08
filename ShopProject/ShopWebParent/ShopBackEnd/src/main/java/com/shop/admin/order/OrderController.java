package com.shop.admin.order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.setting.SettingService;
import com.shop.common.entity.Customer;
import com.shop.common.entity.Order;
import com.shop.common.entity.OrderStatus;
import com.shop.common.entity.OrderTrack;
import com.shop.common.entity.Setting;
import com.shop.common.entity.SettingCategory;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private SettingService settingService;


	@GetMapping("/orders")
	public String listFirstPage(Model model) {
		return listByPage(1, "id", "asc", null, model);
	}

	@GetMapping("/orders/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, @Param("sortField") String sortField,
			@Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {
		Page<Order> page = orderService.listByPage(pageNum, sortDir, sortField, keyword);
		List<Order> orders = page.getContent();
		long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
		long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		getListCurrenciesSetting(model);

		model.addAttribute("orders", orders);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("endCount", endCount);
		return "orders/orders";
	}

	private void getListCurrenciesSetting(Model model) {
		List<Setting> currenciesSetting = settingService.findByCategory(SettingCategory.CURRENCY);
		for (Setting currency : currenciesSetting) {
			model.addAttribute(currency.getKey(), currency.getValue());
		}
	}

	@GetMapping("/orders/detail/{id}")
	public String viewDetail(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Order order = orderService.get(id);
			getListCurrenciesSetting(model);
			model.addAttribute("order", order);
			return "orders/order_detail_modal";
		} catch (OrderNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/orders";
		}
	}

	@GetMapping("/orders/edit/{id}")
	public String edit(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Order order = orderService.get(id);
			getListCurrenciesSetting(model);
			model.addAttribute("order", order);
			return "orders/order_form";
		} catch (OrderNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/order_form";
		}
	}

	@GetMapping("/orders/delete/{id}")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			orderService.delete(id);
			redirectAttributes.addFlashAttribute("message", "Delete Success");
		} catch (OrderNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/orders";
	}

	@PostMapping("/orders/save")
	public String save(Order order, HttpServletRequest request ,RedirectAttributes redirectAttributes) {
		addOrderTrack(order, request);
		orderService.save(order);
		redirectAttributes.addFlashAttribute("message", "Save Success");
		return "redirect:/orders";
	}
	
	private void addOrderTrack(Order order, HttpServletRequest request) {
		String orderTrackId[] = request.getParameterValues("orderTrackId");
		String orderTrackStatus[] = request.getParameterValues("orderTrackStatus");
		String orderTrackUpdatedTime[] = request.getParameterValues("orderTrackUpdatedTime");
		String orderTrackNotes[] = request.getParameterValues("orderTrackNotes");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Set<OrderTrack> orderTracks = order.getOrderTracks();
		
		for(int i = 0; i < orderTrackId.length; i++) {
			OrderTrack trackRecord = new OrderTrack();
			Integer trackId = Integer.parseInt(orderTrackId[i]);
			trackRecord.setId(trackId);
			trackRecord.setOrder(order);
			trackRecord.setNotes(orderTrackNotes[i]);
			trackRecord.setStatus(OrderStatus.valueOf(orderTrackStatus[i]));
			
			try {
				trackRecord.setUpdatedTime(dateFormat.parse(orderTrackUpdatedTime[i]));
			}catch(ParseException e) {
				e.printStackTrace();
			}
			orderTracks.add(trackRecord);
		}
		
		OrderStatus newOrderStatus = order.getStatus();
		orderTracks.add(new OrderTrack(newOrderStatus.defaultDescription(), new Date(), newOrderStatus, order));
	}

}
