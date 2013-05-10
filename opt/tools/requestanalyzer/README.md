CQ Performance Analysis: Optimizing Response Time
====================================

**Sample code for the following webinar on Thursday, October 11, 2012.**

Sling Request Analyzer
------------------------------------
The [Sling Request Analyzer] is a tool to deploy into your Felix console, which can then be configured to give you a more detailed breakdown of the request specifics.   For ongoing use, we request you download the most current version of the tool at [Sling Request Analyzer Source Code].

For the purpose of convenience, the compiled version (as of time of writing) that was used in the webinar is also linked here: [Sling Request Analyzer Temp Build].


Usage
------------------------------------

For full installation details, see [Sling Request Analyzer].

Basic usage:

1. Install the [org.apache.sling.reqanalyzer-0.0.1-SNAPSHOT.jar] as an OSGi bundle to the Apache Felix Web Console (/system/console)

2. This will generate a file logs/requesttracker.txt

3. The same jar installed in step 1 allows you to inspect the requesttracker.txt file when it is used as a standalone java app:

  $ java -jar org.apache.sling.reqanalyzer-0.0.1-R1352779.jar `<filename>`


[Sling Request Analyzer]: http://sling.staging.apache.org/documentation/bundles/request-analysis.html
[Sling Request Analyzer Source Code]: http://svn.apache.org/viewvc/sling/trunk/contrib/extensions/reqanalyzer/
[Sling Request Analyzer Temp Build]: org.apache.sling.reqanalyzer-0.0.1-SNAPSHOT.jar
[org.apache.sling.reqanalyzer-0.0.1-SNAPSHOT.jar]: org.apache.sling.reqanalyzer-0.0.1-SNAPSHOT.jar
