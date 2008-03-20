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
package org.sgodden.echo.ext20.peers;

import nextapp.echo.app.Component;
import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

import org.sgodden.echo.ext20.TimeField;

public class TimeFieldPeer
        extends AbstractComponentSynchronizePeer {

    protected static final Service TIME_FIELD_SERVICE = JavaScriptService.forResource("EchoExt20.TimeField",
            "/org/sgodden/echo/ext20/resource/js/Ext20.TimeField.js");

    static {
        WebContainerServlet.getServiceRegistry().add(TIME_FIELD_SERVICE);
    }

    public TimeFieldPeer() {
        super();
        addOutputProperty(TimeField.TIME_CHANGED_PROPERTY);
        addOutputProperty(TimeField.FIELD_LABEL_PROPERTY);
        addOutputProperty(TimeField.ALLOW_BLANK_PROPERTY);
        addOutputProperty(TimeField.TIME_FORMAT_PROPERTY);
    }

    public Class getComponentClass() {
        return TimeField.class;
    }

    public String getClientComponentType(boolean shortType) {
        return shortType ? "E2TMF" : "Ext20TimeField";
    }

    
    /**
     * @see nextapp.echo.webcontainer.AbstractComponentSynchronizePeer#getInputPropertyClass(java.lang.String)
     */
    @Override
    public Class getInputPropertyClass(String propertyName) {
        if (TimeField.TIME_CHANGED_PROPERTY.equals(propertyName)) {
            return String.class;
        }
        return null;
    }

    
    /**
     * @see nextapp.echo.webcontainer.ComponentSynchronizePeer#storeInputProperty(Context, Component, String, int, Object)
     */
    @Override
    public void storeInputProperty(Context context, Component component, String propertyName, int propertyIndex, Object newValue) {
        if (propertyName.equals(TimeField.TIME_CHANGED_PROPERTY)) {
            ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
            clientUpdateManager.setComponentProperty(component, TimeField.TIME_CHANGED_PROPERTY, newValue);
        }
    }

    /**
     * @see nextapp.echo.webcontainer.ComponentSynchronizePeer#init(Context)
     */
    @Override
    public void init(Context context) {
        super.init(context);
    //ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
    //serverMessage.addLibrary(DATE_FIELD_SERVICE.getId());
    }
}
