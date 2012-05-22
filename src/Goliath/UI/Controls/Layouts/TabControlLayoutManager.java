/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Goliath.UI.Controls.Layouts;

import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Constants.Position;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.UI.Controls.UserControls.TabbedControl;

/**
 *
 * @author shakthikumarsubbaraj
 */
public class TabControlLayoutManager extends LayoutManager<TabbedControl>
{
    private HashTable<IContainer, Position> m_oTabAlignments;
    
    public TabControlLayoutManager()
    {
    }

    /**
     * Sets the position of the tabs for this control
     * @param toPosition the new position
     */
    public final void setTabAlignment(IContainer toContainer, Position toPosition)
    {
        if (m_oTabAlignments == null)
        {
            m_oTabAlignments = new HashTable<IContainer, Position>();
        }
        
        Position loPosition = m_oTabAlignments.put(toContainer, toPosition);
        if (loPosition != toPosition)
        {
            toContainer.invalidate();
        }
    }
    
    /**
     * Gets where the tabs are positioned
     * @return the tab position
     */
    public Position getTabAlignment(IContainer toContainer)
    {
        return m_oTabAlignments != null && m_oTabAlignments.containsKey(toContainer) ?
               m_oTabAlignments.get(toContainer) : Position.TOP_CENTER();
    }

    @Override
    protected void onControlRemoved(TabbedControl toContainer)
    {
        if (m_oTabAlignments != null)
        {
            m_oTabAlignments.remove(toContainer);
        }
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
        float lnHeaderHeight = 0;
        float lnHeaderWidth = 0;
        Position loTabAlignment = getTabAlignment(toContainer);


        // Find the number of visible panels and the visible header list
        List<String> loPanels = toContainer.getPanels();

        // Find the size of the panels
        for (String lcPanel : loPanels)
        {
            IContainer loPanel = toContainer.getPanelContainer(lcPanel);
            IContainer loPanelHeader = toContainer.getPanelHeader(lcPanel);
            Dimension loDimension = getContainedControlDimensions(loPanel);
            Dimension loHeaderDimension = getContainedControlDimensions(loPanelHeader);
            
            if (toContainer.isPanelSelected(lcPanel))
            {
                lnHeight = Math.max(lnHeight, loDimension.getHeight());
                lnWidth = Math.max(lnWidth, loDimension.getWidth());
            }
            
            lnHeaderHeight = loTabAlignment == Position.TOP_CENTER() || loTabAlignment == Position.BOTTOM_CENTER() ? 
                    Math.max(lnHeaderHeight, loHeaderDimension.getHeight()) : 
                    lnHeaderHeight + loHeaderDimension.getWidth();
            
            lnHeaderWidth = loTabAlignment == Position.TOP_CENTER() || loTabAlignment == Position.BOTTOM_CENTER() ? 
                    lnHeaderWidth + loHeaderDimension.getWidth() :
                    Math.max(lnHeaderWidth, loHeaderDimension.getHeight());
        }
        
        return new Dimension(lnWidth, lnHeight);
    }

    @Override
    protected void layoutChildren(TabbedControl toContainer)
    {
        List<String> loPanels = toContainer.getPanels();
        Dimension loContainerSize = toContainer.getContentSize();
        Position loTabAlignment = getTabAlignment(toContainer);
        
        float lnPanelHeight = 0;
        float lnPanelWidth = 0;
        float lnHeaderWidth = 0;
        float lnHeaderHeight = 0;
        float lnTabSize = getTabSize(toContainer);
        float lnPanelTop = 0;
        float lnPanelLeft = 0;
                

        if (loTabAlignment == Position.TOP_CENTER() || loTabAlignment == Position.BOTTOM_CENTER())
        {
            lnPanelHeight = loContainerSize.getHeight() - lnTabSize;
            lnPanelWidth = loContainerSize.getWidth();
            lnHeaderWidth = (float) (loContainerSize.getWidth() / loPanels.size());
            lnHeaderHeight = lnTabSize;
            lnPanelTop = loTabAlignment == Position.TOP_CENTER() ? lnTabSize : 0;
            lnPanelLeft = 0;
        }
        else if (loTabAlignment == Position.MIDDLE_LEFT() || loTabAlignment == Position.MIDDLE_RIGHT())
        {
            lnPanelHeight = loContainerSize.getHeight();
            lnPanelWidth = Math.max(0,loContainerSize.getWidth() - lnTabSize);
            lnHeaderWidth = lnTabSize;
            lnHeaderHeight = (float) (loContainerSize.getHeight() / loPanels.size());
            lnPanelTop = 0;
            lnPanelLeft = loTabAlignment == Position.BOTTOM_LEFT() ? lnTabSize : 0;
        }

        int lnCount = 0;
        for (String lcKey : loPanels)
        {
            IContainer loPanelHeader = toContainer.getPanelHeader(lcKey);
            IContainer loPanel = toContainer.getPanelContainer(lcKey);

            loPanelHeader.setVisible(true);
            loPanel.setVisible(toContainer.isPanelSelected(lcKey));
            
            if (loTabAlignment == Position.TOP_CENTER() || loTabAlignment == Position.BOTTOM_CENTER())
            {
                setContainedControlLocation(toContainer, loPanelHeader, lnHeaderWidth * lnCount, loTabAlignment == Position.TOP_CENTER() ? 0 : lnPanelHeight);
                setContainedControlSize(toContainer, loPanelHeader, lnHeaderWidth, lnHeaderHeight);
                loPanelHeader.setTextRotation(0);
            }
            else
            {
                setContainedControlLocation(toContainer, loPanelHeader, loTabAlignment == Position.MIDDLE_LEFT() ? 0 : lnPanelWidth, lnHeaderWidth * lnCount);
                setContainedControlSize(toContainer, loPanelHeader, lnHeaderWidth, lnHeaderHeight);
                
                // Rotate the header text
                loPanelHeader.setTextRotation( loTabAlignment == Position.MIDDLE_LEFT() ? -90 : 90);
            }
            
            if (loPanel.isVisible())
            {   
                setContainedControlLocation(toContainer, loPanel, lnPanelLeft, lnPanelTop);
                setContainedControlSize(toContainer, loPanel, lnPanelWidth, lnPanelHeight);
            }
            lnCount++;
        }
    }

    /**
     * Gets the size of the tabs, this will be the width if the tab is positions on the left or right, the height if the tabs
     * are top or bottom positioned
     * @return the size of the tab
     */
    private float getTabSize(TabbedControl toContainer)
    {
        float lnSize = 0;
        
        List<String> loPanels = toContainer.getPanels();
        for (String lcPanelKey : loPanels)
        {
            IContainer loPanelHeader = toContainer.getPanelHeader(lcPanelKey);
            Dimension loDimension = getContainedControlDimensions(loPanelHeader);
            
            lnSize = Math.max(lnSize, loDimension.getHeight());
        }
        return lnSize;
    }
}
