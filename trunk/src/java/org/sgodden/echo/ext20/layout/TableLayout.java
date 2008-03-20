package org.sgodden.echo.ext20.layout;

import java.io.Serializable;

public class TableLayout 
	implements Layout, Serializable {
	
	private static final long serialVersionUID = 20080213L;
	
	private int columns;
	
	public TableLayout(int columns) {
		this.columns = columns;
	}
	
	public int getColumns() {
		return columns;
	}

}
