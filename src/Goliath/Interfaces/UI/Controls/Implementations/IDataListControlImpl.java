/* ========================================================
 * IDataListControlImpl.java
 *
 * Author:      kenmchugh
 * Created:     Dec 14, 2010, 2:19:21 PM
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

import Goliath.Interfaces.Collections.IList;



/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 14, 2010
 * @author      kenmchugh
**/
public interface IDataListControlImpl<T>
        extends IControlImpl
{
    void setData(IList<T> toItems, IImplementedControl toControl);
    IList<T> getData(IImplementedControl toControl);
    boolean removeItem(T toItem, IImplementedControl toControl);
    boolean addItem(T toItem, IImplementedControl toControl);
    boolean addAll(IList<T> toItems, IImplementedControl toControl);
    boolean removeAll(IList<T> toItems, IImplementedControl toControl);
    boolean removeAll(IImplementedControl toControl);


    // TODO : refactor this to allow a render function instead
    void setRenderProperty(String tcPropertyName, IImplementedControl toControl);
}
