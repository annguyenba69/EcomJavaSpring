package com.shop.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shop.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
	public static final int USERS_PER_PAGE = 5;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> listAll() {
		return (List<User>) userRepository.findAll();
	}

	public Page<User> listAll(Integer pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("desc") ? sort.descending() : sort.ascending();
		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
		if(keyword != null) {
			return userRepository.findAll(keyword, pageable);
		}
		return userRepository.findAll(pageable);
	}

	public void updateEnabledStatus(boolean status, Integer id) throws UserNotFoundException {
		Long countById = userRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find user with id: "+ id);
		}
		userRepository.updateEnabledStatus(status, id);
	}
	
	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		if(isUpdatingUser) {
			User existingUser = userRepository.findById(user.getId()).get();
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			}else {
				encodedPassword(user);
			}
		}else {
			encodedPassword(user);
		}
		return userRepository.save(user);
	}
	
	private void encodedPassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find user with id: "+ id);
		}
	}
	
	public boolean checkUniqueEmail(Integer id, String email) {
		User existingUser = userRepository.findByEmail(email);
		if(existingUser == null) return true;
		boolean isCreatingNew = (id == null || id == 0);
		if(isCreatingNew) {
			if(existingUser != null) {
				return false;
			}
		}else {
			if(existingUser != null && existingUser.getId() != id) {
				return false;
			}
		}
		return true;
	}
	
	public void delete(Integer id) throws UserNotFoundException {
		long countById = userRepository.countById(id);
		if(countById == 0) {
			throw new UserNotFoundException("Could not find user with id: "+id);
		}
		userRepository.deleteById(id);
	}
	
	public User getByEmail(String email) throws UserNotFoundException {
		try {
			return userRepository.findByEmail(email);
		}catch(NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find user with email: "+ email);
		}
	}
	
	public User updateAccount(User userInForm) {
		User userInDb = userRepository.findById(userInForm.getId()).get();
		if(!userInForm.getPassword().isEmpty()) {
			userInDb.setPassword(userInForm.getPassword());
			encodedPassword(userInDb);
		}
		
		if(userInForm.getPhotos() != null) {
			userInDb.setPhotos(userInForm.getPhotos());
		}
		userInDb.setFirstName(userInForm.getFirstName());
		userInDb.setLastName(userInForm.getLastName());
		
		return userRepository.save(userInDb);
	}
}
