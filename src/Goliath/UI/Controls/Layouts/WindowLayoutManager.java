/* ========================================================
 * WindowLayoutManager.java
 *
 * Author:      admin
 * Created:     Sep 1, 2011, 6:10:52 PM
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
import Goliath.UI.Controls.ControlBounds;
import Goliath.UI.Views.View;
import Goliath.UI.Windows.Window;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Sep 1, 2011
 * @author      admin
 **/
public class WindowLayoutManager extends LayoutManager<Window>
{
    

    /**
     * Creates a new instance of BorderLayoutManager
     */
    public WindowLayoutManager()
    {   
    }

    @Override
    protected Dimension onCalculateDimension(Window toContainer, Dimension toContainerSize)
    {
        View loView = Goliath.Utilities.isNull(toContainer.getChromeView(), toContainer.getView());
        return loView == null ? toContainerSize : getContainedControlDimensions(loView);
    }

    @Override
    protected void layoutChildren(Window toContainer)
    {
        View loView = Goliath.Utilities.isNull(toContainer.getChromeView(), toContainer.getView());
        
        // If the size of the window is not set, the set it to the size of the parents
        if (!toContainer.isSizeSet())
        {
            // Update the size of this container if needed
            ControlBounds loBounds = calculateControlBounds(toContainer);
            Dimension loSize = loBounds.getSize();
            if (loSize.getWidth() == 0 || loSize.getHeight() == 0)
            {
                Dimension loCurrentSize = toContainer.getImplementation().getSize(toContainer.getControl());
                loSize = new Dimension(
                        loSize.getWidth() == 0 ? Math.max(loSize.getWidth(), loCurrentSize.getWidth()) : loSize.getWidth(),
                        loSize.getHeight() == 0 ? Math.max(loSize.getHeight(), loCurrentSize.getHeight()) : loSize.getHeight());
            }
            toContainer.setPreferredSize(loSize);
        }
        
        
        if (loView != null)
        {
            Dimension loSize = toContainer.getContentSize();
            setContainedControlSize(toContainer, loView, loSize);
            setContainedControlLocation(toContainer, loView, 0, 0);
        }
    }
}
