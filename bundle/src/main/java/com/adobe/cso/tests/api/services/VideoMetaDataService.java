package com.adobe.cso.tests.api.services;

import java.io.InputStream;

import com.adobe.cso.tests.api.domain.Video;

public interface VideoMetaDataService {
	Video createVideo(InputStream inputStream);
}
