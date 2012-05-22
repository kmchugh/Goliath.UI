/* ========================================================
 * ClientType.java
 *
 * Author:      kmchugh
 * Created:     Aug 25, 2010, 3:14:23 PM
 *
 * Description
 * --------------------------------------------------------
 * Base functionallity for the client types
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.UI;

import Goliath.ClientType;
import Goliath.Graphics.Dimension;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 25, 2010
 * @author      kmchugh
**/
public abstract class UIClientType extends ClientType
{
    private Dimension m_oScreenSize;
    private Dimension m_oClientSize;
    private Dimension m_oSize;
    private int m_nColourDepth;

    /**
     * Creates a new instance of ClientType
     */
    public UIClientType()
    {
    }

    public void setScreenSize(Dimension toDim)
    {
        m_oScreenSize = toDim;
    }

    public Dimension getScreenSize()
    {
        return m_oScreenSize;
    }

    public void setClientSize(Dimension toDim)
    {
        m_oClientSize = toDim;
    }

    public Dimension getClientSize()
    {
        return m_oClientSize;
    }

    public void setSize(Dimension toDim)
    {
        m_oSize = toDim;
    }

    public Dimension getSize()
    {
        return m_oSize;
    }

    public void setColourDepth(int tnDepth)
    {
        m_nColourDepth = tnDepth;
    }

    public int getColourDepth()
    {
        return m_nColourDepth;
    }
}
