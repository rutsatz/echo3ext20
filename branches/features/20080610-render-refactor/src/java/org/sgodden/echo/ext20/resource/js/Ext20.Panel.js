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
EchoExt20.Panel = Core.extend(EchoExt20.ExtComponent, {
    
    $load: function() {
        Echo.ComponentFactory.registerType("Ext20Panel", this);
        Echo.ComponentFactory.registerType("E2P", this);
    },
    
    componentType: "Ext20Panel",
    
    doKeyPress: function() {
        this.fireEvent({type: "keyPress", source: this});
    },

    doToolClick: function() {
            this.fireEvent({type: "toolClick", source: this});
    }
    
});

/**
* Sync peer for panels.
*/
EchoExt20.PanelSync = Core.extend(EchoExt20.ExtComponentSync, {
    
    $load: function() {
        Echo.Render.registerPeer("Ext20Panel", this);
    },
	
    /**
     * Reference to a method to invoke when the server
     * update is complete.
     */
    _serverUpdateCompleteRef: null,
    
    $virtual: {
		
        // whether the component should be hidden when adding children, to avoid
        // noticeable progressive updates on slower rendering browsers such as FF.
        hideWhenAddingChildren: true,
		
        newExtComponentInstance: function(options) {
            return new Ext.Panel(options);
        },
		
        notifyChildLayoutUpdate: function() {
            this._childLayoutUpdatesOccurred = true;
        }
    },
    
    renderUpdate: function(update){

        EchoExt20.ExtComponentSync.prototype.renderUpdate.call(this, update);

        // check for any property updates
        if (update.getUpdatedProperty("title") != null) {
                this.extComponent.setTitle(this.component.get("title"));
        }
        
        if (update.hasRemovedChildren()) {
            var removedChildren = update.getRemovedChildren();
            for (var i = 0; i < removedChildren.length; i++) {
                // all children have to be ext components anyway
                var child = removedChildren[i];
                /*
                 * Not if it is a window, because it was never
                 * added to the parent ext container in the first place.
                 */
                if (!(child instanceof EchoExt20.Window)) {
                    var childExtComponent = child.peer.extComponent;
                    this.extComponent.remove(childExtComponent);
                }
            }
        }
        
        if (update.hasAddedChildren()) {
            // hide ourselves to prevent progressive rendering in slower browsers
            if (this.hideWhenAddingChildren) {
                /*
                 * If the only children added were windows, then there's no need to hide
                 * ourselves, since it does not involve changes to our own div.
                 */
                var doHide = false;
                var addedChildren = update.getAddedChildren();
                for (var i = 0; i < addedChildren.length && !doHide; i++) {
                    var componentType = addedChildren[i].componentType;
                    if ( !(componentType == "Ext20Window") ) {
                        doHide = true;
                    }
                }
                if (doHide) {
                    this.extComponent.getEl().dom.style.visibility = 'hidden';

                    // and add a server update complete listener to show ourselves again, if we haven't already
                    if (this._serverUpdateCompleteRef == null) {
                        this._serverUpdateCompleteRef = Core.method(this, this._serverUpdateComplete);
                        this.client.addServerUpdateCompleteListener(this._serverUpdateCompleteRef);
                    }
                }
	    }
            
            this._createChildItems(update, update.getAddedChildren());
            this._conditionalDoLayout(update.getAddedChildren());
        }
        
        this.syncExtComponent(update);
    },
    
    renderDisplay: function(update) {
        EchoExt20.ExtComponentSync.prototype.renderDisplay.call(this, update);
        this.syncExtComponent();
    },
    
    renderDispose: function(update) {
        EchoExt20.ExtComponentSync.prototype.renderDispose.call(this, update);
        if (this._serverUpdateCompleteRef != null) {
            this.client.removeServerUpdateCompleteListener(this._serverUpdateCompleteRef);
        }
    },
    
    syncExtComponent: function() {
        if (this._parentElement != null) {
            this.extComponent.setHeight(this._parentElement.offsetHeight);
            this.extComponent.setWidth(this._parentElement.offsetWidth);
        }
    },
    
    /**
     * Re-shows the component after being hidden during an update
     */
    _serverUpdateComplete: function() {
        this.extComponent.getEl().dom.style.visibility = 'visible';
    },
    
    createExtComponent: function(update, options) {
	// process basic properties
	options['bodyStyle'] = "";
		
        var padding = this.component.get("padding");
        if (padding != null) {
            options['bodyStyle'] = "padding: " + padding + ";";
        }
		
	if (this.component.get("background")) {
	    options['bodyStyle'] += "background: " + this.component.get("background") + ";";
	}
		
	var transparent = this.component.get("transparent");
	if (transparent) {
	    options['bodyStyle'] += "background: transparent;";
	} 
        
        var border = this.component.get("border");
        if (border != null) {
            options['border'] = border;
        }
        
        var collapsible = this.component.get("collapsible");
        if (collapsible != null) {
            options['collapsible'] = collapsible;
        }
        
        var height = this.component.get("height");
        if (height != null) {
            options['height'] = height;
        }
        
        var html = this.component.get("html");
        if (html != null) {
            options['html'] = html;
        }
        
        var title = this.component.get("title");
        if (title != null) {
            options['title'] = title;
        }
        
        var width = this.component.get("width");
        if (width != null) {
            options['width'] = width;
        }
		
        /*
         * Tool ids are passed as a comma-separated string.
         * These are the little tool icons that appear in the title
         * bar of the panel.
         * For a list of allowed tool names, see the documentation
         * for Ext.Panel
         */
        var toolIds = this.component.get("toolIds");
        if (toolIds != null) {
                
            toolConfigs = [];
            
            var toolIdArray = [];
            if (toolIds.indexOf(',') != -1) {
                    toolIdArray = toolIds.split(',');
            }
            else {
                    toolIdArray.push(toolIds);
            }

            for (var i = 0; i < toolIdArray.length; i++) {
                    var toolId = toolIdArray[i];
                    
                    var scopeHolder = new Object();
                    scopeHolder.component = this.component;
                    scopeHolder.toolId = toolId;
                    
                    var toolConfig = {
                            id: toolId,
                            handler: function() {
                                    this.component.set("toolIdClicked", this.toolId);
                                    this.component.doToolClick();
                            },
                            scope: scopeHolder
                    }
                    toolConfigs.push(toolConfig);
            }
            
            options['tools'] = toolConfigs;
        }

        // now handle the layout
        var layout = this.component.get("layout");
        if (layout != null) {
            if (layout instanceof EchoExt20.AccordionLayout) {
                options['layout'] = 'accordion';
                //options['layoutConfig'] = {titleCollapse: true, animate: true};
            }
            else if (layout instanceof EchoExt20.BorderLayout) {
                options['layout'] = 'border';
            }
            else if (layout instanceof EchoExt20.FormLayout) {
                options['layout'] = 'form';
                options['labelAlign'] = 'right'; // FIXME - configure the form layout properly
            }
            else if (layout instanceof EchoExt20.FitLayout) {
                options['layout'] = 'fit';
            }
            else if (layout instanceof EchoExt20.ColumnLayout) {
                options['layout'] = 'column';
            }
            else if (layout instanceof EchoExt20.TableLayout) {
                options['layout'] = 'table';
                options['layoutConfig'] = {columns: layout.columns};
                var defaultPadding = layout.defaultPadding;
                if (defaultPadding != null) {
                    options['defaults'] = {bodyStyle: 'padding:' + layout.defaultPadding};
                }
            }
            else {
                throw new Error("Unsupported layout");
            }
        }
        
        var children = this._createChildComponentArrayFromComponent();
        
        options['buttons'] = this._createButtons(update, children);
        
        this._makeToolbar(update, children, "top", options);
        this._makeToolbar(update, children, "bottom", options);
        
        this._registerKeyPresses(update, options);
        
        this.extComponent = this.newExtComponentInstance(options);
        
        if (children.length > 0) {
            this._createChildItems(update, children);
            this._conditionalDoLayout(children);
        }
        
        return this.extComponent;
    },
    
    _registerKeyPresses: function(update, options) {
        if (this.component.get("registeredKeyPresses") != null) {
            // FIXME - implement this properly
            options['keys'] = [];
            
            var keyString = this.component.get("registeredKeyPresses");
            //alert(keyString);
            if (keyString == "enter") {
                options['keys'].push({
                    key: Ext.EventObject.ENTER,
                    fn: this._handleKeyPress,
                    scope: this
                });
            }
            else if (keyString == "esc") {
                options['keys'].push({
                    key: Ext.EventObject.ESC,
                    fn: this._handleKeyPress,
                    scope: this
                });
            }
        }
    },
    
    _handleKeyPress: function(key, evt) {
        if (key == Ext.EventObject.ENTER) {
            this.component.set("keyPressed", "enter");
        }
        else if (key == Ext.EventObject.ESC) {
            this.component.set("keyPressed", "esc");
        }
        evt.stopEvent();
        this.component.doKeyPress();
    },
    
    _makeToolbar: function(update, children, position, options) {
        var done = false;
        
        for (var i = 0; i < children.length && !done; i++) {
            if (children[i] instanceof EchoExt20.Toolbar) {
                var childPosition = children[i].get("position");
                if (childPosition == position) {
                    // create the child
                    Echo.Render.renderComponentAdd(update, children[i], null);
                    var tbar = children[i].peer.extComponent;
                    if (tbar == null) {
                        throw new Error("No toobar ext component was created during renderAdd");
                    }
                    if (position == "top") {
                        options["tbar"] = tbar;
                    }
                    else if (position == "bottom") {
                        options["bbar"] = tbar;
                    }
                }
            }
        }
        
    },
    
    /**
     * Calls doLayout on the ext component under the following
     * conditions:
     * 1) we are not the top container
     * 2) any of our children
     * has a border layout (since they can size their north regions
     * wrongly).
     */
    _conditionalDoLayout: function(children) {
        var done = false;
        for (var i = 0; i < children.length && !done; i++) {
            var layout = children[i].get("layout");
            if ( layout != null && layout instanceof EchoExt20.BorderLayout ) {
                this.extComponent.doLayout();
                done = true;
            }
        }
    },
    
    _createChildComponentArrayFromComponent: function() {
        var componentCount = this.component.getComponentCount();
        var children = new Array(componentCount);
        for (var i = 0; i < componentCount; i++) {
            children[i] = this.component.getComponent(i);
        }
        return children;
    },
    
    _createButtons: function(update, children) {
        var buttons = [];
        for (var i = 0; i < children.length; i++) {
            var child = children[i];
            if (child instanceof EchoExt20.Button
                    && child.get("addToButtonBar") == true) {
                Echo.Render.renderComponentAdd(update, child, null);
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
    
    _createChildItems: function(update, children) {
        for (var i = 0; i < children.length; i++) {
            var child = children[i];
            /*
             *  if this is not an ext20 component, we need to wrap it
             *  so that it can operate within an ext container.
             */
            if (! (child instanceof EchoExt20.ExtComponent) ) {
                    // we don't renderAdd here - ext does it lazily
                    var wrapper = new EchoExt20.Echo3SyncWrapper(update, child);
                    this.extComponent.add(wrapper);
            }
            else if (child instanceof EchoExt20.Window) {
                this._createWindow(update, child);
            }
            else if ( 
                  (
                    !(child instanceof EchoExt20.Button)
                    || (child instanceof EchoExt20.Button && child.get("addToButtonBar") == false)
                  )
                  && !(child instanceof EchoExt20.Toolbar) // toolbars handled separately
                ) {
                Echo.Render.renderComponentAdd(update, child, null); 
                
                // add the ext component created by the peer to the child items array
                var childExtComponent = child.peer.extComponent;
                if (childExtComponent == null) {
                    throw new Error("No child ext component was created during renderAdd for component type: " + child.componentType);
                } 
                else {
                    // make sure we can get back to the echo component from the ext component
                    childExtComponent.echoComponent = child;
                    this.extComponent.add(childExtComponent);
                }
            }
        }
		
        // now make sure that the child indexes are set, so that, for instance, when a certain
        // tab is selected in a tabbed pane, it can work out what the index of the displayed component
        // is.
        for (var i = 0; i < this.component.getComponentCount(); i++) {
                var child = this.component.getComponent(i);
                child.childIndex = i;
        }
    },
    
    _createWindow: function(update, child) {
        Echo.Render.renderComponentAdd(update, child, null);
        EchoExt20.ExtComponentSync.openWindows.push(child.peer.extComponent);
        child.peer.extComponent.doLayout();
        child.peer.extComponent.show();
    }
    
});