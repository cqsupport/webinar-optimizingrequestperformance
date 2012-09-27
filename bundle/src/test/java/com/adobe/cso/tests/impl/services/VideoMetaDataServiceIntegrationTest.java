package com.adobe.cso.tests.impl.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.adobe.cso.tests.api.domain.Video;

public class VideoMetaDataServiceIntegrationTest {
	public static String TEST_DATA_FILE = "META-INF/data/video/big_buck_bunny_480p_h264.mov";
	public static String TEST_BACKUP_FILE = "mymov.mov";
	
	private VideoMetaDataServiceImpl service;
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private InputStream inputStream;
	
	@Before
	public void setUp() throws FileNotFoundException
	{
		//Just test the questions service directly, not in OSGi container
		service = new VideoMetaDataServiceImpl();
		
		//Use the default properties of the service
		service.activate(new HashMap<String, Object>());
		
		inputStream = getClass().getClassLoader().getResourceAsStream(TEST_DATA_FILE);
	}
	
	@After
	public void tearDown() throws IOException
	{
		service = null;
		
		if (inputStream != null)
			inputStream.close();
	}

	@Test
	public void testDataParse() throws FileNotFoundException, IOException
	{
		Video video = service.createVideo(inputStream);
		Assert.assertNotNull("video object not null", video);
	}
}
