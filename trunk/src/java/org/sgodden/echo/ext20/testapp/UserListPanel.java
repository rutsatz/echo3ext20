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
package org.sgodden.echo.ext20.testapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nextapp.echo.app.event.ActionListener;
import org.sgodden.echo.ext20.Button;
import org.sgodden.echo.ext20.Panel;
import org.sgodden.echo.ext20.TextField;
import org.sgodden.echo.ext20.data.SimpleStore;
import org.sgodden.echo.ext20.grid.ColumnConfiguration;
import org.sgodden.echo.ext20.grid.ColumnModel;
import org.sgodden.echo.ext20.grid.GridPanel;
import org.sgodden.echo.ext20.layout.BorderLayout;
import org.sgodden.echo.ext20.layout.BorderLayoutData;
import org.sgodden.echo.ext20.layout.TableLayout;

/**
 * A panel which displays a list of users.
 * 
 * @author sgodden
 */
public class UserListPanel 
        extends Panel {
    
    private GridPanel userGridPanel;
    
    private Object[][] data = makeData();
    
    public UserListPanel(){
        super(new BorderLayout());
        setBorder(false);
        setRenderId("userList");
                
        final Panel filterOptions = new Panel(new TableLayout(2));
        filterOptions.setBorder(false);
        filterOptions.setLayoutData(new BorderLayoutData(BorderLayout.NORTH));
        add(filterOptions);
        
        final TextField tf = new TextField();
        tf.setEmptyText("Enter search text");
        filterOptions.add(tf);
        
        Button button = new Button("Search");
        filterOptions.add(button);
        
        List<ColumnConfiguration> cols = new ArrayList<ColumnConfiguration>();
        cols.add(new ColumnConfiguration("User ID", "userid"));
        cols.add(new ColumnConfiguration("Name", "name"));
        ColumnModel columnModel = new ColumnModel(cols);

        SimpleStore store = new SimpleStore(
                data,
                0,
                new String[]{"id", "userid", "name", "date"});

        userGridPanel = new GridPanel(columnModel, store);
        add(userGridPanel);
        
        userGridPanel.setLayoutData(new BorderLayoutData(BorderLayout.CENTER));

    }
    
    /**
     * Adds a listener to be notifies when a row is actioned.
     * @param listener the listener to be added.
     */
    public void addActionListener(ActionListener listener) {
        userGridPanel.addActionListener(listener);
    }
    
    /**
     * Returns the (only) selected row index of the grid.
     * @return
     */
    public int getSelectedIndex() {
        return userGridPanel.getSelectionModel().getMinSelectedIndex();
    }
    
    /**
     * Returns the selected row of data.
     * @return
     */
    public Object[] getSelectedRow() {
        return data[getSelectedIndex()];
    }
    
    /**
     * Makes dummy data for users.
     * @return the dummy data.
     */
    private Object[][] makeData() {
        int rows = 10;
        
        Object[][] ret = new Object[rows][];
        
        for (int i = 0; i < ret.length; i++) {
            Object[] row = new Object[4];
            row[0] = String.valueOf(i);
            row[1] = "User id asdasdasdasdasd " + i;
            row[2] = "Name asdasdasdasdasdasd " + i;
            row[3] = new Date();
            ret[i] = row;
        }
        
        return ret;
    }

}
