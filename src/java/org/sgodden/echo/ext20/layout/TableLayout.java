package org.sgodden.echo.ext20.layout;

import java.io.Serializable;

public class TableLayout
        implements Layout, Serializable {

    private static final long serialVersionUID = 20080213L;
    private int columns;
    private int defaultPadding = 5;

    public TableLayout(int columns) {
        this.columns = columns;
    }

    public TableLayout(int columns, int defaultPadding) {
        this.columns = columns;
        this.defaultPadding = defaultPadding;
    }

    public int getColumns() {
        return columns;
    }

    public int getDefaultPadding() {
        return defaultPadding;
    }
}
