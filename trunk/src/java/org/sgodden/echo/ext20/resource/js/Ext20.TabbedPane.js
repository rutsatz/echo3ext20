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
EchoExt20.TabbedPane = Core.extend(EchoExt20.ExtComponent, {
    
    $load: function() {
        EchoApp.ComponentFactory.registerType("Ext20TabbedPane", this);
        EchoApp.ComponentFactory.registerType("E2TP", this);
    },

    focusable: true,
    
    componentType: "Ext20TabbedPane",
    
    doActiveTabChange: function(){
        this.fireEvent({
            type: "activeTabChangeEvent",
            source: this
        });
    }
    
});

/**
* Sync peer for tabbed panes.
*/
EchoExt20.TabbedPaneSync = Core.extend(EchoExt20.ExtComponentSync, {
    
    $load: function() {
        EchoRender.registerPeer("Ext20TabbedPane", this);
    },
	
	_tabCount: 0,
    
    createExtComponent: function(update, options) {
        options['activeTab'] = this.component.get("activeTabIndex");
        options['buttons'] = this._createButtons(update);
        
        var ret = new Ext.TabPanel(options);
        ret.on(
			"beforetabchange", 
			function(tabPanel, newTab, oldTab){
				this.component.set(
					"activeTabIndex",
					newTab.echoComponent.tabIndex
					);
				this.component.doActiveTabChange();
			},
			this
		);
        ret.on(
			"tabchange", 
			function(tabPanel, newTab, oldTab){
				ret.doLayout();
			},
			this
		);
        
        this._createChildItems(ret, update);
        
        return ret;
        
    },
    
    _createButtons: function(update) {
        var buttons = [];
        for (var i = 0; i < this.component.getComponentCount(); ++i) {
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
    
    _createChildItems: function(tabPanel, update) {
        for (var i = 0; i < this.component.getComponentCount(); ++i) {
            var child = this.component.getComponent(i);
            if ( !(child instanceof EchoExt20.Button) ) {
                EchoRender.renderComponentAdd(update, child, null);
                
                // add the ext component created by the peer to the child items array
                var childExtComponent = child.peer.extComponent;
				
				// we need to be able to get back to the tab index later
				
                if (childExtComponent == null) {
                    throw new Error("No child ext component was created during renderAdd for component type: " + child.componentType);
                } 
                else {
                    tabPanel.add(childExtComponent);
					
					// we need to be able to get back to the tab index later
					child.tabIndex = i; 
					childExtComponent.echoComponent = child;
                }
            }
        }    	
    },
    
	// FIXME - adding tabs during an update
    renderUpdate: function(){}
    
});