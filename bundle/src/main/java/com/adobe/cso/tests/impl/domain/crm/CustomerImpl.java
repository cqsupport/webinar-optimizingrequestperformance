package com.adobe.cso.tests.impl.domain.crm;

import java.util.Date;

import com.adobe.cso.tests.api.domain.Customer;

public class CustomerImpl implements Customer {
	private String name;
	private String phone;
	private String email;
	private String city;
	private String postCode;
	private String country;
	private Date joined;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getPostCode() {
		return postCode;
	}
	
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public Date getJoined() {
		return joined;
	}
	
	public void setJoined(Date joined) {
		this.joined = joined;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((joined == null) ? 0 : joined.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result
				+ ((postCode == null) ? 0 : postCode.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerImpl other = (CustomerImpl) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (joined == null) {
			if (other.joined != null)
				return false;
		} else if (!joined.equals(other.joined))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (postCode == null) {
			if (other.postCode != null)
				return false;
		} else if (!postCode.equals(other.postCode))
			return false;
		return true;
	}
	
	public static CustomerImpl fromCsvLine(String line) throws IllegalArgumentException
	{		
		return fromCsvLine(line, ",");
	}
	
	@SuppressWarnings("deprecation")
	public static CustomerImpl fromCsvLine(String line, String delimiter) throws IllegalArgumentException
	{
		CustomerImpl customer = new CustomerImpl();
		int partsLen = 7;
		
		//The date is stored as "Dec 15, 2001", so if we are deliming by "," need to take that into account
		if (delimiter.equals(","))
			partsLen = 8;
		
		//For test purposes only, in reality would need to use a more bullet proof parsing for CSV lines
		String[] parts = line.split(delimiter);
		if (parts.length != partsLen)
			throw new IllegalArgumentException("the line must have 8 parts when split by it's delimiter: " + line);
		
		customer.setName(parts[0]);
		customer.setPhone(parts[1]);
		customer.setEmail(parts[2]);
		customer.setCity(parts[3]);
		customer.setPostCode(parts[4]);
		customer.setCountry(parts[5]);
		
		if (partsLen == 7)
			customer.setJoined(new Date(Date.parse(parts[6])));
		else
			customer.setJoined(new Date(Date.parse(parts[6] + ", " + parts[7])));
		
		return customer;
	}
}
