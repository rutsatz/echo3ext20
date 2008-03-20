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
package org.sgodden.echo.ext20;

import nextapp.echo.app.Component;

/**
 * A html editor field.
 * <p/>
 * TODO - listeners on value change
 * 
 * @author simon
 *
 */
public class HtmlEditor 
		extends Component {
	
	public static final String TEXT_CHANGED_PROPERTY = "text";
	
	public HtmlEditor() {
		super();
	}
	
	public HtmlEditor(String text) {
		setText(text);
	}
	
	public void setText(String text) {
		setProperty(TEXT_CHANGED_PROPERTY, text);
	}
	
	public String getText() {
		return (String) getProperty(TEXT_CHANGED_PROPERTY);
	}

    public void processInput(String inputName, Object inputValue) {    	
    	if (TEXT_CHANGED_PROPERTY.equals(inputName)) {
    		setText((String)inputValue);
    	}
    }


}
