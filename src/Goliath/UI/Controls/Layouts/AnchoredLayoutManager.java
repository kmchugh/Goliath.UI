/* ========================================================
 * WeightedLayoutManager.java
 *
 * Author:      christinedorothy
 * Created:     Jun 10, 2011, 2:44:53 PM
 *
 * Description
 * --------------------------------------------------------
 * A LayoutManager that anchors the children to specific location
 * (e.g. TOP, BOTTOM)
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
import Goliath.Graphics.Constants.Orientation;
import Goliath.Graphics.Dimension;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.Controls.UserControls.TabbedControl;


        
/**
 * The AnchoredLayoutManager anchors controls to either the near or far sides of the control depending on the orientation of the 
 * layout manager.  If this is in a Horizontal orientation then Near would be considered the left side, and far would be considered
 * the right side.  In a vertical orientation then near would be the top, far would be the bottom.
 * 
 * 
 *
 * @version     1.0 Jun 10, 2011
 * @author      christinedorothy
**/
public class AnchoredLayoutManager<T extends TabbedControl> extends LayoutManager<T>
{
    private HashTable<IContainer, Orientation> m_oOrientations;
    private HashTable<IContainer, Float> m_oHeaderSizes;
    private HashTable<IContainer, Boolean> m_oDisplayHeaders;
    private HashTable<IContainer, Float> m_oPanelGaps;
    private HashTable<IContainer, Float> m_oAnchorGaps;
    
    /**
     * Creates a new instance of the Anchored layout manager.  By default this will
     * be created with the vertical orientation.
     */
    public AnchoredLayoutManager()
    {
    }

    @Override
    protected void onControlRemoved(T toContainer)
    {
        if (m_oOrientations != null)
        {
            m_oOrientations.remove(toContainer);
        }
        
        if (m_oHeaderSizes != null)
        {
            m_oHeaderSizes.remove(toContainer);
        }
        
        if (m_oDisplayHeaders != null)
        {
            m_oDisplayHeaders.remove(toContainer);
        }
        
        if (m_oPanelGaps != null)
        {
            m_oPanelGaps.remove(toContainer);
        }
        
        if (m_oAnchorGaps != null)
        {
            m_oAnchorGaps.remove(toContainer);
        }
    }
    
    
    
    /**
     * To calculate the minimum dimensions needed for the controls.
     * If there is a sequence map set, it will calculate the controls listed in the
     * sequence map. If no sequence map is set, it will calculate the children
     * of the control to which this LayoutManager is attached.
     *
     * @param toContainerSize   The size of the control to which this LayoutManager is attached.
     * @return                  The dimension requested based on the children's dimension.
     */
    @Override
    protected Dimension onCalculateDimension(T toContainer, Dimension toContainerSize)
    {
        float lnMinor = 0;
        float lnMajor = 0;
        Orientation loOrientation = getOrientation(toContainer);


        // Find the number of visible panels and the visible header list
        List<String> loPanels = toContainer.getPanels();
        List<String> loFarPanels = sortPanels(toContainer, loPanels);

        if (loPanels != null)
        {
            for (String lcKey : loPanels)
            {
                if (toContainer.isHeaderVisible(lcKey) && getDisplayHeaders(toContainer))
                {
                    lnMajor+=getHeaderSize(toContainer);
                    Dimension loSize = getContainedControlDimensions(toContainer.getPanelHeader(lcKey));
                    lnMinor = Math.max(lnMinor, loOrientation == Orientation.VERTICAL() ? loSize.getWidth() : loSize.getHeight());
                }
            }
        }

        // Find the size of the panels
        boolean llFarGap = false;
        float lnPanelGap = getPanelGap(toContainer);
        float lnAnchorGap = getMinimumAnchorGap(toContainer);
        for (String lcPanel : loPanels)
        {
            Dimension loSize = getContainedControlDimensions(toContainer.getPanelContainer(lcPanel));
            
            lnMajor+= loOrientation == Orientation.VERTICAL() ? loSize.getHeight() : loSize.getWidth() ;
            lnMinor = loOrientation == Orientation.VERTICAL() ? Math.max(lnMinor, loSize.getWidth()) : Math.max(lnMinor, loSize.getHeight());
            
            
            if (!lcPanel.equals(loPanels.get(loPanels.size()-1)))
            {
                if ((!llFarGap && loFarPanels.contains(lcPanel)))
                {
                    lnMajor += lnAnchorGap;
                    llFarGap = true;
                }
                else
                {
                    lnMajor += lnPanelGap;
                }
            }
        }
        return loOrientation == Orientation.VERTICAL() ? new Dimension(lnMinor, lnMajor) : new Dimension(lnMajor, lnMinor);        
    }
    
    /**
     * To layout the children. This method will determine how the children is drawn.
     * If there is a sequence map set, it will draw the controls listed in the
     * sequence map. If no sequence map is set, it will draw the children
     * of the control to which this LayoutManager is attached with a default ordering,
     * first come first served.
     */
    @Override
    protected void layoutChildren(T toContainer)
    {
        Dimension loClientSize = toContainer.getContentSize();
        
        // Find the number of visible panels and the visible header list
        List<String> loPanels = toContainer.getPanels();
        List<String> loFarPanels = sortPanels(toContainer, loPanels);
        
        float lnMinorMaxSize = 0;
        float lnMajorOffset = 0;
        float lnMinorOffset = 0;
        float lnAnchorGap = getMinimumAnchorGap(toContainer);
        float lnPanelGap = getPanelGap(toContainer);
        Orientation loOrientation = getOrientation(toContainer);
        
        // TODO: The algorithm below can be optimised
        
        // Find the actual size of the gap
        float lnGap = 0;
        boolean llFarGap = false;
        for (String lcPanel : loPanels)
        {
            IControl loControl = toContainer.getPanelHeader(lcPanel);
            if (toContainer.isHeaderVisible(lcPanel) && getDisplayHeaders(toContainer))
            {
                lnGap += getHeaderSize(toContainer);
            }
            loControl = toContainer.getPanelContainer(lcPanel);
            Dimension loPanelSize = getContainedControlDimensions(loControl);
            lnGap += loOrientation == Orientation.VERTICAL() ? loPanelSize.getHeight() : loPanelSize.getWidth();
            
            if (!lcPanel.equals(loPanels.get(loPanels.size()-1)))
            {
                if ((!llFarGap && loFarPanels.contains(lcPanel)))
                {
                    // No need to add the gap to the calculation as that is what we are figuring out
                    llFarGap = true;
                }
                else
                {
                    lnGap += lnPanelGap;
                }
            }
            
        }
        lnAnchorGap = Math.max((loOrientation == Orientation.VERTICAL() ? loClientSize.getHeight() : loClientSize.getWidth()) - lnGap, lnAnchorGap);
        
        if (loOrientation == Orientation.VERTICAL())
        {
            lnMinorMaxSize = loClientSize.getWidth();
            lnMinorOffset = 0;
        }
        else
        {
            lnMinorMaxSize = loClientSize.getHeight();
            lnMinorOffset = 0;
        }
        
        llFarGap = false;
        int lnCount = 0;
        for (String lcPanel : loPanels)
        {
            IControl loControl = toContainer.getPanelHeader(lcPanel);
            
            
            
            // Layout the header
            loControl.setVisible(toContainer.isHeaderVisible(lcPanel) && getDisplayHeaders(toContainer));
            if (loControl.isVisible())
            {
                setControlSize(toContainer, loControl, getHeaderSize(toContainer), lnMinorMaxSize);
                setControlLocation(toContainer, loControl, lnMajorOffset, lnMinorOffset);
                
                lnMajorOffset += getHeaderSize(toContainer);
            }
            
            // Layout the container
            loControl = toContainer.getPanelContainer(lcPanel);
            loControl.setVisible(true);
            
            Dimension loPanelSize = getContainedControlDimensions(loControl);
            
            setControlSize(toContainer, loControl, loOrientation == Orientation.VERTICAL() ? loPanelSize.getHeight() : loPanelSize.getWidth(), lnMinorMaxSize);
            setControlLocation(toContainer, loControl, lnMajorOffset, lnMinorOffset);

            lnMajorOffset += loOrientation == Orientation.VERTICAL() ? loControl.getSize().getHeight() : loControl.getSize().getWidth();
            
            if (!lcPanel.equals(loPanels.get(loPanels.size()-1)))
            {
                if ((!llFarGap && loPanels.size() > lnCount+2 && loFarPanels.contains(loPanels.get(lnCount+1))))
                {
                    lnMajorOffset += lnAnchorGap;
                    llFarGap = true;
                }
                else
                {
                    lnMajorOffset += lnPanelGap;
                }
            }
        }
    }
    
    /**
     * Helper function to sort the panels based on the layout constraints.  This function will reorder toPanelList
     * @param toControl the control to sort the panels from
     * @param toPanelList the full list of panels to sort
     * @return the list of FAR panels
     */
    private synchronized List<String> sortPanels(T toContainer, List<String> toPanelList)
    {
        List<String> loNear = new List<String>();
        List<String> loFar = new List<String>();
        
        // Organise the panels
        for (String lcPanel : toPanelList)
        {
            if (toContainer.getPanelContainer(lcPanel).getProperty(ControlLayoutProperty.LAYOUTCONSTRAINT().getValue()) == AnchoredLayoutConstants.FAR())
            {
                loFar.add(lcPanel);
            }
            else
            {
                loNear.add(lcPanel);
            }
        }
        
        toPanelList.clear();
        toPanelList.addAll(loNear);
        toPanelList.addAll(loFar);
        return loFar;
        
    }
    
    /**
     * Helper function to set the location of the control based on the orientation
     * @param toControl the control to move
     * @param tnMajor the major axis position
     * @param tnMinor the minor axis position
     */
    private void setControlLocation(T toContainer, IControl toControl, float tnMajor, float tnMinor)
    {
        if(getOrientation(toContainer) == Orientation.VERTICAL())
        {
            setContainedControlLocation(toContainer, toControl, tnMinor, tnMajor);
        }
        else
        {
            setContainedControlLocation(toContainer, toControl, tnMajor, tnMinor);
        }
    }
    
    /**
     * Helper function to set the size of the control based on the orientation
     * @param toControl the control to size
     * @param tnMajor the major axis size
     * @param tnMinor the minor axis size
     */
    private void setControlSize(T toContainer, IControl toControl, float tnMajor, float tnMinor)
    {
        if(getOrientation(toContainer) == Orientation.VERTICAL())
        {
            setContainedControlSize(toContainer, toControl, tnMinor, tnMajor);
        }
        else
        {
            setContainedControlSize(toContainer, toControl, tnMajor, tnMinor);
        }
    }
    
    
    /**
     * Gets the size of the panel headers
     * @return the size of the header
     */
    public final float getHeaderSize(T toContainer)
    {
        return m_oHeaderSizes != null && m_oHeaderSizes.containsKey(toContainer) ?
               m_oHeaderSizes.get(toContainer) : 30;
    }

    /**
     * Sets the size of the headers
     * @param tnHeaderSize the new size
     */
    public final void setHeaderSize(T toContainer, float tnHeaderSize)
    {
        if (m_oHeaderSizes == null)
        {
            m_oHeaderSizes = new HashTable<IContainer, Float>();
        }
        Float loSize = m_oHeaderSizes.put(toContainer, tnHeaderSize);
        if (loSize != null && loSize.floatValue() != tnHeaderSize)
        {
            toContainer.invalidate();
        }
    }
    
    /**
     * Checks if the panel headers should be displayed in this layout or not
     * @return true to display false otherwise
     */
    public final boolean getDisplayHeaders(T toContainer)
    {
        return m_oDisplayHeaders != null && m_oDisplayHeaders.containsKey(toContainer) ? 
               m_oDisplayHeaders.get(toContainer) : true;
    }
    
    /**
     * Sets if the panel headers should be displayed using this manager.
     * @param tlShouldDisplay true to display false to hide
     */
    public final void setDisplayHeaders(T toContainer, boolean tlShouldDisplay)
    {
        if (m_oDisplayHeaders == null)
        {
            m_oDisplayHeaders = new HashTable<IContainer, Boolean>();
        }
        Boolean loSize = m_oDisplayHeaders.put(toContainer, tlShouldDisplay);
        if (loSize != null && loSize.booleanValue() != tlShouldDisplay)
        {
            toContainer.invalidate();
        }
    }
    
    /**
     * Gets the orientation of this layout manager
     * @return the orientation of the manager
     */
    public final Orientation getOrientation(T toContainer)
    {
        return m_oOrientations != null && m_oOrientations.containsKey(toContainer) ? 
               m_oOrientations.get(toContainer) : Orientation.HORIZONTAL();
    }
    
    /**
     * Sets the orientation of this layout manager
     * @param toOrientation sets if this will layout in a horizontal or vertical orientation
     */
    public final void setOrientation(T toContainer, Orientation toOrientation)
    {
        if (m_oOrientations == null)
        {
            m_oOrientations = new HashTable<IContainer, Orientation>();
        }
        Orientation loSize = m_oOrientations.put(toContainer, toOrientation);
        if (loSize != null && loSize != toOrientation)
        {
            toContainer.invalidate();
        }
    }
    
    /**
     * Gets the space that should be placed between panels when they are rendered
     * @return the amount of space to leave between panels when they are rendered
     */
    public float getPanelGap(T toContainer)
    {
        return m_oPanelGaps != null && m_oPanelGaps.containsKey(toContainer) ?
               m_oPanelGaps.get(toContainer) : 25;
    }
    
    /**
     * Sets the gap between panels in the layout manager
     * @param tnPanelGap the space between panels
     */
    public void setPanelGap(T toContainer, float tnPanelGap)
    {
        if (m_oPanelGaps == null)
        {
            m_oPanelGaps = new HashTable<IContainer, Float>();
        }
        Float loSize = m_oPanelGaps.put(toContainer, tnPanelGap);
        if (loSize != null && loSize.floatValue() != tnPanelGap)
        {
            toContainer.invalidate();
        }
    }
    
    /**
     * Gets the minimum amount of space to leave between anchors (NEAR and FAR)
     * @return the space to leave between anchors
     */
    public float getMinimumAnchorGap(T toContainer)
    {
        return m_oAnchorGaps != null && m_oAnchorGaps.containsKey(toContainer) ? 
               m_oAnchorGaps.get(toContainer) : 50;
    }
    
    /**
     * Sets the amount of space that must be left between panels in the NEAR and FAR anchors
     * @param tnMinimumGap the minimum space to separate the anchor points by
     */
    public void setMinimumAnchorGap(T toContainer, float tnMinimumGap)
    {
        if (m_oAnchorGaps == null)
        {
            m_oAnchorGaps = new HashTable<IContainer, Float>();
        }
        Float loSize = m_oAnchorGaps.put(toContainer, tnMinimumGap);
        if (loSize != null && loSize.floatValue() != tnMinimumGap)
        {
            toContainer.invalidate();
        }
    }
}
