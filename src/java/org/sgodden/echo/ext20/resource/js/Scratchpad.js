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

    _createPanelString: function(region) {
		var ret = "{\n" + "\tregion: '" + region + "',";
		ret += "\n\txtype: 'panel',";
		ret += "\n\tlayout: 'fit'";
		ret +="\n},\n";

        return ret;
    },

    
    // from renderAdd
    
		return;
    
    	this._renderAddStaticViewport(parentElement);
    	return;
    
		var layout = this.component.getRenderProperty("layout");
		alert(parentElement);

		// just testing a border layout right now
		var s = "var viewport = new Ext.Panel({\n";
		
		s += "\trenderTo: parentElement,";
		
		if (layout instanceof EchoExt20.BorderLayout) {
			s += "layout: 'border',\n";
			s += "items: [\n";

			if (layout.north) {
				s += this._createPanelString('north');
			}
			if (layout.east) {
				s += this._createPanelString('east');
			}
			if (layout.south) {
				s += this._createPanelString('south');
			}
			if (layout.west) {
				s += this._createPanelString('west');
			}
			if (layout.center) {
				s += this._createPanelString('center');
			}
		}
		s += "]\n});";

		alert(s);
		
		eval(s);
		
		this._viewport = viewport;
		
		alert(viewport.layout);
    
    },

    
    // END
    
    // FROM renderdisplay
    // END
    
    
EchoExt20.ViewportSync = Core.extend(EchoRender.ComponentSync, {

    $load: function() {
        EchoRender.registerPeer("Ext20Viewport", this);
    },
    
    renderAdd: function(update, parentElement) {
		this._parentElement = parentElement;
    },
    
	renderDisplay: function() {
		alert("Render display");
	},

	renderDispose: function(update) {
		alert("Not implemented");
	},
    
    renderUpdate: function(update) {
    	// find out how many children we have and add them to the correct regions
		alert("Not implemented");
    },
    
});

    _createViewport: function() {
		//alert(this._parentElement.clientHeight);

		var viewport = new Ext.Panel({
			renderTo: this._parentElement,
			layout: 'border',
			height: this._parentElement.clientHeight,
			width: this._parentElement.clientWidth,
			items: [{
			    region: 'north',
			    html: 'Page Title',
			    autoHeight: true,
			    border: false,
			    margins: '0 0 5 0'
				}, 
				{
			    region: 'west',
			    collapsible: true,
			    title: 'Navigation',
			    xtype: 'treepanel',
			    width: 200,
			    autoScroll: true,
			    split: true,
			    loader: new Ext.tree.TreeLoader(),
			    root: new Ext.tree.AsyncTreeNode({
			        expanded: true,
			        children: [{
			            text: 'Menu Option 1',
			            leaf: true
			        }, {
			            text: 'Menu Option 2',
			            leaf: true
			        }, {
			            text: 'Menu Option 3',
			            leaf: true
			        }]
			    }),
			    rootVisible: false,
			    listeners: {
			        click: function(n) {
			            Ext.Msg.alert('Navigation Tree Click', 'You clicked: "' + n.attributes.text + '"');
			        }
			    }
			}, 
			{
			    region: 'center',
			    xtype: 'panel',
			    layout: 'fit'
			},
			{
			    region: 'south',
			    title: 'Information',
			    collapsible: true,
			    html: 'Information goes here',
			    split: true,
			    height: 100,
			    minHeight: 100
			}]
		});
		var tabPanel = new Ext.TabPanel({
		    split: true,
		    deferredRender: false,
		    items: {
		        title: 'Default Tab',
		        html: 'The first tab\'s content. Others may be added dynamically'
		    }
		});
		tabPanel.setActiveTab(0);
		
		tabPanel.add({
		    title: 'Tab 2',
		    html: 'The second tab<p/>asdasd'
		});

		// create the button panel and add it to the tab panel
		var buttonsPanel = new Ext.Panel({
		    title: 'Tab 3',
		    layout: 'table',
		    defaults: {
		        // applied to each contained panel
		        bodyStyle:'padding:20px'
		    },
		    layoutConfig: {
		        // The total column count must be specified here
		        columns: 2
		    }
		});

		// create a panel for the first button and add it to the button panel
		var button1Panel = new Ext.Panel({layout: 'fit'});
		buttonsPanel.add(button1Panel);
		
		var button = new Ext.Button({
		    text: 'My first button'
		});
		button1Panel.add(button);
		button1Panel.doLayout();

		// create a panel for the first button and add it to the button panel
		var button2Panel = new Ext.Panel({layout: 'fit'});
		buttonsPanel.add(button2Panel);
		
		var button2 = new Ext.Button({
		    text: 'My second button'
		});
		button2Panel.add(button2);
		button2Panel.doLayout();
		
		buttonsPanel.doLayout();
		
		tabPanel.add(buttonsPanel);
		tabPanel.doLayout();

		// and add the tab panel to the center of the viewport
		var center = viewport.getComponent(2);
		center.add(tabPanel);
		center.doLayout();
		
		viewport.syncSize();

		this._viewport = viewport;
		this._parentElement.style.zoom = '100%'; // IE6 HACK to force display
    }

    ====
    
    
		// just testing a border layout right now
		var s = "var viewport = new Ext.Panel({\n";
		
		var parentElement = this._parentElement;
		
		s += "id: '" + this.component.renderId + "',\n";
		s += "renderTo: parentElement,\n";
		
		if (layout instanceof EchoExt20.BorderLayout) {
			s += "layout: 'border',\n";
			s += "items: [\n";
			
			var started = false;

			if (layout.north) {
				s += this._createPanelString('north');
				started = true;
			}
			if (layout.east) {
				if (started == true) {
					s += ",";
				}
				s += this._createPanelString('east');
				started = true;
			}
			if (layout.south) {
				if (started == true) {
					s += ",";
				}
				s += this._createPanelString('south');
				started = true;
			}
			if (layout.west) {
				if (started == true) {
					s += ",";
				}
				s += this._createPanelString('west');
				started = true;
			}
			if (layout.center) {
				if (started == true) {
					s += ",";
				}
				s += this._createPanelString('center');
			}
		}
		s += "]\n});";

		//alert(s);
		
		eval(s);
		
		this._viewport = viewport;
    
    ===
    
        
    _createPanelString: function(region) {
		var ret = "{\n" + "\tregion: '" + region + "',";
		ret += "\n\txtype: 'panel',";
		ret += "\n\tlayout: 'fit',";
		ret += "\n\thtml: '" + region + "'";
		ret +="\n}\n";

        return ret;
    }
    