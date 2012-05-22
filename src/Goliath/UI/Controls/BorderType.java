/* ========================================================
 * BorderType.java
 *
 * Author:      kmchugh
 * Created:     Dec 1, 2010, 12:18:49 PM
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

import Goliath.DynamicEnum;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 1, 2010
 * @author      kmchugh
**/
public class BorderType extends DynamicEnum
{
    public BorderType(String tcValue)
    {
        super(tcValue);
    }

    private static BorderType g_oEMPTY;

    public static BorderType EMPTY()
    {
        if (g_oEMPTY == null)
        {
            g_oEMPTY = new BorderType("EMPTY");
        }
        return g_oEMPTY;
    }

    private static BorderType g_oRounded;

    public static BorderType ROUNDED()
    {
        if (g_oRounded == null)
        {
            g_oRounded = new BorderType("ROUNDED");
        }
        return g_oRounded;
    }
    
    private static BorderType g_oImplementation;

    public static BorderType IMPLEMENTATION_PROVIDED()
    {
        if (g_oImplementation == null)
        {
            g_oImplementation = new BorderType("IMPLEMENTATION_PROVIDED");
        }
        return g_oImplementation;
    }


}
