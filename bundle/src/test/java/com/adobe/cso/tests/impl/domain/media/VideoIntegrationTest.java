package com.adobe.cso.tests.impl.domain.media;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.adobe.cso.tests.impl.services.VideoMetaDataServiceIntegrationTest;

public class VideoIntegrationTest {

	public static String TEST_DATA_FILE = VideoMetaDataServiceIntegrationTest.TEST_DATA_FILE;
	public static String TEST_BACKUP_FILE = VideoMetaDataServiceIntegrationTest.TEST_BACKUP_FILE;
	
	private VideoImpl video;
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private InputStream inputStream;
	
	private OutputStream outputStream;
	
	@Before
	public void setUp() throws FileNotFoundException
	{
		video = new VideoImpl();
	}
	
	@After
	public void tearDown() throws IOException
	{
		video = null;
		
		if (inputStream != null)
			inputStream.close();
		
		if (outputStream != null)
			outputStream.close();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testFileLocationNotSet() throws IOException
	{
		inputStream = getClass().getClassLoader().getResourceAsStream(TEST_DATA_FILE+"IDONTEXIST-IFIDID-ITWOULDBEAREALLYWEIRDFILE");
		outputStream = new FileOutputStream(folder.newFile(TEST_BACKUP_FILE));
		
		video.doBackUp(outputStream);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testFileLocationNotExists() throws IOException
	{
		inputStream = getClass().getClassLoader().getResourceAsStream(TEST_DATA_FILE+"IDONTEXIST-IFIDID-ITWOULDBEAREALLYWEIRDFILE");
		outputStream = new FileOutputStream(folder.newFile(TEST_BACKUP_FILE));
		
		video.setInputStream(inputStream);
		video.doBackUp(outputStream);
	}
	
	@Test
	public void testBackUpFileLocationWrite() throws IOException
	{
		inputStream = getClass().getClassLoader().getResourceAsStream(TEST_DATA_FILE);
		outputStream = new FileOutputStream(folder.newFile(TEST_BACKUP_FILE));
		
		video.setInputStream(inputStream);
		video.doBackUp(outputStream);
	}
	
	//TODO Test synchronized lock
}
