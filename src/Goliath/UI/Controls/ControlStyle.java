/* ========================================================
 * ControlStyle.java
 *
 * Author:      admin
 * Created:     Aug 28, 2011, 2:30:22 PM
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

import Goliath.Collections.PropertySet;
import Goliath.Graphics.BoxDimension;
import Goliath.Graphics.Constants.Position;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import java.awt.Color;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 28, 2011
 * @author      admin
 **/
public class ControlStyle extends Goliath.Object
{
    private static String CONTROLBOUNDS = "CONTROLBOUNDS";
    private static String COLOUR = "COLOUR";
    private static String BACKGROUNDCOLOUR = "BACKGROUNDCOLOUR";
    private static String BACKGROUNDIMAGE = "BACKGROUNDIMAGE";
    private static String BACKGROUNDIMAGEALIGNMENT = "BACKGROUNDIMAGEALIGNMENT";
    private static String BORDER = "BORDER";
    private static String MARGIN = "MARGIN";
    private static String PADDING = "PADDING";
    private static String OPACITY = "OPACITY";
    private static String FONTSIZE = "FONTSIZE";
    private static String BACKGROUNDLOCATION = "BACKGROUNDLOCATION";
    private static String BACKGROUNDSIZE = "BACKGROUNDSIZE";
    private PropertySet m_oPropertySet;
    private boolean m_lVisible;
    
    
    

    /**
     * Creates a new instance of ControlStyle
     */
    public ControlStyle()
    {
    }
    
    
    /**
     * Gets the specified property, null if not set
     * @param <K>
     * @param tcProperty the property to get
     * @return the value of the specified property
     */
    private <K> K getProperty(String tcProperty)
    {
        return (K)(m_oPropertySet == null ? null : m_oPropertySet.getProperty(tcProperty));
    }
    
    /**
     * Sets the specified property
     * @param <K>
     * @param tcProperty the property to set
     * @param toValue the value to set the property to
     * @return true if the property has changed due to this call, false if no change
     */
    private <K> boolean setProperty(String tcProperty, K toValue)
    {
        if (m_oPropertySet == null)
        {
            m_oPropertySet = new PropertySet();
        }
        // TODO : Should only return true if the property values change
        return m_oPropertySet.setProperty(tcProperty, toValue);
    }
    
    /**
     * Sets the background size for this style
     * @param toDimension the new size
     * @return true if the value changed as a result of this call
     */
    public boolean setBackgroundSize(Dimension toDimension)
    {
        return setProperty(BACKGROUNDSIZE, toDimension);
    }
    
    /**
     * Gets the size that should be applied to the background for this style
     * @return the size of the background
     */
    public Dimension getBackgroundSize()
    {
        return getProperty(BACKGROUNDSIZE);
    }
    
    /**
     * sets the location of the background for this control
     * @param toLocation the background location
     * @return true if the value has changed as a result of this call
     */
    public boolean setBackgroundLocation(Point toLocation)
    {
        return setProperty(BACKGROUNDLOCATION, toLocation);
    }
    
    /**
     * Gets the background location for this style
     * @return the location of the background
     */
    public Point getBackgroundLocation()
    {
        return getProperty(BACKGROUNDLOCATION);
    }
    
    /**
     * Sets the font size for this style
     * @param tnSize the size of the font for this style
     * @return true if the value has changed as a result of this call
     */
    public boolean setFontSize(float tnSize)
    {
        return setProperty(FONTSIZE, tnSize);
    }
    
    /**
     * Gets the font size for this style
     * @return the styles font size
     */
    public float getFontSize()
    {
        // TODO: This should inherit if not set
        Float loValue = getProperty(FONTSIZE);
        if (loValue == null)
        {
            loValue = 12f;
        }
        return loValue;
    }
    
    /**
     * Gets the opacity of this style, if it is not set, this will return 1 to say that the control
     * is fully opaque
     * @return the opacity of the style
     */
    public float getOpacity()
    {
        Float loValue = getProperty(OPACITY);
        return loValue == null ? 1f : loValue;
    }
    
    /**
     * Sets the opacity of this style, if the range is between 0 and 1, if the 
     * value is outside the range, then the range limits will be used
     * @param tnOpacity the opacity
     * @return true if this call changed the opacity value
     */
    public boolean setOpacity(float tnOpacity)
    {
        if (tnOpacity < 0)
        {
            tnOpacity = 0;
        }
        else if (tnOpacity > 1)
        {
            tnOpacity = 1;
        }
        
        return setProperty(OPACITY, tnOpacity);
    }
    
    /**
     * Gets the border settings for this style
     * @return the border settings, or null if none have been set
     */
    public BorderSettings getBorder()
    {
        return getProperty(BORDER);
    }
    
    /**
     * Sets the border settings for this style, if the border has previously been
     * set, this will overwrite the current border settings
     * @param toBorder the new border settings
     */
    public boolean setBorder(BorderSettings toBorder)
    {
        return setProperty(BORDER, toBorder);
    }
    
    /**
     * Gets the margin for this style, the margin is the transparent
     * area around the control
     * @return the margin
     */
    public BoxDimension getMargin()
    {
        return getProperty(MARGIN);
    }
    
    /**
     * Sets the dimension for this style
     * @param toMargin the margin
     * @return true if the margin changed as a result of this call
     */
    public boolean setMargin(BoxDimension toMargin)
    {
        return setProperty(MARGIN, toMargin);
    }
    
    /**
     * Gets the padding for this style, the padding is the transparent
     * area around the control
     * @return the padding
     */
    public BoxDimension getPadding()
    {
        return getProperty(PADDING);
    }
    
    /**
     * Sets the dimension for this style
     * @param toPadding the padding
     * @return true if the padding changed as a result of this call
     */
    public boolean setPadding(BoxDimension toPadding)
    {
        return setProperty(PADDING, toPadding);
    }
    
    /**
     * Checks if the size has been explicitly set for this control
     * @return true if the size has been set
     */
    public boolean isSizeSet()
    {
        ControlBounds loBounds = getProperty(CONTROLBOUNDS);
        return loBounds != null && loBounds.isSizeSet();
    }
    
    /**
     * Gets the control bounds for this style
     * @param tlCreate if true, then will create the control bounds if they do not already exist
     * @return the control bounds
     */
    public ControlBounds getControlBounds(boolean tlCreate)
    {
        ControlBounds loBounds = getProperty(CONTROLBOUNDS);
        if (loBounds == null && tlCreate)
        {
            loBounds = new ControlBounds();
            setProperty(CONTROLBOUNDS, loBounds);
        }
        return loBounds;
    }
    
    /**
     * Sets the control bounds for this control, if the control bounds already
     * exist this will over write the bounds that exist
     * @param toBounds the bounds to set
     * @return true if the bounds change as a result of this call
     */
    public boolean setControlBounds(ControlBounds toBounds)
    {
        return setProperty(CONTROLBOUNDS, toBounds);
    }
    
    /**
     * Checks if this style is visible or not
     * @return true for visible, false otherwise
     */
    public boolean getVisible()
    {
        return m_lVisible;
    }
    
    /**
     * Sets if the style if visible or not
     * @param tlVisible the value to set the visible flag to
     * @return true if the value has changed as a result of this call
     */
    public boolean setVisible(boolean tlVisible)
    {
        if (m_lVisible != tlVisible)
        {
            m_lVisible = tlVisible;
            return true;
        }
        return false;
    }
    
    /**
     * Gets the colour for this style
     * @return the colour set for this style
     */
    public Color getColour()
    {
        return getProperty(COLOUR);
    }
    
    /**
     * Sets the colour for this style
     * @param toColour the colour to set
     * @return true if this call changes the value
     */
    public boolean setColour(Color toColour)
    {
        return setProperty(COLOUR, toColour);
    }
    
    /**
     * Gets the background colour for this style
     * @return the background colour set for this style
     */
    public Color getBackgroundColour()
    {
        return getProperty(BACKGROUNDCOLOUR);
    }
    
    /**
     * Sets the background colour for this style
     * @param toColour the background colour to set
     * @return true if this call changes the value
     */
    public boolean setBackgroundColour(Color toColour)
    {
        return setProperty(BACKGROUNDCOLOUR, toColour);
    }
    
    /**
     * Gets the background image for this style
     * @return the background image set for this style
     */
    public Goliath.Graphics.Image getBackgroundImage()
    {
        return getProperty(BACKGROUNDIMAGE);
    }
    
    /**
     * Sets the background colour for this style
     * @param toColour the background colour to set
     * @return true if this call changes the value
     */
    public boolean setBackgroundImage(Goliath.Graphics.Image toImage)
    {
        return setProperty(BACKGROUNDIMAGE, toImage);
    }
    
    /**
     * Gets the background image alignment for this style
     * @return the background image alignment set for this style
     */
    public Position getBackgroundImageAlignment()
    {
        return getProperty(BACKGROUNDIMAGEALIGNMENT);
    }
    
    /**
     * Sets the background alignment for this style
     * @param toColour the background alignment to set
     * @return true if this call changes the value
     */
    public boolean setBackgroundImageAlignment(Position toImage)
    {
        return setProperty(BACKGROUNDIMAGEALIGNMENT, toImage);
    }
}
