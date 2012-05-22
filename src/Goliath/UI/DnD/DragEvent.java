/* ========================================================
 * DragEvent.java
 *
 * Author:      kenmchugh
 * Created:     Feb 22, 2011, 3:19:02 PM
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

import Goliath.Constants.MimeType;
import Goliath.Event;
import Goliath.Interfaces.UI.Controls.IControl;


        
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
public class DragEvent<S extends IControl, T extends IControl, D> extends Event<T>
{
    private D m_oData;
    private MimeType m_oType;
    private S m_oSource;

    /**
     * Creates a new instance of DragEvent
     */
    public DragEvent(T toTarget, S toSource, D toData, MimeType toType)
    {
        super(toTarget);
        m_oData = toData;
        m_oSource = toSource;
        m_oType = toType;
    }

    public D getData()
    {
        return m_oData;
    }

    public S getSourceControl()
    {
        return m_oSource;
    }

    public MimeType getMimeType()
    {
        return m_oType;
    }
}
