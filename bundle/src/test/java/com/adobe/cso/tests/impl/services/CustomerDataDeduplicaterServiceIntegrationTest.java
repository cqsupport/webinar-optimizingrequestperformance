package com.adobe.cso.tests.impl.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.adobe.cso.tests.api.domain.Customer;

/**
 * Not a full CQ integration test, just a test to ensure can parse the csv
 * and put it into Customer objects.
 */
public class CustomerDataDeduplicaterServiceIntegrationTest {
	public static String TEST_DATA_FILE = "META-INF/data/csv/customers-dump-test.csv";

	private CustomerDataDeduplicaterServiceImpl service;
	
	private InputStream dataInputStream;
	
	@Before
	public void setUp()
	{
		//Just test the questions service directly, not in OSGi container
		service = new CustomerDataDeduplicaterServiceImpl();
		
		//Use the default properties of the service
		service.activate(new HashMap<String, Object>());
		
		//Set the inputstream to the test data we are going to use
		dataInputStream = getClass().getClassLoader().getResourceAsStream(TEST_DATA_FILE);
	}
	
	@After
	public void tearDown() throws IOException
	{
		service = null;
		
		if (dataInputStream != null)
			dataInputStream.close();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNoDataInputStreamSet() throws Exception
	{
		service.parse();
	}

	@Test
	public void testDataParse() throws Exception
	{
		service.setDataInputStream(dataInputStream);
		Set<Customer> customer = service.parse();
		
		Assert.assertNotNull("customer return is not null", customer);
		Assert.assertTrue("customer count is right, with no duplicates", customer.size() == 51);
	}
}
