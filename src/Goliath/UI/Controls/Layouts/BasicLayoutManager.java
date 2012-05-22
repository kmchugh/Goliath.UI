/* ========================================================
 * BasicLayoutManager.java
 *
 * Author:      kenmchugh
 * Created:     Nov 15, 2010, 4:55:11 PM
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
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Controls.ControlBounds;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Nov 15, 2010
 * @author      kenmchugh
**/
public class BasicLayoutManager extends LayoutManager<IContainer>
{
    /**
     * Creates a new instance of BasicLayoutManager
     */
    public BasicLayoutManager()
    {
    }

    @Override
    protected Dimension onCalculateDimension(IContainer toContainer, Dimension toContainerSize)
    {
        float lnHeight = 0;
        float lnWidth = 0;
        for (IControl loControl : toContainer.getChildren())
        {
            ControlBounds loBounds = getContainedControlBounds(loControl);
            Dimension loSize = loBounds.getSize();

            float lnXOffset = (loBounds.getLocation() != null) ? loBounds.getLocation().getX() : 0;
            float lnYOffset = (loBounds.getLocation() != null) ? loBounds.getLocation().getY() : 0;

            lnHeight = Math.max(lnHeight, loSize.getHeight() + lnYOffset);
            lnWidth = Math.max(lnWidth, loSize.getWidth() + lnXOffset);
        }
        return new Dimension(lnWidth, lnHeight);
    }



    @Override
    protected void layoutChildren(IContainer toContainer)
    {
        // No need to do anything here
    }

    


}
