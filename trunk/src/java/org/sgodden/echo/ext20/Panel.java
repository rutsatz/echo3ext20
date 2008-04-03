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
import org.sgodden.echo.ext20.layout.TableLayout;

/**
 * An ext panel.
 * 
 * @author goddens
 *
 */
public class Panel extends ExtComponent {

    private static final long serialVersionUID = 20080102L;
    
    public static final String PROPERTY_PADDING = "padding";
    public static final String PROPERTY_BORDER = "border";
    
    public static final String LAYOUT_PROPERTY = "layout";
    public static final String TITLE_PROPERTY = "title";
    public static final String SPLIT_PROPERTY = "split";
    public static final String COLLAPSIBLE_PROPERTY = "collapsible";
    public static final String WIDTH_PROPERTY = "width";
    public static final String HEIGHT_PROPERTY = "height";
    public static final String HTML_PROPERTY = "html";

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
    
    public void setHeight(int pixels) {
        setProperty(HEIGHT_PROPERTY, pixels);
    }
    
    public void setHtml(String html) {
        setProperty(HTML_PROPERTY, html);
    }
    
    public void setWidth(int pixels) {
        setProperty(WIDTH_PROPERTY, pixels);
    }
    
    public void setCollapsible(boolean collapsible) {
        setProperty(COLLAPSIBLE_PROPERTY, collapsible);
    }
    
    public Layout getLayout() {
        return (Layout) getProperty(LAYOUT_PROPERTY);
    }
    
    /**
     * Adds a component to this container.
     * <p/>
     * Note that if this panel's layout is an instance of
     * {@link org.sgodden.echo.ext20.layout.TableLayout}, and the component
     * being added is not a panel, then it will be wrapped in a panel.  This
     * is necessary in order for table layout padding defaults to take effect.
     * @param comp the component to add.
     */
    @Override
    public void add(Component comp) {
        if (getLayout() instanceof TableLayout && !(comp instanceof Panel)) {
            Panel panel = new Panel();
            panel.setBorder(false);
            super.add(panel);
            panel.addNoWrapCheck(comp);
        }
        else {
            super.add(comp);
        }
    }
    
    /**
     * Removes the specified component from the container.
     * <p/>
     * Contains special processing in case this panel has a table layout
     * and the component was wrapped in a panel.
     * @param comp
     */
    @Override
    public void remove(Component comp) {
        
        if (getLayout() instanceof TableLayout && !(comp instanceof Panel)) {
            // Loop through the children.  If we find the passed component
            // inside the child, remove the child.
            for (Component child : getComponents()) {
                if (child instanceof Panel) {
                    if (child.getComponent(0) == comp) {
                        super.remove(child);
                    }
                }
            }
        }
        else {
            super.remove(comp);
        }
        
    }
    
    /**
     * Adds a component without checking whether it needs to be
     * wrapped in an outer panel.
     * @param comp
     */
    private void addNoWrapCheck(Component comp) {
        super.add(comp);
    }
    
    /**
     * Adds a button to the panel's button bar, rather than directly to its
     * layout.
     * @param button
     */
    public void addButton(Button button) {
        button.setAddToButtonBar(true);
        add(button);
    }
}
