<%--

  Customer De-duplicater component.

  

--%><%@page import="org.apache.sling.api.resource.Resource"%>
<%@page import="com.adobe.cso.tests.services.CustomerDataDeduplicaterService"%>
<%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%><%
	CustomerDataDeduplicaterService service = sling.getService(CustomerDataDeduplicaterService.class);
	Resource csvResource = resourceResolver.getResource(service.getCustomerDataContentLocation());
	
	if (csvResource.adaptTo(File.class))
	
	
%>