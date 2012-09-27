package com.adobe.cso.tests.api.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Video {
	void setInputStream(InputStream inputStream);

	InputStream getInputStream();
	
	boolean doBackUp(OutputStream outputStream) throws IOException;
}