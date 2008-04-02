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
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

import org.sgodden.echo.ext20.ComboBox;
import org.sgodden.echo.ext20.TextField;

public class ComboBoxPeer
        extends TextFieldPeer {

    protected static final Service COMBO_BOX_SERVICE = JavaScriptService.forResource("EchoExt20.ComboBox",
            "/org/sgodden/echo/ext20/resource/js/Ext20.ComboBox.js");

    static {
        WebContainerServlet.getServiceRegistry().add(COMBO_BOX_SERVICE);
    }

    public ComboBoxPeer() {
        super();
    }

    @Override
    public Class getComponentClass() {
        return ComboBox.class;
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return shortType ? "E2CB" : "Ext20ComboBox";
    }

    /**
     * @see nextapp.echo.webcontainer.ComponentSynchronizePeer#init(Context)
     */
    @Override
    public void init(Context context) {
        super.init(context);
    //ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
    //serverMessage.addLibrary(TEXT_FIELD_SERVICE.getId());
    }
}
