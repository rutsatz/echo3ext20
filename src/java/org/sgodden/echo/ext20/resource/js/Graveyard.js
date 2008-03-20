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
		return;
    
    	/*
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
		*/
