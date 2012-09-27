package com.adobe.cso.tests.api.domain;

import java.util.Date;

public interface Customer {
	String getName();
	
	void setName(String name);
	
	String getPhone();
	
	void setPhone(String phone);
	
	String getEmail();
	
	void setEmail(String email);
	
	String getCity();
	
	void setCity(String city);
	
	String getPostCode();
	
	void setPostCode(String postCode);
	
	String getCountry();
	
	void setCountry(String country);
	
	Date getJoined();
	
	void setJoined(Date joined);
}