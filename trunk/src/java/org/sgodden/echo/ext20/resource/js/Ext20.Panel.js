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
EchoExt20.Panel = Core.extend(EchoApp.Component, {
    
    $load: function() {
        EchoApp.ComponentFactory.registerType("Ext20Panel", this);
        EchoApp.ComponentFactory.registerType("E2P", this);
    },
    
    componentType: "Ext20Panel"
    
});

/**
* Sync peer for panels.
*/
EchoExt20.PanelSync = Core.extend(EchoExt20.ExtComponentSync, {
    
    _serverUpdateCompleteRef: null,
    
    $load: function() {
        EchoRender.registerPeer("Ext20Panel", this);
    },
    
    $virtual: {
        newExtComponentInstance: function(options) {
            return new Ext.Panel(options);
        }
    },
    
    renderUpdate: function(update){
        
        if (update.hasRemovedChildren()) {
            var removedChildren = update.getRemovedChildren();
            for (var i = 0; i < removedChildren.length; i++) {
                // all children have to be ext components anyway
                var child = removedChildren[i];
                var childExtComponent = child.peer.extComponent;
                this.extComponent.remove(childExtComponent);
            }
        }
        
        if (update.hasAddedChildren()) {
            // hide ourselves to prevent progressive rendering in slower browsers
            this.extComponent.getEl().dom.style.visibility = 'hidden';
            
            // and add a server update complete listener if we haven't already
            if (this._serverUpdateCompleteRef == null) {
                this._serverUpdateCompleteRef = Core.method(this, this._serverUpdateComplete);
                this.client.addServerUpdateCompleteListener(this._serverUpdateCompleteRef);
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

        // handle properties
        var padding = this.component.get("padding");
        if (padding != null) {
            options['bodyStyle'] = "padding: " + padding;
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

        // now handle the layout
        var layout = this.component.get("layout");
        if (layout != null) {
            if (layout instanceof EchoExt20.AccordionLayout) {
                options['layout'] = 'accordion';
                options['layoutConfig'] = {titleCollapse: true, animate: true};
            }
            else if (layout instanceof EchoExt20.BorderLayout) {
                options['layout'] = 'border';
            }
            else if (layout instanceof EchoExt20.FormLayout) {
                options['layout'] = 'form';
                options['labelAlign'] = 'right'; // FIXME - configure the form layout properly
            }
            else if (layout instanceof EchoExt20.FitLayout) {
                if (this.component.getComponentCount() > 1) {
                    throw new Error("Only one child may be specified for layout 'fit'");
                }
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
//        else {
//            options['layout'] = 'table';
//            options['layoutConfig'] = {columns: 1};
//        }
        
        var children = this._createChildComponentArrayFromComponent();
        
        options['buttons'] = this._createButtons(update, children);
        
        this._makeToolbar(update, children, "top", options);
        this._makeToolbar(update, children, "bottom", options);
        
        this.extComponent = this.newExtComponentInstance(options);
        
        if (children.length > 0) {
            this._createChildItems(update, children);
            this._conditionalDoLayout(children);
        }
        
        return this.extComponent;
    },
    
    _makeToolbar: function(update, children, position, options) {
        var done = false;
        
        for (var i = 0; i < children.length && !done; i++) {
            if (children[i] instanceof EchoExt20.Toolbar) {
                var childPosition = children[i].get("position");
                if (childPosition == position) {
                    // create the child
                    EchoRender.renderComponentAdd(update, children[i], null);
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
        
        //if (this._parentElement != null) {
        //    return;
        //}
        
        var done = false;
        for (var i = 0; i < children.length && !done; i++) {
            var layout = children[i].get("layout");
            if ( layout != null && layout instanceof EchoExt20.BorderLayout ) {
                //alert("Calling doLayout on " + this.component.renderId);
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
    
    _createChildItems: function(update, children) {
        for (var i = 0; i < children.length; i++) {
            var child = children[i];
            if ( 
                  (
                    !(child instanceof EchoExt20.Button)
                    || (child instanceof EchoExt20.Button && child.get("addToButtonBar") == false)
                  )
                  && !(child instanceof EchoExt20.Toolbar)
                ) {
                EchoRender.renderComponentAdd(update, child, null); // null because ext components create the necessary extra divs themselves
                
                // add the ext component created by the peer to the child items array
                var childExtComponent = child.peer.extComponent;
                if (childExtComponent == null) {
                    throw new Error("No child ext component was created during renderAdd for component type: " + child.componentType);
                } 
                else {
                    this.extComponent.add(childExtComponent);
                }
            }
        }
    }
    
});