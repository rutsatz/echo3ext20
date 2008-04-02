/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sgodden.echo.ext20;

import org.sgodden.echo.ext20.data.SimpleStore;

/**
 * A combobox control with support for autocomplete.
 * <p/>
 * FIXME - immmediate selection listener.
 * 
 * @author sgodden
 */
public class ComboBox 
        extends TextField {
    
    public static final String DISPLAY_FIELD_PROPERTY = "displayField";
    public static final String EDITABLE_PROPERTY = "editable";
    public static final String FORCE_SELECTION_PROPERTY = "forceSelection";
    public static final String STORE_PROPERTY = "store";
    public static final String TYPE_AHEAD_PROPERTY = "typeAhead";
    public static final String VALUE_FIELD_PROPERTY = "valueField";

    public ComboBox(SimpleStore store) {
        super();
        setStore(store);
    }
    
    public ComboBox(SimpleStore store, String fieldLabel) {
        this(store);
    }
    
    public void setDisplayField(String displayField) {
        setProperty(DISPLAY_FIELD_PROPERTY, displayField);
    }
    
    public void setEditable(boolean editable) {
        setProperty(EDITABLE_PROPERTY, editable);
    }
    
    public void setForceSelection(boolean forceSelection) {
        setProperty(FORCE_SELECTION_PROPERTY, forceSelection);
    }
    
    public void setStore(SimpleStore store) {
        setProperty(STORE_PROPERTY, store);
    }
    
    public void setTypeAhead(boolean typeAhead) {
        setProperty(TYPE_AHEAD_PROPERTY, typeAhead);
    }
    
    public void setValueField(String valueField) {
        setProperty(VALUE_FIELD_PROPERTY, valueField);
    }

}