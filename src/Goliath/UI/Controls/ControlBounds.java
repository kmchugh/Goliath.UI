/* ========================================================
 * ControlBounds.java
 *
 * Author:      kmchugh
 * Created:     Nov 19, 2010, 2:20:46 PM
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

import Goliath.Constants.StringFormatType;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Graphics.Rectangle;


        
/**
 * ControlBounds represents the rectangle that is occupied by a control.  This
 * includes minimum, maximum, and preferred sizes.
 *
 * @see         Related Class
 * @version     1.0 Nov 19, 2010
 * @author      kmchugh
**/
public class ControlBounds extends Goliath.Object
{
    private Point m_oLocation;
    private Dimension m_oSize;
    private Dimension m_oMinSize;
    private Dimension m_oMaxSize;
    private Dimension m_oActualSize;
    private Dimension m_oPreferredSize;

    public ControlBounds()
    {
    }

    public ControlBounds(float tnX, float tnY, float tnWidth, float tnHeight)
    {
        this(new Point(tnX, tnY), new Dimension(tnWidth, tnHeight));
    }
    
    public ControlBounds(Point toLocation, Dimension toSize)
    {
        m_oLocation = toLocation;
        m_oSize = toSize;
    }
    
    /**
     * Clears any properties related to core calculations
     */
    private void corePropertyUpdated()
    {
        m_oActualSize = null;
    }

    public boolean isSizeSet()
    {
        return m_oSize != null;
    }

    public boolean isLocationSet()
    {
        return m_oLocation != null;
    }

    public boolean isMinSizeSet()
    {
        return m_oMinSize != null;
    }

    public boolean isMaxSizeSet()
    {
        return m_oMaxSize != null;
    }
    
    /**
     * Gets the rectangle that encompasses the control bounds
     * @return the control bounds rectangle
     */
    public Rectangle getRectangle()
    {
        return new Rectangle(m_oLocation, getSize());
    }


    /**
     * Returns the calculated size, taking min and max into account
     * @return
     */
    public Dimension getSize()
    {
        if (m_oActualSize == null || m_oActualSize.equals(Dimension.EMPTYSIZE()))
        {
            calculateSize();
        }
        return m_oActualSize;
    }

    /**
     * Calculates the actual size of the bounds
     */
    private void calculateSize()
    {
        float lnWidth = 0;
        float lnHeight = 0;
        if (m_oSize != null)
        {
            lnWidth = Math.max(Math.min(m_oSize.getWidth(), m_oMaxSize != null ? m_oMaxSize.getWidth() : Float.MAX_VALUE), m_oMinSize != null ? m_oMinSize.getWidth() : 0);
            lnHeight = Math.max(Math.min(m_oSize.getHeight(), m_oMaxSize != null ? m_oMaxSize.getHeight() : Float.MAX_VALUE), m_oMinSize != null ? m_oMinSize.getHeight() : 0);
        }
        else if (m_oSize == null && m_oPreferredSize != null)
        {
            lnWidth = Math.max(Math.min(m_oPreferredSize.getWidth(), m_oMaxSize != null ? m_oMaxSize.getWidth() : Float.MAX_VALUE), m_oMinSize != null ? m_oMinSize.getWidth() : 0);
            lnHeight = Math.max(Math.min(m_oPreferredSize.getHeight(), m_oMaxSize != null ? m_oMaxSize.getHeight() : Float.MAX_VALUE), m_oMinSize != null ? m_oMinSize.getHeight() : 0);
        }
        else
        {
            lnWidth = m_oMinSize != null ? m_oMinSize.getWidth() : 0;
            lnHeight = m_oMinSize != null ? m_oMinSize.getHeight() : 0;
        }
        m_oActualSize = new Dimension(lnWidth, lnHeight);
    }

    /**
     * Gets the size the bounds have been set to, not taking in to account any min or max modifications
     * @return the actual size of the bounds
     */
    public Dimension getActualSize()
    {
        return (m_oSize == null) ? null : new Dimension(m_oSize.getWidth(), m_oSize.getHeight());
    }

    /**
     * Sets the size of the bounds
     * @param toSize the new size
     */
    public boolean setSize(Dimension toSize)
    {
        if ((m_oSize != null && !m_oSize.equals(toSize)) || (m_oSize == null && toSize != null))
        {
            m_oSize = toSize;
            corePropertyUpdated();
            return true;
        }
        return false;
    }

    /**
     * Sets the size of the bounds
     * @param tnWidth the new width
     * @param tnHeight the new height
     */
    public void setSize(float tnWidth, float tnHeight)
    {
        m_oSize = new Dimension(tnWidth, tnHeight);
        corePropertyUpdated();
    }

    /**
     * Sets the minimum size of the bounds for this rectangle
     * @param toMinSize the minimum size
     */
    public void setMinSize(Dimension toMinSize)
    {
        if ((m_oMinSize != null && !m_oMinSize.equals(toMinSize)) || (m_oMinSize == null && toMinSize != null))
        {
            m_oMinSize = toMinSize;
        }
    }

    /**
     * Gets the minimum bounds for this rectangle
     * @return the minimum size
     */
    public Dimension getMinSize()
    {
        return (m_oMinSize == null) ? null : new Dimension(m_oMinSize.getWidth(), m_oMinSize.getHeight());
    }

    /**
     * Sets the maximum size of the bounds for this rectangle
     * @param toMaxSize the maximum size
     */
    public void setMaxSize(Dimension toMaxSize)
    {
        if ((m_oMaxSize != null && !m_oMaxSize.equals(toMaxSize)) || (m_oMaxSize == null && toMaxSize != null))
        {
            m_oMaxSize = toMaxSize;
        }
    }

    /**
     * Gets the maximum bounds for this rectangle
     * @return the maximum size
     */
    public Dimension getMaxSize()
    {
        return (m_oMaxSize == null) ? null : new Dimension(m_oMaxSize.getWidth(), m_oMaxSize.getHeight());
    }

    /**
     * Gets the location of the bounds rectangle
     * @return the location of the rectangle
     */
    public Point getLocation()
    {
        return (m_oLocation == null) ? null : new Point(m_oLocation.getX(), m_oLocation.getY());
    }

    /**
     * Sets the location of the bounds rectangle
     * @param toPoint the new location of the bounds rectangle
     */
    public boolean setLocation(Point toPoint)
    {
        if ((m_oLocation != null && !m_oLocation.equals(toPoint) || (m_oLocation == null && toPoint != null)))
        {
            m_oLocation = toPoint;
            return true;
        }
        return false;
    }

    /**
     * Sets the new location of the bounds rectangle
     * @param tnX the new x location
     * @param tnY the new y location
     */
    public void setLocation(float tnX, float tnY)
    {
        setLocation(new Point(tnX, tnY));
    }


    public Dimension getPreferredSize()
    {
       return (m_oPreferredSize == null) ? getSize() : new Dimension(m_oPreferredSize.getWidth(), m_oPreferredSize.getHeight());
    }

    public boolean setPreferredSize(Dimension toSize)
    {
        if ((m_oPreferredSize != null && !m_oPreferredSize.equals(toSize)) || (m_oPreferredSize == null && !toSize.equals(getSize())))
        {
            m_oPreferredSize = toSize;
            corePropertyUpdated();
            return true;
        }
        return false;
    }

    @Override
    protected String formatString(StringFormatType toFormat)
    {
        Dimension loSize = getPreferredSize();
        return ((m_oLocation != null) ? ("{x: " + m_oLocation.getX() + ", y: " + m_oLocation.getY() + "}") : "") +
                ((loSize != null) ? ("{width: " + loSize.getWidth() + ", height: " + loSize.getHeight()) + "}" : "");
    }






}
