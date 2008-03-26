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
* Ext initialisation.
*/
Ext.QuickTips.init();

/**
* Simply defines the namespace.
*/
EchoExt20 = {};

/**
* Abstract base class for all Ext20 sync peers.
*/
EchoExt20.ExtComponentSync = Core.extend(EchoRender.ComponentSync, {
    
    $abstract: {
        createExtComponent: function(update, options) {}
    },
    
    $virtual: {
        syncExtComponent: function(update) {}
    },
	
    isExtComponent: true,
    
    extComponent: null,
    
    _parentElement: null,
    
    renderAdd: function(update, parentElement) {
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
        */
        
        if (this.component.parent.peer.isExtComponent) {
            var options = this.component.parent.peer.extChildOptions;
            if (options == null) {
                options = {};
            }
            options['id'] = this.component.renderId;
            
            this.extComponent = this.createExtComponent(
                update,
                options
            );
            
            this._parentElement = null;
        }
        else {
            this._parentElement = parentElement; // rendering will be deferred until renderDisplay
        }
    },
    
    debugOptions: function(prefix, options) {
        var out = prefix += "\n";
        for (var key in options) {
            out += key + ": " + options[key] + "\n";
        }
        alert(out);
    },
    
    renderDisplay: function(update) {
        // if the parent is not an ext component, then we must be being added to a
        // regular div, which has to happen here, when we can guarantee that the div
        // is in the DOM tree
        if (this.extComponent == null) {
            var options = {
                id: this.component.renderId,
                renderTo: this._parentElement
            };
            this.extComponent = this.createExtComponent(
                update,
                options
            );
        }
        
        this.syncExtComponent(update);
    },
    
    renderDispose: function(update) {
        this.extComponent.destroy();
    }
    
});

EchoExt20.PropertyTranslator = {
    toJsObject: function(client, propertyElement) {
        return eval("(" + propertyElement.firstChild.data + ")"); // FIXME - security risk - use parseJSON instead
    }
};

EchoExt20.BorderLayout = Core.extend({
});

EchoExt20.PropertyTranslator.BorderLayout = {
    toProperty: function(client, propertyElement) {
        return new EchoExt20.BorderLayout();
    }
}

EchoSerial.addPropertyTranslator("Ext20BorderLayout", EchoExt20.PropertyTranslator.BorderLayout);
EchoSerial.addPropertyTranslator("E2BL", EchoExt20.PropertyTranslator.BorderLayout);

EchoExt20.FitLayout = Core.extend({
});

EchoExt20.PropertyTranslator.FitLayout = {
    toProperty: function(client, propertyElement) {
        return new EchoExt20.FitLayout();
    }
}

EchoSerial.addPropertyTranslator("Ext20FitLayout", EchoExt20.PropertyTranslator.FitLayout);
EchoSerial.addPropertyTranslator("E2FL", EchoExt20.PropertyTranslator.FitLayout);

EchoExt20.ColumnLayout = Core.extend({
});

EchoExt20.PropertyTranslator.ColumnLayout = {
    toProperty: function(client, propertyElement) {
        return new EchoExt20.ColumnLayout();
    }
}

EchoSerial.addPropertyTranslator("Ext20ColumnLayout", EchoExt20.PropertyTranslator.ColumnLayout);
EchoSerial.addPropertyTranslator("E2CL", EchoExt20.PropertyTranslator.ColumnLayout);

EchoExt20.FormLayout = Core.extend({
});

EchoExt20.PropertyTranslator.FormLayout = {
    toProperty: function(client, propertyElement) {
        return new EchoExt20.FormLayout();
    }
}

EchoSerial.addPropertyTranslator("Ext20FormLayout", EchoExt20.PropertyTranslator.FormLayout);
EchoSerial.addPropertyTranslator("E2FML", EchoExt20.PropertyTranslator.FormLayout);

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

EchoSerial.addPropertyTranslator("Ext20TableLayout", EchoExt20.PropertyTranslator.TableLayout);
EchoSerial.addPropertyTranslator("E2TL", EchoExt20.PropertyTranslator.TableLayout);


EchoExt20.PropertyTranslator.SimpleStore = {
    toProperty: function(client, propertyElement) {
        var obj = EchoExt20.PropertyTranslator.toJsObject(client, propertyElement);
        return new Ext.data.SimpleStore({
            fields: obj.fields,
            id: obj.id,
            data: obj.data
        });
    }
};

EchoSerial.addPropertyTranslator("Ext20SimpleStore", EchoExt20.PropertyTranslator.SimpleStore);
EchoSerial.addPropertyTranslator("E2SS", EchoExt20.PropertyTranslator.SimpleStore);

EchoExt20.PropertyTranslator.ColumnModel = {
    toProperty: function(client, propertyElement) {
        var obj = EchoExt20.PropertyTranslator.toJsObject(client, propertyElement);
        return new Ext.grid.ColumnModel(obj.columns);
    }
};

EchoSerial.addPropertyTranslator("Ext20ColumnModel", EchoExt20.PropertyTranslator.ColumnModel);
EchoSerial.addPropertyTranslator("E2CM", EchoExt20.PropertyTranslator.ColumnModel);
