/* ========================================================
 * IGrid.java
 *
 * Author:      kmchugh
 * Created:     Aug 5, 2010, 7:09:56 PM
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

package Goliath.Interfaces.UI.Controls;

import Goliath.Interfaces.Collections.IList;
import Goliath.UI.Controls.AbstractTableColumnDefinition;



/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 5, 2010
 * @author      kmchugh
**/
public interface ITable<T>
        extends IControl
{
    // TODO: AbstractTableColumnDefinition should be changed to use an interface so the implementation is cleaner
    void setColumnMap(IList<AbstractTableColumnDefinition<T>> toColumns);
    void setData(java.util.List<T> toData);
    java.util.List<T> getData();

    void addItem(T toItem);

    IList<T> getSelectedItems();

    IList<AbstractTableColumnDefinition<T>> getColumnDefinitions();

    boolean isEditable(T toItem);

    boolean selectItem(T toItem);
    
    boolean selectItems(IList<T> toItem);

    void addItems(IList<T> toItems);

    void removeItems(T toItem);

    void removeItems(IList<T> toItems);

    void refreshData();

    void setAutoCreateRowSorter(boolean tlFlag);

    boolean getAutoCreateRowSorter();

}
