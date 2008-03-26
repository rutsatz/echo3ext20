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
    
    $load: function() {
        EchoRender.registerPeer("Ext20Panel", this);
    },
    
    _syncSizeRequired: false,
    
    syncExtComponent: function(update) {
        if (this._parentElement != null) {
            this.extComponent.setHeight(this._parentElement.offsetHeight);
            this.extComponent.setWidth(this._parentElement.offsetWidth);
        }
        
        if (this._syncSizeRequired) {
            //this.extComponent.syncSize();
            this.extComponent.doLayout();
            this._syncSizeRequired = false;
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
            this._createChildItems(update, update.getAddedChildren());
            // FIXME - how about adding buttons in this phase?
        }
        
    },
    
    createExtComponent: function(update, options) {
        
        //options['bufferResize'] = true;

        var title = this.component.get("title");
        if (title != null) {
            options['title'] = title;
        }
        
        var collapsible = this.component.get("collapsible");
        if (collapsible != null) {
            options['collapsible'] = collapsible;
        }

        var padding = this.component.get("padding");
        if (padding != null) {
            options['bodyStyle'] = "padding: " + padding;
        }
        
        var border = this.component.get("border");
        if (border != null) {
            options['border'] = border;
        }
        else {
            options['border'] = false;
        }
        
        var width = this.component.get("width");
        if (width != null) {
            options['width'] = width;
        }
        
        var height = this.component.get("height");
        if (height != null) {
            options['height'] = height;
        }
        
        var layout = this.component.get("layout");
        if (layout != null) {
            if (layout instanceof EchoExt20.BorderLayout) {
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
        else {
            options['layout'] = 'table';
            options['layoutConfig'] = {columns: 1};
        }
        
        var children = this._createChildComponentArrayFromComponent();
        
        options['buttons'] = this._createButtons(update, children);
        
        this.extComponent = new Ext.Panel(options);
        
        this._createChildItems(update, children);
        
        //this.extComponent.doLayout();
        
        return this.extComponent;
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
            if ( !(child instanceof EchoExt20.Button)
                    || (child instanceof EchoExt20.Button && child.get("addToButtonBar") == false)
                ) {
                this.extChildOptions = {};
                EchoRender.renderComponentAdd(update, child, null); // null because ext components create the necessary extra divs themselves
                
                // add the ext component created by the peer to the child items array
                var childExtComponent = child.peer.extComponent;
                if (childExtComponent == null) {
                    throw new Error("No child ext component was created during renderAdd for component type: " + child.componentType);
                } 
                else {
                    this.extComponent.add(childExtComponent);
                    this.extComponent.doLayout();
                    this._syncSizeRequired = true;
                }
                delete this.extChildOptions;
            }
        }
    }
    
});