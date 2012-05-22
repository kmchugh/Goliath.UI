/* ========================================================
 * Accordion.java
 *
 * Author:      kmchugh
 * Created:     Aug 3, 2010, 5:24:49 PM
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

package Goliath.UI.Controls.UserControls;

import Goliath.UI.Controls.ToggleButton;
import Goliath.UI.Controls.Layouts.AccordionLayoutManager;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Graphics.Image;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Controls.Group;
import Goliath.UI.Controls.Layouts.BorderLayoutManager;
import Goliath.UI.Controls.Layouts.GridLayoutManager;
import Goliath.UI.Constants.UIEventType;
        
/**
 * The Accordion is a control that contains collapsible panels
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 3, 2010
 * @author      kmchugh
**/
public class TabbedControl extends Group
{
    // TODO: This should use panels instead of headers and groups for the containers when the panel control is complete
    public class PanelSettings
    {
        private boolean m_lSelected;
        private boolean m_lCanDeselect;
        private boolean m_lCanSelect;
        private boolean m_lShowHeader;
        private IContainer m_oHeader;
        private IContainer m_oBody;

        public PanelSettings(IContainer toHeader, IContainer toBody)
        {
            m_lSelected = false;
            m_lCanDeselect = true;
            m_lCanSelect = true;
            m_lShowHeader = true;
            m_oHeader = toHeader;
            m_oBody = toBody;
        }

        public boolean canShowHeader()
        {
            return m_lShowHeader;
        }

        public boolean canDeselect()
        {
            return m_lCanDeselect;
        }

        public boolean canSelect()
        {
            return m_lCanSelect;
        }

        public boolean isSelected()
        {
            return m_lSelected;
        }

        public boolean setShowHeader(boolean tlShow)
        {
            if (m_lShowHeader != tlShow)
            {
                m_lShowHeader = tlShow;
                return true;
            }
            return false;
        }

        public boolean setCanDeselect(boolean tlCanDeselect)
        {
            if (m_lCanDeselect != tlCanDeselect)
            {
                m_lCanDeselect = tlCanDeselect;
                if (!m_lCanDeselect)
                {
                    m_lSelected = true;
                }
                return true;
            }
            return false;
        }

        public boolean setCanSelect(boolean tlCanSelect)
        {
            if (m_lCanSelect != tlCanSelect)
            {
                m_lCanSelect = tlCanSelect;
                if (!m_lCanSelect)
                {
                    m_lSelected = false;
                }
                return true;
            }
            return false;
        }

        public boolean setSelected(boolean tlIsSelected)
        {
            tlIsSelected = (tlIsSelected && m_lCanSelect) || (!tlIsSelected && m_lCanDeselect) ? tlIsSelected : !tlIsSelected;
            if (m_lSelected != tlIsSelected)
            {
                m_lSelected = tlIsSelected;
                return true;
            }
            return false;
        }

        public IContainer getHeader()
        {
            return m_oHeader;
        }

        public IContainer getBody()
        {
            return m_oBody;
        }
    }

    /**
     * Gets the tabbed control that is containing this content, if the content
     * does not exist inside a tabbed control, then null will be returned
     * @param toContent the content to get the tabbed control parent for
     * @return the tabbed control containing the control specified, or null if the content is not inside a tabbed control
     */
    public static TabbedControl getTabbedControlFromContent(IControl toContent)
    {
        while (toContent != null &&
                !Goliath.DynamicCode.Java.isEqualOrAssignable(TabbedControl.class, toContent.getClass()))
        {
            toContent = toContent.getParent();
        }
        return (TabbedControl)toContent;
    }

    
    private List<String> m_oKeys;
    private HashTable<String, PanelSettings> m_oPanelSettings;
    private int m_nMaxSelectable;
    private List<String> m_oSelected;
    
    /**
     * Creates a new instance of Accordion
     */
     public TabbedControl()
    {
        initialiseComponent();
    }

    /**
     * Initialise the component
     */
    private void initialiseComponent()
    {
        setLayoutManager(AccordionLayoutManager.class);
        setMaxSelectable(1);
    }

    /**
     * Creates the specified panel if it does not already exist.
     * @param tcKey the key for this panel
     * @param tcTitle the title for the panel
     * @return the newly created panel, or the existing panel
     */
    public IContainer createPanel(String tcKey, String tcTitle)
    {
        // TODO: Allow a hook for users to add their own functionallity (Event which passes created panel)
        if (!containsPanel(tcKey))
        {
            Group loReturn = new Group(getImplementationType());
            loReturn.setLayoutManager(GridLayoutManager.class);
            loReturn.addClass("tabbedControlPanelContainer");
            addPanel(tcKey, tcTitle, loReturn);
            return loReturn;
        }
        return getPanelContainer(tcKey);
    }

    /**
     * Adds a panel to this Accordion
     * @param tcTitle the title of the panel
     * @param toContents the container to use for the panel
     */
    public void addPanel(String tcTitle, IContainer toContents)
    {
        addPanel(tcTitle, tcTitle, toContents);
    }

    /**
     * Adds a panel to this Accordion
     * @param tcKey the key for the panel
     * @param tcTitle the title of the panel
     * @param toContents the container to use for the panel
     */
    public void addPanel(String tcKey, String tcTitle, IContainer toContents)
    {
        IContainer loHeaderControl = createHeaderControl(tcKey, tcTitle, null);
        addPanel(tcKey, loHeaderControl, toContents);
    }

    /**
     * Adds a panel to this Accordion
     * @param tcTitle the title of the panel
     * @param toImage the image to display for the panel
     * @param toContents the container to use for the panel
     */
    public void addPanel(String tcTitle, Image toImage, IContainer toContents)
    {
        addPanel(tcTitle, toImage, tcTitle, toContents);
    }

    /**
     * Adds a panel to this Accordion
     * @param tcKey the key for the panel
     * @param tcTitle the title of the panel
     * @param toImage the image to display for the panel
     * @param toContents the container to use for the panel
     */
    public void addPanel(String tcKey, Image toImage, String tcTitle, IContainer toContents)
    {
        IContainer loHeaderControl = createHeaderControl(tcKey, tcTitle, toImage);
        addPanel(tcKey, loHeaderControl, toContents);
    }

    /**
     * Actual worker function for creating the panel and keys
     * @param tcKey the key for the panel
     * @param toHeaderControl the control used for the header of the panel
     * @param toContents the contents of the panel
     */
    private void addPanel(String tcKey, IContainer toHeaderControl, IContainer toContents)
    {
        // Case insensitive on the keys
        tcKey = tcKey.toLowerCase();

        if (m_oKeys == null)
        {
            m_oKeys = new List<String>();
        }

        if (!m_oKeys.contains(tcKey))
        {
            m_oKeys.add(tcKey);
        }

        if (m_oPanelSettings == null)
        {
            m_oPanelSettings = new HashTable<String, PanelSettings>();
        }
        m_oPanelSettings.put(tcKey, new PanelSettings(toHeaderControl, toContents));

        // Add the contents to the accordion
        addControl(toHeaderControl);
        addControl(toContents);
        invalidate();
    }

    /**
     * 
     * @param tcKey the key of the panel to move
     * @param tnNewIndex the new index of the panel
     */
    public void movePanelToIndex(String tcKey, int tnNewIndex)
    {
        if (containsPanel(tcKey) && m_oKeys != null && m_oKeys.size() >= tnNewIndex)
        {
            tcKey = tcKey.toLowerCase();
            m_oKeys.remove(tcKey);
            m_oKeys.add(tnNewIndex, tcKey);
        }
    }

    /**
     * Sets if the panel header can be seen or not
     * @param tcKey the key of the panel to show or hide the panel for
     * @param tlVisible the visibility setting
     */
    public void setHeaderVisibility(String tcKey, boolean tlVisible)
    {
        PanelSettings loSettings = getPanelSetting(tcKey);
        if (loSettings != null)
        {
            if (loSettings.setShowHeader(tlVisible))
            {
                invalidate();
            }
        }
    }


    /**
     * Sets if the panel is allowed to contract or not
     * @param tcKey the key of the panel
     * @param tlCanDeselect true if allowed to contract, otherwise false
     */
    public void setPanelDeselection(String tcKey, boolean tlCanDeselect)
    {
        PanelSettings loSettings = getPanelSetting(tcKey);
        if (loSettings != null)
        {
            if (loSettings.setCanDeselect(tlCanDeselect))
            {
                invalidate();
            }
        }
    }

    /**
     * Sets if the panel is allowed to be selected or not
     * @param tcKey the key of the panel
     * @param tlCanSelect true if allowed to be selected, otherwise false
     */
    public void setPanelExpansion(String tcKey, boolean tlCanSelect)
    {
        PanelSettings loSettings = getPanelSetting(tcKey);
        if (loSettings != null)
        {
            if (loSettings.setCanSelect(tlCanSelect))
            {
                invalidate();
            }
        }
    }

    /**
     * Checks if there is a panel by the specified key
     * @param tcKey the key of the panel
     * @return true if the panel exists
     */
    public boolean containsPanel(String tcKey)
    {
        return m_oKeys != null ? m_oKeys.contains(tcKey.toLowerCase()) : false;
    }
    
    /**
     * Gets the maximum number of panels that can be expanded at one time
     * @return the maximum number of panels that can be expanded at one time
     */
    public int getMaxSelectable()
    {
        return m_nMaxSelectable;
    }
    
    /**
     * Sets the number of panels that can be expanded at one time
     * @param tnMax the maximum number of panels that can be expanded at one time
     */
    public void setMaxSelectable(int tnMax)
    {
        if (tnMax > 0)
        {
            m_nMaxSelectable = tnMax;
        }
    }

    /**
     * Gets the container for the specified panel
     * @param tcKey the key for the panel
     * @return the contents of the panel
     */
    public IContainer getPanelContainer(String tcKey)
    {
        PanelSettings loSettings = getPanelSetting(tcKey);
        return loSettings == null ? null : loSettings.getBody();
    }

    /**
     * Gets the panel header container for the specified key, if the key does not exist, this will
     * return null
     * @param tcKey the key to get the header for
     * @return the panel header container
     */
    public IContainer getPanelHeader(String tcKey)
    {
        PanelSettings loSettings = getPanelSetting(tcKey);
        return loSettings == null ? null : loSettings.getHeader();
    }

    private PanelSettings getPanelSetting(String tcKey)
    {
        return (m_oPanelSettings == null) ? null : m_oPanelSettings.get(tcKey.toLowerCase());
    }

    /**
     * Expands the specified panel
     * @param tcKey the panel to expand
     */
    public boolean selectPanel(String tcKey)
    {
        tcKey = tcKey.toLowerCase();
        if (containsPanel(tcKey) && !isPanelSelected(tcKey))
        {
            PanelSettings loSettings = getPanelSetting(tcKey);
            if (loSettings != null)
            {
                if (loSettings.setSelected(true))
                {
                    if (m_oSelected == null)
                    {
                        m_oSelected = new List<String>();
                    }
                    m_oSelected.add(tcKey);
                    updateSelectedList();
                    invalidate();

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Contracts the specified panel
     * @param tcKey the panel to contract
     */
    public boolean deselectPanel(String tcKey)
    {
        tcKey = tcKey.toLowerCase();
        // TODO: Impelemnt a minimum number of panels to be expanded
        if (containsPanel(tcKey) && isPanelSelected(tcKey))
        {
            PanelSettings loSettings = getPanelSetting(tcKey);
            if (loSettings != null)
            {
                if (loSettings.setSelected(false))
                {
                    if (m_oSelected != null && m_oSelected.contains(tcKey))
                    {
                        m_oSelected.remove(tcKey);
                    }
                    updateSelectedList();
                    invalidate();

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method just makes sure the correct number of panels are expanded
     */
    private void updateSelectedList()
    {
        List<String> loList = new List(getSelectedPanels());
        if (loList.size() > getMaxSelectable())
        {
            for (int i=0; i< loList.size() - getMaxSelectable(); i++)
            {
                deselectPanel(loList.get(i));
            }
        }
    }

    /**
     * Checks if the specified panel is expanded
     * @param tcKey the panel to check
     * @return true if the panel is expanded, false otherwise
     */
    public boolean isPanelSelected(String tcKey)
    {
        PanelSettings loSettings = getPanelSetting(tcKey);
        return loSettings == null ? false : loSettings.isSelected();
    }

    /**
     * Checks if the specified panel header should be displayed
     * @param tcKey the panel to check
     * @return true if the panel is should be displayed, false otherwise
     */
    public boolean isHeaderVisible(String tcKey)
    {
        PanelSettings loSettings = getPanelSetting(tcKey);
        return loSettings == null ? false : loSettings.canShowHeader();
    }

    /**
     * Gets the list of panels that are in this accordion, it the order they are displayed
     * @return the list of panels
     */
    public List<String> getPanels()
    {
        return m_oKeys == null ? new List<String>(0) : m_oKeys;
    }

    /**
     * Get the list of panels that are epanded, this list may not be in order
     * @return the list of expanded panels
     */
    public List<String> getSelectedPanels()
    {
        return m_oSelected == null ? new List<String>(0) : m_oSelected;
    }

    /**
     * Gets the number of panels that are expanded in this control
     * @return the number of expanded panels
     */
    public int getSelectedPanelCount()
    {
        List<String> loReturn = getSelectedPanels();
        return loReturn == null ? 0 : loReturn.size();
    }

    /**
     * Gets the total number of panels in the Accordion
     * @return the number of panels
     */
    public int getPanelCount()
    {
        return (m_oKeys != null ? m_oKeys.size() : 0);
    }

    /**
     * Removes the specified panel from the accordion control
     * @param tcKey the panel to remove
     */
    public void removePanel(String tcKey)
    {
        if (m_oKeys != null && m_oKeys.contains(tcKey))
        {
            PanelSettings loSettings = getPanelSetting(tcKey);
            if (loSettings != null)
            {
                removeControl(loSettings.getHeader());
                removeControl(loSettings.getBody());
                m_oKeys.remove(tcKey);
                m_oPanelSettings.remove(tcKey);
            }
        }

        if (m_oPanelSettings.isEmpty())
        {
            m_oPanelSettings = null;
        }
        
        if (m_oKeys.isEmpty())
        {
            m_oKeys = null;
        }

        // Removing a panel invalidates the control
        invalidate();
    }
    
    /**
     * Expand all the panels specified in the list
     * @param toPanelKeys the list of panels to expand
     */
    public void selectPanels(List<String> toPanelKeys)
    {
        for (String lcKey : toPanelKeys)
        {
            selectPanel(lcKey);
        }
    }

    /**
     * Contract all panels in the list
     * @param toPanelKeys the list of panels to contract
     */
    public void deselectPanels(List<String> toPanelKeys)
    {
        for (String lcKey : toPanelKeys)
        {
            deselectPanel(lcKey);
        }
    }

    /**
     * Expands all the panels, up to the maximum number of expandable
     */
    public void selectAll()
    {
        selectPanels(getPanels());

    }

    /**
     * Contracts all the panels
     */
    public void deselectAll()
    {
        deselectPanels(getPanels());
    }


    /**
     * This just ensures that the minimum number of panels are expanded
     */
//    @Override
//    protected void onUpdate()
//    {
//        // Make sure at least one panel is expanded
//        if (m_oKeys != null && m_oKeys.size() > 0 && getSelectedPanelCount() == 0)
//        {
//            selectPanel(m_oKeys.get(0));
//        }
//        super.onUpdate();
//    }

    /**
     * Creates the default panel header area
     * @param tcKey the key of the panel
     * @param tcTitle the title of the panel
     * @param toImage the image to use for the header area
     * @return the default header that was created
     */
    private IContainer createHeaderControl(String tcKey, String tcTitle, Image toImage)
    {
        Group loReturn = new Group();
        loReturn.addClass("tabbedControlPanelHeader");
        tcKey = tcKey.toLowerCase();

        loReturn.setLayoutManager(BorderLayoutManager.class);
        
        ToggleButton loButton = new ToggleButton(tcTitle);
        loButton.setProperty("panelKey", tcKey);
        
        loButton.addEventListener(UIEventType.ONCLICK(), Delegate.build(this, "onPanelHeaderClicked"));

        //loButton.setBorderSize(0, 0, 0, 5);
        loReturn.addControl(loButton);
        
        return loReturn;
    }



    /**
     * Occurs when the header has been clicked, this expands or contracts the clicked panel
     * @param toEvent the system click event
     */
    private void onPanelHeaderClicked(Event<IControl> toEvent)
    {
        String lcKey = (String)toEvent.getTarget().getProperty("panelKey");
        boolean llChanged  = false;
        if (isPanelSelected(lcKey))
        {
            llChanged = deselectPanel(lcKey);
        }
        else
        {
            llChanged = selectPanel(lcKey);
        }
        if (llChanged)
        {
            this.update();
            fireEvent(UIEventType.ONSELECTIONCHANGED(), new Event<TabbedControl>(this));
        }
    }

    
}
