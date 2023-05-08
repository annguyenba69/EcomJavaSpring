package com.shop.admin.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Role;

@Service
public class RoleService {

	@Autowired private RoleRepository roleRepository;
	
	public List<Role> listAll(){
		return (List<Role>) roleRepository.findAll();
	}
	
}
