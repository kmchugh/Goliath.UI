/* ========================================================
 * ControlImplementationType.java
 *
 * Author:      kmchugh
 * Created:     Aug 18, 2010, 12:48:28 PM
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
 * @version     1.0 Aug 18, 2010
 * @author      kmchugh
**/
public class ControlImplementationType extends Goliath.DynamicEnum
{
    /**
     * Creates a new instance of an EventTypes Object
     *
     * @param tcValue The value for the event type
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public ControlImplementationType(String tcValue)
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }

    private static ControlImplementationType g_oDefault;
    public static ControlImplementationType DEFAULT()
    {
        if (g_oDefault == null)
        {
            try
            {
                g_oDefault = new ControlImplementationType("DEFAULT");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oDefault;
    }

    private static ControlImplementationType g_oAWTSystemTray;
    public static ControlImplementationType AWTSYSTEMTRAY()
    {
        if (g_oAWTSystemTray == null)
        {
            try
            {
                g_oAWTSystemTray = new ControlImplementationType("AWTSYSTEMTRAY");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oAWTSystemTray;
    }
}
