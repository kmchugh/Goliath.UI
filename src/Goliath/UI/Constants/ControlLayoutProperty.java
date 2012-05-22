/* ========================================================
 * ControlLayoutProperty.java
 *
 * Author:      kmchugh
 * Created:     Feb 17, 2011, 9:18:08 AM
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

package Goliath.UI.Constants;

import Goliath.DynamicEnum;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Feb 17, 2011
 * @author      kmchugh
**/
public class ControlLayoutProperty extends DynamicEnum
{
    private static ControlLayoutProperty g_oLayoutConstraint;
    public static ControlLayoutProperty LAYOUTCONSTRAINT()
    {
        if (g_oLayoutConstraint == null)
        {
            g_oLayoutConstraint = new ControlLayoutProperty("layoutConstraint");
        }
        return g_oLayoutConstraint;
    }
    /**
     * Creates a new instance of ControlLayoutProperty
     */
    protected ControlLayoutProperty(String tcValue)
    {
        super(tcValue);
    }


}
