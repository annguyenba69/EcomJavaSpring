package com.shop.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	public User findByEmail(String email);
	
	public Long countById(Integer id);
	
	@Query("UPDATE User u SET u.enabled = ?1 WHERE u.id = ?2")
	@Modifying
	public void updateEnabledStatus(boolean status, Integer id);
	
	public Page<User> findAll(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ',"
			+ "u.lastName) LIKE %?1%")
	public Page<User> findAll(String keyword ,Pageable pageable);
	
	
}
