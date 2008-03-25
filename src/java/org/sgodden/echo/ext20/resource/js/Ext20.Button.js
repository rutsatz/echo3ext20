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
EchoExt20.Button = Core.extend(EchoApp.AbstractButton, {
	
    $load: function() {
        EchoApp.ComponentFactory.registerType("Ext20Button", this);
        EchoApp.ComponentFactory.registerType("E2B", this);
    },

    componentType: "Ext20Button"
	
});

EchoExt20.ButtonSync = Core.extend(EchoExt20.ExtComponentSync, {

    $load: function() {
        EchoRender.registerPeer("Ext20Button", this);
    },
    
    _handleClickEventRef: null,
    
    $construct: function() {
    	this._handleClickEventRef = Core.method(this, this._handleClickEvent);
    },
    
    createExtComponent: function(update, options) {
    
    	options['text'] = this.component.get("text")
    
    	var extComponent = new Ext.Button(options);
    	extComponent.on('click', this._handleClickEventRef);
    	
    	return extComponent;
    },
    
    _handleClickEvent: function() {
    	this.component.doAction();
    },
    
    renderUpdate: function(){}

});