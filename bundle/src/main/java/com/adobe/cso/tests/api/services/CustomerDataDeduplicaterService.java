package com.adobe.cso.tests.api.services;

import java.io.InputStream;
import java.util.Set;

import com.adobe.cso.tests.api.domain.Customer;

public interface CustomerDataDeduplicaterService {
	void setDataInputStream(InputStream dataInputStream);

	InputStream getDataInputStream();
	
	Set<Customer> parse() throws Exception;
}
