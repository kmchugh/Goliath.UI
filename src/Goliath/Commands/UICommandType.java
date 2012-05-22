/* =========================================================
 * UICommandType.java
 *
 * Author:      kenmchugh
 * Created:     Sep 23, 2009, 4:17:40 PM
 *
 * Description
 * --------------------------------------------------------
 * <Description>
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Commands;

/**
 *
 * @author kenmchugh
 */
public class UICommandType extends Goliath.DynamicEnum
{
    public UICommandType(String tcValue)
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }

    private static UICommandType g_oDefault;
    /**
     *  Static singleton for DEFAULT formatting
     * @return The DEFAULT string format type
     */
    public static UICommandType DEFAULT()
    {
        if (g_oDefault == null)
        {
            try
            {
                g_oDefault = new UICommandType("DEFAULT");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oDefault;
    }

}
