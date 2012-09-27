package com.adobe.cso.tests.impl.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cso.tests.api.services.YahooQuestionsService;

@SuppressWarnings("serial")
@Component(
        label = "%YahooQuestionsServlet.label",
        description = "%YahooQuestionsServlet.description",
        metatype = true,
        immediate=true)
@Service(value = Servlet.class)
@Properties({
        @Property(name = "service.description", value = "%YahooQuestionsServlet.service.description"),
        @Property(name = "sling.servlet.resourceTypes", value = {"sling/servlet/default"}, propertyPrivate = true),
        @Property(name = "sling.servlet.methods", value = {"POST", "GET"}, propertyPrivate = true),
        @Property(name = "sling.servlet.selectors", value = {"getquestions"}),
        @Property(name = "sling.servlet.extensions", value = {"html"}, propertyPrivate = true)
})
public class YahooQuestionsServlet extends SlingAllMethodsServlet {
	private static final Logger logger = LoggerFactory.getLogger(YahooQuestionsServlet.class);
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    protected YahooQuestionsService questServlet;
    
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
    		if (selector.equals("getquestions"))
    		{
    	    	if (logger.isDebugEnabled())logger.debug("{} selector detected", selector);
    	    	
    			getQuestions(request, response);
    			processed = true;
    		}
    	}
    	
    	if (!processed)
    		logger.warn("unhandled selector(s) in request: requestSelectors={}",selectors);
    }
    
    protected void getQuestions(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
    {
    	String requestSearchTerms = (String) request.getAttribute("searchTerms");
    	String requestMaxCountString = (String) request.getAttribute("maxCount");
    	Integer requestMaxCount = requestMaxCountString != null ? Integer.parseInt(requestMaxCountString) : 0;
    	
    	if (logger.isDebugEnabled()) logger.debug("request variables set to: searchTerms={}; maxCount={}", requestSearchTerms, requestMaxCount);
    	
    	String searchTerms = requestSearchTerms == null ? questServlet.getYahooQuestionsSearchTerms() : requestSearchTerms;
    	Integer maxCount = requestMaxCount <= 0 ? 1 : requestMaxCount;
    	
    	if (logger.isDebugEnabled()) logger.debug("search variables coalesced to: searchTerms={}; maxCount={}", searchTerms, maxCount);
    	
    	questServlet.setYahooQuestionsSearchTerms(searchTerms);
    	questServlet.setYahooQuestionsMaxResultsPerTerm(maxCount);
    	
    	//For test purposes, we don't really care about the results, more about the call
    	questServlet.retrieveQuestions();
    	
    	if (logger.isDebugEnabled()) logger.debug("questions retrieved");
    }
    
    @Activate
    public void activate(ComponentContext context)
    {
    	
    }
}
