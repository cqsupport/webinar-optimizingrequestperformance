package com.adobe.cso.tests.impl.domain.crm;

import org.junit.Assert;
import org.junit.Test;

public class CustomerUnitTest {

	public static final String CSV_LINE_OK = "Harriet,1 14 224 8523-0299,non@Morbimetus.edu,Statesboro,D4V 6N6,Mali,Feb 29, 2012";
	
	public static final String CSV_LINE_TOO_LITTLE = "Harriet,1 14 224 8523-0299,non@Morbimetus.edu";
	
	public static final String CSV_LINE_TOO_MANY = "Harriet,1 14 224 8523-0299,non@Morbimetus.edu,Statesboro,D4V 6N6,Mali,Feb 29, 2012,Mali,Feb 29, 2012";
	
	@Test(expected=IllegalArgumentException.class)
	public void testStaticCSVParseTooLittle()
	{
		CustomerImpl.fromCsvLine(CSV_LINE_TOO_LITTLE);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testStaticCSVParseTooMany()
	{
		CustomerImpl.fromCsvLine(CSV_LINE_TOO_MANY);
	}
	
	@Test
	public void testStaticCSVParse()
	{
		CustomerImpl customer = CustomerImpl.fromCsvLine(CSV_LINE_OK);
		Assert.assertNotNull("customer is not null after csv parse", customer);
	}
}
