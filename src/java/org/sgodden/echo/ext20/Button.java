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
     * Creates a button with no text or icon.
     */
    public Button() {
        this(null, null);
    }
    
    /**
     * Creates a button with text.
     *
     * @param text the text to be displayed in the button
     */
    public Button(String text) {
        this(text, null);
    }
    
    /**
     * Creates a button with an icon.
     *
     * @param icon the icon to be displayed in the button
     */
    public Button(ImageReference icon) {
        this(null, icon);
    }

    /**
     * Creates a button with text and an icon.
     *
     * @param text the text to be displayed in the button
     * @param icon the icon to be displayed in the button
     */
    public Button(String text, ImageReference icon) {
        super();
        
        setModel(new DefaultButtonModel());
    
        setIcon(icon);
        setText(text);
    }
	
}
