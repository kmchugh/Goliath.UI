/* ========================================================
 * Table.java
 *
 * Author:      kmchugh
 * Created:     Aug 5, 2010, 7:08:15 PM
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

package Goliath.UI.Controls;

import Goliath.Collections.List;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.Collections.IRefreshable;
import Goliath.Interfaces.UI.Controls.ITable;
import Goliath.Interfaces.UI.Controls.Implementations.ITableImpl;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 5, 2010
 * @author      kmchugh
**/
public class Table<T> extends Control
    implements ITable<T> , IRefreshable
{
    /**
     * Helper function to get the implementation control for the button
     * @return
     */
    protected final ITableImpl<T> getTableImplementation()
    {
        return (ITableImpl<T>)getImplementation();
    }


    private IList<AbstractTableColumnDefinition<T>> m_oColumns;
    private java.util.List<T> m_oData;

    public Table()
    {
        getTableImplementation().setAutoCreateRowSorter(getControl(), true);
    }

    public Table(IList<AbstractTableColumnDefinition<T>> toColumns)
    {
        super();
        setColumnMap(toColumns);
        getTableImplementation().setAutoCreateRowSorter(getControl(), true);
    }

    public Table(IList<AbstractTableColumnDefinition<T>> toColumns, List<T> toData)
    {
        super();
        m_oColumns = toColumns;
        m_oData = toData;
        getTableImplementation().updatedColumnMap(getControl());
        getTableImplementation().updatedData(getControl());
        getTableImplementation().setAutoCreateRowSorter(getControl(), true);
        this.invalidate();
    }

    public boolean canRefresh()
    {
        return (m_oData != null && m_oData instanceof IRefreshable);
    }

    @Override
    public void refresh() throws Throwable
    {
        if (canRefresh())
        {
            IRefreshable refreshable =  (IRefreshable) m_oData;
            refreshable.refresh();
        }
    }

    @Override
    public final void setColumnMap(IList<AbstractTableColumnDefinition<T>> toColumns)
    {
        m_oColumns = toColumns;
        getTableImplementation().updatedColumnMap(getControl());
        this.invalidate();
    }

    @Override
    public void setData(java.util.List<T> toData)
    {
        if (m_oData != toData)
        {
            m_oData = toData;
            getTableImplementation().updatedData(getControl());
            // If we have updated the data, then the selection is no longer valid
            getTableImplementation().clearSelection(getControl());
            this.invalidate();
        }
    }

    @Override
    public void addItem(T toItem)
    {
        if (m_oData == null)
        {
            m_oData = new List<T>();
        }
        if (m_oData.add(toItem))
        {
            getTableImplementation().updatedData(getControl());
            // If we have updated the data, then the selection is no longer valid
            getTableImplementation().clearSelection(getControl());
            this.invalidate();
        }

    }

    @Override
    public void addItems(IList<T> toItems)
    {
        if (m_oData == null)
        {
            m_oData = toItems;
        }
        else
        {
            m_oData.addAll(toItems);
        }
        getTableImplementation().updatedData(getControl());
        // If we have updated the data, then the selection is no longer valid
        getTableImplementation().clearSelection(getControl());
        this.invalidate();
    }

    @Override
    public void removeItems(T toItem)
    {
        if (m_oData != null)
        {
            m_oData.remove(toItem);
        }
        getTableImplementation().updatedData(getControl());
        // If we have updated the data, then the selection is no longer valid
        getTableImplementation().clearSelection(getControl());
        
        this.invalidate();
    }

    @Override
    public void removeItems(IList<T> toItems)
    {
        if (m_oData != null)
        {
            m_oData.removeAll(toItems);
        }
        getTableImplementation().updatedData(getControl());
        
        // If we have updated the data, then the selection is no longer valid
        getTableImplementation().clearSelection(getControl());
        this.invalidate();
    }

    @Override
    public boolean selectItem(T toItem)
    {
        int lnCount = getSelectedItems().size();
        if (toItem == null)
        {
            clearSelection();
        }
        else
        {
            if (m_oData != null && m_oData.size() > 0)
            {
                int lnIndex = m_oData.indexOf(toItem);
                if (lnIndex >= 0)
                {
                    getTableImplementation().selectItems(new int[]{lnIndex}, getControl());
                }
            }
        }
        return getSelectedItems().size() != lnCount;
    }

    @Override
    public boolean selectItems(IList<T> toItems)
    {
        if (m_oData != null && m_oData.size() > 0)
        {
            List<Integer> loItems = new List<Integer>();
            for(T loItem : toItems)
            {
                int lnIndex = m_oData.indexOf(loItem);
                if (lnIndex >= 0)
                {
                    loItems.add(lnIndex);
                }
            }
            if (loItems.size() > 0)
            {
                int[] laItems = new int[loItems.size()];
                for (int i=0; i<loItems.size(); i++)
                {
                    laItems[i] = loItems.get(i);
                }
                getTableImplementation().selectItems(laItems, getControl());
            }
        }
        return false;
    }
    
    /**
     * Clears the current selection
     * @return true if the selection collection was changed as a result of this call
     */
    public void clearSelection()
    {
        getTableImplementation().clearSelection(getControl());
    }



    @Override
    public java.util.List<T> getData()
    {
        if (m_oData == null)
        {
            m_oData = new List<T>(0);
        }
        return m_oData;
    }

    @Override
    public void refreshData()
    {
        getTableImplementation().updatedData(getControl());
        // If we have updated the data, then the selection is no longer valid
        getTableImplementation().clearSelection(getControl());
        this.invalidate();
    }

    @Override
    public IList<T> getSelectedItems()
    {
        int[] laSelected = getTableImplementation().getSelectedItems(getControl());
        List<T> loReturn = new List<T>(laSelected.length);
        for(int lnItem : laSelected)
        {
            loReturn.add(m_oData.get(lnItem));
        }
        return loReturn;
    }

    @Override
    public IList<AbstractTableColumnDefinition<T>> getColumnDefinitions()
    {
        return m_oColumns != null ? m_oColumns : new List<AbstractTableColumnDefinition<T>>();
    }

    @Override
    public boolean isEditable(T toItem)
    {
        // TODO: Also implement permissions if this is a data object or business object (any object in fact)
        return (toItem != null);
    }

    @Override
    public boolean getAutoCreateRowSorter()
    {
        return getTableImplementation().getAutoCreateRowSorter(getControl());
    }

    @Override
    public void setAutoCreateRowSorter(boolean tlFlag)
    {
        getTableImplementation().setAutoCreateRowSorter(getControl(), tlFlag);
    }

    

}
