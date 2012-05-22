/* ========================================================
 * ITableImpl.java
 *
 * Author:      kmchugh
 * Created:     Aug 5, 2010, 7:14:21 PM
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

package Goliath.Interfaces.UI.Controls.Implementations;





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
public interface ITableImpl<T>
        extends IControlImpl
{
    void updatedColumnMap(IImplementedControl toControl);
    void updatedData(IImplementedControl toControl);

    int[] getSelectedItems(IImplementedControl toControl);

    void selectItems(int[] taRows,  IImplementedControl toControl);
    void clearSelection(IImplementedControl toControl);

    void setAutoCreateRowSorter(IImplementedControl toControl, boolean tlFlag);
    boolean getAutoCreateRowSorter(IImplementedControl toControl);

}
