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
 * Component implementation for Ext.SplitButton.
 */
EchoExt20.SplitButton = Core.extend(EchoExt20.Button, {
	
    $load: function() {
        Echo.ComponentFactory.registerType("Ext20SplitButton", this);
        Echo.ComponentFactory.registerType("E2SB", this);
    },

    componentType: "Ext20SplitButton"
	
});

/**
 * Synchronisation peer for split button.  Currently incomplete, and does
 * nothing in addition to the button sync peer.
 */
EchoExt20.SplitButtonSync = Core.extend(EchoExt20.ButtonSync, {
	 $load: function() {
        Echo.Render.registerPeer("Ext20SplitButton", this);
    },
    
	newExtComponentInstance: function(options) {
      return new Ext.SplitButton(options);
  },
  
  _setIconUrl: function() {
   		var iconUrl = null;
   		if (this.component.isEnabled() || this.component.get("disabledIcon") == null) {
	   		iconUrl = Echo.Sync.ImageReference.getUrl(
               	this.component.render("icon"));
    	}
    	else {
    		iconUrl = Echo.Sync.ImageReference.getUrl(
               	this.component.render("disabledIcon"));
   		}
    	if (iconUrl != null) {
           	var el = this.extComponent.getEl().down("///////button"); // the left button td
           	el.dom.style.backgroundImage = "url(" + iconUrl + ")";
           	el = el.up("table");
           	if (this.component.get("text")) {
               	el.addClass("x-btn-text-icon");        
           }	
            else {
               	el.addClass("x-btn-icon");        
           	}
   		}
    }
});