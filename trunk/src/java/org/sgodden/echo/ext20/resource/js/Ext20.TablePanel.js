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
EchoExt20.TablePanel = Core.extend(EchoApp.Component, {
	
    $load: function() {
        EchoApp.ComponentFactory.registerType("Ext20TablePanel", this);
        EchoApp.ComponentFactory.registerType("E2TBP", this);
    },

    componentType: "Ext20TablePanel"
	
});

EchoExt20.TablePanelSync = Core.extend(EchoExt20.ExtComponentSync, {

    $load: function() {
        EchoRender.registerPeer("Ext20TablePanel", this);
    },
    
    _innerPanel: null,
    _outerPanel: null,
    
    createExtComponent: function(update, options) {
    	options['buttons'] = this._createButtons(update);
    	options['layout'] = 'fit';
    	
    	var title = this.component.get('title');
    	if (title != null) {
    		options['title'] = title;
    	}
    	
    	var collapsible = this.component.get('collapsible');
    	if (collapsible != null) {
    		options['collapsible'] = collapsible;
    	}
    	
    	this._outerPanel = new Ext.Panel(options);
    	this._createInnerPanel(update);
    	
    	this._outerPanel.add(this._innerPanel);
    	
    	return this._outerPanel;
    },
    
    _createInnerPanel: function(update) {
    	this._innerPanel = new Ext.Panel({
    		border: false,
    		layout: 'table',
    		layoutConfig: {columns: 1}
    	});
    	
    	this._createChildren(update);
    },
    
        
    _createChildren: function(update) {
    	//alert("Creating child items for null layout");
		for (var i = 0; i < this.component.getComponentCount(); ++i) {
            var child = this.component.getComponent(i);
            if (!(child instanceof EchoExt20.Button)) {
            	this.extChildOptions = {};
	    		EchoRender.renderComponentAdd(update, child, null); // null because ext components create the necessary extra divs themselves
		    	
		    	// add the ext component created by the peer to the child items array
		    	var childExtComponent = child.peer.extComponent;
		    	if (childExtComponent == null) {
		    		throw new Error("No child ext component was created during renderAdd for component type: " + child.componentType);
		    	} 
		    	else {
		    		this._innerPanel.add(childExtComponent);
		    	}
		    	delete this.extChildOptions;
	    	}
		}
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
