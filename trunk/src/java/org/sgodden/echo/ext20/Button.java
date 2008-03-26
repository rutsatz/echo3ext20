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

import nextapp.echo.app.ImageReference;
import nextapp.echo.app.button.AbstractButton;
import nextapp.echo.app.button.DefaultButtonModel;

/**
 * An ext button.
 * 
 * @author goddens
 *
 */
public class Button extends AbstractButton {

    private static final long serialVersionUID = 20080102L;
    
    /**
     * When set to true, tells the client side rendering engine that this
     * button should go into the container's button bar, rather than being
     * added directly to its layout.
     */
    public static final String ADD_TO_BUTTON_BAR_PROPERTY = "addToButtonBar";

    /**
     * Creates a button with no text or icon.
     */
    public Button() {
        super();
        setModel(new DefaultButtonModel());
        setAddToButtonBar(false);
    }

    /**
     * Creates a button with text.
     *
     * @param text the text to be displayed in the button
     */
    public Button(String text) {
        this();
        setText(text);
    }

    /**
     * Creates a button with an icon.
     *
     * @param icon the icon to be displayed in the button
     */
    public Button(ImageReference icon) {
        this();
        setIcon(icon);
    }

    /**
     * Creates a button with text and an icon.
     *
     * @param text the text to be displayed in the button
     * @param icon the icon to be displayed in the button
     */
    public Button(String text, ImageReference icon) {
        this(text);
        setIcon(icon);
    }
    
    /**
     * See {@link #ADD_TO_BUTTON_BAR_PROPERTY}.
     * @param handleSpecial
     */
    void setAddToButtonBar(boolean addToButtonBar) {
        setProperty(ADD_TO_BUTTON_BAR_PROPERTY, addToButtonBar);
    }
}
