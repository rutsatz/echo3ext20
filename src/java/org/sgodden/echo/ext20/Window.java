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

import java.util.EventListener;
import nextapp.echo.app.event.ActionEvent;
import org.sgodden.echo.ext20.layout.Layout;

/**
 * A floating window.
 * 
 * @author goddens
 *
 */
public class Window 
        extends Panel {

    private static final long serialVersionUID = 20080102L;
    
    public static final String WINDOW_LISTENERS_CHANGED_PROPERTY = "windowListeners";
    public static final String INPUT_WINDOW_CLOSING = "windowClosing";
    
    public static final String MODAL_PROPERTY = "modal";

    public Window(Layout layout, String title) {
        super(layout, title);
    }

    public Window(Layout layout) {
        super(layout);
    }
    
    public Window(String title) {
        super(title);
    }

    public Window() {
        super();
    }
    
    /**
     * Sets whether the window is modal.
     * @param modal whether the window is modal.
     */
    public void setModal(boolean modal) {
        setProperty(MODAL_PROPERTY, modal);
    }
    
    /**
     * Adds the specified window listener.
     * @param l the window listener to add.
     */
    public void addWindowListener(WindowListener l) {
        getEventListenerList().addListener(WindowListener.class, l);
        firePropertyChange(WINDOW_LISTENERS_CHANGED_PROPERTY, null, l);
    }
    
    /**
     * Removes the specified window listener.
     * @param l the window listener to remove.
     */
    public void removeWindowListener(WindowListener l) {
        getEventListenerList().removeListener(WindowListener.class, l);
        firePropertyChange(WINDOW_LISTENERS_CHANGED_PROPERTY, null, l);
    }
    
    /**
     * Returns whether there are any window listeners registered in this window.
     * @return <code>true</code> if there are listeners registered, <code>false</code> if not.
     */
    public boolean hasWindowListeners() {
        return getEventListenerList().getListenerCount(WindowListener.class) != 0;
    }
    
        
    /**
     * @see nextapp.echo.app.Component#processInput(java.lang.String, java.lang.Object)
     */
    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
        if (INPUT_WINDOW_CLOSING.equals(inputName)) {
            fireWindowClosingEvent();
        }
    }
    
        
    /**
     * Fires a window closing event to all listeners.
     */
    private void fireWindowClosingEvent() {
        if (!hasEventListenerList()) {
            return;
        }
        EventListener[] listeners = getEventListenerList().getListeners(WindowListener.class);
        ActionEvent e = null;
        for (int i = 0; i < listeners.length; ++i) {
            if (e == null) {
                e = new ActionEvent(this, INPUT_WINDOW_CLOSING);
            }
            ((WindowListener) listeners[i]).windowClosing(e);
        }
    }


    
}