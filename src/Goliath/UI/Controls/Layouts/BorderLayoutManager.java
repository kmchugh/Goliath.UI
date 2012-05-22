/* ========================================================
 * BorderLayoutManager.java
 *
 * Author:      kmchugh
 * Created:     Aug 3, 2010, 6:29:23 PM
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

import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Graphics.Dimension;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Constants.ControlLayoutProperty;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 3, 2010
 * @author      kmchugh
**/
public class BorderLayoutManager extends LayoutManager<IContainer>
{
    

    /**
     * Creates a new instance of BorderLayoutManager
     */
    public BorderLayoutManager()
    {
        
    }

    @Override
    protected Dimension onCalculateDimension(IContainer toContainer, Dimension toContainerSize)
    {
        Dimension loPageStart = null;
        Dimension loPageEnd = null;
        Dimension loLineStart = null;
        Dimension loLineEnd = null;
        Dimension loCenter = null;

        // Get all the contained controls
        List<IControl> loControls = toContainer.getChildren();
        for (IControl loControl : loControls)
        {
            BorderLayoutConstants loConstraint = loControl.getProperty(ControlLayoutProperty.LAYOUTCONSTRAINT().getValue());
            if (loConstraint == null || loConstraint.equals(BorderLayoutConstants.CENTER()))
            {
                loCenter = getContainedControlDimensions(loControl);
            }
            else if (loConstraint.equals(BorderLayoutConstants.PAGE_START()))
            {
                loPageStart = getContainedControlDimensions(loControl);
            }
            else if (loConstraint.equals(BorderLayoutConstants.PAGE_END()))
            {
                loPageEnd = getContainedControlDimensions(loControl);
            }
            else if (loConstraint.equals(BorderLayoutConstants.LINE_START()))
            {
                loLineStart = getContainedControlDimensions(loControl);
            }
            else if (loConstraint.equals(BorderLayoutConstants.LINE_END()))
            {
                loLineEnd = getContainedControlDimensions(loControl);
            }
        }
        
        float lnHeight = ((loPageStart != null) ? loPageStart.getHeight() : 0) +
                Math.max((loCenter != null) ? loCenter.getHeight() : 0, 
                    Math.max((loLineEnd != null) ? loLineEnd.getHeight() : 0, (loLineStart != null) ? loLineStart.getHeight() : 0)) +
                ((loPageEnd != null) ? loPageEnd.getHeight() : 0);
        
        float lnWidth = Math.max(Math.max(((loLineStart != null) ? loLineStart.getWidth() : 0) +
                ((loCenter != null) ? loCenter.getWidth() : 0) +
                ((loLineEnd != null) ? loLineEnd.getWidth() : 0), 
                    ((loPageStart != null) ? loPageStart.getHeight() : 0)),
                    ((loPageEnd != null) ? loPageEnd.getHeight() : 0));


        return new Dimension(lnWidth, lnHeight);
    }

    



    @Override
    protected void layoutChildren(IContainer toContainer)
    {
        IControl loPageStart = null;
        IControl loPageEnd = null;
        IControl loLineStart = null;
        IControl loLineEnd = null;
        IControl loCenter = null;
        HashTable<IControl, Dimension> loSizes = new HashTable<IControl, Dimension>(5);

        Dimension loSize = toContainer.getContentSize();

        // Get all the contained controls
        List<IControl> loControls = toContainer.getChildren();
        for (IControl loControl : loControls)
        {
            loSizes.put(loControl, getContainedControlDimensions(loControl));
            BorderLayoutConstants loConstraint = loControl.getProperty(ControlLayoutProperty.LAYOUTCONSTRAINT().getValue());
            if (loConstraint == null)
            {
                loCenter = loControl;
            }
            else if (loConstraint.equals(BorderLayoutConstants.PAGE_START()))
            {
                loPageStart = loControl;
            }
            else if (loConstraint.equals(BorderLayoutConstants.PAGE_END()))
            {
                loPageEnd = loControl;
            }
            else if (loConstraint.equals(BorderLayoutConstants.LINE_START()))
            {
                loLineStart = loControl;
            }
            else if (loConstraint.equals(BorderLayoutConstants.LINE_END()))
            {
                loLineEnd = loControl;
            }
            else
            {
                loCenter = loControl;
            }
        }

        float lnPageStartHeight = getHeight(loPageStart, loSizes);
        float lnPageEndHeight = getHeight(loPageEnd, loSizes);
        float lnLineStartWidth = getWidth(loLineStart, loSizes);
        float lnLineEndWidth = getWidth(loLineEnd, loSizes);

        float lnMaxHeight = loSize.getHeight();
        float lnMaxWidth = loSize.getWidth();

        setContainedControlSize(toContainer, loPageStart, lnMaxWidth, lnPageStartHeight);
        setContainedControlSize(toContainer, loPageEnd, lnMaxWidth, lnPageEndHeight);

        setContainedControlSize(toContainer, loLineStart, lnLineStartWidth, lnMaxHeight - lnPageStartHeight - lnPageEndHeight);
        setContainedControlSize(toContainer, loLineEnd, lnLineEndWidth, lnMaxHeight - lnPageStartHeight - lnPageEndHeight);

        setContainedControlSize(toContainer, loCenter, lnMaxWidth - lnLineStartWidth - lnLineEndWidth, lnMaxHeight - lnPageStartHeight - lnPageEndHeight);
        
        setContainedControlLocation(toContainer, loPageStart, 0, 0);
        setContainedControlLocation(toContainer, loLineStart, 0, 0 + lnPageStartHeight);
        setContainedControlLocation(toContainer, loCenter, 0 + lnLineStartWidth, 0  + lnPageStartHeight);
        setContainedControlLocation(toContainer, loLineEnd, lnMaxWidth + 0 - lnLineEndWidth, 0 + lnPageStartHeight);
        setContainedControlLocation(toContainer, loPageEnd, 0, lnMaxHeight + 0 - lnPageEndHeight);

    }



   /**
    * Helper function for calculating the height of the specified area
    * @param toControl the control to get the size of
    * @param toLookup the lookup hashtable
    * @return the size of the control
    */
    private float getHeight(IControl toControl, HashTable<IControl, Dimension> toLookup)
    {
        if (toControl == null)
        {
            return 0;
        }
        Dimension loSize = toLookup.get(toControl);
        return loSize == null ? 0 : loSize.getHeight();
    }

    /**
    * Helper function for calculating the width of the specified area
    * @param toControl the control to get the size of
    * @param toLookup the lookup hashtable
    * @return the size of the control
    */
    private float getWidth(IControl toControl, HashTable<IControl, Dimension> toLookup)
    {
        if (toControl == null)
        {
            return 0;
        }
        Dimension loSize = toLookup.get(toControl);
        return loSize == null ? 0 : loSize.getWidth();
    }

}
