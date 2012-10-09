CQ Performance Analysis: Optimizing Response Time
====================================

**Sample code for the following webinar on Thursday, October 11, 2012.**

CQ Request Log Analyzer (RLog)
------------------------------------

Using RLog.jar to see long running requests, more details at the [RLog KB Article].

CQ includes various helper tools located in:
    <cq-installation-dir>/crx-quickstart/opt/helpers

One of these, rlog.jar, can be used to quickly sort request.log so that requests are displayed by duration, from longest to shortest time.


### Usage

The following command shows the possible arguments:

<pre>
$java -jar rlog.jar 
Request Log Analyzer Version 21584 Copyright 2005 Day Management AG 
Usage: 
  java -jar rlog.jar [options] <filename> 
Options: 
  -h               Prints this usage. 
  -n <maxResults>  Limits output to <maxResults> lines. 
  -m <maxRequests> Limits input to <maxRequest> requests. 
  -xdev            Exclude POST request to CRXDE.
</pre>


For example, you can run it specifying request.log file as a parameter and show the 10 first requests that have the longest duration:


### Sample Output

$ java -jar ../opt/helpers/rlog.jar -n 10 request.log 

     18051ms 31/Mar/2009:11:15:34 +0200 200 GET /content/geometrixx/en/company.html text/ html 
      2198ms 31/Mar/2009:11:15:20 +0200 200 GET /libs/cq/widgets.js application/x-javascript
      1981ms 31/Mar/2009:11:15:11 +0200 200 GET /libs/wcm/content/welcome.html text/html
      1973ms 31/Mar/2009:11:15:52 +0200 200 GET /content/campaigns/geometrixx.teasers..html text/html
      1883ms 31/Mar/2009:11:15:20 +0200 200 GET /libs/security/cq-security.js application/x-javascript
      1876ms 31/Mar/2009:11:15:20 +0200 200 GET /libs/tagging/widgets.js application/x-javascript
      1869ms 31/Mar/2009:11:15:20 +0200 200 GET /libs/tagging/widgets/themes/default.js application/x-javascript
      1729ms 30/Mar/2009:16:45:56 +0200 200 GET /libs/wcm/content/welcome.html text/html; charset=utf-8 
      1510ms 31/Mar/2009:11:15:34 +0200 200 GET /bin/wcm/contentfinder/asset/view.json/ content/dam?_dc=1238490934657&query=&mimeType=image&_charset_=utf-8 application/json
      1462ms 30/Mar/2009:17:23:08 +0200 200 GET /libs/wcm/content/welcome.html text/html; charset=utf-8 
	  
You may need to concatenate the individual request.log files if you need to do this operation on a large data sample.


[RLog KB Article]: http://dev.day.com/docs/en/cq/current/howto/performance_monitor.html#Using%20rlog.jar%20to%20find%20requests%20with%20long%20duration%20times