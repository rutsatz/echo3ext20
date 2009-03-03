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
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sgodden.echo.ext20.Button;
import org.sgodden.echo.ext20.Menu;
import org.sgodden.echo.ext20.MenuItem;
import org.sgodden.echo.ext20.Panel;
import org.sgodden.echo.ext20.SortOrder;
import org.sgodden.echo.ext20.TextField;
import org.sgodden.echo.ext20.Toolbar;
import org.sgodden.echo.ext20.ToolbarButton;
import org.sgodden.echo.ext20.ToolbarFill;
import org.sgodden.echo.ext20.ToolbarSeparator;
import org.sgodden.echo.ext20.ToolbarTextItem;
import org.sgodden.echo.ext20.data.DefaultSimpleStore;
import org.sgodden.echo.ext20.data.SimpleStore;
import org.sgodden.echo.ext20.grid.ColumnConfiguration;
import org.sgodden.echo.ext20.grid.ColumnModel;
import org.sgodden.echo.ext20.grid.GridPanel;
import org.sgodden.echo.ext20.layout.FitLayout;

/**
 * A panel which displays a list of users.
 * 
 * @author sgodden
 */
public class UserListPanel
        extends Panel {

    private static final transient Log log = LogFactory.getLog(UserListPanel.class);
    private GridPanel userGridPanel;
    private int startIndex = 1;
    private Object[][] data;

    public UserListPanel() {
        super(new FitLayout());
        setBorder(false);
        setRenderId("userList");
        setTitle("User list");

        List<ColumnConfiguration> cols = new ArrayList<ColumnConfiguration>();
        cols.add(new ColumnConfiguration("User ID", 200, true, "userid", false));
        cols.add(new ColumnConfiguration("Name", 200, true, "name", false));
        cols.add(new ColumnConfiguration("Role", 200, true, "role", true));
        ColumnModel columnModel = new ColumnModel(cols);

        data = makeData();
        
        userGridPanel = new GridPanel(columnModel);
        userGridPanel.setTableModel(makeTableModel());
        userGridPanel.setToolbar(makeToolbar());
        userGridPanel.setGroupField("role");
        userGridPanel.setSortField("userid");
        userGridPanel.setSortOrder(SortOrder.ASCENDING);
        add(userGridPanel);

        addKeyPressListener("enter", new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                log.info("Enter key was pressed");
            }
        });
        
        Button changeButton = new Button("Change the data model");
        addButton(changeButton);
        changeButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				startIndex += 10;
				userGridPanel.setTableModel(makeTableModel());
			}});

    }
    
    private SimpleStore makeSimpleStore() {
		DefaultSimpleStore store = new DefaultSimpleStore(makeData(), 
				makeColumnNames());

		return store;
	}
    
    private TableModel makeTableModel() {
    	DefaultTableModel ret = new DefaultTableModel(
    			makeData(),
    			makeColumnNames());
    	return ret;
    }
    
    private String[] makeColumnNames() {
    	return new String[] {"userid", "name", "role"};
    }

    /**
	 * Adds a listener to be notifies when a row is actioned.
	 * 
	 * @param listener
	 *            the listener to be added.
	 */
    public void addActionListener(ActionListener listener) {
        userGridPanel.addActionListener(listener);
    }

    /**
     * Returns the (only) selected row index of the grid.
     */
    public int getSelectedIndex() {
        return userGridPanel.getSelectionModel().getMinSelectedIndex();
    }

    /**
     * Returns the selected row of data.
     * @return the selected row of data.
     */
    public Object[] getSelectedRow() {
        return data[getSelectedIndex()];
    }

    /**
     * Makes dummy data for users.
     * @param startIndex the start index for the number placed into the text.
     * @return the dummy data.
     */
    private Object[][] makeData() {
        int rows = 10;

        data = new Object[rows][];

        for (int i = 0; i < data.length; i++) {
            Object[] row = new Object[3];
            row[0] = "User id  " + (startIndex + i);
            row[1] = "Name " + (startIndex + i);
            row[2] = "Role" + (i % 2 == 0 ? "User" : "Admin");
            data[i] = row;
        }

        return data;
    }

    private Toolbar makeToolbar() {
        Toolbar ret = new Toolbar();

        ToolbarButton button = new ToolbarButton();
        button.setIconClass("icon-settings");
        button.setTooltip("Show configuration options");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                log.info("Toolbar button was pressed");
            }
        });

        button.setMenu(makeMenu());

        ToolbarButton button2 = new ToolbarButton("Button2");
        button2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                log.info("Button2 was pressed");
            }
        });

        ret.add(button);
        ret.add(new ToolbarSeparator());
        ret.add(new ToolbarFill());
        ret.add(new ToolbarSeparator());
        ret.add(new ToolbarTextItem("Some text"));
        ret.add(button2);
        ret.add(new ToolbarSeparator());

        TextField tf = new TextField();
        tf.setEmptyText("Enter search criteria");
        ret.add(tf);

        return ret;
    }

    private Menu makeMenu() {
        Menu ret = new Menu();

        MenuItem item1 = new MenuItem("Item 1");
        ret.add(item1);
        item1.setIconClass("icon-folder-add");

        item1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                log.info("Menu item was clicked");
            }
        });

        return ret;
    }
}