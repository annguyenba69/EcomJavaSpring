package com.shop.admin.menu;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Menu;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MenuService {

	@Autowired
	private MenuRepository menuRepository;

	public List<Menu> findAll() {
		return menuRepository.findAllByOrderByTypeAscPositionAsc();
	}

	public void save(Menu menu) {
		setDefaultAlias(menu);
		if (menu.getId() != null) {
			setPositionForEditMenu(menu);
		} else {
			setPositionForNewMenu(menu);
		}
		menuRepository.save(menu);
	}

	private void setPositionForEditMenu(Menu menu) {
		Menu existMenu = menuRepository.findById(menu.getId()).get();
		if (!existMenu.getType().equals(menu.getType())) {
			setPositionForNewMenu(menu);
		}
	}

	private void setPositionForNewMenu(Menu menu) {
		Long newPosition = menuRepository.countByType(menu.getType()) + 1;
		menu.setPosition(newPosition.intValue());
	}

	private void setDefaultAlias(Menu menu) {
		if (menu.getAlias() == null || menu.getAlias().isEmpty()) {
			menu.setAlias(menu.getTitle().replace(" ", "-"));
		}
	}

	public String checkUnique(Integer id, String title, String alias) {
		boolean isCreatingNew = (id == null || id == 0);
		Menu menuByTitle = menuRepository.findByTitle(title);
		Menu menuByAlias = menuRepository.findByAlias(alias);
		if (isCreatingNew) {
			if (menuByTitle != null) {
				return "DuplicatedTitle";
			} else {
				if (menuByAlias != null) {
					return "DuplicatedAlias";
				}
			}
		} else {
			if (menuByTitle != null && menuByTitle.getId() != id) {
				return "DuplicatedTitle";
			} else {
				if (menuByAlias != null && menuByAlias.getId() != id) {
					return "DuplicatedAlias";
				}
			}
		}
		return "Ok";
	}

	public Menu get(Integer id) throws MenuNotFoundException {
		try {
			return menuRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new MenuNotFoundException("Could not found Menu with id: " + id);
		}
	}

	public void updateEnabledStatus(Integer id, boolean enabled) throws MenuNotFoundException {
		if (!menuRepository.existsById(id)) {
			throw new MenuNotFoundException("Could not found menu with id: " + id);
		}
		menuRepository.updateEnabledStatus(id, enabled);
	}

	public void delete(Integer id) throws MenuNotFoundException {
		if (!menuRepository.existsById(id)) {
			throw new MenuNotFoundException("Could not found menu with id: " + id);
		}
		menuRepository.deleteById(id);
	}
	
	public void moveMenu(Integer id, MenuMoveDirection direction) throws MenuNotFoundException, MenuUnmoveException {
		if(direction.equals(MenuMoveDirection.UP)) {
			moveMenuUp(id);
		}else {
			moveMenuDown(id);
		}
	}

	private void moveMenuUp(Integer id) throws MenuNotFoundException, MenuUnmoveException {
		Optional<Menu> findById = menuRepository.findById(id);
		if (!findById.isPresent()) {
			throw new MenuNotFoundException("Could not found menu with id: " + id);
		}
		Menu currentMenu = findById.get();
		List<Menu> allMenuWithTheSameType = menuRepository.findByTypeOrderByPositionAsc(currentMenu.getType());
		Integer currentMenuIndex = allMenuWithTheSameType.indexOf(currentMenu);
		if (currentMenuIndex == 0) {
			throw new MenuUnmoveException("The menu with id: " + id + " is already the first position");
		}
		Integer previousMenuIndex = currentMenuIndex - 1;
		Menu previousMenu = allMenuWithTheSameType.get(previousMenuIndex);

		currentMenu.setPosition(previousMenuIndex + 1);
		allMenuWithTheSameType.set(previousMenuIndex, currentMenu);

		previousMenu.setPosition(currentMenuIndex + 1);
		allMenuWithTheSameType.set(previousMenuIndex, previousMenu);

		menuRepository.saveAll(Arrays.asList(currentMenu, previousMenu));
	}

	private void moveMenuDown(Integer id) throws MenuNotFoundException, MenuUnmoveException {
		Optional<Menu> findById = menuRepository.findById(id);
		if (!findById.isPresent()) {
			throw new MenuNotFoundException("Could not found menu with id: " + id);
		}
		Menu currentMenu = findById.get();
		List<Menu> allMenuWithTheSameType = menuRepository.findByTypeOrderByPositionAsc(currentMenu.getType());
		Integer currentMenuIndex = allMenuWithTheSameType.indexOf(currentMenu);
		if(currentMenuIndex == allMenuWithTheSameType.size() - 1) {
			throw new MenuUnmoveException("The menu with id: " + id + " is already the first position");
		}
		Integer nextMenuIndex = currentMenuIndex + 1;
		Menu nextMenu = allMenuWithTheSameType.get(nextMenuIndex);
		
		currentMenu.setPosition(nextMenuIndex + 1);
		allMenuWithTheSameType.set(nextMenuIndex, currentMenu);
		
		nextMenu.setPosition(currentMenuIndex + 1);
		allMenuWithTheSameType.set(currentMenuIndex, nextMenu);
		
		menuRepository.saveAll(Arrays.asList(currentMenu, nextMenu));
	}

	public enum MenuMoveDirection {
		UP, DOWN
	}
}
