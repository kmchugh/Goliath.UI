/* ========================================================
 * BorderLayoutConstants.java
 *
 * Author:      kmchugh
 * Created:     Aug 3, 2010, 6:54:54 PM
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
 * @version     1.0 Aug 3, 2010
 * @author      kmchugh
**/
public class BorderLayoutConstants extends DynamicEnum
{


    private static BorderLayoutConstants g_oPageStart;
    public static BorderLayoutConstants PAGE_START()
    {
        if (g_oPageStart == null)
        {
            try
            {
                g_oPageStart = BorderLayoutConstants.createEnumeration(BorderLayoutConstants.class, "PAGESTART");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oPageStart;
    }

    private static BorderLayoutConstants g_oPageEnd;
    public static BorderLayoutConstants PAGE_END()
    {
        if (g_oPageEnd == null)
        {
            try
            {
                g_oPageEnd = BorderLayoutConstants.createEnumeration(BorderLayoutConstants.class, "PAGEEND");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oPageEnd;
    }

    private static BorderLayoutConstants g_oLineStart;
    public static BorderLayoutConstants LINE_START()
    {
        if (g_oLineStart == null)
        {
            try
            {
                g_oLineStart = BorderLayoutConstants.createEnumeration(BorderLayoutConstants.class, "LINESTART");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oLineStart;
    }


    private static BorderLayoutConstants g_oLineEnd;
    public static BorderLayoutConstants LINE_END()
    {
        if (g_oLineEnd == null)
        {
            try
            {
                g_oLineEnd = BorderLayoutConstants.createEnumeration(BorderLayoutConstants.class, "LINEEND");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oLineEnd;
    }

    private static BorderLayoutConstants g_oCenter;
    public static BorderLayoutConstants CENTER()
    {
        if (g_oCenter == null)
        {
            try
            {
                g_oCenter = BorderLayoutConstants.createEnumeration(BorderLayoutConstants.class, "CENTER");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oCenter;
    }
    
    
    protected BorderLayoutConstants(String tcValue)
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }
}
