/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sgodden.echo.ext20;

import org.sgodden.echo.ext20.layout.FormLayout;
import org.sgodden.echo.ext20.layout.Layout;

/**
 * A container used for grouping fields.
 * 
 * @author sgodden
 */
public class FieldSet 
        extends Panel {
    
    public static final String CHECKBOX_TOGGLE_PROPERTY = "checkboxToggle";
    
    /**
     * Creates a new field set, using a form layout.
     */
    public FieldSet() {
        super(new FormLayout());
    }
    
    /**
     * Creates a new field set, using the specified layout.
     * @param layout
     */
    public FieldSet(Layout layout) {
        super(layout);
    }
    
    /**
     * Specified whether to render a checkbox into the fieldset frame 
     * just in front of the legend.
     * @param toggle whether to add a checkbox.
     */
    public void setCheckboxToggle(boolean toggle) {
        setProperty(CHECKBOX_TOGGLE_PROPERTY, toggle);
    }

}
