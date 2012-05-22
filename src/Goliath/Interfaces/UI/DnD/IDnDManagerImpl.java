/* ========================================================
 * IDnDManagerImpl.java
 *
 * Author:      kenmchugh
 * Created:     Feb 22, 2011, 1:23:39 PM
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

package Goliath.Interfaces.UI.DnD;

import Goliath.Collections.List;
import Goliath.Constants.MimeType;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Controls.ControlImplementationType;



/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Feb 22, 2011
 * @author      kenmchugh
**/
public interface IDnDManagerImpl
{
    /**
     * Checks if this implementation supports the implementation type specified
     * @param toType the type to check
     * @return true if this does support the type specified
     */
    boolean supports(ControlImplementationType toType);

    /**
     * Allows a user to register a drop target
     * @param toControl the control to register as a drop target
     * @param toAccepts the MimeTypes this control will accept
     * @return true if this control is added to the list of drop targets
     */
    boolean registerTarget(IControl toControl, List<MimeType> toAccepts);

    /**
     * Removes the control as a drop target
     * @param toControl the control to remove
     * @return the control to remove as a drop target
     */
    boolean unRegisterTarget(IControl toControl);

}
