/* =================================================================
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
#
# ================================================================= */
package org.sgodden.echo.ext20.testapp;

import java.util.ArrayList;
import java.util.List;


import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 * Application servlet.
 * 
 * @author goddens
 *
 */
public class AppServlet extends WebContainerServlet {

	private static final long serialVersionUID = 20080107L;
	
	private static final Service extService;
	private static final Service echoExtService;
	
	static {
		List resourceList = new ArrayList();
		resourceList.add("/adapter/ext/ext-base.js");
		resourceList.add("/ext-all-debug.js");
		
		String[] resources = new String[resourceList.size()];
		resourceList.toArray(resources);
		extService = JavaScriptService.forResources("Ext20", resources);
		
		resourceList = new ArrayList();
		resourceList.add("/org/sgodden/echo/ext20/resource/js/Ext20.js");
		resourceList.add("/org/sgodden/echo/ext20/resource/js/Ext20.Button.js");
		resourceList.add("/org/sgodden/echo/ext20/resource/js/Ext20.HtmlPanel.js");
		resourceList.add("/org/sgodden/echo/ext20/resource/js/Ext20.Panel.js");
		resourceList.add("/org/sgodden/echo/ext20/resource/js/Ext20.TabbedPane.js");
		resourceList.add("/org/sgodden/echo/ext20/resource/js/Ext20.TablePanel.js");
		resourceList.add("/org/sgodden/echo/ext20/resource/js/Ext20.TextField.js");
		resourceList.add("/org/sgodden/echo/ext20/resource/js/Ext20.HtmlEditor.js");
		resourceList.add("/org/sgodden/echo/ext20/resource/js/Ext20.GridPanel.js");
		resourceList.add("/json2.js");
		
		resources = new String[resourceList.size()];
		resourceList.toArray(resources);
		echoExtService = JavaScriptService.forResources("EchoExt20", resources);
	}
	
	public AppServlet() {
		super();
		addStartupScript(extService);
		addStartupScript(echoExtService);
	}

	public ApplicationInstance newApplicationInstance() {
		return new AppInstance();
	}

}
