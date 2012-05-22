/* ========================================================
 * FlowLayoutManager.java
 *
 * Author:      kmchugh
 * Created:     Feb 17, 2011, 12:42:24 PM
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

package Goliath.UI.Controls.Layouts;

import Goliath.Collections.List;
import Goliath.Graphics.Constants.Alignment;
import Goliath.Graphics.Constants.Orientation;
import Goliath.Graphics.Dimension;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;


        
/**
 * The FlowLayoutManager attempts to fit as many controls as it can in line, if the control does not fit
 * then a "line break" is put in to move the control to the next line.  The line is vertical or horizontal
 * based on the preference of the manager
 *
 * @see         Related Class
 * @version     1.0 Feb 17, 2011
 * @author      kmchugh
**/
public class FlowLayoutManager extends LayoutManager<IContainer>
{
    private Orientation m_oOrientation;
    private Alignment m_oAlignment;

    /**
     * Creates a new instance of AccordionLayout
     */
    public FlowLayoutManager()
    {
        this(Orientation.HORIZONTAL(), Alignment.LEFT());
    }

    /**
     * Creates a new instance of this layout manager
     * @param toOrientation the orientation of the layout
     */
    public FlowLayoutManager(Orientation toOrientation)
    {
        this(toOrientation, Alignment.LEFT());
    }

    /**
     * Creates a new instance of this layout manager
     * @param toOrientation the orientation of the layout
     */
    public FlowLayoutManager(Orientation toOrientation, Alignment toAlignment)
    {
        setOrientation(toOrientation);
        setAlignment(toAlignment);
    }

    /**
     * Helper function to get the major axis
     * @param toOrientation the orintation of the axis
     * @param toDimension the dimension to get the value from
     * @return the major axis value
     */
    private float getMajorAxis(Orientation toOrientation, Dimension toDimension)
    {
        return toOrientation.equals(Orientation.HORIZONTAL()) ? toDimension.getWidth() : toDimension.getHeight();
    }

    /**
     * Helper function to get the minor axis
     * @param toOrientation the orintation of the axis
     * @param toDimension the dimension to get the value from
     * @return the minor axis value
     */
    private float getMinorAxis(Orientation toOrientation, Dimension toDimension)
    {
        return toOrientation.equals(Orientation.HORIZONTAL()) ? toDimension.getHeight() : toDimension.getWidth();
    }

    /**
     * Calculates the size this control should be based on it's contents
     * @return the size the control should be
     */
    @Override
    protected Dimension onCalculateDimension(IContainer toContainer, Dimension toContainerSize)
    {
        Dimension loContainerSize = toContainer.getContentSize();
        float lnMajorAxis = getMajorAxis(m_oOrientation, loContainerSize);
        float lnCumulatedMajor = 0;
        float lnCumulatedMinor = 0;
        float lnMaxMinor = 0;
        float lnMaxMajor = 0;
        int lnControlCount = 0;

        // if the Major Axis is zero, then we can change it here, if it is not zero then it can not be changed
        if (lnMajorAxis <= 0)
        {
            lnMajorAxis = Float.MAX_VALUE;
        }

        // Get the list of contained controls
        List<IControl> loControls = toContainer.getChildren();

        // Loop through each control and calculate where the control should be positioned, controls should not be positioned outside the major axis unless they are the only control
        for (IControl loControl : loControls)
        {
            // Don't include non visible controls by default
            if (!loControl.participatesInLayout())
            {
                // TODO: At the time I comment this, all control does not participates in layout
//                continue;
            }

            // Get the size of this control
            Dimension loSize = getContainedControlDimensions(loControl);

            float lnControlMajor = getMajorAxis(m_oOrientation, loSize);
            float lnControlMinor = getMinorAxis(m_oOrientation, loSize);

            lnMaxMinor = Math.max(lnMaxMinor, lnControlMinor);

            // Check if the control can be placed here or if it needs to be moved
            if (lnCumulatedMajor + lnControlMajor > lnMajorAxis)
            {
                // The control is too big, if it is not the only control move it to the next position.
                if (lnControlCount == 0)
                {
                    // This is the only control on this axis, so it has to be placed here
                    lnCumulatedMajor += lnControlMajor;
                    lnMajorAxis = lnCumulatedMajor;
                    lnMaxMajor = Math.max(lnMaxMajor, lnCumulatedMajor);
                    lnControlCount++;
                }
                else
                {
                    // This needs to be moved to the next position
                    lnCumulatedMajor = lnControlMajor;
                    lnCumulatedMinor += lnMaxMinor;
                    lnMaxMinor = lnControlMinor;
                    lnControlCount = 0;
                }
            }
            else
            {
                // this control fits
                lnCumulatedMajor+= lnControlMajor;
                lnMaxMajor = Math.max(lnMaxMajor, lnCumulatedMajor);
                lnControlCount++;
            }
        }
        // Increment the minor if needed
        lnCumulatedMinor += lnMaxMinor;

        float lnWidth = m_oOrientation.equals(Orientation.HORIZONTAL()) ? lnMaxMajor : lnCumulatedMinor;
        float lnHeight = m_oOrientation.equals(Orientation.HORIZONTAL()) ? lnCumulatedMinor : lnMaxMajor;

        return new Dimension(lnWidth, lnHeight);
    }


    @Override
    protected void layoutChildren(IContainer toContainer)
    {
        Dimension loContainerSize = toContainer.getContentSize();
        float lnMajorAxis = getMajorAxis(m_oOrientation, loContainerSize);
        float lnCumulatedMajor = 0;
        float lnCumulatedMinor = 0;
        float lnMaxMinor = 0;
        float lnMaxMajor = 0;
        int lnControlCount = 0;
        float lnMaxWidth = loContainerSize.getWidth();

        // if the Major Axis is zero, then we can change it here, if it is not zero then it can not be changed
        if (lnMajorAxis <= 0)
        {
            lnMajorAxis = Float.MAX_VALUE;
        }

        // Get the list of contained controls
        List<IControl> loControls = toContainer.getChildren();

        // Loop through all the controls, if alignment is left, then front to back, otherwise back to front
        for (int i = (m_oAlignment == Alignment.LEFT() ? 0 : loControls.size()-1),
                lnLength = (m_oAlignment == Alignment.LEFT() ? loControls.size() : 0);
                (m_oAlignment == Alignment.LEFT() ? i < lnLength : i>=lnLength);
                i=i+(m_oAlignment == Alignment.LEFT() ? 1 : -1))
        {
            IControl loControl = loControls.get(i);

            // If the control does not participate in the layout, don't include it here
            if (!loControl.participatesInLayout())
            {
                // TODO: At the time I comment this, all control does not participates in layout
//                continue;
            }

            // Get the size of this control
            Dimension loSize = getContainedControlDimensions(loControl);
            setContainedControlSize(toContainer, loControl, loSize);

            float lnControlMajor = getMajorAxis(m_oOrientation, loSize);
            float lnControlMinor = getMinorAxis(m_oOrientation, loSize);

            lnMaxMinor = Math.max(lnMaxMinor, lnControlMinor);

            // Check if the control can be placed here or if it needs to be moved
            if (lnCumulatedMajor + lnControlMajor > lnMajorAxis)
            {
                // The control is too big, if it is not the only control move it to the next position.
                if (lnControlCount == 0)
                {
                    // This is the only control on this axis, so it has to be placed here
                    setContainedControlLocation(toContainer, loControl,
                            getAlignedLeft(m_oOrientation.equals(Orientation.HORIZONTAL()) ? lnCumulatedMajor : lnCumulatedMinor, lnMaxWidth, loSize.getWidth()),
                            m_oOrientation.equals(Orientation.HORIZONTAL()) ? lnCumulatedMinor : lnCumulatedMajor);
                    lnCumulatedMajor += lnControlMajor;
                    lnMajorAxis = lnCumulatedMajor;
                    lnMaxMajor = Math.max(lnMaxMajor, lnCumulatedMajor);
                    lnControlCount++;
                }
                else
                {
                    // This needs to be moved to the next position
                    lnCumulatedMinor += lnMaxMinor;
                    lnCumulatedMajor = 0;

                    setContainedControlLocation(toContainer, loControl,
                            getAlignedLeft(m_oOrientation.equals(Orientation.HORIZONTAL()) ? lnCumulatedMajor : lnCumulatedMinor, lnMaxWidth, loSize.getWidth()),
                            m_oOrientation.equals(Orientation.HORIZONTAL()) ? lnCumulatedMinor : lnCumulatedMajor);

                    lnCumulatedMajor += lnControlMajor;
                    lnMaxMinor = lnControlMinor;
                    lnControlCount = 0;
                }
            }
            else
            {
                // this control fits
                setContainedControlLocation(toContainer, loControl,
                            getAlignedLeft(m_oOrientation.equals(Orientation.HORIZONTAL()) ? lnCumulatedMajor : lnCumulatedMinor, lnMaxWidth, loSize.getWidth()),
                            m_oOrientation.equals(Orientation.HORIZONTAL()) ? lnCumulatedMinor : lnCumulatedMajor);
                
                lnCumulatedMajor+= lnControlMajor;
                lnMaxMajor = Math.max(lnMaxMajor, lnCumulatedMajor);
                lnControlCount++;
            }
        }
        lnCumulatedMinor += lnMaxMinor;
    }

    private float getAlignedLeft(float tnValue, float tnContainerWidth, float tnControlWidth)
    {
        return (getAlignment() == Alignment.LEFT()) ? tnValue : tnContainerWidth - tnControlWidth - tnValue;
    }

    /**
     * Gets the orientation preference of this control
     * @return the direction the control will favour when layout out its content
     */
    public Orientation getOrientation()
    {
        return m_oOrientation;
    }

    /**
     * Sets the orientation the manager will favour when layout out content
     * @param toOrientation the new orientation
     */
    public final void setOrientation(Orientation toOrientation)
    {
        if(m_oOrientation != toOrientation)
        {
            m_oOrientation = toOrientation;
        }
    }

    /**
     * Gets the Alignment preference of this control
     * @return the direction the control will favour when layout out its content
     */
    public Alignment getAlignment()
    {
        return m_oAlignment;
    }

    /**
     * Sets the Alignment the manager will favour when layout out content
     * @param toAlignment the new Alignment
     */
    public final void setAlignment(Alignment toAlignment)
    {
        if(m_oAlignment != toAlignment)
        {
            m_oAlignment = toAlignment;
        }
    }
}
