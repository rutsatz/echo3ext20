package org.sgodden.echo.ext20;

import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.event.EventListenerList;

import org.sgodden.echo.ext20.buttons.AddButton;
import org.sgodden.echo.ext20.buttons.RemoveButton;
import org.sgodden.echo.ext20.fieldgroup.FieldGroupEvent;
import org.sgodden.echo.ext20.fieldgroup.FieldGroupFactory;
import org.sgodden.echo.ext20.fieldgroup.FieldGroupListener;
import org.sgodden.echo.ext20.layout.ColumnLayout;
import org.sgodden.echo.ext20.layout.ColumnLayoutData;

/**
 * This component allows the entry of values in one or more field groups. It
 * initially displays one field group with a button to add another field group.
 * When more than one field group is active, all groups have a remove button and
 * the last group has an add button.
 * 
 * The field groups are supplied by the FieldGroupFactory that is applied to the
 * FieldGroupContainer, and the factory will be called each time the add button
 * is pressed.
 * 
 * Whenever the add button or remove buttons are pressed, the registered
 * FieldGroupListener instances will be notified that the action is about to
 * take place, optionally cancelling the action.
 * 
 * @author Lloyd Colling
 * 
 */
public class FieldGroupContainer extends Panel {

    FieldGroupFactory factory = null;
    Map < Integer, Panel > containedGroups = new LinkedHashMap < Integer, Panel >();
    EventListenerList listenerList = new EventListenerList();
    int initialFieldGroups = 1;
    AddButton addButton;
    boolean initted = false;
    String removeButtonText = null;
    private int groupIndex = 0;

    public FieldGroupContainer() {
        super(new ColumnLayout());
    }
    
    public Panel getContainedGroup(int index) {
        return containedGroups.get(index);
    }
    
    public int getContainedGroupCount() {
        return containedGroups.size();
    }

    private int getNextGroupIndex() {
    	return groupIndex++;
    }

    @Override
    public void init() {
        super.init();
        if (factory == null)
            throw new IllegalStateException(
                    "Field group factory must be set before initialistion");
        if (initted)
            return;
        initted = true;
        Panel groupedPanel = new Panel();
        groupedPanel.setLayoutData(new ColumnLayoutData(1.0));
        Panel contentPanel = new Panel();
        contentPanel.setRoundedBorders(Boolean.TRUE);
        contentPanel.setPadding("5px");

        contentPanel.add(factory.getFieldGroup(0));
        groupedPanel.add(contentPanel);

        add(groupedPanel);
        containedGroups.put(getNextGroupIndex(), groupedPanel);

        if (addButton == null)
            addButton = new AddButton();
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int index = getNextGroupIndex();
                if (fireWillAdd(index)) {
                    doAddFieldGroup(index);
                }
            }
        });
        addButton(addButton);
        
        for (int i = 1; i < initialFieldGroups; i++)
            doAddFieldGroup(getNextGroupIndex());
    }

    protected void doAddFieldGroup(final int index) {
        // if we're going from one group to more then add remove button to first
        // group
        if (containedGroups.size() == 1) {
            final Entry< Integer, Panel > firstGroupEntry = containedGroups.entrySet().iterator().next();
            RemoveButton removeButton = new RemoveButton();
            if (removeButtonText != null)
                removeButton.setText(removeButtonText);
            removeButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    if (fireWillRemove(firstGroupEntry.getKey())) {
                        doRemoveGroup(firstGroupEntry.getKey());
                    }
                }
            });
            firstGroupEntry.getValue().addButton(removeButton);
        }

        Panel groupedPanel = new Panel();
        groupedPanel.setLayoutData(new ColumnLayoutData(1.0));
        Panel contentPanel = new Panel();
        contentPanel.setRoundedBorders(Boolean.TRUE);
        contentPanel.setPadding("5px");
        contentPanel.add(factory.getFieldGroup(index));
        groupedPanel.add(contentPanel);

        RemoveButton removeButton = new RemoveButton();
        if (removeButtonText != null)
            removeButton.setText(removeButtonText);
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (fireWillRemove(index)) {
                    doRemoveGroup(index);
                }
            }
        });
        groupedPanel.addButton(removeButton);

        add(groupedPanel);
        containedGroups.put(index, groupedPanel);
    }

    protected void doRemoveGroup(int index) {
        // if we're going down to one group, remove the remove button from the
        // remaining group
        if (containedGroups.size() == 2) {
        	LinkedList< Panel > lastTwoGroups = new LinkedList< Panel >(containedGroups.values());
        	Container lastGroup;
        	if (containedGroups.get(index).equals(lastTwoGroups.getFirst())) {
        		lastGroup = lastTwoGroups.getLast();
        	} else {
        		lastGroup = lastTwoGroups.getFirst();
        	}
            for (Component c : lastGroup.getComponents()) {
                if (c instanceof RemoveButton)
                    lastGroup.remove(c);
            }
        }
        
        Container p = containedGroups.remove(index);
        remove(p);
    }

    protected boolean fireWillAdd(int index) {
        FieldGroupEvent e = new FieldGroupEvent(this, index);
        for (EventListener listener : listenerList
                .getListeners(FieldGroupListener.class)) {
            ((FieldGroupListener) listener).willAddFieldGroup(e);
        }
        return !e.isActionCancelled();
    }

    protected boolean fireWillRemove(int index) {
        FieldGroupEvent e = new FieldGroupEvent(this, index);
        for (EventListener listener : listenerList
                .getListeners(FieldGroupListener.class)) {
            ((FieldGroupListener) listener).willRemoveFieldGroup(e);
        }
        return !e.isActionCancelled();
    }

    public void setFieldGroupFactory(FieldGroupFactory factory) {
        this.factory = factory;
    }
    
    public void addFieldGroupListener(FieldGroupListener listener) {
        listenerList.addListener(FieldGroupListener.class, listener);
    }
    
    public void removeFieldGroupListener(FieldGroupListener listener) {
        listenerList.removeListener(FieldGroupListener.class, listener);
    }
    
    /**
     * Sets the number of field groups to display initially
     * @param number
     */
    public void setNumberInitialFieldGroups(int number) {
        if (number < 1)
            throw new IllegalArgumentException("Number of initial field groups must be at least one");
        
        this.initialFieldGroups = number;
    }
    
    public void setAddButtonText(String text) {
        if (addButton == null)
            addButton = new AddButton();
        addButton.setText(text);
    }
    
    public void setRemoveButtonText(String text) {
        this.removeButtonText = text;
    }
}
