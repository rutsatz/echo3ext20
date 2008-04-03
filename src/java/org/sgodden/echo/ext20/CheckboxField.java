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

/**
 * A text field.
 * <p/>
 * TODO - listeners on value change
 * 
 * 
 * @author simon
 *
 */
public class CheckboxField
        extends ExtComponent {

    public static final String SELECTED_CHANGED_PROPERTY = "selected";
    public static final String FIELD_LABEL_PROPERTY = "fieldLabel";

    public CheckboxField() {
        super();
        setSelected(false);
    }

    public CheckboxField(boolean selected) {
        this();
        setSelected(selected);
    }

    public CheckboxField(boolean selected, String fieldLabel) {
        setSelected(selected);
        setFieldLabel(fieldLabel);
    }

    public void setSelected(boolean selected) {
        setProperty(SELECTED_CHANGED_PROPERTY, selected);
    }

    public boolean getSelected() {
        return (Boolean) getProperty(SELECTED_CHANGED_PROPERTY);
    }

    public void setFieldLabel(String fieldLabel) {
        setProperty(FIELD_LABEL_PROPERTY, fieldLabel);
    }

    @Override
    public void processInput(String inputName, Object inputValue) {
        if (SELECTED_CHANGED_PROPERTY.equals(inputName)) {
            setSelected((Boolean) inputValue);
        }
    }
}
