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
EchoExt20.TextArea = Core.extend(EchoExt20.TextField, {
	
    $load: function() {
        EchoApp.ComponentFactory.registerType("Ext20TextArea", this);
        EchoApp.ComponentFactory.registerType("E2TA", this);
    },

    componentType: "Ext20TextArea"
	
});

EchoExt20.TextAreaSync = Core.extend(EchoExt20.TextFieldSync, {

    $load: function() {
        EchoRender.registerPeer("Ext20TextArea", this);
    },

    newExtComponentInstance: function(options) {
        return new Ext.form.TextArea(options);
    }

});
