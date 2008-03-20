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

import org.sgodden.echo.ext20.layout.Layout;

/**
 * An ext panel.
 * 
 * @author goddens
 *
 */
public class Panel extends Component {

    private static final long serialVersionUID = 20080102L;
    
    public static final String PROPERTY_PADDING = "padding";
    public static final String PROPERTY_BORDER = "border";
    
    public static final String LAYOUT_PROPERTY = "layout";
    public static final String TITLE_PROPERTY = "title";
    public static final String SPLIT_PROPERTY = "split";
    public static final String COLLAPSIBLE_PROPERTY = "collapsible";
    public static final String WIDTH_PROPERTY = "width";

    public Panel() {
        this(null, null);
    }

    /**
     * Creates a viewport with the specified layout.
     * 
     * @param regionFlags
     */
    public Panel(Layout layout) {
        this(layout, null);
    }

    public Panel(Layout layout, String title) {
        super();
        setProperty(LAYOUT_PROPERTY, layout);
        setTitle(title);
    }

    public void setTitle(String title) {
        setProperty(TITLE_PROPERTY, title);
    }

    /**
     * FIXME - should be using the existing setInsets method instead.
     * @param pixels
     */
    public void setPadding(int pixels) {
        setProperty(PROPERTY_PADDING, pixels + "px");
    }

    public void setBorder(Boolean border) {
        setProperty(PROPERTY_BORDER, border);
    }
}
