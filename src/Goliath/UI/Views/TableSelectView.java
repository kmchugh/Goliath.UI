/* ========================================================
 * TableSelectView.java
 *
 * Author:      kmchugh
 * Created:     Dec 10, 2010, 10:39:55 AM
 *
 * Description
 * --------------------------------------------------------
 * General Class Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.UI.Views;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Delegate;
import Goliath.DynamicCode.Java;
import Goliath.Event;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.Collections.IRefreshable;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.ButtonMap;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.Controls.AbstractTableColumnDefinition;
import Goliath.UI.Controls.Layouts.BorderLayoutConstants;
import Goliath.UI.Controls.Table;
import Goliath.UI.Controls.TableColumnDefinition;
import Goliath.UI.Controls.UserControls.DynamicEditors.ObjectEditor;
import Goliath.UI.Constants.UIEventType;
import java.util.logging.Level;
import java.util.logging.Logger;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 10, 2010
 * @author      kmchugh
**/
public class TableSelectView<T> extends HeaderFooterView
{
    private static String CMDEDITKEY = "CMDEDITKEY";
    private static String CMDNEWKEY = "CMDNEWKEY";
    private static String CMDADDKEY = "CMDADDKEY";
    private static String CMDDELETEKEY = "CMDDELETEKEY";
    private static String CMDREFRESHKEY = "CMDREFRESHKEY";
    private static String CMDREMOVEKEY = "CMDREMOVEKEY";
    private static String CMDSELECTKEY = "CMDSELECTKEY";

    private Table<T> m_oTable;
    private Class<T> m_oClass;
    private ObjectEditor<T> m_oEditor;
    private TableSelectView<T> m_oSelectView;
    private int m_nMaxSelection;
    private int m_nMinSelection;
    private IDelegate<IList<T>> m_oGetSelectionList;
    
    private boolean m_lAllowEdit;
    private boolean m_lAllowNew;
    private boolean m_lAllowAdd;
    private boolean m_lAllowDelete;
    private boolean m_lAllowRemove;
    private boolean m_lCanRefresh;

    private boolean m_lIsEditing;
    private boolean m_lIsAdding;
    private boolean m_lIsSelecting;
    
    private HashTable<String, String> m_oPropertyNameMap;

    /**
     * Creates a new instance of the TableSelectView
     * @param toDisplayClass
     */
    public TableSelectView(Class<T> toDisplayClass, HashTable<String, String> toDisplayProperties, boolean tlSelect, boolean tlAllowEdit, boolean tlAllowNew, boolean tlAllowDelete, boolean tlAllowRemove, boolean tlAllowAdd)
    {
        super(false);
        m_lCanRefresh = true;
        m_oClass = toDisplayClass;
        m_oPropertyNameMap = toDisplayProperties;
        setAllowAdd(tlAllowAdd);
        setAllowRemove(tlAllowRemove);
        setAllowEdit(tlAllowEdit);
        setAllowNew(tlAllowNew);
        setAllowDelete(tlAllowDelete);
        createContent();
        initialiseComponent();
    }

    /**
     * Creates a new instance of the TableSelectView
     * @param toDisplayClass
     */
    public TableSelectView(Class<T> toDisplayClass, boolean tlSelect, boolean tlAllowEdit, boolean tlAllowNew, boolean tlAllowDelete, boolean tlAllowRemove, boolean tlAllowAdd)
    {
        this(toDisplayClass, null, tlSelect, tlAllowEdit, tlAllowNew, tlAllowDelete, tlAllowRemove, tlAllowAdd);
    }

    /**
     * Creates a new instance of the TableSelectView
     * @param toDisplayClass
     */
    public TableSelectView(Class<T> toDisplayClass, boolean tlSelect, boolean tlAllowEdit, boolean tlAllowNew, boolean tlAllowDelete, boolean tlAllowRemove)
    {
        this(toDisplayClass, tlSelect, tlAllowEdit, tlAllowNew, tlAllowDelete, false, false);
    }

    /**
     * Creates a new instance of the TableSelectView
     * @param toDisplayClass
     */
    public TableSelectView(Class<T> toDisplayClass, boolean tlSelect, boolean tlAllowEdit, boolean tlAllowNew, boolean tlAllowDelete)
    {
        this(toDisplayClass, tlSelect, tlAllowEdit, tlAllowNew, tlAllowDelete, false, false);
    }

    /**
     * Creates a new instance of the TableSelectView
     * @param toDisplayClass
     */
    public TableSelectView(Class<T> toDisplayClass, boolean tlAllowEdit, boolean tlAllowNew, boolean tlAllowDelete)
    {
        this(toDisplayClass, false, tlAllowEdit, tlAllowNew, tlAllowDelete);
    }

    /**
     * Creates a new instance of the TableSelectView
     * @param toDisplayClass
     */
    public TableSelectView(Class<T> toDisplayClass, boolean tlAllowEdit, boolean tlAllowNew)
    {
        this(toDisplayClass, true, tlAllowEdit, tlAllowNew, false);
    }

    /**
     * Creates a new instance of the TableSelectView
     * @param toDisplayClass
     */
    public TableSelectView(Class<T> toDisplayClass, boolean tlAllowEdits)
    {
        this(toDisplayClass, true, tlAllowEdits, false, false);
    }

    /**
     * Creates a new instance of the TableSelectView
     * @param toDisplayClass
     */
    public TableSelectView(Class<T> toDisplayClass)
    {
        this(toDisplayClass, true, false, false, false);
    }

    private void setAllowEdit(boolean tlAllowEdit)
    {
        if (m_lAllowEdit != tlAllowEdit)
        {
            m_lAllowEdit = tlAllowEdit;
            // TODO: recreate the control as the properties have changed
        }
    }

    private void setAllowRemove(boolean tlAllowRemove)
    {
        if (m_lAllowRemove != tlAllowRemove)
        {
            m_lAllowRemove = tlAllowRemove;
            // TODO: recreate the control as the properties have changed
        }
    }

    private void setAllowAdd(boolean tlAllowAdd)
    {
        if (m_lAllowAdd != tlAllowAdd)
        {
            m_lAllowAdd = tlAllowAdd;
            // TODO: recreate the control as the properties have changed
        }
    }

    private void setAllowNew(boolean tlAllowNew)
    {
        if (m_lAllowNew != tlAllowNew)
        {
            m_lAllowNew = tlAllowNew;
            // TODO: recreate the control as the properties have changed
        }
    }

    private void setAllowDelete(boolean tlAllowDelete)
    {
        if (m_lAllowDelete != tlAllowDelete)
        {
            m_lAllowDelete = tlAllowDelete;
            // TODO: recreate the control as the properties have changed
        }
    }

    @Override
    protected List<ButtonMap> createButtonMap()
    {
        List<ButtonMap> loButtons = super.createButtonMap();
        
        if (m_lAllowNew)
        {
            loButtons.add(0, new ButtonMap(CMDNEWKEY, "New", new List<String>(new String[]{"AddButton"}), Delegate.build(this, "onNewClicked")));
        }
        if (m_lAllowAdd)
        {
            loButtons.add(0, new ButtonMap(CMDADDKEY, "Add", new List<String>(new String[]{"AddButton"}), Delegate.build(this, "onAddClicked")));
        }
        if (m_lAllowEdit)
        {
            loButtons.add(0, new ButtonMap(CMDEDITKEY, "Edit", new List<String>(new String[]{"EditButton"}), Delegate.build(this, "onEditClicked")));
        }
        if (m_lAllowDelete)
        {
            loButtons.add(0, new ButtonMap(CMDDELETEKEY, "Delete", new List<String>(new String[]{"DeleteButton"}), Delegate.build(this, "onDeleteClicked")));
        }
        if (m_lAllowRemove)
        {
            loButtons.add(0, new ButtonMap(CMDREMOVEKEY, "Remove", new List<String>(new String[]{"RemoveButton"}), Delegate.build(this, "onRemoveClicked")));
        }
        if (m_lCanRefresh /*&& m_oTable != null && m_oTable.canRefresh()*/)
        {
            loButtons.add(0, new ButtonMap(CMDREFRESHKEY, "Refresh", new List<String>(new String[]{"RefreshButton"}), Delegate.build(this, "onRefreshClicked")));
        }
        
        
        return loButtons;
    }

    /**
     * Hook to allow subclass to interact with the delete
     * @param toItems the items to delete
     * @return true if the items were deleted successfully
     */
    protected boolean onDelete(IList<T> toItems)
    {
        return true;
    }

    /**
     * Event occurs when the add button is clicked
     * @param toEvent the event
     */
    private void onNewClicked(Event<IControl> toEvent)
    {
        if (m_lAllowNew)
        {
            getEditor().setValue(onCreateNewObject());
            m_oEditor.setEditable(true);
            m_lIsAdding = true;
            updateControls();
        }
    }

    /**
     * Event occurs when the add button is clicked
     * @param toEvent the event
     */
    private void onAddClicked(Event<IControl> toEvent)
    {
        if (m_lAllowAdd)
        {
            m_lIsAdding = true;
            m_lIsSelecting = true;
            updateControls();
        }
    }

    /**
     * Event occurs when the edit button is clicked
     * @param toEvent the event
     */
    private void onRefreshClicked(Event<IControl> toEvent)
    {
        if (m_oTable != null)
        {
            try {
                m_oTable.refresh();
            } catch (Throwable ex) {
                Logger.getLogger(TableSelectView.class.getName()).log(Level.SEVERE, null, ex);
            }
            //m_oTable.setData(loadData());
            updateControls();
        }
    }


    protected void onEditClicked(Event<IButton> toEvent)
    {
        if (m_lAllowEdit)
        {
            T loItem = m_oTable.getSelectedItems().get(0);
            if (loItem != null)
            {
                m_lIsEditing = true;
                // Edit the item with the editor
                getEditor().setValue(loItem);
                updateControls();
            }
        }
    }

    private void showEditor()
    {
        ObjectEditor<T> loEditor = getEditor();
        if (loEditor.getParent() == null)
        {
            getContentContainer().addControl(loEditor, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.PAGE_START());
            invalidate();
        }
    }

    private void hideEditor()
    {
        if (m_oEditor != null)
        {
            ObjectEditor<T> loEditor = getEditor();
            if (loEditor.getParent() != null)
            {
                loEditor.getParent().removeControl(loEditor);
                invalidate();
            }
        }
    }

    private void showSelectionView()
    {
        TableSelectView<T> loView = getSelectList();

        if (loView.getParent() == null)
        {
            getContentContainer().addControl(loView, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.PAGE_START());
            invalidate();
        }
    }

    private void hideSelectionView()
    {
        if (m_oSelectView != null)
        {
            TableSelectView<T> loView = getSelectList();
            if (loView.getParent() != null)
            {
                loView.getParent().removeControl(loView);
                invalidate();
            }
        }
    }

    

    /**
     * Event occurs when the delete button is clicked
     * @param toEvent the event
     */
    private void onDeleteClicked(Event<IControl> toEvent)
    {
        if (m_lAllowDelete)
        {
            // TODO: Prompt for delete
            IList<T> loItems = m_oTable.getSelectedItems();
            if (onDelete(loItems))
            {
                m_oTable.removeItems(loItems);
            }
            updateControls();
        }
    }

    /**
     * Event occurs when the remove button is clicked
     * @param toEvent the event
     */
    private void onRemoveClicked(Event<IControl> toEvent)
    {
        if (m_lAllowRemove)
        {
            // TODO: Prompt for delete
            m_oTable.getData().removeAll(m_oTable.getSelectedItems());
            m_oTable.clearSelection();
            updateControls();
        }
    }

    /**
     * Event occurs when the remove button is clicked
     * @param toEvent the event
     */
    private void onSelectClicked(Event<IControl> toEvent)
    {
        // Do nothing here
    }

    public void setSelectionListDelegate(IDelegate<IList<T>> toDelegate)
    {
        m_oGetSelectionList = toDelegate;
    }

    private void onSelectionMade(Event<IButton> toEvent)
    {
        m_oTable.getData().addAll(m_oSelectView.getSelected());
        m_lIsAdding = false;
        m_lIsSelecting = false;
        this.invalidate();
        updateControls();
    }

    private void onSelectionCancelled(Event<TableSelectView<T>> toEvent)
    {
        hideSelectionView();
        m_lIsAdding = false;
        m_lIsSelecting = false;
        this.invalidate();
        updateControls();
    }

    public final TableSelectView<T> getSelectList()
    {
        if (m_oSelectView == null)
        {
            m_oSelectView = new TableSelectView<T>(m_oClass, true, false, false, false, false, false);
            m_oSelectView.setMaxSize(1000, this.getSize().getHeight()*.4f);

            // Set up the property map
            if (m_oPropertyNameMap != null)
            {
                for(String lcProperty : m_oPropertyNameMap.keySet())
                {
                    m_oSelectView.setPropertyName(lcProperty, m_oPropertyNameMap.get(lcProperty));
                }
            }

            IButton loOkayButton = m_oSelectView.getButtonFor(CMDOKAYKEY);
            IButton loCloseButton = m_oSelectView.getButtonFor(CMDCANCELKEY);
            loOkayButton.clearEventListeners(UIEventType.ONCLICK());
            loCloseButton.clearEventListeners(UIEventType.ONCLICK());

            loOkayButton.addEventListener(UIEventType.ONCLICK(), Delegate.build(this, "onSelectionMade"));
            loCloseButton.addEventListener(UIEventType.ONCLICK(), Delegate.build(this, "onSelectionCancelled"));


            if (m_oGetSelectionList != null)
            {
                try
                {
                    m_oSelectView.setData(m_oGetSelectionList.invoke((Object[])null));
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }

        }
        return m_oSelectView;
    }
    
    /**
     * Gets the detail editor control
     * @return the detail editor
     */
    public final ObjectEditor<T> getEditor()
    {
        if (m_oEditor == null)
        {
            m_oEditor = new ObjectEditor<T>();

            // Set up the property map
            if (m_oPropertyNameMap != null)
            {
                for(String lcProperty : m_oPropertyNameMap.keySet())
                {
                    m_oEditor.setPropertyName(lcProperty, m_oPropertyNameMap.get(lcProperty));
                }
            }

            IList<String> loProperties = getObjectProperties();

            for (String lcProperty : loProperties)
            {
                m_oEditor.addProperty(lcProperty);
            }

            m_oEditor.setColumns((int)Math.ceil(loProperties.size()/5));

            configureObjectEditor(m_oEditor);

            m_oEditor.addEventListener(ObjectEditor.SAVED(), Delegate.build(this, "onObjectSaved"));
            m_oEditor.addEventListener(ObjectEditor.CANCELLED(), Delegate.build(this, "onObjectCancelled"));
            m_oEditor.setClassTemplate(getSupportedClass());

            
        }
        return m_oEditor;
    }

    /**
     * Hook method for allowing subclasses to configure the object editor
     * @param toEditor the object editor to configure
     */
    protected void configureObjectEditor(ObjectEditor toEditor)
    {
    }



    private void initialiseComponent()
    {
        m_nMinSelection = 1;
        m_nMaxSelection = 1;
        getButtonFor(CMDOKAYKEY).setTooltip("Select");
        updateControls();
    }

    /**
     * Sets the maximum number of items that can be selected in this list
     * @param tnMaxSelection the max number of items that can be selected
     */
    public void setMaxSelection(int tnMaxSelection)
    {
        m_nMaxSelection = tnMaxSelection;
    }

    /**
     * Gets the maximum number of items that can be selected from this list
     * @return the max number of items that can be selected
     */
    public int getMaxSelection()
    {
        return m_nMaxSelection;
    }

    /**
     * Sets the minimum number of items that can be selected in this list
     * @param tnMinSelection the min number of items that can be selected
     */
    public void setMinSelection(int tnMinSelection)
    {
        m_nMinSelection = tnMinSelection;
    }

    /**
     * Gets the minimum number of items that can be selected from this list
     * @return the min number of items that can be selected
     */
    public int getMinSelection()
    {
        return m_nMinSelection;
    }

    /**
     * Gets the class supported by this Table view
     * @return the table view class
     */
    public Class<T> getSupportedClass()
    {
        return m_oClass;
    }

    /**
     * Creates the content for this view
     * @param toContainer the content container
     */
    @Override
    protected final void onPopulateContent(IContainer toContainer)
    {
        configureView();
        toContainer.addControl(getTable());
    }

    
    /**
     * Hook method to allow sub classes to configure, occurs before content is added
     */
    protected void configureView()
    {
    }

    /**
     * Gets the table, creates it if it doesn't already exist
     * @return the table for the view
     */
    private Table getTable()
    {
        if (m_oTable == null)
        {
            List<AbstractTableColumnDefinition<T>> loColumns = new List<AbstractTableColumnDefinition<T>>();
            for (String lcString : getObjectProperties())
            {
                loColumns.add(new TableColumnDefinition<T>(lcString, getPropertyHeader(lcString)));
            }

            m_oTable = new Table<T>(loColumns);
            m_oTable.addEventListener(UIEventType.ONSELECTIONCHANGED(), Delegate.build(this, "onDataListChanged"));
            m_oTable.setData(loadData());
        }
        return m_oTable;
    }

    /**
     * Sets the data displayed by the table
     * @param toData the data list
     */
    public final void setData(IList<T> toData)
    {
         if (toData != null && toData instanceof IRefreshable)
        {
            m_lCanRefresh = true;
        }
        else
        {
            m_lCanRefresh = false;
        }

        
        m_oTable.setData(toData);
        updateControls();
        invalidate();

        
    }

    /**
     * Loads or populates the data from the source
     * @return the list of data items
     */
    protected IList<T> loadData()
    {
        return null;    
    }

    /**
     * Gets the list of properties to be displayed by the control
     * @return the list of properties
     */
    protected List<String> getObjectProperties()
    {
        return m_oPropertyNameMap == null ? Java.getPropertyMethods(getSupportedClass()) : new List<String>(m_oPropertyNameMap.keySet());
    }

    /**
     * Gets the displayable name of the property
     * @return the list of properties
     */
    protected String getPropertyHeader(String tcProperty)
    {
        return (m_oPropertyNameMap != null && m_oPropertyNameMap.containsKey(tcProperty.toLowerCase())) ? m_oPropertyNameMap.get(tcProperty.toLowerCase()) : tcProperty;
    }

    /**
     * Sets the name that will be displayed for each of the properties
     * @param tcProperty the property
     * @param tcDisplay the text to display for the property
     */
    public void setPropertyName(String tcProperty, String tcDisplay)
    {
        if (m_oPropertyNameMap == null)
        {
            m_oPropertyNameMap = new HashTable<String, String>();
        }
        m_oPropertyNameMap.put(tcProperty.toLowerCase(), tcDisplay);
    }

    /**
     * Event that occurs when the selected data changes
     * @param toEvent the event
     */
    private void onDataListChanged(Event<IControl> toEvent)
    {
        updateControls();
        fireEvent(UIEventType.ONSELECTIONCHANGED());
    }

    /**
     * Gets the list of items that have been selected
     * @return the list of selected items
     */
    public IList<T> getSelected()
    {
        return m_oTable != null ? m_oTable.getSelectedItems() : new List<T>(0);
    }

    public boolean selectItem(T toItem)
    {
        return (m_oTable != null && m_oTable.selectItem(toItem));
    }

    /**
     * Creates a new object when the add button is clicked
     * @return the new object
     */
    protected T onCreateNewObject()
    {
        try
        {
            return getSupportedClass().newInstance();
        }
        catch(Throwable ex)
        {
            Application.getInstance().log(ex);
            return null;
        }
    }

    /**
     * Updates the footer controls
     */
    protected void updateControls()
    {
        if (m_lIsAdding || m_lIsEditing)
        {
            if (m_lIsSelecting)
            {
                showSelectionView();
            }
            else
            {
                showEditor();
            }
        }
        else
        {
            hideEditor();
            hideSelectionView();
        }
        for(String tcKey : getButtonKeys())
        {
            int lnSelected = m_oTable.getSelectedItems().size();
            if (tcKey.equalsIgnoreCase(CMDCANCELKEY))
            {
                getButtonFor(CMDCANCELKEY).setEnabled(true && !m_lIsEditing && !m_lIsAdding);
            }
            else if (tcKey.equalsIgnoreCase(CMDREFRESHKEY))
            {
                getButtonFor(CMDREFRESHKEY).setEnabled(m_lCanRefresh && !m_lIsEditing && !m_lIsAdding);
            }
            else if (tcKey.equalsIgnoreCase(CMDOKAYKEY))
            {
                getButtonFor(CMDOKAYKEY).setEnabled(lnSelected >= m_nMinSelection && lnSelected <= m_nMaxSelection && !m_lIsEditing && !m_lIsAdding);
            }
            else if (tcKey.equalsIgnoreCase(CMDDELETEKEY))
            {
                getButtonFor(CMDDELETEKEY).setEnabled(lnSelected >= 1 && !m_lIsEditing && !m_lIsAdding);
            }
            else if (tcKey.equalsIgnoreCase(CMDEDITKEY))
            {
                getButtonFor(CMDEDITKEY).setEnabled(lnSelected == 1 && !m_lIsEditing && !m_lIsAdding);
            }
            else if (tcKey.equalsIgnoreCase(CMDNEWKEY))
            {
                getButtonFor(CMDNEWKEY).setEnabled(true && !m_lIsEditing && !m_lIsAdding);
            }
            else if (tcKey.equalsIgnoreCase(CMDADDKEY))
            {
                getButtonFor(CMDADDKEY).setEnabled(true && !m_lIsEditing && !m_lIsAdding);
            }
            else if (tcKey.equalsIgnoreCase(CMDREMOVEKEY))
            {
                getButtonFor(CMDREMOVEKEY).setEnabled(true && !m_lIsEditing && !m_lIsAdding && lnSelected > 0);
            }
            else if (tcKey.equalsIgnoreCase(CMDSELECTKEY))
            {
                getButtonFor(CMDSELECTKEY).setEnabled(true && !m_lIsEditing);
            }
        }
    }

    /**
     * Event handler when the ObjectEditor saves it's data
     * @param toEvent the event
     */
    private void onObjectSaved(Event<IControl> toEvent)
    {
        T loItem = (T)m_oEditor.getValue();
        if (loItem != null)
        {
            if (onSave(loItem))
            {
                if (m_lIsAdding)
                {
                    m_oTable.addItem(loItem);
                }
                m_oEditor.setValue(null);
                m_lIsEditing = false;
                m_lIsAdding = false;
                m_lIsSelecting = false;
                m_oTable.refreshData();
                m_oTable.selectItem(loItem);
            }
        }
        updateControls();
    }

    /**
     * Hook to allow subclass to interact with the save
     * @param toItem the item to save
     * @return true if the item was saved successfully
     */
    protected boolean onSave(T toItem)
    {
        return true;
    }

    /**
     * Event handler when the ObjectEditor cancels it's save
     * @param toEvent the event
     */
    private void onObjectCancelled(Event<IControl> toEvent)
    {
        m_oEditor.setValue(null);
        m_lIsEditing = false;
        m_lIsAdding = false;
        m_lIsSelecting = false;
        updateControls();
    }
}