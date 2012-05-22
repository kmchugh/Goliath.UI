/* ========================================================
 * WeightedLayoutConstants.java
 *
 * Author:      christinedorothy
 * Created:     Jun 10, 2011, 3:01:37 PM
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

package Goliath.UI.Controls.Layouts;

import Goliath.DynamicEnum;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jun 10, 2011
 * @author      christinedorothy
**/
public class AnchoredLayoutConstants extends DynamicEnum
{
    protected AnchoredLayoutConstants(String tcValue)
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }

    private static AnchoredLayoutConstants g_oNear;
    public static AnchoredLayoutConstants NEAR()
    {
        if (g_oNear == null)
        {
            try
            {
                g_oNear = AnchoredLayoutConstants.createEnumeration(AnchoredLayoutConstants.class, "NEAR");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oNear;
    }

    private static AnchoredLayoutConstants g_oFar;
    public static AnchoredLayoutConstants FAR()
    {
        if (g_oFar == null)
        {
            try
            {
                g_oFar = AnchoredLayoutConstants.createEnumeration(AnchoredLayoutConstants.class, "FAR");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oFar;
    }
}
