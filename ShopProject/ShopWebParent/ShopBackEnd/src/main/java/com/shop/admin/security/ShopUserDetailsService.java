package com.shop.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shop.admin.user.UserRepository;
import com.shop.common.entity.User;

public class ShopUserDetailsService implements UserDetailsService{

	@Autowired private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if(user != null) {
			return new ShopUserDetails(user);
		}
		throw new UsernameNotFoundException("Could Not Find User With Email: " +email);
	}

}
