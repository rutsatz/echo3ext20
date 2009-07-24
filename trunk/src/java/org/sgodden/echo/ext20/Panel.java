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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

import org.sgodden.echo.ext20.layout.Layout;

/**
 * The main container component for Ext.
 * 
 * @author goddens
 *
 */
public class Panel extends ExtComponent {

    private static final long serialVersionUID = 20090324L;

    /**
     * Sets the base css for this panel. (Defaults to 'x-panel')
     */
    public static final String BASE_CSS_CLASS = "baseCssClass";
    /**
     * A CSS class that will provide a background image to be used as the header icon (defaults to '').
     */
    public static final String ICON_CSS_CLASS = "iconCssClass";
    /**
     * Whether the panel show scroll its contents to handle overflow,
     * or clip overflowing contents (defaults to false, to clip).
     */
    public static final String PROPERTY_AUTOSCROLL = "autoScroll";
    /**
     * The tab tip when the Panel is added to a TabbedPane
     */
    public static final String PROPERTY_TABTIP = "tabTip";
    
    /**
     * Whether to render a border around the whole panel.
     * <p>
     * Type: Boolean.
     * </p>
     */
    public static final String PROPERTY_BORDER = "border";
    /**
     * Whether to render a border around the panel body.
     * <p>
     * Type: Boolean.
     * </p>
     */
    public static final String PROPERTY_BODY_BORDER = "bodyBorder";
    /**
     * Padding for the overall panel.
     * <p>
     * Type: String in CSS padding specification style.
     * </p>
     */
    public static final String PROPERTY_PADDING = "padding";
    /**
     * The background color for the panel body.
     * <p>
     * Type: Color.
     * </p>
     */
    public static final String PROPERTY_BODY_BACKGROUND = "bodyBackground";
    /**
     * Padding for the panel body.
     * <p>
     * Type: String in CSS padding specification style.
     * </p>
     */
    public static final String PROPERTY_BODY_PADDING = "bodyPadding";
    /**
     * The height of the panel, in pixels.
     * <p>
     * Type: Integer.
     * </p>
     */
    public static final String PROPERTY_HEIGHT = "height";
    /**
     * Whether the panel should have a transparent background.
     * <p>
     * Type: Boolean.
     * </p>
     */
    public static final String PROPERTY_BODY_TRANSPARENT = "bodyTransparent";
    
    /**
     * The width of the panel, in pixels.
     * <p>
     * Type: Integer.
     * </p>
     */
    public static final String PROPERTY_WIDTH = "width";
    
    public static final String PROPERTY_TITLE = "title";
    
    public static final String COLLAPSIBLE_PROPERTY = "collapsible";
    public static final String HTML_PROPERTY = "html";
    public static final String LAYOUT_PROPERTY = "layout";
    public static final String SPLIT_PROPERTY = "split";
    public static final String TOOL_IDS_PROPERTY = "toolIds";
    
    public static final String INPUT_KEY_PRESSED = "keyPressed";
    public static final String INPUT_KEYPRESS_ACTION = "keyPress";
    public static final String INPUT_TOOLCLICK_ACTION = "toolClick";
    public static final String BEFORE_EXPAND_ACTION = "dontexpand";
    public static final String INPUT_TOOLID_CLICKED = "toolIdClicked";
    public static final String KEYPRESS_LISTENERS_CHANGED_PROPERTY = "keyPressListeners";
    public static final String REGISTERED_KEY_PRESSES_PROPERTY="registeredKeyPresses";
    public static final String TOOLCLICK_LISTENERS_CHANGED_PROPERTY = "toolclickListeners";
    public static final String BEFOREEXPAND_LISTENERS_CHANGED_PROPERTY = "dontexpandListeners";
    /**
     * Whether the panel should be drawn with rounded borders.
     * <p>
     * Type: Boolean.
     * </p>
     */
    public static final String ROUNDED_BORDERS_PROPERTY = "frame";
    
    /**
     * Whether the title bar should appear above or below the tool bar.
     */
    public static final String TITLE_POSITION = "titlePosition";
    
    /**
     * Whether the panel should be 'floating' (absolute positioning)
     * <p>
     * Type: Boolean.
     * </p>
     */
    public static final String FLOATING_PROPERTY = "floating";
    /**
     * The x position of the panel (used only when floating)
     * <p>
     * Type: Integer.
     * </p>
     */
    public static final String POSITION_X = "positionX";
    /**
     * The y position of the panel (used only when floating)
     * <p>
     * Type: Integer.
     * </p>
     */
    public static final String POSITION_Y = "positionY";
    /**
     * The relative (to the panel's container) x position of the panel (used only when floating)
     * <p>
     * Type: Integer.
     * </p>
     */
    public static final String RELATIVE_POSITION_X = "relativePositionX";
    /**
     * The relative (to the panel's container) y position of the panel (used only when floating)
     * <p>
     * Type: Integer.
     * </p>
     */
    public static final String RELATIVE_POSITION_Y = "relativePositionY";
    
    /**
     * The anchor position to which this panel works out it's relative position when floating.
     * May be one of: TL, TR, BL, BR which are Top Left, Top Right, Bottom Left, Bottom Right respectively.
     * <p>
     * Type: String.
     * </p>
     */
    public static final String RELATIVE_ANCHOR_POSITION = "relativeAnchorPosition";
    
    public static enum TitlePosition{
        ABOVE_TOOLBAR, BELOW_TOOLBAR
    }
    
    public static enum RelativeAnchorPosition {
        TL,
        TR,
        BL,
        BR;
        
        public static RelativeAnchorPosition forName(String name) {
            if ("TL".equals(name)) {
                return TL;
            } else if ("TR".equals(name)) {
                return TR;
            } else if ("BL".equals(name)) {
                return BL;
            } else if ("BR".equals(name)) {
                return BR;
            } else {
                return null;
            }
        }
    }
    
    private Map<String, Set<ActionListener>> keyPressListeners;
    private Map<Tool, Set<ActionListener>> toolListeners;
    private List<ActionListener> beforeExpandListeners;
    
    private Toolbar topToolbar;
    private Toolbar bottomToolbar;
    
    private String keyPressed;
    private String toolIdClicked;
    
    private int nonButtonBarChildCount = 0;
    
    private Menu contextMenu;

    /**
     * Creates a new empty panel with the default container layout.
     */
    public Panel() {
        this(null, null);
    }
    
    /**
     * Creates a new panel.
     * @param title the title for the panel.
     */
    public Panel(String title) {
        this(null, title);
    }

    /**
     * Creates a new panel.
     * @param layout the layout for the panel.
     */
    public Panel(Layout layout) {
        this(layout, null);
    }

    /**
     * Creates a new panel.
     * @param layout the layout for the panel.
     * @param title the title for the panel.
     */
    public Panel(Layout layout, String title) {
        super();
        set(LAYOUT_PROPERTY, layout);
        setTitle(title);
    }

    /**
     * Returns the panel's bottom toolbar.
     * @return the bottom toolbar.
     */
    public Toolbar getBottomToolbar() {
        return bottomToolbar;
    }
    
    /**
     * Returns the panel's top toolbar.
     * @return the top toolbar.
     */
    public Toolbar getToolbar() {
    	return topToolbar;
    }

    /**
     * Constructor taking a map of configuration options,
     * to be groovy-friendly.
     */
//    public Panel(Map<String, Object> options) {
//        super();
//        setOptions(options);
//    }

    /**
     * Sets the options for the component as a map.
     * @param options
     */
    public void setOptions(Map<String, Object> options) {
        for (String key : options.keySet()) {
            if (key.equals(PROPERTY_TITLE)) {
                setTitle((String)options.get(key));
            }
            else if (key.equals(HTML_PROPERTY)) {
                setHtml((String)options.get(key));
            }
            else {
                throw new IllegalArgumentException("Unknown property: " + key);
            }
        }
    }

    /**
     * Sets the title of the panel.
     * @param title the title of the panel.
     */
    public void setTitle(String title) {
        set(PROPERTY_TITLE, title);
    }
    
    /**
     * Returns the panel's title.
     * @return the panel's title.
     */
    public String getTitle() {
        return (String) get(PROPERTY_TITLE);
    }
    /**
     * Sets the tabTip of the panel.
     * @param tabTip the tabTip of the panel.
     */
    public void setTabTip( String tabTip) {
    	set( PROPERTY_TABTIP, tabTip);
    }
    /**
     * Returns the panel's tabTip
     * @return If the panel's tabTip is setted, then return the tabTip, else return the title 
     */
    public String getTabTip() {
    	String tabTip = (String) get( PROPERTY_TABTIP);
    	if ( tabTip == null) return getTitle();
		return tabTip;
    }

    
    /**
     * Sets the padding of the overall panel, in CSS style.
     * @param padding the padding of the overall panel, in CSS style.
     */
    public void setPadding(String padding) {
        set(PROPERTY_PADDING, padding);
    }

    /**
     * Sets the padding of the panel body, in CSS style.
     * @param padding the padding of the panel body, in CSS style.
     */
    public void setBodyPadding(String padding) {
        set(PROPERTY_BODY_PADDING, padding);
    }

    /**
     * Sets whether the panel's border should be shown.
     * @param border whether the panel's border should be shown.
     */
    public void setBorder(Boolean border) {
        set(PROPERTY_BORDER, border);
    }

    /**
     * Sets the background color of the panel body.
     * @param color the background color of the panel body.
     */
    public void setBodyBackground(Color color) {
        set(PROPERTY_BODY_BACKGROUND, color);
    }

    /**
     * Sets whether the panel's body border should be shown.
     * @param border whether the panel's body border should be shown.
     */
    //public void setBodyBorder(Boolean border) {
    //    setProperty(PROPERTY_BODY_BORDER, border);
    //}
    
    /**
     * Sets the height of the panel in pixels.
     * @param pixels the height of the panel in pixels.
     */
    public void setHeight(int pixels) {
        set(PROPERTY_HEIGHT, pixels);
    }
    
    /**
     * Specified some arbitrary HTML to show as the panel's contents.
     * <p/>
     * Note that this cannot be used in conjunction with the {@link #add(Component)} method.
     * @param html the HTML to show as the panel's contents.
     */
    public void setHtml(String html) {
        set(HTML_PROPERTY, html);
    }
    
    /**
     * Sets the width of the panel in pixels.
     * @param pixels the width of the panel in pixels.
     */
    public void setWidth(int pixels) {
        set(PROPERTY_WIDTH, pixels);
    }
    
    /**
     * Sets whether to use overflow:'auto' on the panel's body element 
     * and show scroll bars automatically when necessary (true), or
     * whether  
     * to clip any overflowing content (false) - default to false.
     * @param autoScroll whether to scroll the contents of the panel.
     */
    public void setAutoScroll(boolean autoScroll) {
        set(PROPERTY_AUTOSCROLL, autoScroll);
    }
    /**
     * Returns the scrollability of the panel.
     * @return is the panel scrollable.
     */
    public boolean getAutoScroll(){
        Object scrollable = get(PROPERTY_AUTOSCROLL);
        if(scrollable == null){
            return false;
        }
        else{
            return (Boolean) scrollable;
        }
    }

	/**
     * Sets whether the panel should be collapsible.
     * @param collapsible whether the panel should be collapsible.
     */
    public void setCollapsible(boolean collapsible) {
        set(COLLAPSIBLE_PROPERTY, collapsible);
    }
    
    /**
     * Returns the layout in use by this panel.
     * @return the layout in use by this panel.
     */
    public Layout getLayout() {
        return (Layout) get(LAYOUT_PROPERTY);
    }
    
    /**
     * Sets the layout on the panel.
     * @param layout the layout to use in the panel.
     */
    public void setLayout(Layout layout) {
        set(LAYOUT_PROPERTY, layout);
    }
    
    /**
     * If set to <code>true</code>, renders the panel body's background
     * as transparent.
     * @param transparent whether the panel body's background should be transparent.
     */
    public void setBodyTransparent(boolean transparent) {
        set(PROPERTY_BODY_TRANSPARENT, transparent);
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
        if ( !(comp instanceof Button)
                || (comp instanceof Button && !((Button)comp).isAddToButtonBar()) ) {
            nonButtonBarChildCount--;
            super.remove(comp);
        }
        else {
            super.remove(comp);
        }
    }
    
    /**
     * See {@link Component#removeAll()}.
     */
    @Override
    public void removeAll() {
        nonButtonBarChildCount = 0;
        super.removeAll();
    }
    
    /**
     * Adds a button to the panel's button bar, rather than directly to its
     * layout.
     * @param button
     */
    public void addButton(Button button) {
        button.setAddToButtonBar(true);
        super.add(button);
    }
    
    /**
     * Adds a listener to repond to the click event of the specified tool,
     * also adding the tool to the panel if it does not already exist.
     * <p/>
     * Tools are small (8x8) icons added to the title bar of a panel.
     * <p/>
     * See the documentation for Ext.Panel.tools for further information.
     * 
     * @param tool the tool to add.
     * @param listener the listener to respond to the click event.
     */
    public void addToolListener(Tool tool, ActionListener listener) {
        if (toolListeners == null) {
            toolListeners = new HashMap<Tool, Set<ActionListener>>();
        }
        
        Set<ActionListener> listeners = toolListeners.get(tool);
        if (listeners == null) {
            listeners = new HashSet<ActionListener>();
            toolListeners.put(tool, listeners);
        }
        
        listeners.add(listener);
        
        StringBuffer sb = new StringBuffer();
        for (Tool theTool : toolListeners.keySet()) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(theTool.toString().toLowerCase());
        }
        set(TOOL_IDS_PROPERTY, sb.toString());
        firePropertyChange(TOOLCLICK_LISTENERS_CHANGED_PROPERTY, null, listener);
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
        
        if (toolbar != null) {
            toolbar.setPosition(Toolbar.Position.BOTTOM);
            add(toolbar);
        }
        
        this.bottomToolbar = toolbar;
    }
    
    @Override
    public void processInput(String inputName, Object inputValue) {
        if (INPUT_KEY_PRESSED.equals(inputName)) {
            this.keyPressed = (String) inputValue;
        }
        else if (INPUT_TOOLID_CLICKED.equals(inputName)) {
            this.toolIdClicked = (String) inputValue;
        }
        else if (INPUT_KEYPRESS_ACTION.equals(inputName)) {
            fireKeyEvent();
        }
        else if (INPUT_TOOLCLICK_ACTION.equals(inputName)) {
            fireToolClickEvent();
        } else if ( BEFORE_EXPAND_ACTION.equals( inputName)) {
        	fireBeforeExpandEvent();
        }
        else {
            super.processInput(inputName, inputValue);
        }
    }
        
    private void fireBeforeExpandEvent() {
    	for ( ActionListener listener : beforeExpandListeners) {
    		listener.actionPerformed( null);
    	}
		
	}

	/**
     * Adds a listener to be notified when the passed key is pressed and this
     * component either has the focus, or is the ancestor of the focused
     * component.
     * <p/>
     * TODO - document the keyPress format.
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
        
        set(REGISTERED_KEY_PRESSES_PROPERTY, sb.toString());
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
    
    public void addBeforeExpandListener( ActionListener listener) {
    	beforeExpandListeners = new ArrayList<ActionListener>();
    	beforeExpandListeners.add( listener);
    	firePropertyChange(BEFOREEXPAND_LISTENERS_CHANGED_PROPERTY, null, listener);
    }
    
    /**
     * Returns whether any key press listeners are registered on this component.
     * @return true if there are any listeners registered, false if not.
     */
    public boolean hasKeyPressListeners() {
        return (keyPressListeners != null && keyPressListeners.size() > 0);
    }
    
    /**
     * Returns whether any key tool listeners are registered on this component.
     * @return true if there are any listeners registered, false if not.
     */
    public boolean hasToolListeners() {
        return (toolListeners != null && toolListeners.size() > 0);
    }
    
    /**
     * Sets the buttons for the button bar.  Note that this does NOT remove
     * any existing buttons.
     * @param buttons the buttons to add.
     */
    public void setButtons(Button[] buttons) {
        for (Button button : buttons) {
            addButton(button);
        }
    }

    /**
     * Fires tool click events to registered listeners.
     */
    private void fireToolClickEvent() {
        Tool tool = Tool.valueOf(toolIdClicked.toUpperCase());
        if (!(toolListeners.containsKey(tool))) {
            throw new IllegalStateException("Too click event fired when no listener registered: " + toolIdClicked);
        }
        
        ActionEvent e = new ActionEvent(this, toolIdClicked);
        
        for (ActionListener listener : toolListeners.get(tool)) {
            listener.actionPerformed(e);
        }
    }
    
    public Boolean getRoundedBorders() {
        return (Boolean)get(ROUNDED_BORDERS_PROPERTY);
    }
    
    public void setRoundedBorders(Boolean roundBorders) {
        set(ROUNDED_BORDERS_PROPERTY, roundBorders);
    }
    
    public String getTitlePosition() {
        return (String)get(TITLE_POSITION);
    }
    
    public void setTitlePosition(TitlePosition titlePosition) {
        set(TITLE_POSITION, titlePosition.toString());
    }
    
    public String getBaseCssClass() {
        return (String)get(BASE_CSS_CLASS);
    }
    
    /**
     * @param The base css class to be used for this panel. This 
     * class should be defined in the css file for this application
     */
    public void setBaseCssClass(String baseCssClass) {
        set(BASE_CSS_CLASS,baseCssClass);
    }
    
    /**
     * Returns the css class that will be used to provide a background image
     * used as the header icon.
     * @return
     */
    public String getIconCssClass() {
        return (String)get(ICON_CSS_CLASS);
    }
    
    public void setIconCssClass(String cls) {
        set(ICON_CSS_CLASS, cls);
    }
    
    public boolean getFloating() {
        Boolean isFloating = (Boolean)get(FLOATING_PROPERTY);
        if (isFloating == null)
            return false;
        else
            return isFloating.booleanValue();
    }
    
    public void setFloating(boolean floating) {
        set(FLOATING_PROPERTY, Boolean.valueOf(floating));
    }
    
    public Integer getPositionX() {
        return (Integer)get(POSITION_X);
    }
    
    public void setPositionX(Integer position) {
        set(POSITION_X, position);
    }
    
    public Integer getPositionY() {
        return (Integer)get(POSITION_Y);
    }
    
    public void setPositionY(Integer position) {
        set(POSITION_Y, position);
    }
    
    public Integer getRelativePositionX() {
        return (Integer)get(RELATIVE_POSITION_X);
    }
    
    public void setRelativePositionX(Integer position) {
        set(RELATIVE_POSITION_X, position);
    }
    
    public Integer getRelativePositionY() {
        return (Integer)get(RELATIVE_POSITION_Y);
    }
    
    public void setRelativePositionY(Integer position) {
        set(RELATIVE_POSITION_Y, position);
    }

    public RelativeAnchorPosition getRelativeAnchorPosition() {
        String pos = (String)get(RELATIVE_ANCHOR_POSITION);
        return RelativeAnchorPosition.forName(pos);
    }
    
    public void setRelativeAnchorPosition(RelativeAnchorPosition anchor) {
        set(RELATIVE_ANCHOR_POSITION, anchor.name());
    }
        
    public Menu getContextMenu() {
        return contextMenu;
    }
    
    public void setContextMenu(Menu menu) {
        if (contextMenu != null)
            remove(contextMenu);
        this.contextMenu = menu;
        if (menu != null)
            add(contextMenu);
    }

	public boolean hasBeforeExpandListeners() {
		if ( beforeExpandListeners == null) return false;
		return beforeExpandListeners.size() > 0;
	}
}