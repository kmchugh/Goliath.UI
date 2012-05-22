/* ========================================================
 * AccordionLayout.java
 *
 * Author:      kmchugh
 * Created:     Nov 19, 2010, 11:58:10 AM
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
import Goliath.Graphics.Dimension;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.UI.Controls.ControlBounds;
import Goliath.UI.Controls.UserControls.TabbedControl;
import java.util.LinkedHashMap;

        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Nov 19, 2010
 * @author      kmchugh
**/
public class AccordionLayoutManager extends LayoutManager<TabbedControl>
{
    private float m_nHeaderSize;

    /**
     * Creates a new instance of AccordionLayout
     */
    public AccordionLayoutManager()
    {
        setHeaderSize(30);
    }

    /**
     * Calculates the size this control should be based on it's contents
     * @return the size the control should be
     */
    @Override
    protected Dimension onCalculateDimension(TabbedControl toContainer, Dimension toContainerSize)
    {
        float lnWidth = 0;
        float lnHeight = 0;


        // Find the number of visible panels and the visible header list
        List<String> loPanels = toContainer.getPanels();
        List<String> loVisiblePanels = new List<String>();

        if (loPanels != null)
        {

            for (String lcKey : loPanels)
            {
                if (toContainer.isPanelSelected(lcKey))
                {
                    loVisiblePanels.add(lcKey);
                }

                if (toContainer.isHeaderVisible(lcKey))
                {
                    lnHeight+=getHeaderSize();
                    Dimension loSize = getContainedControlDimensions(toContainer.getPanelHeader(lcKey));
                    lnWidth = Math.max(lnWidth, loSize.getWidth());
                }
            }
        }

        // Find the size of the panels
        for (String lcPanel : loVisiblePanels)
        {
            Dimension loSize = getContainedControlDimensions(toContainer.getPanelContainer(lcPanel));
            
            lnHeight+= loSize.getHeight();
            lnWidth = Math.max(lnWidth, loSize.getWidth());
        }

        return new Dimension(lnWidth, lnHeight);
    }


    @Override
    protected void layoutChildren(TabbedControl toContainer)
    {
        List<String> loPanels = toContainer.getPanels();

        // TODO : Be able to modify which panels are the floating panels, should be possible to have more than one
        String lcFloatingPanel = loPanels != null && loPanels.size() > 0 ? loPanels.get(0) : null;

        int lnVisibleHeaders = 0;
        for (String lcKey : loPanels)
        {
            if (toContainer.isHeaderVisible(lcKey))
            {
                lnVisibleHeaders++;
            }
        }
        
        Dimension loControlSize = toContainer.getContentSize();
        float lnPanelHeight = (float)((loControlSize.getHeight() - (lnVisibleHeaders * m_nHeaderSize)));
        
        LinkedHashMap<IContainer, ControlBounds> loBounds = new LinkedHashMap<IContainer, ControlBounds>();

        // Loop through and sort out the sizes
        for (String lcKey : loPanels)
        {
            IContainer loPanelHeader = toContainer.getPanelHeader(lcKey);
            IContainer loPanel = toContainer.getPanelContainer(lcKey);

            boolean llHeaderVisible = toContainer.isHeaderVisible(lcKey);
            boolean llPanelVisible = toContainer.isPanelSelected(lcKey);

            // Sort out the header
            loPanelHeader.setVisible(llHeaderVisible);

            // Size the header
            if (llHeaderVisible)
            {
                ControlBounds loBound = new ControlBounds();
                loBound.setSize(new Dimension(loControlSize.getWidth(), m_nHeaderSize));
                loBounds.put(loPanelHeader, loBound);
            }

            loPanel.setVisible(llPanelVisible);

            // Sort out the panel
            if (llPanelVisible)
            {
                ControlBounds loBound = loPanel.getLayoutManager().calculateControlBounds(toContainer);
                loBound.setSize(loControlSize.getWidth(), loBound.getSize().getHeight());
                loBounds.put(loPanel, loBound);
                lnPanelHeight -= loBound.getSize().getHeight();
            }
        }

        // Adjust the size for any leftover
        if (lnPanelHeight != 0)
        {
            IContainer loFloatingPanel = toContainer.getPanelContainer(lcFloatingPanel);
            ControlBounds loFloatingBounds = loBounds.get(loFloatingPanel);
            if (loFloatingBounds != null)
            {
                Dimension loFloatingDimension = loFloatingBounds.getSize();
                loFloatingBounds.setSize(loFloatingDimension.getWidth(), loFloatingDimension.getHeight() + lnPanelHeight);
            }
        }

        float lnOffset = 0;//toContainer.getBorderTop();
        float lnX = 0;//toContainer.getBorderLeft();

        // Now actually position
        // Loop through and sort out the sizes
        for (IContainer loControl : loBounds.keySet())
        {
            Dimension loSize = loBounds.get(loControl).getSize();
            setContainedControlSize(toContainer, loControl, loSize.getWidth(), loSize.getHeight());
            setContainedControlLocation(toContainer, loControl, lnX, lnOffset);
            
            lnOffset += loControl.getSize().getHeight();
        }
    }


    /**
     * Gets the size of the accordion panel headers
     * @return the size of the header
     */
    public final float getHeaderSize()
    {
        return m_nHeaderSize;
    }

    /**
     * Sets the size of the accordion headers
     * @param tnHeaderSize the new size
     */
    public final void setHeaderSize(float tnHeaderSize)
    {
        if (m_nHeaderSize != tnHeaderSize && tnHeaderSize >= 0)
        {
            m_nHeaderSize = tnHeaderSize;
        }
    }
}
