package com.adobe.cso.tests.impl.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.NonExistingResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cso.tests.api.domain.Video;
import com.adobe.cso.tests.api.services.VideoMetaDataService;

@SuppressWarnings("serial")
@Component(
        label = "%VideoMetaDataServlet.label",
        description = "%VideoMetaDataServlet.description",
        metatype = true,
        immediate=true)
@Service(value = Servlet.class)
@Properties({
        @Property(name = "service.description", value = "%VideoMetaDataServlet.service.description"),
        @Property(name = "sling.servlet.resourceTypes", value = {"sling/servlet/default"}, propertyPrivate = true),
        @Property(name = "sling.servlet.methods", value = {"POST", "GET"}, propertyPrivate = true),
        @Property(name = "sling.servlet.selectors", value = {"backup"}),
        @Property(name = "sling.servlet.extensions", value = {"mov"}, propertyPrivate = true)
})
public class VideoMetaDataServlet extends SlingAllMethodsServlet {
	private static final Logger logger = LoggerFactory.getLogger(VideoMetaDataServlet.class);
	
	//TODO Make this an OSGi configuration property
	private static String TEMP_BACKUP_LOCATION = System.getProperty("java.io.tmpdir");
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    protected VideoMetaDataService vidService;
    
    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
    	//we would never normally do these types of operation on a GET, but this is test only
    	doPost(request, response);
    }
	
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
    	
    	if (logger.isDebugEnabled()) logger.debug("entering doPost, resourcePath={}", request.getResource().getPath());
    	
    	String[] selectors = request.getRequestPathInfo().getSelectors();
    	String resourcePath = request.getResource().getPath();
    	boolean processed = false;
    	
    	for (String selector : selectors)
    	{
    		if (selector.equals("backup"))
    		{
    	    	if (logger.isDebugEnabled()) logger.debug("{} selector detected", selector);
    	    	 
    	    	synchronized (resourcePath.intern()) {
    	    		backUpVideo(request, response);
				}
    			processed = true;
    		}
    	}
    	
    	if (!processed)
    		logger.warn("unhandled selector(s) in request: requestSelectors={}",selectors);
    }
    
    protected void backUpVideo(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
    {
    	String contentPath = request.getResource().getPath();
    	
    	//Strip out the selectors
    	for (String selector : request.getRequestPathInfo().getSelectors())
    	{
    		contentPath = contentPath.replace("."+selector, "");
    	}   	
    	
    	//Get the resource
    	Resource resource = request.getResource().getResourceResolver().getResource(contentPath);
    	
    	if (resource == null || resource instanceof NonExistingResource)
    	{
    		logger.error("the resource could not be afoundm: {}", contentPath);
    		return;
    	}

    	String requestBackUpLocation = (String) request.getAttribute("backUpLocation");
    	String backUpLocation = requestBackUpLocation == null ? TEMP_BACKUP_LOCATION + StringUtils.substringAfterLast(contentPath, "/") : requestBackUpLocation;
    	
    	InputStream resourceInputStream = null;
    	
    	try
    	{
        	Node resourceNode = resource.adaptTo(Node.class);
        	Node contentNode = resourceNode.getNode("jcr:content");
        	resourceInputStream = contentNode.getProperty("jcr:data").getBinary().getStream();
	    	
	    	if (resourceInputStream == null)
	    	{
	    		logger.error("the resource could not be adapated to an InputStream: {}", request.getResource().getPath());
	    		return;
	    	}
	    	
	    	Video video = vidService.createVideo(resourceInputStream);
	    	
	    	File backUpFile = new File(backUpLocation);
	    	if (backUpFile.exists())
	    		backUpFile.delete();
	    	
			video.doBackUp(new FileOutputStream(backUpFile));

		} catch (IOException e) {
			logger.error("a big boy did it and ran away, not good. IOException.  {}", e);
		} catch (PathNotFoundException e) {
			logger.error("a big boy did it and ran away, not good. PathNotFoundException.  {}", e);
		} catch (RepositoryException e) {
			logger.error("a big boy did it and ran away, not good. RepositoryException.  {}", e);
		}
    	finally
    	{
    		if (resourceInputStream != null)
    		{
				try {
					resourceInputStream.close();
				} catch (IOException e) {
					logger.error("a big boy did it and ran away, not good. could not close the inputstream.  {}", e);
				}
    		}
    	}
    }
}
