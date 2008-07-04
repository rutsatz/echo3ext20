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
/**
 * @fileoverview
 * Ext20 framework main module.
 */

/**
* Ext initialisation.
*/
Ext.QuickTips.init();


/**
 * @namespace
 * Namespace for application framework.
 */
EchoExt20 = {};

/**
 * Base class for all ext20 components.
 */
EchoExt20.ExtComponent = Core.extend(Echo.Component, {
	
    /**
     * Fires a before render event.
     */
    doBeforeRender: function() {
        this.fireEvent({type: "beforeRender", source: this});
    }
});

/**
 * Provides a wrapper allowing echo3 sync peers to work within
 * extjs.
 * @constructor
 */
EchoExt20.Echo3SyncWrapper = function(update, wrappedComponent) {
    EchoExt20.Echo3SyncWrapper.superclass.constructor.call(this);

    this.wrappedComponent = wrappedComponent;
    this.wrappedRootElement = document.createElement("div");

    /*
     * We need to call render add immediately, but defer
     * adding it until this wrapper is rendered.
     */
    Echo.Render.renderComponentAdd(update, this.wrappedComponent, this.wrappedRootElement);
}

Ext.extend(EchoExt20.Echo3SyncWrapper, Ext.Component, {
    /**
     * Invokes the echo3 sync peer lazily on render.
     * @param {Object} ct the container div.
     * @param {Object} position the child div to which we should render the echo3 component.
     */
    onRender: function(ct, position) {
        if (position == null) {
            position.appendChild(this.wrappedRootElement);
        }
        else {
            ct.appendChild(this.wrappedRootElement);
        }
        // necessary for ext internal processing
        this.el = new Ext.Element(this.wrappedRootElement);
       
        /*
         * Intercept the wrapped component's peer's renderDispose to ensure that this wrapper is disposed.
         */
        this.wrapped.peer.renderDispose = this.wrapped.peer.renderDispose.createInterceptor(this.onRenderDispose, this);
    },
    
    onRenderDispose: function(update) {
        this.ownerCt.remove(this);
    },
    
    setSize: function() {
        // FIXME - implement setSize
    }
});

/**
* Abstract base class for all Ext20 sync peers.
*/
EchoExt20.ExtComponentSync = Core.extend(Echo.Render.ComponentSync, {
    
    $abstract: {
        /**
         * Called during renderAdd to actually create the ext component.
         */
        createExtComponent: function(update, options) {}
    },

    $virtual: {
        /**
         * Adds the ext component to the parent.  This may need
         * to be overridden by certain subclasses (such as buttons
         * which may have to add themselves to the parent's button
         * bar rather than its main layout.
         */
        _addExtComponentToParent: function() {
            /*
             * If the parent is an ext container, then tell it
             * to add this new child.
             */
            this.component.parent.peer.extComponent.add(this.extComponent);
        }
    }
    
    $static: {
        openWindows: []
    },
    
    /**
     * Simple marker property to identify that this as an ext-related peer.
     */
    isExtComponent: true,
    
    /**
     * The ext component created by the peer.
     */
    extComponent: null,

    /**
     * An array of wrappers for non-ext children,
     * that must be added to the ext container
     * when it is created.
     */
    _nonExtChildren: null,
    
    /**
     * If this is not an ext component, then this references the
     * DOM element we have been asked to render ourself into.
     */
    _parentElement: null,
	
    /**
     * Whether this panel is the root ext container of
     * the browser window, or an open window.
     */
    _isRootContainer: false,

    /**
     * Whether updates to child layouts occurred
     * on this server update.
     */
    _childLayoutUpdatesOccurred: false,
    
    /**
     * If we are the root ext container, then we have to perform special
     * processing once all rendering has been done.
     */
    renderDisplayCompleteRef: null,

    /**
     * The last server update for the component.
     */
    _update: null,
    
    /**
     * Notifies the root container that layout changes
     * occurred, and that it therefore needs to redo its
     * layout.
     */
    _notifyLayoutChanges: function() {
        if (this._isRootContainer) {
                this._childLayoutUpdatesOccurred = true;
        }
        else {
                // not the root, tell the parent
                this.component.parent.peer._notifyLayoutChanges();
        }
    },

    
    renderAdd: function(update, parentElement) {
        this._update = update;

        /*
        If the component's parent's peer indicates that it is
        an ext component, then we need to create the ext component
        here in the renderAdd phase, so that is available at the start
        of the parent's renderDisplay phase.
        
        If the component's parent's peer does not indicate that it
        is an ext component, then we need to defer creation of
        the ext component until renderDisplay, because we will
        be rendering it direct to a DOM element, and ext can only
        render to a DOM element which has been added to the DOM tree,
        and this is only guaranteed to be true during renderDisplay.

        However, in either case, we must create our children, since
        renderAdd needs to be called on the peers of all components
        in this phase so that internal processing within the echo
        client framework takes place.
        */
            
        /*
         * We need to know if we are a root container (i.e. an ext container
         * whose parent is not an ext container), so that we can
         * perform required layout operations on server update completion
         * if we or any of our children have had layout changes.
         */
        if ( (this instanceof EchoExt20.PanelSync)
                  && !(this.component.parent.peer.isExtComponent) 
                ) {
                
            this._isRootContainer = true;
            this.renderDisplayCompleteRef = Core.method(this, this._rootRenderDisplayComplete);
            Echo.Render.addRenderDisplayCompleteListener(this.renderDisplayCompleteRef);
        }

        /*
         * We always defer actual creation of the ext component to
         * the renderDisplay phase.
         */
        if (update.renderContext.displayRequired == null) {
            update.renderContext.displayRequired = [];
        }
        update.renderContext.displayRequired.push(this.component);

        /*
         * If the parent is not an ext component, then retain
         * a reference to the parent element so that we can ask
         * the ext component to render to it in the render
         * display phase.
         */
        if (!(this.component.parent.peer.isExtComponent)) {
            this._parentElement = parentElement; 
        }

        /*
         * If the component has children, call renderAdd on them
         * all now.
         */
        for (var i = 0; i < this.component.getComponentCount(); i++) {
            this._doRenderAddChild(update, this.component.getComponent(i);
        }
       
    },

    /**
     * Performs renderAdd processing for one child
     */
    _doRenderAddChild(update, child): function() {
        /*
         * If it's not an ext component, we need to wrap it,
         * and record it in the array of child components
         * that need adding once the ext component has
         * been created.
         */
        if (! (child instanceof EchoExt20.ExtComponent) ) {
            /*
             * This constructor takes care of performing renderAdd
             * on the non-ext child.
             */
            var wrapper = new EchoExt20.Echo3SyncWrapper(update, child);
            if (this._nonExtChildren == null) {
                this._nonExtChildren = [];
            }
            this._nonExtChildren.push(wrapper);
        }
        else {
            /*
             * It's an ext child, just call renderAdd immediately.
             */
            Echo.Render.renderComponentAdd(update, child, null);
        }
    },
    
    /**
     * Use by root containers to redo their layout if child
     * layout updates occurred, after the render display
     * phase is complete.
     */
    _rootRenderDisplayComplete: function() {
        if (this._childLayoutUpdatesOccurred) {
            this.extComponent.doLayout();
            this._childLayoutUpdatesOccurred = false;
        }
    },

    _createComponent: function() {
        // if the parent is not an ext component, then we must be being added to a
        // regular div, which has to happen here, when we can guarantee that the div
        // is in the DOM tree
        if (this.extComponent == null) {
            /*
             * For IE only, we need to set the overflow of the main content pane
             * to 'visible', or we get bugs whereby the background disappears when
             * floating content over the top (for instance when you drop down a combo
             * box
             */
            if (Core.Web.Env.BROWSER_INTERNET_EXPLORER) {
                var contentPaneEl = document.getElementById("approot").firstChild;
                contentPaneEl.style.overflow = "visible";
            }

            options = {};
            options['id'] = this.component.renderId;
           
            /*
             * If the parent is an ext component, then process
             * any layout data.
             */
            if (this.parent.peer.isExtComponent) {
                var layout = this.component.parent.get("layout");
                if (layout != null) {
                    // border layout
                    if (layout instanceof EchoExt20.BorderLayout) {
                        var layoutData = this.component.get("layoutData");
                        // layout data mandatory for a border layout
                        if (layoutData == null) {
                            throw new Error("No layout data provided for component in a border layout");
                        }
                        var region = this._convertToExtRegion(layoutData.region);
                        options['region'] = region;
                        // if we are in the north, and have no height set, then we need autoHeight true.
                        // fixme - how about handling width in west and east, and height in south?
                        if (region == 'north') {
                            var height = this.component.get("height");
                            if (height == null) {
                                options['autoHeight'] = true;
                            }
                        }
                        
                    }
                    else if (layout instanceof EchoExt20.ColumnLayout) {
                        var layoutData = this.component.get("layoutData");
                        // layout data is NOT mandatory for column layout
                        if (layoutData != null) {
                            options['columnWidth'] = parseFloat(layoutData.columnWidth);
                        }
                    }
                    else if (layout instanceof EchoExt20.FormLayout) {
                        var layoutData = this.component.get("layoutData");
                        // layout data is NOT mandatory for column layout
                        if (layoutData != null) {
                            options['anchor'] = layoutData.anchor;
                        }
                    }
                    // other layouts (form layout, fit layout, table layout) do not require layout data on their children
                }
            }
            else {
                /*
                 * Parent is not an ext element so just inform the ext component
                 * that it needs to render to the parent element.
                 */
                if (this._parentElement == null) {
                    throw new Error("Parent is not an ext component, but parentElement is null: " + this.component.renderId);
                }
                options["renderTo"] = this._parentElement;
            }
            
            this.extComponent = this.createExtComponent(
                update,
                options
            );
            
            if (this.component.get("alignTo") != null) {
                this.extComponent.on("render", function(){
                    this.alignTo(this.component.get("alignTo"));
                }, this);
            }
            
            this._addBeforeRenderListener();
            
            this._parentElement = null;

            /*
             * If there are non-ext children to add, add them now.
             */
            if (this._nonExtChildren != null) {
                for (var nonExtChild in this._nonExtChildren) {
                    this.extComponent.add(nonExtChild);
                }
            }

            if (this.component.parent.peer.isExtComponent) {
                if (this.component.parent.peer.extComponent == null) {
                    throw new Error("The parent's ext component has not been created yet - cannot add child: " + this.component.renderId);
                }
                this._addExtComponentToParent();
            }
        }
    },

    /**
     * Convenience method to turn a single character region into an
     * ext region equivalent.
     */
    _convertToExtRegion: function(shortRegion) {
        var ret = null;
        
        if (shortRegion == 'n') {
            ret = 'north';
        }
        else if (shortRegion == 'e') {
            ret = 'east';
        }
        else if (shortRegion == 's') {
            ret = 'south';
        }
        else if (shortRegion == 'w') {
            ret = 'west';
        }
        else if (shortRegion == 'c') {
            ret = 'center';
        }
        
        if (ret == null) {
            throw new Error("Unknown short region code: " + shortRegion);
        }
        
        return ret;
    },
    
    /**
     * Convenience method to debug out a set of options.
     */
    debugOptions: function(prefix, options) {
        var out = prefix += "\n";
        for (var key in options) {
            out += key + ": " + options[key] + "\n";
        }
        alert(out);
    },
    
    renderDisplay: function(update) {
        this._createComponent();
    },
    
    _addBeforeRenderListener: function() {
        if (this.component._listenerList != null
                && this.component._listenerList.hasListeners("beforeRender")) {
            this.extComponent.on("beforerender", this._beforeRender, this);
        }
    },
    
    _beforeRender: function() {
        this.component.doBeforeRender();
    },
    
    renderDispose: function(update) {
        if (this._parentElement != null) {
            // we are the top level container, so remove the
            // update listener we added earlier
            Echo.Render.removeRenderDisplayCompleteListener(this.renderDisplayCompleteRef);
        }
        this.extComponent.destroy();
    },
    
    renderUpdate: function(update) {
		
        if (update.hasAddedChildren() || update.hasRemovedChildren()) {
                this._notifyLayoutChanges();
        }
		
        if (update.hasUpdatedProperties()) {
            var alignToUpdate = update.getUpdatedProperty("alignTo");
            if (alignToUpdate != null) {
                var alignToString = alignToUpdate.newValue;
                this.alignTo(alignToString);
            }
        }
    },
    
    alignTo: function(alignToString) {
        
        var strings = alignToString.split(",");
        
        var otherId = "c_" + strings[0];
        
        var alignmentString = strings[1];
        
        var offsets = strings[2].split(':');
        var offsetX = offsets[0];
        var offsetY = offsets[1];
        
        var extEl = this.extComponent.getEl();
        extEl.dom.style.position = "absolute";
        extEl.alignTo(otherId, alignmentString, [offsetX,offsetY]);
    },
    
    renderFocus: function() {
//        this.extComponent.on("render", function(){
//            this.extComponent.focus(true);
//        }, this);
        this.extComponent.focus();
    }
    
});

// Abstract component classes and sync peers

// Property translators

EchoExt20.PropertyTranslator = {
    toJsObject: function(client, propertyElement) {
        var jsonArray = new Array();
        for (i = 0; i < propertyElement.childNodes.length; i++) {
            jsonArray.push(propertyElement.childNodes.item(i).data);
        }
        var jsonText = jsonArray.join("");
        return eval("(" + jsonText + ")"); // FIXME - security risk - use parseJSON instead
    }
};

EchoExt20.AccordionLayout = Core.extend({
});

EchoExt20.PropertyTranslator.AccordionLayout = {
    toProperty: function(client, propertyElement) {
        return new EchoExt20.AccordionLayout();
    }
}

Echo.Serial.addPropertyTranslator("Ext20AccordionLayout", EchoExt20.PropertyTranslator.AccordionLayout);
Echo.Serial.addPropertyTranslator("E2AL", EchoExt20.PropertyTranslator.AccordionLayout);

EchoExt20.BorderLayout = Core.extend({
});

EchoExt20.PropertyTranslator.BorderLayout = {
    toProperty: function(client, propertyElement) {
        return new EchoExt20.BorderLayout();
    }
}

Echo.Serial.addPropertyTranslator("Ext20BorderLayout", EchoExt20.PropertyTranslator.BorderLayout);
Echo.Serial.addPropertyTranslator("E2BL", EchoExt20.PropertyTranslator.BorderLayout);

EchoExt20.FitLayout = Core.extend({
});

EchoExt20.PropertyTranslator.FitLayout = {
    toProperty: function(client, propertyElement) {
        return new EchoExt20.FitLayout();
    }
}

Echo.Serial.addPropertyTranslator("Ext20FitLayout", EchoExt20.PropertyTranslator.FitLayout);
Echo.Serial.addPropertyTranslator("E2FL", EchoExt20.PropertyTranslator.FitLayout);

EchoExt20.ColumnLayout = Core.extend({
});

EchoExt20.PropertyTranslator.ColumnLayout = {
    toProperty: function(client, propertyElement) {
        return new EchoExt20.ColumnLayout();
    }
}

Echo.Serial.addPropertyTranslator("Ext20ColumnLayout", EchoExt20.PropertyTranslator.ColumnLayout);
Echo.Serial.addPropertyTranslator("E2CL", EchoExt20.PropertyTranslator.ColumnLayout);

EchoExt20.FormLayout = Core.extend({
});

EchoExt20.PropertyTranslator.FormLayout = {
    toProperty: function(client, propertyElement) {
        return new EchoExt20.FormLayout();
    }
}

Echo.Serial.addPropertyTranslator("Ext20FormLayout", EchoExt20.PropertyTranslator.FormLayout);
Echo.Serial.addPropertyTranslator("E2FML", EchoExt20.PropertyTranslator.FormLayout);

EchoExt20.TableLayout = Core.extend({
    columns: 0,
    defaultPadding: '',
    
    $construct: function(columns, defaultPadding) {
        this.columns = columns;
        this.defaultPadding = defaultPadding;
    }
    
});

EchoExt20.PropertyTranslator.TableLayout = {
    toProperty: function(client, propertyElement) {
        var columns = propertyElement.getAttribute('c');
        if (columns == null) {
            columns = '1';
        }
        var defaultPadding = propertyElement.getAttribute('dp');
        if (defaultPadding == null) {
            defaultPadding = '';
        }
        return new EchoExt20.TableLayout(
            parseInt(columns),
            defaultPadding
        );
    }
}

Echo.Serial.addPropertyTranslator("Ext20TableLayout", EchoExt20.PropertyTranslator.TableLayout);
Echo.Serial.addPropertyTranslator("E2TL", EchoExt20.PropertyTranslator.TableLayout);


EchoExt20.PropertyTranslator.SimpleStore = {
    toProperty: function(client, propertyElement) {
        return EchoExt20.PropertyTranslator.toJsObject(client, propertyElement);
		/*
        return new Ext.data.SimpleStore({
            fields: obj.fields,
            id: obj.id,
            data: obj.data
        });
        */
    }
};

Echo.Serial.addPropertyTranslator("Ext20SimpleStore", EchoExt20.PropertyTranslator.SimpleStore);
Echo.Serial.addPropertyTranslator("E2SS", EchoExt20.PropertyTranslator.SimpleStore);

EchoExt20.PropertyTranslator.ColumnModel = {
    toProperty: function(client, propertyElement) {
        var obj = EchoExt20.PropertyTranslator.toJsObject(client, propertyElement);
        return new Ext.grid.ColumnModel(obj.columns);
    }
};

Echo.Serial.addPropertyTranslator("Ext20ColumnModel", EchoExt20.PropertyTranslator.ColumnModel);
Echo.Serial.addPropertyTranslator("E2CM", EchoExt20.PropertyTranslator.ColumnModel);