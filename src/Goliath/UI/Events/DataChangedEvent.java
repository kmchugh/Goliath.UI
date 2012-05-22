/* ========================================================
 * DocumentChangedEvent.java
 *
 * Author:      kenmchugh
 * Created:     Mar 4, 2011, 9:53:18 PM
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

import Goliath.Event;
import Goliath.UI.Controls.TextArea;


        
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
public class DataChangedEvent<T> extends Event<TextArea>
{
    private DataChangeType m_oType;
    private long m_nStart;
    private long m_nEnd;
    private T m_oData;

    /**
     * Creates a new instance of DocumentChangedEvent
     */
    public DataChangedEvent(TextArea toTarget, DataChangeType toType, long tnStart, long tnEnd, T toData)
    {
        super(toTarget);
        m_oType = toType;
        m_nStart = tnStart;
        m_nEnd = tnEnd;
        m_oData = toData;
    }

    public DataChangeType getType()
    {
        return m_oType;
    }

    public long getStartIndex()
    {
        return m_nStart;
    }

    public long getEndIndex()
    {
        return m_nEnd;
    }

    public long getChangeLength()
    {
        return m_nEnd - m_nStart;
    }

    public T getData()
    {
        return m_oData;
    }

}
