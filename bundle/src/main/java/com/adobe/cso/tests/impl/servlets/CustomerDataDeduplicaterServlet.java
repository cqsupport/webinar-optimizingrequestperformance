package com.adobe.cso.tests.impl.servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

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

import com.adobe.cso.tests.api.services.CustomerDataDeduplicaterService;

@SuppressWarnings("serial")
@Component(
        label = "%CustomerDataDeduplicaterServlet.label",
        description = "%CustomerDataDeduplicaterServlet.description",
        metatype = true,
        immediate=true)
@Service(value = Servlet.class)
@Properties({
        @Property(name = "service.description", value = "%CustomerDataDeduplicaterServlet.service.description"),
        @Property(name = "sling.servlet.resourceTypes", value = {"sling/servlet/default"}, propertyPrivate = true),
        @Property(name = "sling.servlet.methods", value = {"POST", "GET"}, propertyPrivate = true),
        @Property(name = "sling.servlet.selectors", value = {"deduplicate"}),
        @Property(name = "sling.servlet.extensions", value = {"csv"}, propertyPrivate = true)
})
public class CustomerDataDeduplicaterServlet extends SlingAllMethodsServlet {
	private static final Logger logger = LoggerFactory.getLogger(CustomerDataDeduplicaterServlet.class);
	
    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    protected CustomerDataDeduplicaterService dedupService;
    
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
    	boolean processed = false;
    	
    	for (String selector : selectors)
    	{
    		if (selector.equals("deduplicate"))
    		{
    	    	if (logger.isDebugEnabled()) logger.debug("{} selector detected", selector);
    	    	
    			try {
					deduplicateData(request, response);
					processed = true;
					
				} catch (ValueFormatException e) {
					logger.error("error: {}", e);
				} catch (PathNotFoundException e) {
					logger.error("error: {}", e);
				} catch (RepositoryException e) {
					logger.error("error: {}", e);
				}
    		}
    	}
    	
    	if (!processed)
    		logger.warn("unhandled selector(s) in request: requestSelectors={}",selectors);
    }
    
    protected void deduplicateData(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ValueFormatException, PathNotFoundException, RepositoryException
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
    	
    	Node resourceNode = resource.adaptTo(Node.class);
    	Node contentNode = resourceNode.getNode("jcr:content");
    	InputStream resourceInputStream = contentNode.getProperty("jcr:data").getBinary().getStream();
    	
    	if (resourceInputStream == null)
    	{
    		logger.error("the resource could not be adapated to an InputStream: {}", request.getResource().getPath());
    		return;
    	}
    	
    	dedupService.setDataInputStream(resourceInputStream);
    	
    	try {
			dedupService.parse();
		} catch (Exception e) {
			logger.error("problem parsing the customer data: {}", e);
		}
    }
}
