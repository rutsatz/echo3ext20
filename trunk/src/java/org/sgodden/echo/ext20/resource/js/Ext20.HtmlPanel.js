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
EchoExt20.HtmlPanel = Core.extend(EchoApp.Component, {
	
    $load: function() {
        EchoApp.ComponentFactory.registerType("Ext20HtmlPanel", this);
        EchoApp.ComponentFactory.registerType("E2HP", this);
    },

    componentType: "Ext20HtmlPanel"
	
});

// TODO - should be merged into Panel
EchoExt20.HtmlPanelSync = Core.extend(EchoExt20.ExtComponentSync, {

    $load: function() {
        EchoRender.registerPeer("Ext20HtmlPanel", this);
    },
    
    createExtComponent: function(update, options) {
    	//options['buttons'] = this._createButtons(update);
    	//options['layout'] = 'fit';
    	
    	var title = this.component.get('title');
    	if (title != null) {
    		options['title'] = title;
    	}
    	
    	var collapsible = this.component.get('collapsible');
    	if (collapsible != null) {
    		options['collapsible'] = collapsible;
    	}
         
        var border = this.component.get("border");
        if (border != null) {
            options['border'] = border;
        }
        
        var padding = this.component.get("padding");
        if (padding != null) {
            options['bodyStyle'] = "padding: " + padding;
        }
                
        var width = this.component.get("width");
        if (width != null) {
            options['width'] = width;
        }
        
        options['html'] = this.component.get('html');
        
        return new Ext.Panel(options);
    },
      
    _createButtons: function(update) {
    	var buttons = [];
		for (var i = 0; i < this.component.getComponentCount() && child == null; ++i) {
            var child = this.component.getComponent(i);
            if (child instanceof EchoExt20.Button) {
		    	EchoRender.renderComponentAdd(update, child, null);
		    	var button = child.peer.extComponent;
		    	if (button == null) {
		    		throw new Error("No child ext component was created during renderAdd for component type: " + child.componentType);
		    	}
		    	else {
		    		buttons.push(button)
		    	}
            }
        }
        return buttons;
    },
    
    renderUpdate: function(){}

});
