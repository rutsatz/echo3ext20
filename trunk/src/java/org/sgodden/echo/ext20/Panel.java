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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nextapp.echo.app.Component;

import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
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
    
    public static final String INPUT_KEYPRESS_ACTION = "keyPress";
    public static final String KEYPRESS_LISTENERS_CHANGED_PROPERTY = "keyPressListeners";
    public static final String INPUT_KEY_PRESSED = "keyPressed";
    public static final String REGISTERED_KEY_PRESSES_PROPERTY="registeredKeyPresses";
    
    private Map<String, Set<ActionListener>> keyPressListeners;
    
    private Toolbar topToolbar;
    private Toolbar bottomToolbar;
    
    private String keyPressed;

    public Panel() {
        this(null, null);
    }
    
    public Panel(String title) {
        this(null, title);
    }

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
    
    /**
     * Sets the panel's top tool bar.
     * @param toolbar the tool bar to put at the top of the panel.
     */
    public void setToolbar(Toolbar toolbar) {
        if (topToolbar != null) {
            remove(topToolbar);
        }
        
        toolbar.setPosition(Toolbar.Position.TOP);
        add(toolbar);
        
        this.topToolbar = toolbar;
    }
    
    /**
     * Sets the panel's bottom tool bar.
     * @param toolbar the tool bar to put at the bottom of the panel.
     */
    public void setBottomToolbar(Toolbar toolbar) {
        if (bottomToolbar != null) {
            remove(bottomToolbar);
        }
        
        toolbar.setPosition(Toolbar.Position.BOTTOM);
        add(toolbar);
        
        this.bottomToolbar = toolbar;
    }
    
    @Override
    public void processInput(String inputName, Object inputValue) {
        if (INPUT_KEY_PRESSED.equals(inputName)) {
            this.keyPressed = (String) inputValue;
        }
        else if (INPUT_KEYPRESS_ACTION.equals(inputName)) {
            fireKeyEvent();
        }
        else {
            super.processInput(inputName, inputValue);
        }
    }
        
    /**
     * Adds a listener to be notified when the passed key is pressed and this
     * component either has the focus, or is the ancestor of the focused
     * component.
     * <p/>
     * @TODO - document the keyPress format.
     * 
     * @param keyPress the key press the listener wants to be notified of.
     * @param listener the listener to notify.
     */
    public void addKeyPressListener(String keyPress, ActionListener listener) {
        if (keyPressListeners == null) {
            keyPressListeners = new HashMap<String, Set<ActionListener>>();
        }
        
        Set<ActionListener> listeners = keyPressListeners.get(keyPress);
        if (listeners == null) {
            listeners = new HashSet<ActionListener>();
            keyPressListeners.put(keyPress, listeners);
        }
        
        listeners.add(listener);
        updateRegisteredKeyPresses();
        firePropertyChange(KEYPRESS_LISTENERS_CHANGED_PROPERTY, null, listener);
    }
    
    private void updateRegisteredKeyPresses(){
        StringBuffer sb = new StringBuffer();
        
        for (String keyPress : keyPressListeners.keySet()) {
            if (sb.length() > 0) {
                sb.append(':');
            }
            sb.append(keyPress);
        }
        
        setProperty(REGISTERED_KEY_PRESSES_PROPERTY, sb.toString());
    }

    private void fireKeyEvent() {
        if (!(keyPressListeners.containsKey(keyPressed))) {
            throw new IllegalStateException("Key press fired when no listener registered: " + keyPressed);
        }
        
        ActionEvent e = new ActionEvent(this, keyPressed);
        
        for (ActionListener listener : keyPressListeners.get(keyPressed)) {
            listener.actionPerformed(e);
        }
    }
    
    /**
     * Returns whether any key press listeners are registered on this component.
     * @return true if there are any listeners registered, false if not.
     */
    public boolean hasKeyPressListeners() {
        return (keyPressListeners != null && keyPressListeners.size() > 0);
    }
    
}