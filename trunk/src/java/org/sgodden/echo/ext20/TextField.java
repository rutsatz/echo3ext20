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
 * A text field.
 * <p/>
 * TODO - listeners on value change
 * 
 * 
 * @author simon
 *
 */
public class TextField 
		extends Component {
	
	public static final String TEXT_CHANGED_PROPERTY = "text";
	public static final String FIELD_LABEL_PROPERTY = "fieldLabel";
	public static final String ALLOW_BLANK_PROPERTY = "allowBlank";
	
	public TextField() {
		super();
	}
	
	public TextField(String text) {
		this();
		setText(text);
	}
	
	public TextField(String text, String fieldLabel) {
		setText(text);
		setFieldLabel(fieldLabel);
	}
	
	public void setText(String text) {
		setProperty(TEXT_CHANGED_PROPERTY, text);
	}
	
	public String getText() {
		return (String) getProperty(TEXT_CHANGED_PROPERTY);
	}
	
	public void setFieldLabel(String fieldLabel) {
		setProperty(FIELD_LABEL_PROPERTY, fieldLabel);
	}
	
	public void setBlankAllowed(boolean blankAllowed) {
		setProperty(ALLOW_BLANK_PROPERTY, blankAllowed);
	}

    public void processInput(String inputName, Object inputValue) {    	
    	if (TEXT_CHANGED_PROPERTY.equals(inputName)) {
    		setText((String)inputValue);
    	}
    }

}
