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

    public static final String VALUE_CHANGED_PROPERTY = "value";
    public static final String FIELD_LABEL_PROPERTY = "fieldLabel";
    public static final String ALLOW_BLANK_PROPERTY = "allowBlank";
    public static final String EMPTY_TEXT_PROEPRTY = "emptyText";

    public TextField() {
        super();
    }

    public TextField(String text) {
        this();
        setValue(text);
    }

    public TextField(String text, String fieldLabel) {
        setValue(text);
        setFieldLabel(fieldLabel);
    }

    public void setValue(Object value) {
        setProperty(VALUE_CHANGED_PROPERTY, value);
    }

    public Object getValue() {
        return getProperty(VALUE_CHANGED_PROPERTY);
    }

    public String getText() {
        return (String) getProperty(VALUE_CHANGED_PROPERTY);
    }
    
    public void setFieldLabel(String fieldLabel) {
        setProperty(FIELD_LABEL_PROPERTY, fieldLabel);
    }

    public void setBlankAllowed(boolean blankAllowed) {
        setProperty(ALLOW_BLANK_PROPERTY, blankAllowed);
    }
    
    public void setEmptyText(String emptyText) {
        setProperty(EMPTY_TEXT_PROEPRTY, emptyText);
    }

    @Override
    public void processInput(String inputName, Object inputValue) {
        if (VALUE_CHANGED_PROPERTY.equals(inputName)) {
            setValue((String) inputValue);
        }
    }
}
