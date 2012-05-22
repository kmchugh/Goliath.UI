/* ========================================================
 * CallbackMap.java
 *
 * Author:      kenmchugh
 * Created:     Mar 18, 2011, 10:52:25 AM
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

package Goliath.UI;

import Goliath.Constants.EventType;
import Goliath.Interfaces.IDelegate;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Mar 18, 2011
 * @author      kenmchugh
**/
public class CallbackMap extends Goliath.Object
{
    private EventType m_oEvent;
    private IDelegate m_oCallback;

    /**
     * Creates a new instance of CallbackMap
     */
    public CallbackMap(EventType toEvent, IDelegate toCallback)
    {
        m_oEvent = toEvent;
        m_oCallback = toCallback;
    }

    public EventType getEvent()
    {
        return m_oEvent;
    }

    public IDelegate getCallback()
    {
        return m_oCallback;
    }
}
