/* ========================================================
 * BorderSettings.java
 *
 * Author:      admin
 * Created:     Aug 29, 2011, 9:06:14 AM
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

import Goliath.Graphics.BoxDimension;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 29, 2011
 * @author      admin
 **/
public class BorderSettings extends Goliath.Object
{
    private BoxDimension m_oDimension;
    private BorderType m_oBorderType;

    /**
     * Creates a new instance of BorderSettings
     */
    public BorderSettings(BorderType toBorder)
    {
        m_oBorderType = toBorder;
    }
    
    /**
     * Gets the border type that this control is using, this could be null
     * @return the border type being used by this control
     */
    public final BorderType getBorderType()
    {
        return m_oBorderType;
    }
    
    /**
     * Sets the size of the borders for this control
     * @param tnTop the top border
     * @param tnRight the right border
     * @param tnBottom the bottom border
     * @param tnLeft the left border
     */
    public void setBorderSize(float tnTop, float tnRight, float tnBottom, float tnLeft)
    {
        m_oDimension = new BoxDimension(tnTop, tnRight, tnBottom, tnLeft);
    }

    /**
     * Gets the size of the border area at the top
     * @return the border size at the top of the control
     */
    public float getTop()
    {
        return m_oDimension == null ? 0 : m_oDimension.getTop();
    }

    /**
     * Gets the size of the border area at the right
     * @return the border size at the right of the control
     */
    public float getRight()
    {
        return m_oDimension == null ? 0 : m_oDimension.getRight();
    }

    /**
     * Gets the size of the border area at the bottom
     * @return the border size at the bottom of the control
     */
    public float getBottom()
    {
        return m_oDimension == null ? 0 : m_oDimension.getBottom();
    }

    /**
     * Gets the size of the border area at the left
     * @return the border size at the left of the control
     */
    public float getLeft()
    {
        return m_oDimension == null ? 0 : m_oDimension.getLeft();
    }
}
