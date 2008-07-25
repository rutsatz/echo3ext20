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

import nextapp.echo.app.ImageReference;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * Abstract base class for all buttons.
 * 
 * @author sgodden
 */
@SuppressWarnings("serial")
public abstract class AbstractButton 
        extends ExtComponent {
    
    public static final String TEXT_PROPERTY = "text";
    public static final String PROPERTY_DISABLED_ICON = "disabledIcon";
    public static final String PROPERTY_ENABLED_ICON = "icon";
    public static final String INPUT_ACTION = "action";
    public static final String ACTION_COMMAND_PROPERTY = "actionCommand";
    public static final String TOOLTIP_TEXT_PROPERTY = "tooltipText";
    
    public static final String ACTION_LISTENERS_CHANGED_PROPERTY = "actionListeners";
    
    private String actionCommand;

    /**
     * Creates a button with no text or icon.
     */
    public AbstractButton() {
        super();
    }

    /**
     * Creates a button with text.
     *
     * @param text the text to be displayed in the button
     */
    public AbstractButton(String text) {
        this();
        setText(text);
    }

    /**
     * Creates a button with text and an icon.
     *
     * @param text the text to be displayed in the button
     * @param iconUrl the icon to be displayed in the button
     */
    public AbstractButton(String text, ImageReference icon) {
        this(text);
        setIcon(icon);
    }
    
    /**
     * Returns the icon displayed in the button
     * when the button is disabled.
     * 
     * @return the icon
     */
    public ImageReference getDisabledIcon() {
        return (ImageReference) getComponentProperty(PROPERTY_ENABLED_ICON);
    }
    
    /**
     * Returns the icon displayed in the button.
     * 
     * @return the icon
     */
    public ImageReference getIcon() {
        return (ImageReference) getComponentProperty(PROPERTY_ENABLED_ICON);
    }

    
    /**
     * Returns whether any <code>ActionListener</code>s are registered.
     * 
     * @return true if any action listeners are registered
     */
    public boolean hasActionListeners() {
        return getEventListenerList().getListenerCount(ActionListener.class) != 0;
    }
    
    /**
     * Adds an <code>ActionListener</code> to the button.
     * <code>ActionListener</code>s will be invoked when the button
     * is clicked.
     * 
     * @param l the <code>ActionListener</code> to add
     */
    public void addActionListener(ActionListener l) {
        getEventListenerList().addListener(ActionListener.class, l);
        // Notification of action listener changes is provided due to 
        // existence of hasActionListeners() method. 
        firePropertyChange(ACTION_LISTENERS_CHANGED_PROPERTY, null, l);
    }
    
    
    /**
     * Perform a button click programatically.
     */
    public void doClick() {
        fireActionEvent();
    }
    
    /**
     * Fires an action event to all listeners.
     */
    private void fireActionEvent() {
        if (!hasEventListenerList()) {
            return;
        }
        EventListener[] listeners = getEventListenerList().getListeners(ActionListener.class);
        ActionEvent e = null;
        for (int i = 0; i < listeners.length; ++i) {
            if (e == null) {
                e = new ActionEvent(this, actionCommand);
            }
            ((ActionListener) listeners[i]).actionPerformed(e);
        }
    }
    
    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
        if (INPUT_ACTION.equals(inputName)) {
            fireActionEvent();
        }
    }

    /**
     * Removes the specified action listener.
     * @param l the listener to remove.
     */
    public void removeActionListener(ActionListener l) {
        if (!hasEventListenerList()) {
            return;
        }
        getEventListenerList().removeListener(ActionListener.class, l);
        // Notification of action listener changes is provided due to 
        // existence of hasActionListeners() method. 
        firePropertyChange(ACTION_LISTENERS_CHANGED_PROPERTY, l, null);

    }
    
    public void setActionCommand(String actionCommand) {
        this.actionCommand = actionCommand;
    }

    /**
     * Sets the icon displayed in the button.
     * 
     * @param newValue the new icon
     */
    public void setDisabledIcon(ImageReference newValue) {
        setComponentProperty(PROPERTY_DISABLED_ICON, newValue);
    }
    
    /**
     * Sets the text to appear in the button.
     * @param text the text.
     */
    public void setText(String text) {
        setComponentProperty(TEXT_PROPERTY, text);
    }

    /**
     * Sets the icon displayed in the button.
     * 
     * @param newValue the new icon
     */
    public void setIcon(ImageReference newValue) {
        setComponentProperty(PROPERTY_ENABLED_ICON, newValue);
    }
    
    /**
     * Sets the passed text as the tool tip for the component.
     * @param tooltip
     */
    public void setTooltip(String tooltip) {
        setComponentProperty(TOOLTIP_TEXT_PROPERTY, tooltip);
    }
    

}
