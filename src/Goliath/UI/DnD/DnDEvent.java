/* ========================================================
 * DnDEvent.java
 *
 * Author:      kenmchugh
 * Created:     Feb 22, 2011, 2:51:33 PM
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

package Goliath.UI.DnD;

import Goliath.UI.Constants.UIEventType;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Feb 22, 2011
 * @author      kenmchugh
**/
public class DnDEvent extends UIEventType
{
    private static DnDEvent g_oDragEnter;
    public static DnDEvent DRAGENTER()
    {
        if (g_oDragEnter == null)
        {
            g_oDragEnter = new DnDEvent("DRAGENTER");
        }
        return g_oDragEnter;
    }

    private static DnDEvent g_oDragExit;
    public static DnDEvent DRAGEXIT()
    {
        if (g_oDragExit == null)
        {
            g_oDragExit = new DnDEvent("DRAGEXIT");
        }
        return g_oDragExit;
    }

    private static DnDEvent g_oDragOver;
    public static DnDEvent DRAGOVER()
    {
        if (g_oDragOver == null)
        {
            g_oDragOver = new DnDEvent("DRAGOVER");
        }
        return g_oDragOver;
    }

    private static DnDEvent g_oDrop;
    public static DnDEvent DROP()
    {
        if (g_oDrop == null)
        {
            g_oDrop = new DnDEvent("DROP");
        }
        return g_oDrop;
    }

    private static DnDEvent g_oDropActionChanged;
    public static DnDEvent DROPACTIONCHANGED()
    {
        if (g_oDropActionChanged == null)
        {
            g_oDropActionChanged = new DnDEvent("DROPACTIONCHANGED");
        }
        return g_oDropActionChanged;
    }

    /**
     * Creates a new instance of DnDEvent
     */
    protected DnDEvent(String tcType)
    {
        super(tcType);
    }


}
