package com.danze.service;

import java.util.Map;

public interface AddressService {
	
	public Map<String, Object> getAddress(String userId) throws RuntimeException;
	
}
