/* =========================================================
 * UICommandArgs.java
 *
 * Author:      kmchugh
 * Created:     07-Jul-2008, 13:01:02
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
 * =======================================================*/

package Goliath.Commands;

import Goliath.Arguments.Arguments;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 07-Jul-2008
 * @author      kmchugh
**/
public class UICommandArgs extends Arguments
{
    private org.w3c.dom.Node m_oNode = null;
    
    /** Creates a new instance of ExecuteCommandCommandArgs
     * @param toNode 
     */
    public UICommandArgs(org.w3c.dom.Node toNode)
    {
        m_oNode = toNode;
    }
    
    public org.w3c.dom.Node getNode()
    {
        return m_oNode;
    }
}
