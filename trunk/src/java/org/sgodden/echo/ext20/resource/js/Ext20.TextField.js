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
EchoExt20.TextField = Core.extend(EchoApp.Component, {
	
    $load: function() {
        EchoApp.ComponentFactory.registerType("Ext20TextField", this);
        EchoApp.ComponentFactory.registerType("E2TF", this);
    },

    focusable: true,

    componentType: "Ext20TextField"
	
});

EchoExt20.TextFieldSync = Core.extend(EchoExt20.ExtComponentSync, {

    $load: function() {
        EchoRender.registerPeer("Ext20TextField", this);
    },

    $virtual: {
        newExtComponentInstance: function(options) {
            return new Ext.form.TextField(options);
        }
    },
    
    _handleBlurEventRef: null,
    
    $construct: function() {
    	this._handleBlurEventRef = Core.method(this, this._handleBlurEvent);
    },
    
    createExtComponent: function(update, options) {
        if (this.component.get('value') != null) {
            options['value'] = this.component.get("value");
        }
        if (this.component.get('fieldLabel') != null) {
            options['fieldLabel'] = this.component.get("fieldLabel");
        }
        if (this.component.get("allowBlank") != null) {
                options['allowBlank'] = this.component.get("allowBlank");
        }
        if (this.component.get("emptyText") != null) {
                options['emptyText'] = this.component.get("emptyText");
        }
    	if ( !(this.component.isEnabled()) ) {
    		options['disabled'] = true;
    	}
    
    	var extComponent = this.newExtComponentInstance(options)
    	extComponent.on('blur', this._handleBlurEventRef);
    	
    	return extComponent;
    },
    
    _handleBlurEvent: function() {
    	this.component.set("value", this.extComponent.getValue());
    },
    
    renderUpdate: function(update){
        EchoExt20.ExtComponentSync.prototype.renderUpdate.call(this, update);
        this.extComponent.setValue(this.component.get("text"));
    },
    
    renderFocus: function() {
        this.extComponent.focus(true);
    }

});
