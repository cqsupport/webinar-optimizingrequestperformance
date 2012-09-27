package com.adobe.cso.tests.impl.domain.media;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cso.tests.api.domain.Video;

public class VideoImpl implements Video {
	private static Logger logger = LoggerFactory.getLogger(VideoImpl.class);
	
	private InputStream inputStream;

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}	
	
	public boolean doBackUp(OutputStream outputStream) throws IOException
	{		
		if (inputStream == null)
			throw new IllegalStateException("the inputStream property must be set before backing up");
		
		if (outputStream == null)
			throw new IllegalStateException("the outputStream property must be non null");
			
		logger.info("starting copy of video.... ");
		
		//TODO This is just a test, just read in the file to memory to simulate
		IOUtils.copy(inputStream, outputStream);

		logger.info("..... finished copy of video");
		
		return true;
	}
}
