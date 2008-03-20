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

import nextapp.echo.app.ContentPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sgodden.echo.ext20.Button;
import org.sgodden.echo.ext20.HtmlEditor;
import org.sgodden.echo.ext20.HtmlPanel;
import org.sgodden.echo.ext20.Panel;
import org.sgodden.echo.ext20.TabbedPane;
import org.sgodden.echo.ext20.TextField;
import org.sgodden.echo.ext20.data.SimpleStore;
import org.sgodden.echo.ext20.grid.ColumnConfiguration;
import org.sgodden.echo.ext20.grid.ColumnModel;
import org.sgodden.echo.ext20.grid.GridPanel;
import org.sgodden.echo.ext20.layout.BorderLayout;
import org.sgodden.echo.ext20.layout.BorderLayoutData;
import org.sgodden.echo.ext20.layout.FitLayout;
import org.sgodden.echo.ext20.layout.FormLayout;

public class ApplicationContentPane
        extends ContentPane implements ActionListener {

    private static final long serialVersionUID = 20080103L;
    private static final Log log = LogFactory.getLog(ApplicationContentPane.class);
    /**
     * Disabled text field used for status feedback.
     */
    private TextField statusField;
    /**
     * Number of button clicks performed.
     */
    private int buttonClicks = 0;
    /**
     * Panel which toggles between user list and user edit
     */
    private Panel userPanel;
    private GridPanel userListPanel;
    private Panel userEditPanel;

    public ApplicationContentPane() {
        super();
        //add(new HtmlPanel("<h1>HTML PANEL!!!</h1>"));
        addViewport();
    }

    /**
     * Adds the main viewport (the overall screen).
     */
    private void addViewport() {

        /*
         * For some reason, this is all only working
         * if the outer panel has fit layout.  Border layout
         * just seems to go wrong.
         */
        Panel outer = new Panel(new FitLayout());
        outer.setRenderId("viewport");
        add(outer);

        Panel main = new Panel(new BorderLayout());
        outer.add(main);
        main.setRenderId("main");

        HtmlPanel panel = new HtmlPanel("<h1>NORTH</h1>");
        panel.setRenderId("northPanel");
        panel.setLayoutData(new BorderLayoutData(BorderLayout.NORTH));
        main.add(panel);

        statusField = new TextField("Click the button and see what happens");
        statusField.setEnabled(false);
        statusField.setRenderId("southPanelTextField");
        statusField.setLayoutData(new BorderLayoutData(BorderLayout.SOUTH));
        main.add(statusField);

        main.add(createWestPanel());

        TabbedPane tabs = createTabbedPane();
        tabs.setRenderId("tabs");
        tabs.setLayoutData(new BorderLayoutData(BorderLayout.CENTER));
        main.add(tabs);

        Button button = new Button("Press me!");
        button.setRenderId("button");
        tabs.add(button);

        button.addActionListener(this);

    }

    /**
     * Creates a silly panel for the west region.
     * @return
     */
    private Panel createWestPanel() {
        Panel ret = new Panel();
        ret.setLayoutData(new BorderLayoutData(BorderLayout.WEST));
        ret.setRenderId("westPanel");

        HtmlPanel panel = new HtmlPanel("<p>First panel</p>");
        panel.setTitle("First panel");
        panel.setCollapsible(true);
        ret.add(panel);

        panel = new HtmlPanel("<p>Second panel</p>");
        panel.setTitle("Second panel");
        panel.setCollapsible(true);
        ret.add(panel);

        return ret;
    }

    /**
     * Creates the tabs that go in the centre region.
     * @return
     */
    private TabbedPane createTabbedPane() {
        TabbedPane ret = new TabbedPane();

        HtmlPanel centerPanel = new HtmlPanel("<h1>Welcome</h1>" +
                "<p>This test application is very rudimentary right now.</p>" +
                "<p/>" +
                "<p>I will put some details here about what it does soon</p>.");
        centerPanel.setTitle("Welcome");
        ret.add(centerPanel);

        HtmlPanel panel2 = new HtmlPanel("<h1>PANEL 2</h1><p><em>I am the second center panel</em></p>");
        panel2.setTitle("Panel 2");
        ret.add(panel2);

        ret.add(createUserPanel());

        return ret;
    }

    /**
     * Creates a panel which toggles between a list of users, and 
     * an edit panel for the selected user. 
     * @return
     */
    private Panel createUserPanel() {
        userPanel = new Panel(
                new FitLayout(),
                "Users");

        userPanel.setRenderId("userPanel");

        final String[][] data = makeData();

        userListPanel = createUserList(data);
        userPanel.add(userListPanel);
        userPanel.setRenderId("userListPanel");

        userListPanel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // remove the list panel
                userPanel.remove(userListPanel);
                // get the selected row of the data
                int selectedRow = userListPanel.getSelectionModel().getMinSelectedIndex(); // only one row can be selected
                // create the form panel with that data
                createUserEditPanel(data[selectedRow]);
                userPanel.add(userEditPanel);
            }
        });


        return userPanel;
    }

    /**
     * Creates a panel which toggles between a list of users and an edit panel
     * for a particular user.
     * @return
     */
    private GridPanel createUserList(String[][] data) {
        List<ColumnConfiguration> cols = new ArrayList<ColumnConfiguration>();
        cols.add(new ColumnConfiguration("User ,ID", "userid"));
        cols.add(new ColumnConfiguration("Name", "name"));
        ColumnModel columnModel = new ColumnModel(cols);

        SimpleStore store = new SimpleStore(
                data,
                0,
                new String[]{"id", "userid", "name"});

        final GridPanel ret = new GridPanel(columnModel, store);
        return ret;
    }

    /**
     * Creates an html editor panel.
     * @return
     */
    private Panel createHtmlEditorPanel() {
        Panel ret = new Panel(new FitLayout());
        ret.setTitle("Welcome");

        HtmlEditor htmlEditor = new HtmlEditor("<b>Here's a HTML editor.</b><br><br><i>Note that in Firefox, this cannot be added as a component which is not initially rendered (e.g. as a tab which is not initially selected in a tabbed panel), due to a bug in Firefox's implementation of doc.designMode.</i><br>");
        ret.add(htmlEditor);

        return ret;
    }

    /**
     * Creates a form panel to edit the selected user.
     * @return
     */
    private void createUserEditPanel(String[] data) {
        userEditPanel = new Panel(new FormLayout());
        userEditPanel.setPadding(5);
        userEditPanel.setRenderId("userFormPanel");

        TextField codeField = new TextField(data[1], "Code");
        codeField.setBlankAllowed(false);
        userEditPanel.add(codeField);

        TextField nameField = new TextField(data[2], "Name");
        nameField.setBlankAllowed(false);
        userEditPanel.add(nameField);


        Button cancelButton = new Button("cancel");
        userEditPanel.add(cancelButton);

        // and add a listener to the cancel button which removes that form panel
        // and puts the list back
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userPanel.remove(userEditPanel);
                userPanel.add(userListPanel);
            }
        });
    }

    public void actionPerformed(ActionEvent arg0) {
        statusField.setText("You clicked the button " + ++buttonClicks + " times.");
    }
    
    /**
     * Makes enough data to test the eval bug in Firefox.
     * @return
     */
    private String[][] makeData() {
        int rows = 200;
        
        String[][] ret = new String[rows][];
        
        for (int i = 0; i < ret.length; i++) {
            String[] row = new String[3];
            row[0] = String.valueOf(i);
            row[1] = "User id asdasdasdasdasd " + i;
            row[2] = "Name asdasdasdasdasdasd " + i;
            ret[i] = row;
        }
        
        return ret;
    }
}
