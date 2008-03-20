This project contains echo3 components and rendering peers for the Ext 2.0 widget set.

Explanation of project layout
-----------------------------
It's a pretty standard webapp layout.  In the 'contrib' directory, you will find the results of running "ant dist" in the svn checkout of the echo3 trunk.  The "Echo3_App.jar" and "Echo3_WebContainer.jar" have been extracted from there and put into src/webapp/WEB-INF/lib.

If you want to get source attachments to the echo3 classes, then you need to extract that tar.gz where it is.

BUILDING
--------
1. Copy build.properties.sample to build.properties and alter the two properties in there to point to the correct locations for the servlet jar and 
your tomcat deployment directoy.
2. Run ant.  The war file will be produced in the 'dist' directory and copied to the deployment directory.
