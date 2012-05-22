/* ========================================================
 * Combobox.java
 *
 * Author:      kenmchugh
 * Created:     Dec 14, 2010, 1:53:51 PM
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
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.Interfaces.UI.Controls.Implementations.IDataListControlImpl;
import Goliath.Interfaces.UI.Controls.Implementations.IValuedControlImpl;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 14, 2010
 * @author      kenmchugh
**/
public class Combobox<T> extends Control
    implements IValueControl<T>, IList<T>
{
    private IList<T> m_oItems;

    /**
     * Helper function to get the control implementation
     * @return
     */
    protected IValuedControlImpl<T> getValuedControlImplementation()
    {
        return (IValuedControlImpl<T>)getImplementation();
    }

    protected IDataListControlImpl<T> getDataListControlImplementation()
    {
        return (IDataListControlImpl<T>)getImplementation();
    }

    public Combobox()
    {
        initialiseComponent();
    }

    protected Combobox(ControlImplementationType toType)
    {
        super(toType);
        initialiseComponent();
    }

    /**
     * Creates a new instance of Label
     * @param tcLabel the text of the label
     */
    public Combobox(IList<T> toItems)
    {
        super();
        setData(toItems);
        initialiseComponent();
    }

    private void initialiseComponent()
    {
    }

    public final void setData(IList<T> toItems)
    {
        m_oItems = toItems;
        updateData();
    }

    public final IList<T> getData()
    {
        return m_oItems == null ? new List<T>(0) : new List<T>(m_oItems);
    }

    private void updateData()
    {
        // Get the selected item
        T loItem = getValue();

        // Clear all the current values
        getDataListControlImplementation().removeAll(getControl());

        // Add in all the current items
        getDataListControlImplementation().addAll(m_oItems, getControl());

        // Reselect the appropriate item if it is still in the list
        if (loItem != null && m_oItems.contains(loItem))
        {
            setValue(loItem);
        }
    }



    @Override
    public T getValue()
    {
        return getValuedControlImplementation().getValue(getControl());
    }

    @Override
    public void setValue(T toValue)
    {
        getValuedControlImplementation().setValue(toValue, getControl());
    }

    public void setRenderProperty(String tcRenderProperty)
    {
        getDataListControlImplementation().setRenderProperty(tcRenderProperty, getControl());
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        return m_oItems.toArray(a);
    }

    @Override
    public Object[] toArray()
    {
        return m_oItems.toArray();
    }

    @Override
    public java.util.List<T> subList(int fromIndex, int toIndex)
    {
        return m_oItems.subList(fromIndex, toIndex);
    }

    @Override
    public int size()
    {
        return m_oItems == null ? 0 : m_oItems.size();
    }

    @Override
    public T set(int index, T element)
    {
        if (m_oItems == null)
        {
            m_oItems = new List<T>(0);
        }
        T loReturn = m_oItems.set(index, element);
        return loReturn;
    }

    @Override
    public boolean retainAll(Collection<?> toItems)
    {
        if (m_oItems == null)
        {
            return false;
        }
        else
        {
            if (m_oItems.retainAll(toItems))
            {
                updateData();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> toItems)
    {
        if (m_oItems == null)
        {
            return false;
        }
        else
        {
            if (m_oItems.removeAll(toItems))
            {
                updateData();
                return true;
            }
        }
        return false;
    }

    @Override
    public T remove(int tnIndex)
    {
        T loReturn = m_oItems != null ? null : m_oItems.remove(tnIndex);
        if (loReturn != null)
        {
            updateData();
        }
        return loReturn;
    }

    @Override
    public boolean remove(java.lang.Object toObject)
    {
        boolean llReturn = m_oItems == null ? false : m_oItems.remove(toObject);
        if (llReturn)
        {
            updateData();
        }
        return llReturn;
    }

    @Override
    public ListIterator<T> listIterator(int tnIndex)
    {
        return m_oItems == null ? new List<T>(0).listIterator(tnIndex) : m_oItems.listIterator(tnIndex);
    }

    @Override
    public ListIterator<T> listIterator()
    {
        return m_oItems == null ? new List<T>(0).listIterator() : m_oItems.listIterator();
    }

    @Override
    public int lastIndexOf(java.lang.Object toObject)
    {
        return m_oItems == null ? -1 : m_oItems.lastIndexOf(toObject);
    }

    @Override
    public Iterator<T> iterator()
    {
        return m_oItems == null ? new List<T>(0).iterator() : m_oItems.iterator();
    }

    @Override
    public boolean isEmpty()
    {
        return m_oItems == null || m_oItems.isEmpty();
    }

    @Override
    public int indexOf(Object toObject)
    {
        return m_oItems == null ? -1 : m_oItems.indexOf(toObject);
    }

    @Override
    public T get(int tnIndex)
    {
        return m_oItems == null ? null : m_oItems.get(tnIndex);
    }

    @Override
    public boolean containsAll(Collection<?> toItems)
    {
        return m_oItems != null && m_oItems.containsAll(toItems);
    }

    @Override
    public boolean contains(java.lang.Object toObject)
    {
        return m_oItems != null && m_oItems.contains(toObject);
    }

    @Override
    public void clear()
    {
        if (m_oItems != null)
        {
            m_oItems.clear();
            updateData();
        }
    }

    @Override
    public boolean addAll(int tnIndex, Collection<? extends T> toItems)
    {
        boolean llReturn = false;
        if (m_oItems == null)
        {
            m_oItems = new List<T>(toItems);
            llReturn = true;
        }
        else
        {
            llReturn = m_oItems.addAll(tnIndex, toItems);
        }
        if (llReturn)
        {
            updateData();
        }
        return llReturn;
    }

    @Override
    public boolean addAll(Collection<? extends T> toItems)
    {
        boolean llReturn = false;
        if (m_oItems == null)
        {
            m_oItems = new List<T>(toItems);
            llReturn = true;
        }
        else
        {
            llReturn = m_oItems.addAll(toItems);
        }
        if (llReturn)
        {
            updateData();
        }
        return llReturn;
    }

    @Override
    public void add(int tnIndex, T toObject)
    {
        if (m_oItems == null)
        {
            m_oItems = new List<T>(1);
        }
        m_oItems.add(toObject);
        updateData();
    }

    @Override
    public boolean add(T toItem)
    {
         boolean llReturn = false;
        if (m_oItems == null)
        {
            m_oItems = new List<T>();
        }
        llReturn = m_oItems.add(toItem);
        if (llReturn)
        {
            updateData();
        }
        return llReturn;
    }






    

    // TODO: Implement label positioning against the image using the Position
}