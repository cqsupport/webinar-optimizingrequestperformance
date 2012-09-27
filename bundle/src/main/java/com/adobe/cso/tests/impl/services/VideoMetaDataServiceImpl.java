package com.adobe.cso.tests.impl.services;

import java.io.InputStream;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cso.tests.api.domain.Video;
import com.adobe.cso.tests.api.services.VideoMetaDataService;
import com.adobe.cso.tests.impl.domain.media.VideoImpl;

@Component(immediate=true, metatype = true)
@Service
public class VideoMetaDataServiceImpl implements VideoMetaDataService {
    private static Logger logger = LoggerFactory.getLogger(VideoMetaDataServiceImpl.class);
    
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

	public Video createVideo(InputStream inputStream) {
		Video video = new VideoImpl();
		video.setInputStream(inputStream);
		return video;
	}
}
