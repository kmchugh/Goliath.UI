/* ========================================================
 * IView.java
 *
 * Author:      kmchugh
 * Created:     Aug 4, 2010, 12:27:22 PM
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



/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 4, 2010
 * @author      kmchugh
**/
public interface IView
        extends IContainer
{
    String getTitle();
    void setTitle(String tcTitle);
}
