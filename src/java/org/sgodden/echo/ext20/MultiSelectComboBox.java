package org.sgodden.echo.ext20;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import nextapp.echo.app.list.DefaultListModel;
import nextapp.echo.app.list.DefaultListSelectionModel;
import nextapp.echo.app.list.ListCellRenderer;
import nextapp.echo.app.list.ListModel;
import nextapp.echo.app.list.ListSelectionModel;

public class MultiSelectComboBox extends ExtComponent implements AbstractListComponent {

	public static final String VALUE_CHANGED_PROPERTY = "selectedValue";
	public static final String MODEL_CHANGED_PROPERTY = "model";
	public static final String INPUT_ACTION = "action";
	public static final String ACTION_LISTENERS_CHANGED_PROPERTY = "actionListeners";
	
	private ListModel model;
	private ListSelectionModel selectionModel;
	private ListCellRenderer cellRenderer = DEFAULT_LIST_CELL_RENDERER;
	
	public MultiSelectComboBox(DefaultListModel model) {
		super();
		setSelectionModel(new DefaultListSelectionModel());
		setModel(model);
	}
    /**
     * Sets the row selection model. The selection model may not be null.
     * 
     * @param newValue
     *            the new selection model
     */
    public void setSelectionModel(ListSelectionModel newValue) {
        if (newValue == null) {
            throw new IllegalArgumentException( "Selection model may not be null.");
        }
        selectionModel = newValue;
        selectionModel.setSelectionMode( DefaultListSelectionModel.MULTIPLE_SELECTION);
    }

    public void setModel(ListModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Model may not be null");
        }
        this.model = model;
        firePropertyChange(MODEL_CHANGED_PROPERTY, null, model);
        selectionModel.clearSelection();
    }

    public ListModel getModel() {
        return model;
    }

    /**
     * Returns the selection model.
     * 
     * @return the selection model.
     */
    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }
    
	public String getSelectedValue() {
		return (String) super.get( VALUE_CHANGED_PROPERTY);
	}
	
	/** 
	 * @param value The selected items' index, seperated by comma
	 */
	public void setSelectedValue( String value) {
		set( VALUE_CHANGED_PROPERTY, value);
		selectionModel.clearSelection();
		if ( value == null || "".equals( value)) return;
		String[] splitedValue = value.split( ",");
		for (String v : splitedValue) {
			selectionModel.setSelectedIndex( Integer.parseInt( v), true);
		}
	}
	@Override
	public ListCellRenderer getCellRenderer() {
		return cellRenderer;
	}
	@Override
	public void setCellRenderer(ListCellRenderer newValue) {
		cellRenderer = newValue;
	}
	
    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
        if (VALUE_CHANGED_PROPERTY.equals(inputName)) {
            setSelectedValue((String) inputValue);
        }
    }
	public String getValue() {
    	int min = selectionModel.getMinSelectedIndex();
    	int max = selectionModel.getMaxSelectedIndex();
    	if ( max < 0) {
    		return null;
    	}
    	if ( min == max) {
    		return ""+ model.get( min);
    	}
    	String result = "";
    	for ( int i=min; i<=max; i++) {
    		if ( selectionModel.isSelectedIndex( i)) {
	    		result += model.get(i);
	    		if ( i<max) result += ",";
    		}
    	}
        return result;
	}
	/**
	 * @param value is a String contains the display value, seperated by comma
	 */
	public void setValue(String value) {
		selectionModel.clearSelection();
		if ( value == null || "".equals( value)) return;
		
		String[] splited = value.split( ",");
		Set<String> values = new HashSet<String>( Arrays.asList( splited));
		for ( int i=0; i<model.size(); i++) {
			if ( values.contains( model.get( i))) {
				selectionModel.setSelectedIndex( i, true);
			}
		}
	}

}
