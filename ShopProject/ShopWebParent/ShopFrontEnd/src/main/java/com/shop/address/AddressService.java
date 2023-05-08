package com.shop.address;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Address;
import com.shop.common.entity.Customer;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressService {
	
	@Autowired private AddressRepository addressRepository;
	
	public List<Address> listAddress(Customer customer){
		return addressRepository.findByCustomer(customer);
	}
	
	public void save(Address address) {
		addressRepository.save(address);
	}
	
	public Address get(Integer addressId) throws AddressNotFoundException {
		try {
			return addressRepository.findById(addressId).get();
		}catch(NoSuchElementException ex) {
			throw new AddressNotFoundException("Could not found address with id: "+addressId);
		}
	}
	
	public void delete(Integer addressId) throws AddressNotFoundException {
		Address address = addressRepository.findById(addressId).get();
		
		if(address == null) {
			throw new AddressNotFoundException("Could not find Address with id: "+ addressId);
		}
		
		addressRepository.delete(address);
	}
	
	public void setDefaultAddress(Integer defaultAddressId) {
		if(defaultAddressId > 0) {
			addressRepository.setNonDefaultForOthers(defaultAddressId);
			addressRepository.setDefaultAddress(defaultAddressId);
		}
		
	}
	
	public Address getDefaultAddress(Customer customer) {
		return addressRepository.findDefaultByCustomer(customer.getId());
	}
}
