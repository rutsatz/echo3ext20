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
EchoExt20.RadioButton = Core.extend(EchoApp.Component, {
    
    $load: function() {
        EchoApp.ComponentFactory.registerType("Ext20RadioButton", this);
        EchoApp.ComponentFactory.registerType("E2RB", this);
    },

    focusable: true,
    
    componentType: "Ext20RadioButton"
    
});

EchoExt20.RadioButtonSync = Core.extend(EchoExt20.ExtComponentSync, {
    
    $load: function() {
        EchoRender.registerPeer("Ext20RadioButton", this);
    },
    
    _handleBlurEventRef: null,
    
    $construct: function() {
        this._handleBlurEventRef = Core.method(this, this._handleBlurEvent);
    },
    
    createExtComponent: function(update, options) {
        
        var selected = this.component.get("selected");
        
        options['fieldLabel'] = this.component.get("fieldLabel");
        if ( !(this.component.isEnabled()) ) {
            options['disabled'] = true;
        }
        
        options['name'] = this.component.get("name");
        
        var extComponent = new Ext.form.Radio(options);
        extComponent.setValue(selected);
        extComponent.on('blur', this._handleBlurEventRef);
        
        return extComponent;
    },
    
    _handleBlurEvent: function() {
        this.component.set("selected", this.extComponent.getValue());
    },
    
    renderUpdate: function(){},
    
    renderFocus: function() {
        this.extComponent.focus(true);
    }
    
});
