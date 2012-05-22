/* ========================================================
 * ApplicationPanelType.java
 *
 * Author:      kmchugh
 * Created:     Dec 8, 2010, 3:08:54 PM
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

package Goliath.UI.Windows;

import Goliath.DynamicEnum;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 8, 2010
 * @author      kmchugh
**/
public class ApplicationPanelType extends DynamicEnum
{
    private int m_nOrder;

    public int getOrder()
    {
        return m_nOrder;
    }

    /**
     * Creates a new instance of the application panel type
     * @param tcValue the value to use for the panel name and tooltip
     */
    public ApplicationPanelType(String tcValue, int tnOrder)
    {
        super(tcValue);
        this.m_nOrder = tnOrder;
    }


    private static ApplicationPanelType g_oSettings;
    public static ApplicationPanelType SETTINGS()
    {
        if (g_oSettings == null)
        {
            g_oSettings = createEnumeration(ApplicationPanelType.class, "Settings", new Object[]{1});
        }
        return g_oSettings;
    }

    private static ApplicationPanelType g_oTools;
    public static ApplicationPanelType TOOLS()
    {
        if (g_oTools == null)
        {
            g_oTools = createEnumeration(ApplicationPanelType.class, "Tools", new Object[]{0});
        }
        return g_oTools;
    }
    
    private static ApplicationPanelType g_oNone;
    public static ApplicationPanelType NONE()
    {
        if (g_oTools == null)
        {
            g_oTools = createEnumeration(ApplicationPanelType.class, "None", new Object[]{0});
        }
        return g_oTools;
    }
}
