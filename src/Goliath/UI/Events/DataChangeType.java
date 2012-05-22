/* ========================================================
 * DocumentChangeType.java
 *
 * Author:      kenmchugh
 * Created:     Mar 4, 2011, 9:54:33 PM
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

package Goliath.UI.Events;

import Goliath.DynamicEnum;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Mar 4, 2011
 * @author      kenmchugh
**/
public class DataChangeType extends DynamicEnum
{
    private static DataChangeType g_oInsertion;
    public static DataChangeType INSERTION()
    {
        if (g_oInsertion == null)
        {
            g_oInsertion = new DataChangeType("INSERTION");
        }
        return g_oInsertion;
    }

    private static DataChangeType g_oDeletion;
    public static DataChangeType DELETION()
    {
        if (g_oDeletion == null)
        {
            g_oDeletion = new DataChangeType("DELETION");
        }
        return g_oDeletion;
    }

    private static DataChangeType g_oModification;
    public static DataChangeType MODIFICATION()
    {
        if (g_oModification == null)
        {
            g_oModification = new DataChangeType("MODIFICATION");
        }
        return g_oModification;
    }


    /**
     * Creates a new instance of DocumentChangeType
     */
    protected DataChangeType(String tcType)
    {
        super(tcType);
    }


}
