/* ========================================================
 * WindowChromeLayoutManager.java
 *
 * Author:      kenmchugh
 * Created:     Jan 12, 2011, 3:47:00 PM
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

import Goliath.Graphics.Dimension;
import Goliath.Graphics.Constants.Position;
import Goliath.Graphics.Point;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Controls.BorderSettings;
import Goliath.UI.Controls.Label;
import Goliath.UI.Views.ChromeView;
import Goliath.UI.Views.View;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jan 12, 2011
 * @author      kenmchugh
**/
public class WindowChromeLayoutManager extends LayoutManager<ChromeView>
{
    /**
     * Creates a new instance of WindowChromeLayoutManager
     */
    public WindowChromeLayoutManager()
    {
    }
    
    @Override
    protected Dimension onCalculateDimension(ChromeView toContainer, Dimension toContainerSize)
    {
        float lnWidth = 0;
        float lnHeight = 0;


        // Find the size of the contents, a chrome view should only contain a single view
        IControl loControl = toContainer.getView();
        if (loControl != null)
        {
            return getContainedControlDimensions(loControl);
        }

        return new Dimension(lnWidth, lnHeight);
    }
    
    @Override
    protected void layoutChildren(ChromeView toContainer)
    {
        // The chrome view should reflect the size of the window
        toContainer.setSize(toContainer.getParentWindow().getContentSize());
        
        Dimension loSize = toContainer.getContentSize();
        
        int lnAxisOffset = 0;
        
        Label loLabel = toContainer.getTitleLabel();
        loLabel.setVisible(toContainer.getParentWindow().getShowChrome());
        
        for (IControl loControl : toContainer.getControlBoxControls())
        {
            if (loControl.isVisible())
            {
                // Any other controls are layed out as if this were a basic layout manager, but offset to the title position
                setContainedControlLocation(toContainer, loControl, getAdjustedLocation(toContainer, loControl, lnAxisOffset, toContainer.getSize()));
                lnAxisOffset += getMajorAxisSize(toContainer, loControl);
            }
        }
        
        if (getLayoutTitleControls(toContainer))
        {
            for (IControl loControl : toContainer.getTitleControls())
            {
                if (loControl.isVisible())
                {
                    // Any other controls are layed out as if this were a basic layout manager, but offset to the title position
                    setContainedControlLocation(toContainer, loControl, getAdjustedLocation(toContainer, loControl, lnAxisOffset, toContainer.getSize()));
                    lnAxisOffset += getMajorAxisSize(toContainer, loControl);
                }
            }
        }
        
        BorderSettings loBorder = toContainer.getBorder();
        Position loTitlePosition = getTitlePosition(toContainer);
        
        if (loLabel.isVisible())
        {

            if (loTitlePosition != Position.BOTTOM_CENTER() && loTitlePosition != Position.TOP_CENTER())
            {
                loLabel.setTextRotation(loTitlePosition == Position.MIDDLE_LEFT() ? -90 : 90);
            }

            // Lastly we place the title label, it takes up the rest of the title space
            Dimension loLabelSize = getLabelSize(toContainer, loLabel, lnAxisOffset, loSize);
            loLabel.setSize(loLabelSize);
            setContainedControlLocation(toContainer, loLabel, getAdjustedLocation(toContainer, loLabel, lnAxisOffset, loSize));
        }
        
        // If there is no border, but we are displaying a title and controlbox, then adjust the border
        if (loBorder == null || 
                (
                    (loTitlePosition == Position.TOP_CENTER() && loLabel.isVisible() && loBorder.getTop() < loLabel.getSize().getHeight())) ||
                    (loTitlePosition == Position.BOTTOM_CENTER() && loLabel.isVisible() && loBorder.getBottom() < loLabel.getSize().getHeight()) ||
                    (loTitlePosition == Position.MIDDLE_LEFT() && loLabel.isVisible() && loBorder.getLeft() < loLabel.getSize().getWidth()) ||
                    (loTitlePosition == Position.MIDDLE_RIGHT() && loLabel.isVisible() && loBorder.getRight() < loLabel.getSize().getWidth())
                )
        {
            loBorder.setBorderSize(
                    loTitlePosition == Position.TOP_CENTER() && loBorder.getTop() < loLabel.getSize().getHeight() ? 
                        loLabel.getSize().getHeight() : 
                        loBorder.getTop(),
                    loTitlePosition == Position.MIDDLE_RIGHT() && loBorder.getRight() < loLabel.getSize().getWidth() ? 
                        loLabel.getSize().getWidth() : 
                        loBorder.getRight(),
                    loTitlePosition == Position.BOTTOM_CENTER() && loBorder.getBottom() < loLabel.getSize().getHeight() ? 
                        loLabel.getSize().getHeight() : 
                        loBorder.getBottom(),
                    loTitlePosition == Position.MIDDLE_LEFT() && loBorder.getLeft() < loLabel.getSize().getWidth() ? 
                        loLabel.getSize().getWidth() : 
                        loBorder.getLeft()
                    );
        }
        
        
        Dimension loClientSize = toContainer.getContentSize();
        View loView = toContainer.getView();
        setContainedControlSize(toContainer, loView, loClientSize);
        setContainedControlLocation(toContainer, loView, 0, 0);
        
    }
    
    private Dimension getLabelSize(ChromeView toContainer, Label toLabel, float tnOffset, Dimension toClientSize)
    {
        Position loTitlePosition = getTitlePosition(toContainer);
        return new Dimension(
                loTitlePosition == Position.BOTTOM_CENTER() || loTitlePosition == Position.TOP_CENTER() ? toClientSize.getWidth() - tnOffset : toContainer.getMaximiseButton().getSize().getHeight(),
                loTitlePosition == Position.BOTTOM_CENTER() || loTitlePosition == Position.TOP_CENTER() ? toContainer.getMaximiseButton().getSize().getWidth() : toClientSize.getHeight() - tnOffset);
    }
    
    private float getMajorAxisSize(ChromeView toContainer, IControl toControl)
    {
        Position loTitlePosition = getTitlePosition(toContainer);
        return loTitlePosition == Position.BOTTOM_CENTER() || loTitlePosition == Position.TOP_CENTER() ?
                toControl.getSize().getWidth() :
                toControl.getSize().getHeight();
    }
    
    private Point getAdjustedLocation(ChromeView toContainer, IControl toControl, float tnAxisOffset, Dimension toSize)
    {
        Position loTitlePosition = getTitlePosition(toContainer);
        BorderSettings loBorder = toContainer.getBorder();
        
        return new Point(
                loTitlePosition == Position.BOTTOM_CENTER() || loTitlePosition == Position.TOP_CENTER() ? 
                    tnAxisOffset - (loTitlePosition == Position.BOTTOM_CENTER() ? loBorder.getLeft() : loBorder.getLeft()) : 
                    loTitlePosition == Position.MIDDLE_LEFT() ? -loBorder.getLeft() : toSize.getWidth() - toControl.getSize().getWidth() + loBorder.getRight(),
                
                loTitlePosition == Position.BOTTOM_CENTER() || loTitlePosition == Position.TOP_CENTER() ? 
                    loTitlePosition == Position.TOP_CENTER() ? -loBorder.getTop() : toSize.getHeight() - toControl.getSize().getHeight() + loBorder.getBottom() : 
                    tnAxisOffset - loBorder.getTop());
    }
    
    public final void setTitlePosition(ChromeView toView, Position toPosition)
    {
        if (toPosition == Position.TOP_LEFT() ||
                toPosition == Position.TOP_RIGHT() ||
                toPosition == Position.TOP_CENTER() ||
                toPosition == Position.MIDDLE_CENTER())
        {
            toPosition = Position.TOP_CENTER();
        }
        else if(toPosition == Position.BOTTOM_LEFT()||
                toPosition == Position.BOTTOM_RIGHT() ||
                toPosition == Position.BOTTOM_CENTER())
        {
            toPosition = Position.BOTTOM_CENTER();
        }
        else if (toPosition == Position.MIDDLE_LEFT() ||
                    toPosition == Position.MIDDLE_RIGHT())
        {
            // These are correct, so stay the same
        }
        else
        {
            toPosition = Position.TOP_CENTER();
        }
        toView.setProperty(getPropertyName(), toPosition);
    }
    
    public Position getTitlePosition(ChromeView toView)
    {
        return Goliath.Utilities.isNull(toView.<Position>getProperty(getPropertyName()), Position.TOP_CENTER());
    }
    
    public boolean getLayoutTitleControls(ChromeView toView)
    {
        return Goliath.Utilities.isNull(toView.<Boolean>getProperty(getPropertyName()), true);

    }
    
    public void setLayoutTitleControls(ChromeView toView, boolean tlLayout)
    {
        toView.setProperty(getPropertyName(), tlLayout);
    }
    
    protected String getPropertyName()
    {
        return getClass().getSimpleName()+"_"+Goliath.DynamicCode.Java.getCallingMethodName(true, true);
    }

}
