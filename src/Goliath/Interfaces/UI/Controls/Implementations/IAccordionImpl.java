/* ========================================================
 * IAccordionImpl.java
 *
 * Author:      kmchugh
 * Created:     Aug 2, 2010, 8:53:03 PM
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

import Goliath.Interfaces.UI.Controls.IContainer;



/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 2, 2010
 * @author      kmchugh
**/
public interface IAccordionImpl extends IContainerImpl
{
    void addPanel(String tcKey, String tcTitle, IContainer toContents, IImplementedControl toControl);

    void removePanel(String tcKey, IImplementedControl toControl);
}
