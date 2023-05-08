package com.shop.admin.menu;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.Menu;
import com.shop.common.entity.MenuType;

public interface MenuRepository extends CrudRepository<Menu, Integer> {
	
	public List<Menu> findAllByOrderByTypeAscPositionAsc();
	
	public Long countByType(MenuType type);
	
	public Menu findByTitle(String title);
	
	public Menu findByAlias(String alias);
	
	public List<Menu> findByTypeOrderByPositionAsc(MenuType type);
	
	@Query("UPDATE Menu m SET m.enabled = ?2 WHERE m.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
}
