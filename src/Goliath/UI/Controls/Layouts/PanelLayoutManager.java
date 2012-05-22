/* ========================================================
 * PanelLayoutManager.java
 *
 * Author:      christinedorothy
 * Created:     Jun 10, 2011, 3:34:38 PM
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
import Goliath.UI.Controls.UserControls.PanelControl;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jun 10, 2011
 * @author      christinedorothy
**/
public class PanelLayoutManager <T extends PanelControl> extends LayoutManager<T>
{
    private int m_nHeaderHeight;
    private boolean m_lHeaderVisible;

    /**
     * Creates a new instance of PanelLayoutManager
     */
    public PanelLayoutManager()
    {
        m_nHeaderHeight = 25;
        m_lHeaderVisible = false;
    }

    public void setHeaderHeight(int tnHeaderHeight)
    {
        m_nHeaderHeight = tnHeaderHeight;
    }

    public int getHeaderHeight()
    {
        return m_nHeaderHeight;
    }

    @Override
    protected void layoutChildren(T toContainer)
    {
        // TODO: No need to cast
        Dimension loParentDimension = toContainer.getContentSize();

        if (isHeaderVisible())
        {
            IContainer loHeader = toContainer.getHeader();
            setContainedControlSize(toContainer, loHeader, loParentDimension.getWidth(), m_nHeaderHeight);
            setContainedControlLocation(toContainer, loHeader, 0, 0);
        }
        else
        {
            toContainer.getHeader().setVisible(false);
        }

        IContainer loContent = toContainer.getContent();
        setContainedControlSize(toContainer, loContent, loParentDimension.getWidth(), getContainedControlDimensions(loContent).getHeight());
        setContainedControlLocation(toContainer, loContent, 0, (isHeaderVisible() ? m_nHeaderHeight : 0));
    }

    @Override
    protected Dimension onCalculateDimension(T toContainer, Dimension toContainerSize)
    {
        IContainer loHeader = toContainer.getHeader();
        IContainer loContent = toContainer.getContent();

        float lnWidth = 0;
        float lnHeight = 0;

        lnWidth = Math.max(
                    (isHeaderVisible() ? getContainedControlDimensions(loHeader).getWidth() : 0),
                    getContainedControlDimensions(loContent).getWidth()
                );
        lnHeight = isHeaderVisible() ? m_nHeaderHeight : 0;
        lnHeight = lnHeight + getContainedControlDimensions(loContent).getHeight();
        
        return new Dimension(lnWidth, lnHeight);
    }

    public boolean isHeaderVisible()
    {
        return m_lHeaderVisible;
    }

    public void setHeaderVisible(boolean tlVisibility)
    {
        m_lHeaderVisible = tlVisibility;
    }


}
