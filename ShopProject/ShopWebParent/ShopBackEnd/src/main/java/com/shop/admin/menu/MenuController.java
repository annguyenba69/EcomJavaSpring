package com.shop.admin.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.menu.MenuService.MenuMoveDirection;
import com.shop.common.entity.Menu;

@Controller
public class MenuController {

	@Autowired private MenuService menuService;
	
	@GetMapping("/menus")
	public String menus(Model model) {
		List<Menu> menus = menuService.findAll();
		model.addAttribute("menus", menus);
		return "menus/menus";
	}
	
	@GetMapping("/menus/create")
	public String create(Model model) {
		Menu menu = new Menu();
		model.addAttribute("menu", menu);
		return "menus/menu_form";
	}
	
	@PostMapping("/menus/save")
	public String save(Menu menu, RedirectAttributes redirectAttributes) {
		menuService.save(menu);
		redirectAttributes.addFlashAttribute("message", "Save Menu successfull");
		return "redirect:/menus";
	}
	
	@GetMapping("/menus/edit/{id}")
	public String edit(@PathVariable(name = "id") Integer id, Model model ,RedirectAttributes redirectAttributes) {
		try {
			Menu menu = menuService.get(id);
			model.addAttribute("menu", menu);
			return "menus/menu_form";
		}catch(MenuNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/memus";
		}
	}
	
	@GetMapping("/menus/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean status,
			RedirectAttributes redirectAttributes) {
		try {
			menuService.updateEnabledStatus(id, status);
			redirectAttributes.addFlashAttribute("message", "Update enabled successfull");
		}catch(MenuNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/menus"; 
	}
	
	@GetMapping("/menus/delete/{id}")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			menuService.delete(id);
			redirectAttributes.addFlashAttribute("message", "Delete Menu successfull");
		}catch(MenuNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/menus"; 
	}
	
	@GetMapping("/menus/{direction}/{id}")
	public String moveMenu(@PathVariable(name = "direction") String direction, @PathVariable(name = "id") Integer id,
			RedirectAttributes redirectAttributes) {
		try {
			MenuMoveDirection moveDirection = MenuMoveDirection.valueOf(direction.toUpperCase());
			menuService.moveMenu(id, moveDirection);
			redirectAttributes.addFlashAttribute("message", "The menu ID " + id + " has been moved up by one position.");
		}catch(MenuNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}catch(MenuUnmoveException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/menus";
	}
}
