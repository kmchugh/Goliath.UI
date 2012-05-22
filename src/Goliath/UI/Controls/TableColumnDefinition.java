/* ========================================================
 * TableColumnDefinition.java
 *
 * Author:      kmchugh
 * Created:     Aug 5, 2010, 8:04:01 PM
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
public class TableColumnDefinition<T> extends AbstractTableColumnDefinition<T>
{
    /**
     * Creates a new instance of TableColumnDefinition
     */
    public TableColumnDefinition(String tcProperty, String tcTitle)
    {
        super(tcProperty, tcTitle);
    }

    @Override
    public boolean usesValueFunction()
    {
        return false;
    }

    @Override
    public Object getValueFor(T toObject, int tnRow, int tnColumn)
    {
        return null;
    }
}
