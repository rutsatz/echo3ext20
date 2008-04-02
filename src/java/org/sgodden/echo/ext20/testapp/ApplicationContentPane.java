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
import org.sgodden.echo.ext20.layout.BorderLayout;
import org.sgodden.echo.ext20.layout.BorderLayoutData;
import org.sgodden.echo.ext20.layout.ColumnLayout;
import org.sgodden.echo.ext20.layout.FitLayout;
import org.sgodden.echo.ext20.layout.TableLayout;

/**
 * Application content pane for the test application.
 * 
 * @author sgodden
 */
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

    public ApplicationContentPane() {
        super();
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
        outer.setBorder(false);
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
        
        TabbedPane tabs = createTabbedPane();
        tabs.setLayoutData(new BorderLayoutData(BorderLayout.CENTER));
        main.add(tabs);

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
        ret.add(imagePanel);
        
        HtmlPanel titlePanel = new HtmlPanel("<h1>NORTH</h1>");
        titlePanel.setRenderId("northTitlePanel");
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
        imagePanel.setBorder(false);
        ret.add(imagePanel);
        
        HtmlPanel suppliersPanel = new HtmlPanel("Suppliers");
        ret.add(suppliersPanel);
        
//        Panel fit = new Panel(new FitLayout());
//        fit.setWidth(100);
//        ret.add(fit);
//
//        Panel accordion = new Panel(new AccordionLayout());
//        accordion.setTitle("Accordion");
//        accordion.setRenderId("accordion");
//        fit.add(accordion);
        
//        Panel section1 = new Panel();
//        section1.setTitle("Suppliers");
//        section1.setBorder(false);
//        accordion.add(section1);
        
//        HtmlPanel childPanel = new HtmlPanel("Supplier list");
//        //childPanel.setCollapsible(true);
//        childPanel.setTitle("Blah");
//        //childPanel.setPadding(5);
//        accordion.add(childPanel);
//        
//        HtmlPanel childPanel2 = new HtmlPanel("New Supplier request");
//        //childPanel.setCollapsible(true);
//        childPanel2.setTitle("Wer");
//        //childPanel2.setPadding(5);
//        accordion.add(childPanel2);
        
//        Panel section2 = new Panel();
//        section2.setBorder(false);
//        section2.setTitle("Sites");
//        accordion.add(section2);
//        
//        HtmlPanel childPanel3 = new HtmlPanel("Site list");
//        //childPanel.setCollapsible(true);
//        childPanel3.setPadding(5);
//        section2.add(childPanel3);
//        
//        HtmlPanel childPanel4 = new HtmlPanel("New Site request");
//        //childPanel.setCollapsible(true);
//        childPanel4.setPadding(5);
//        section2.add(childPanel4);

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

        ret.add(new UserPanel());

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

    public void actionPerformed(ActionEvent arg0) {
        statusField.setValue("You clicked the button " + ++buttonClicks + " times.");
    }
}
