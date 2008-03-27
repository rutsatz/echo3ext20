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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sgodden.echo.ext20.Button;
import org.sgodden.echo.ext20.CheckboxField;
import org.sgodden.echo.ext20.DateField;
import org.sgodden.echo.ext20.HtmlEditor;
import org.sgodden.echo.ext20.HtmlPanel;
import org.sgodden.echo.ext20.Panel;
import org.sgodden.echo.ext20.RadioButton;
import org.sgodden.echo.ext20.TabbedPane;
import org.sgodden.echo.ext20.TextField;
import org.sgodden.echo.ext20.TimeField;
import org.sgodden.echo.ext20.data.SimpleStore;
import org.sgodden.echo.ext20.grid.ColumnConfiguration;
import org.sgodden.echo.ext20.grid.ColumnModel;
import org.sgodden.echo.ext20.grid.GridPanel;
import org.sgodden.echo.ext20.layout.BorderLayout;
import org.sgodden.echo.ext20.layout.BorderLayoutData;
import org.sgodden.echo.ext20.layout.ColumnLayout;
import org.sgodden.echo.ext20.layout.FitLayout;
import org.sgodden.echo.ext20.layout.FormLayout;
import org.sgodden.echo.ext20.layout.TableLayout;

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
    private Panel userListPanel;
    private GridPanel userGridPanel;
    private Panel userEditPanel;

    public ApplicationContentPane() {
        super();
        //add(new HtmlPanel("<h1>HTML PANEL!!!</h1>"));
        addViewport();
        //addSplitPane();
    }
    
    private void addSplitPane() {
        
        TimeField tf = new TimeField();
        add(tf);
        
        return;
        
//        SplitPane sp1 = new SplitPane(SplitPane.ORIENTATION_VERTICAL);
//        add(sp1);
//        
//        sp1.setSeparatorPosition(new Extent(50));
//        sp1.add(new HtmlPanel("<h1>Top</h1>"));
//        
//        SplitPane sp = new SplitPane(SplitPane.ORIENTATION_HORIZONTAL);
//        sp.setSeparatorPosition(new Extent(100));
//        sp.add(new HtmlPanel("<h1>asdasd</h1>"));
//        sp.add(createUserPanel());
//        sp.setResizable(true);
//        
//        sp1.add(sp);
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
        
        //main.add(createNorthPanel());

        statusField = new TextField("Click the button and see what happens");
        statusField.setEnabled(false);
        statusField.setRenderId("southPanelTextField");
        statusField.setLayoutData(new BorderLayoutData(BorderLayout.SOUTH));
        main.add(statusField);

        main.add(createWestPanel());

//        TabbedPane tabs = createTabbedPane();
//        tabs.setRenderId("tabs");
//        tabs.setLayoutData(new BorderLayoutData(BorderLayout.CENTER));
//        main.add(tabs);
        
        createUserPanel();
        userPanel.setLayoutData(new BorderLayoutData(BorderLayout.CENTER));
        main.add(userPanel);

        Button button = new Button("Press me!");
        button.setRenderId("button");
        //tabs.add(button);

        button.addActionListener(this);

    }
    
    private Panel createNorthPanel() {
        Panel ret = new Panel(new ColumnLayout());
        ret.setLayoutData(new BorderLayoutData(BorderLayout.NORTH));
        
        HtmlPanel imagePanel = new HtmlPanel(
                "<img style='float: left;' src='http://demo.nextapp.com/echo3csjs/image/Logo.png'></img>");
        imagePanel.setRenderId("northImagePanel");
        //imagePanel.setLayoutData(new ColumnLayoutData(.2));
        ret.add(imagePanel);
        
        HtmlPanel titlePanel = new HtmlPanel("<h1>NORTH</h1>");
        titlePanel.setRenderId("northTitlePanel");
        //titlePanel.setLayoutData(new ColumnLayoutData(.8));
        ret.add(titlePanel);
        
        return ret;
    }
    
    private Panel createNorthPanel2() {
        Panel ret = new Panel(new TableLayout(2));
        ret.setLayoutData(new BorderLayoutData(BorderLayout.NORTH));
        
        HtmlPanel imagePanel = new HtmlPanel(
                "<img style='float: left;' src='http://demo.nextapp.com/echo3csjs/image/Logo.png'></img>");
        imagePanel.setRenderId("northImagePanel");
        ret.add(imagePanel);
        
        HtmlPanel titlePanel = new HtmlPanel("<h1>NORTH</h1>");
        titlePanel.setRenderId("northTitlePanel");
        ret.add(titlePanel);
        
        return ret;
    }

    /**
     * Creates a silly panel for the west region.
     * @return
     */
    private Panel createWestPanel() {
        Panel ret = new Panel();
        ret.setTitle("Navigation");
        ret.setWidth(143); // FIXME should not have to do this
        ret.setCollapsible(true);
        ret.setBorder(true);
        ret.setLayoutData(new BorderLayoutData(BorderLayout.WEST));
        ret.setRenderId("westPanel");
        
        HtmlPanel imagePanel = new HtmlPanel(
                "<img src='http://demo.nextapp.com/echo3csjs/image/Logo.png'></img>");
        imagePanel.setRenderId("northImagePanel");
        ret.add(imagePanel);
        
        HtmlPanel titlePanel = new HtmlPanel("Some navigation here");
        titlePanel.setRenderId("northTitlePanel");
        titlePanel.setPadding(5);
        ret.add(titlePanel);


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
        userPanel = new Panel(new FitLayout());
        //userPanel = new Panel();
        userPanel.setTitle("Users");

        userPanel.setRenderId("userPanel");

        final Object[][] data = makeData();

        userListPanel = createUserList(data);
        userPanel.add(userListPanel);

        userGridPanel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // remove the list panel
                userPanel.remove(userListPanel);
                // get the selected row of the data
                int selectedRow = userGridPanel.getSelectionModel().getMinSelectedIndex(); // only one row can be selected
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
    private Panel createUserList(Object[][] data) {
        
        Panel ret = new Panel(new BorderLayout());
        ret.setRenderId("userList");
        
        final Panel filterOptions = new Panel(new TableLayout(2));
        filterOptions.setLayoutData(new BorderLayoutData(BorderLayout.NORTH));
        ret.add(filterOptions);
        
        final TextField tf = new TextField("Aasd");
        filterOptions.add(tf);
        
        Button button = new Button("Search");
        filterOptions.add(button);
        
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                filterOptions.remove(tf);
                filterOptions.add(new TextField("Wwerwer"));
            }
        });
        
        List<ColumnConfiguration> cols = new ArrayList<ColumnConfiguration>();
        cols.add(new ColumnConfiguration("User ID", "userid"));
        cols.add(new ColumnConfiguration("Name", "name"));
        ColumnModel columnModel = new ColumnModel(cols);

        SimpleStore store = new SimpleStore(
                data,
                0,
                new String[]{"id", "userid", "name", "date"});

        userGridPanel = new GridPanel(columnModel, store);
        ret.add(userGridPanel);
        
        userGridPanel.setLayoutData(new BorderLayoutData(BorderLayout.CENTER));
        
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
    private void createUserEditPanel(Object[] data) {
        userEditPanel = new Panel(new FormLayout());
        //userEditPanel.setBorder(true);
        userEditPanel.setPadding(5);
        userEditPanel.setRenderId("userFormPanel");

        final TextField codeField = new TextField((String)data[1], "Code");
        codeField.setBlankAllowed(false);
        userEditPanel.add(codeField);

        final TextField nameField = new TextField((String)data[2], "Name");
        nameField.setBlankAllowed(false);
        userEditPanel.add(nameField);
        
        Calendar cal = Calendar.getInstance(
                ApplicationInstance.getActive().getLocale());

        final DateField dateField = new DateField(cal, "Date");
        dateField.setBlankAllowed(false);
        userEditPanel.add(dateField);

        final TimeField timeField = new TimeField(cal, "Time");
        timeField.setBlankAllowed(false);
        userEditPanel.add(timeField);
        
        final CheckboxField enabledField = new CheckboxField(true, "Enabled");
        userEditPanel.add(enabledField);
        
        final RadioButton userRoleButton = new RadioButton(true, "User");
        userRoleButton.setName("role");
        userEditPanel.add(userRoleButton);
        
        final RadioButton adminRoleButton = new RadioButton(false, "Admin");
        adminRoleButton.setName("role");
        userEditPanel.add(adminRoleButton);

        Button cancelButton = new Button("Cancel");
        userEditPanel.addButton(cancelButton);

        // and add a listener to the cancel button which removes that form panel
        // and puts the list back
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userPanel.remove(userEditPanel);
                userPanel.add(userListPanel);
            }
        });
        
        Button saveButton = new Button("Save");
        userEditPanel.addButton(saveButton);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // print out field values to make sure they have transferred correctly
                log.info("Save button pressed:");
                log.info("  codeField: " + codeField.getText());
                log.info("  nameField: " + nameField.getText());
                log.info("  calendar: " + dateField.getCalendar());
                log.info("  enabled: " + enabledField.getSelected());
                
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
    private Object[][] makeData() {
        int rows = 100;
        
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
