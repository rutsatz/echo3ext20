package org.sgodden.echo.ext20.grid;

import javax.swing.table.TableModel;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import org.sgodden.echo.ext20.Button;
import org.sgodden.echo.ext20.Panel;
import org.sgodden.echo.ext20.TextField;
import org.sgodden.echo.ext20.Toolbar;
import org.sgodden.echo.ext20.ToolbarTextItem;

/**
 * A toolbar which contains paging controls for a grid.
 * <p>
 * This control contains various pieces of text, which default to
 * english.  These can be overriden using the appropriate methods.
 * </p>
 * @author sgodden
 */
public class PagingToolbar extends Toolbar {

    private Button firstButton;
    private Button previousButton;
    private Button nextButton;
    private Button lastButton;
    
    private TextField currentPageTextField;
    private ToolbarTextItem totalPagesTextItem;
    private TextField rowsPerPageTextField;

    private ToolbarTextItem firstDisplayItemIndex;
    private ToolbarTextItem lastDisplayItemIndex;
    private ToolbarTextItem totalItems;

    private int pageOffset;
    private int maxPageOffset;
    private int pageSize;

    private String pageText = "Page";
    private String ofText = "of";
    private String rowsPerPageText = "Rows per page";
    private String displayingItemsText = "Displaying items";
    private String toText = "to";

    private PagingToolbarClient client;
    private TableModel model;

    public PagingToolbar() { }

    public PagingToolbar(
            TableModel model,
            int pageSize,
            PagingToolbarClient client) {

        initialise(model, pageSize, client);
    }

    public void initialise(
            TableModel model,
            int pageSize,
            PagingToolbarClient client) {

        this.pageOffset = 0;
        this.pageSize = pageSize;
        this.client = client;
        this.model = model;

        createComponents();
        setTableModel(model);
        
    }

    private void createComponents() {

        // Add the paging controls

        firstButton = new Button();
        add(firstButton);
        firstButton.setIconClass("x-tbar-page-first");

        previousButton = new Button();
        add(previousButton);
        previousButton.setIconClass("x-tbar-page-prev");

        addSeparator();

        addTextItem(pageText + ":");

        Panel p = new Panel();
        add(p);

        p.addKeyPressListener("enter", new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setPage();
            }
        });
        currentPageTextField = new TextField(3);
        p.add(currentPageTextField);

        addTextItem(ofText);
        totalPagesTextItem = new ToolbarTextItem();
        add(totalPagesTextItem);

        addSeparator();

        nextButton = new Button();
        add(nextButton);
        nextButton.setIconClass("x-tbar-page-next");

        lastButton = new Button();
        add(lastButton);
        lastButton.setIconClass("x-tbar-page-last");

        // add the rows per page control

        addSeparator();

        addTextItem(rowsPerPageText + ":");

        p = new Panel();
        add(p);
        rowsPerPageTextField = new TextField(
                String.valueOf(client.getPageSize()));
        p.add(rowsPerPageTextField);
        rowsPerPageTextField.setSize(3);
        p.addKeyPressListener("enter", new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setPageSize();
            }
        });

        // add the "Displaying items..." information
        addSeparator();

        addTextItem(displayingItemsText);
        firstDisplayItemIndex = new ToolbarTextItem();
        add(firstDisplayItemIndex);
        addTextItem(toText);
        lastDisplayItemIndex = new ToolbarTextItem();
        add(lastDisplayItemIndex);
        addTextItem(ofText);
        totalItems = new ToolbarTextItem();
        add(totalItems);

        /*
         * Since we start on the first page, the previous button will
         * be disabled to start with.
         */
        firstButton.setEnabled(false);
        previousButton.setEnabled(false);

        firstButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setPageOffset(0);
                currentPageTextField.setValue("1");
                enableButtons();
            }
        });

        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setPageOffset(pageOffset - pageSize);
                currentPageTextField.setValue(
                        String.valueOf((pageOffset / pageSize) + 1));
                enableButtons();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setPageOffset(pageOffset + pageSize);
                currentPageTextField.setValue(
                        String.valueOf((pageOffset / pageSize) + 1));
                enableButtons();
            }
        });

        lastButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setPageOffset(
                        ( (model.getRowCount() -1) / pageSize)
                        * pageSize
                    );
                currentPageTextField.setValue(
                        String.valueOf((pageOffset / pageSize) + 1));
                enableButtons();
            }
        });
    }

    private void setPage() {
        int requestedPage = Integer.parseInt(currentPageTextField.getValue());
        int requestedPageOffset = (requestedPage - 1) * pageSize;
        if (requestedPageOffset > maxPageOffset) {
            requestedPageOffset = maxPageOffset;
            currentPageTextField.setValue(
                    String.valueOf( (maxPageOffset / pageSize) + 1) ) ;
        }
        setPageOffset(requestedPageOffset);
        enableButtons();
    }

    private void setPageSize() {
        pageSize = Integer.parseInt(rowsPerPageTextField.getValue());
        client.setPageSize(pageSize);
        // reset back to the start
        setTableModel(model);
    }

    private void enableButtons() {
        // disable them all
        firstButton.setEnabled(false);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        lastButton.setEnabled(false);
        
        if (pageOffset > 0) {
            firstButton.setEnabled(true);
            previousButton.setEnabled(true);
        }
        
        if (pageOffset < maxPageOffset) {
            nextButton.setEnabled(true);
            lastButton.setEnabled(true);
        }
    }

    private void setPageOffset(int pageOffset) {
        this.pageOffset = pageOffset;
        client.setPageOffset(pageOffset);
        firstDisplayItemIndex.setText(String.valueOf(pageOffset +1));
        int last = pageOffset + pageSize < model.getRowCount()
                ? pageOffset + pageSize : model.getRowCount();
        lastDisplayItemIndex.setText(String.valueOf(last));
    }

    public void setTableModel(TableModel model) {
        this.model = model;
        setPageOffset(0);
        maxPageOffset = ( (model.getRowCount() -1) / pageSize)
                        * pageSize;

        firstButton.setEnabled(false);
        previousButton.setEnabled(false);
        nextButton.setEnabled(true);
        lastButton.setEnabled(true);

        totalItems.setText(String.valueOf(model.getRowCount()));

        currentPageTextField.setValue("1");

        int totalPages = (model.getRowCount() / pageSize);
        if (model.getRowCount() % pageSize > 0) {
            totalPages++;
        }

        totalPagesTextItem.setText(String.valueOf(totalPages));
        enableButtons();
    }

    /**
     * Sets the text to be used in place of the english "Displaying items".
     * @param displayingItemsString the text to use.
     */
    public void setDisplayingItemsText(String displayingItemsText) {
        this.displayingItemsText = displayingItemsText;
    }

    /**
     * Sets the text to be used in place of the english "to".
     * @param ofString the text to use.
     */
    public void setOfText(String ofText) {
        this.ofText = ofText;
    }

    /**
     * Sets the text to be used in place of the english "Page".
     * @param pageString the text to use.
     */
    public void setPageText(String pageText) {
        this.pageText = pageText;
    }

    /**
     * Sets the text to be used in place of the english "Rows per page".
     * @param rowsPerPageString the text to use.
     */
    public void setRowsPerPageText(String rowsPerPageText) {
        this.rowsPerPageText = rowsPerPageText;
    }

    /**
     * Sets the text to be used in place of the english "to".
     * @param toString the text to use.
     */
    public void setToText(String toText) {
        this.toText = toText;
    }

}