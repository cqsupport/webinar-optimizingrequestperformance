package com.adobe.cso.tests.impl.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cso.tests.api.domain.Customer;
import com.adobe.cso.tests.api.services.CustomerDataDeduplicaterService;
import com.adobe.cso.tests.impl.domain.crm.CustomerImpl;

@Component(immediate=true, metatype = true)
@Service
public class CustomerDataDeduplicaterServiceImpl implements CustomerDataDeduplicaterService {
    private static Logger logger = LoggerFactory.getLogger(CustomerDataDeduplicaterServiceImpl.class);
    
    private InputStream dataInputStream;
    
    @Activate
    protected void activate(final Map<String, Object> props) {
    	logger.info("Activating ...");
    	update(props);
    }
    
    @Modified
    protected void update(final Map<String, Object> props)
    {
    	logger.info("Modifying ...");
    }
    
    @Deactivate
    protected void deactivate(ComponentContext ctx) {
    	logger.info("Deactivating ...");
    }

	public void setDataInputStream(InputStream dataInputStream) {
		this.dataInputStream = dataInputStream;
	}

	public InputStream getDataInputStream() {
		return dataInputStream;
	}
	
	public Set<Customer> parse() throws Exception
	{
		if (dataInputStream == null)
			throw new IllegalArgumentException("the dataInputStream cannot be null");
		
		logger.info("Starting parse of the customer data .......... ");
		
		Set<Customer> customers = new HashSet<Customer>();
		String line = null;
		CustomerImpl customer;
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((dataInputStream)));
		
		try {
			while((line = bufferedReader.readLine()) != null) {
				try
				{
				    customer = CustomerImpl.fromCsvLine(line);
				    customers.add(customer);
				}
				catch (IllegalArgumentException e)
				{
					//TODO Set to debug to declutter log files, for testing
					logger.debug("error parsing line to customer: {}, {}", line, e);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("something went terribly, terribly wrong");
		}
		finally
		{
			if (bufferedReader != null)
				bufferedReader.close();
		}
		
		
		logger.info(".......... Finished parse of the customer data  ");
		
		return customers;
	}
}
