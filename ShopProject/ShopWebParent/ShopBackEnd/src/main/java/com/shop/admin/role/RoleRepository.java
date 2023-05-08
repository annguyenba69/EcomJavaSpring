package com.shop.admin.role;

import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	
}
