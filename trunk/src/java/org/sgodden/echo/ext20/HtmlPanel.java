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
package org.sgodden.echo.ext20;

import nextapp.echo.app.Component;

/**
 * A panel which displays html markup.
 * 
 * @author goddens
 *
 */
public class HtmlPanel extends Component {

    private static final long serialVersionUID = 20080102L;
    public static final String PROPERTY_PADDING = "padding";
    public static final String PROPERTY_BORDER = "border";
    public static final String HTML_PROPERTY = "html";
    public static final String COLLAPSIBLE_PROPERTY = "collapsible";
    public static final String TITLE_PROPERTY = "title";
    public static final String WIDTH_PROPERTY = "width";


    /**
     * Creates a panel with the specified html markup.
     * 
     * @param html the html markup to format and display.
     */
    public HtmlPanel(String html) {
        super();
        setHtml(html);
    }

    /**
     * Creates a panel with the specified title and html markup.
     * 
     * @param title the title of the panel.
     * @param html the html markup to format and display.
     */
    public HtmlPanel(String title, String html) {
        super();
        setHtml(html);
        setTitle(title);
    }

    public void setHtml(String html) {
        setProperty(HTML_PROPERTY, html);
    }

    public void setTitle(String title) {
        setProperty(TITLE_PROPERTY, title);
    }

    public void setCollapsible(boolean collapsible) {
        setProperty(COLLAPSIBLE_PROPERTY, new Boolean(collapsible));
    }
    
    public void setBorder(Boolean border) {
        setProperty(PROPERTY_BORDER, border);
    }
        
    public void setWidth(int pixels) {
        setProperty(WIDTH_PROPERTY, pixels);
    }
    
    /**
     * FIXME - should be using the existing setInsets method instead.
     * @param pixels
     */
    public void setPadding(int pixels) {
        setProperty(PROPERTY_PADDING, pixels + "px");
    }
}
