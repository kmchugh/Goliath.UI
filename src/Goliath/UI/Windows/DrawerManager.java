/* ========================================================
 * DrawerManager.java
 *
 * Author:      christinedorothy
 * Created:     Jun 21, 2011, 10:20:46 AM
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

package Goliath.UI.Windows;

import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Graphics.Constants.Position;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Controls.ControlDragManager;
import Goliath.UI.Views.View;
import java.util.Collection;

        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jun 21, 2011
 * @author      christinedorothy
**/
public class DrawerManager extends Goliath.Object
{   
    private static DrawerManager g_oDrawerManager;
    private static HashTable<String, IDelegate> g_oDelegates;
    
    // TODO: This needs to be optimised to queue drawing after events have occured for a control
    
    
    /**
     * Gets the singleton instance of the drawer manager
     * @return the drawer manager
     */
    public static DrawerManager getInstance()
    {
        if (g_oDrawerManager == null)
        {
            g_oDrawerManager = new DrawerManager();
        }
        return g_oDrawerManager;
    }
    
    private HashTable<IControl, HashTable<String, Drawer>> m_oDrawers;
    private HashTable<IControl, HashTable<String, Goliath.UI.Windows.DrawerConfig>> m_oDrawerConfigs;

    /**
     * Creates an instance of the Drawer Manager, as there can only be one this constructor is private
     */
    private DrawerManager()
    {
    }
    
    /**
     * Adds a control to the drawer contents
     * @param toControl the control that has the drawer attached
     * @param tcKey the key of the drawer
     * @param toContents the control to add
     * @return true if the control was added, false otherwise
     */
    public boolean addDrawerContents(IControl toControl, String tcKey, IControl toContents)
    {
        Drawer loDrawer = getDrawer(toControl, tcKey);
        if (loDrawer != null)
        {
            loDrawer.getView().addControl(toContents);
            loDrawer.invalidate();
            loDrawer.update();
            return true;
        }
        return false;
    }
    
    /**
     * Adds a Drawer to the control specified, at the position specified.
     * @param toControl the control to add the drawer to
     * @param tcKey is the key of this drawer, the key can be used to identify this drawer
     * @param toPosition the position to add the drawer
     * @return true if the drawer was added, false if it was not
     */
    public boolean addDrawer(IControl toControl, String tcKey, Position toPosition)
    {
        Goliath.UI.Windows.DrawerConfig loConfig = new Goliath.UI.Windows.DrawerConfig(toPosition);
        return addDrawer(toControl, tcKey, loConfig);
    }
    
    /**
     * Adds a drawer to the control using the specified configuration, if a drawer already exists at the location
     * specified, or if a drawer already exists with the same key, then this function not add a drawer and will return false
     * @param toControl The control to add the drawer to
     * @param tcKey the key to use to identify the drawer
     * @param toConfig the configuration of the drawer
     * @return true if the drawer is added to the control, otherwise false
     */
    public synchronized boolean addDrawer(IControl toControl, String tcKey, Goliath.UI.Windows.DrawerConfig toConfig)
    {
        boolean llReturn = false;
        tcKey = tcKey.toLowerCase();
        
        if (m_oDrawers == null)
        {
            m_oDrawers = new HashTable<IControl, HashTable<String, Drawer>>();
        }
        
        if (!m_oDrawers.containsKey(toControl))
        {
            m_oDrawers.put(toControl, new HashTable<String, Drawer>(4));
        }
        
        // Check if there is already a drawer at the specified location
        if (!hasDrawerAtPosition(toControl, toConfig.getPosition()) && ! hasDrawerKey(toControl, tcKey));
        {
            // We are okay to add the drawer
            Drawer loDrawer = new Drawer();
            loDrawer.addControl(new View());
            
            
            m_oDrawers.get(toControl).put(tcKey, loDrawer);
            llReturn = true;
            
            if (setDrawerConfig(toControl, tcKey, toConfig))
            {
                // Add the event listeners to the control
                addEventListeners(toControl);
                if (toControl.isVisible())
                {
                    updateSizeAndLocation(toControl, loDrawer);
                    
                    // Use the same drag handles as the window uses
                    ControlDragManager.getInstance().registerControl(loDrawer, ControlDragManager.getInstance().getDragHandles(toControl));
                }
                
            }
        }
        return llReturn;
    }
    
    // TODO: Implement remove drawer, also should remove the drawer config
    
    /**
     * Sets the drawer configuration for the specified drawer on the control.  If a configuration already exists
     * for this drawer, it will be overwritten by this method
     * @param toControl the control to configure the drawer for
     * @param tcKey the key of the drawer to configure
     * @param toConfig the configuration of the drawer
     * @return true if the drawer configuration was set, false otherwise
     */
    public synchronized boolean setDrawerConfig(IControl toControl, String tcKey, Goliath.UI.Windows.DrawerConfig toConfig)
    {
        boolean llReturn = false;
        if (hasDrawerKey(toControl, tcKey))
        {
            if (m_oDrawerConfigs == null)
            {
                m_oDrawerConfigs = new HashTable<IControl, HashTable<String, Goliath.UI.Windows.DrawerConfig>>();
            }
            
            if (!m_oDrawerConfigs.containsKey(toControl))
            {
                m_oDrawerConfigs.put(toControl, new HashTable<String, Goliath.UI.Windows.DrawerConfig>(4));
            }
            
            m_oDrawerConfigs.get(toControl).put(tcKey, toConfig);
            llReturn = true;
        }
        return llReturn;
    }
    
    /**
     * Checks if there is already a drawer at the specified control position
     * @param toControl the control to check
     * @param toPosition the position to check
     * @return true if there is already a drawer created at the specified position for the control specified
     */
    public boolean hasDrawerAtPosition(IControl toControl, Position toPosition)
    {
        boolean llReturn = false;
        if (m_oDrawerConfigs != null &&
                m_oDrawerConfigs.containsKey(toControl))
        {
            Collection<Goliath.UI.Windows.DrawerConfig> loConfigs = m_oDrawerConfigs.get(toControl).values();
            for (Goliath.UI.Windows.DrawerConfig loConfig : loConfigs)
            {
                if (loConfig.getPosition() == toPosition)
                {
                    llReturn = true;
                    break;
                }
            }
        }
        return llReturn;
    }
    
    /**
     * Checks if the key already exists for a drawer on the specified control
     * @param toControl the control to check
     * @param tcKey the key to search for
     * @return true if the key is already used on the specified control
     */
    public boolean hasDrawerKey(IControl toControl, String tcKey)
    {
        tcKey = tcKey.toLowerCase();
        
        return m_oDrawers != null && m_oDrawers.containsKey(toControl) && m_oDrawers.get(toControl).containsKey(tcKey);
    }
    
    /**
     * Helper function to get the drawer for a specified key
     * @param toControl the control that has the drawers attached
     * @param tcKey the key of the drawer to get
     * @return the drawer, or null if there is no drawer for this control
     */
    private Drawer getDrawer(IControl toControl, String tcKey)
    {
        tcKey = tcKey.toLowerCase();
        if (m_oDrawers != null && m_oDrawers.containsKey(toControl))
        {
            return m_oDrawers.get(toControl).get(tcKey);
        }
        return null;
    }
    
    /**
     * Helper function to add the event listeners to the control with drawers attached
     * @param toControl the control that is to have the drawers attached
     */
    private void addEventListeners(IControl toControl)
    {
        toControl.addEventListener(UIEventType.ONVALIDATED(), getDelegate("VisibleChanged"));
        toControl.addEventListener(UIEventType.ONVISIBLECHANGED(), getDelegate("VisibleChanged"));
        toControl.addEventListener(UIEventType.ONMINIMISED(), getDelegate("Minimised"));
        toControl.addEventListener(UIEventType.ONRESTORED(), getDelegate("Restored"));
        toControl.addEventListener(UIEventType.ONSIZECHANGING(), getDelegate("SizeChanged"));
        toControl.addEventListener(UIEventType.ONCLOSING(), getDelegate("Closing"));
        toControl.addEventListener(UIEventType.ONOPENED(), getDelegate("Opened"));
        toControl.addEventListener(UIEventType.ONACTIVATED(), getDelegate("Activated"));
        toControl.addEventListener(UIEventType.ONPOSITIONCHANGED(), getDelegate("PositionChanged"));
    }
    
    /**
     * Helper function to remove the event listeners to the control with drawers attached
     * @param toControl the control that has the drawers attached
     */
    private void removeEventListeners(IControl toControl)
    {   
        toControl.removeEventListener(UIEventType.ONVALIDATED(), getDelegate("VisibleChanged"));
        toControl.removeEventListener(UIEventType.ONVISIBLECHANGED(), getDelegate("VisibleChanged"));
        toControl.removeEventListener(UIEventType.ONMINIMISED(), getDelegate("Minimised"));
        toControl.removeEventListener(UIEventType.ONRESTORED(), getDelegate("Restored"));
        toControl.removeEventListener(UIEventType.ONSIZECHANGING(), getDelegate("SizeChanged"));
        toControl.removeEventListener(UIEventType.ONCLOSING(), getDelegate("Closing"));
        toControl.removeEventListener(UIEventType.ONOPENED(), getDelegate("Opened"));
        toControl.removeEventListener(UIEventType.ONACTIVATED(), getDelegate("Activated"));
        toControl.removeEventListener(UIEventType.ONPOSITIONCHANGED(), getDelegate("PositionChanged"));
    }
    
    /**
     * Helper function for creating and caching delegates so they can be removed easily
     * as well as cutting down the number of delegates created
     * @param tcKey the key for the delegate
     * @return the delegate
     */
    private IDelegate getDelegate(String tcKey)
    {
        tcKey = tcKey.toLowerCase();
        if (g_oDelegates == null)
        {
            g_oDelegates = new HashTable<String, IDelegate>();
        }
        if (!g_oDelegates.containsKey(tcKey.toLowerCase()))
        {
            g_oDelegates.put(tcKey, Delegate.build(g_oDrawerManager, "on" + tcKey));
        }
        return g_oDelegates.get(tcKey);
    }
    
    /**
     * Helper function to get the list of drawers for the specified control
     * @param toControl the control to get the drawers for
     * @return the list of drawers for this control, if no drawers then an empty list
     */
    private List<Drawer> getDrawersFor(IControl toControl)
    {
        return new List(m_oDrawers.containsKey(toControl) ? m_oDrawers.get(toControl).values() : null);
    }
    
    /**
     * Helper function to get the key for the specified drawer
     * @param toControl the control that the drawer is on
     * @param toDrawer the drawer to get the key for
     * @return the key, or null if the drawer does not exist on the control
     */
    private String getKeyForDrawer(IControl toControl, Drawer toDrawer)
    {
        if (m_oDrawers != null && m_oDrawers.containsKey(toControl))
        {
            for (String lcKey : m_oDrawers.get(toControl).keySet())
            {
                if (m_oDrawers.get(toControl).get(lcKey).equals(toDrawer))
                {
                    return lcKey;
                }
            }
        }
        return null;
    }
    
    /**
     * Helper function to get the configuration for a specified drawer
     * @param toControl the control the drawer is attached to
     * @param toDrawer the drawer to get the configuration for
     * @return the configuration or null of the drawer does not have a configuration
     */
    private Goliath.UI.Windows.DrawerConfig getConfigurationFor(IControl toControl, Drawer toDrawer)
    {
        String lcKey = getKeyForDrawer(toControl, toDrawer);
        if (lcKey != null)
        {
            if (m_oDrawerConfigs != null && m_oDrawerConfigs.containsKey(toControl))
            {
                return m_oDrawerConfigs.get(toControl).get(lcKey);
            }
        }
        return null;
    }
    
    
    /**
     * Helper function to position and size the Drawer windows
     * @param toControl the Control that has the drawer
     * @param toDrawer the drawer to position
     */
    private void updateSizeAndLocation(IControl toControl, Drawer toDrawer)
    {
        if (toControl.isVisible() && !toControl.isInvalidated())
        {
            Goliath.UI.Windows.DrawerConfig loConfig = getConfigurationFor(toControl, toDrawer);
            
            Position loPosition = loConfig.getPosition();
            Point loControlLocation = toControl.getLocation();
            Dimension loControlSize = toControl.getSize();
            Dimension loDrawerSize = toDrawer.getPreferredSize();

            float lnWidth = 30;
            float lnHeight = 30;
            float lnTop = 0;
            float lnLeft = 0;

            if (loPosition == Position.TOP_CENTER())
            {
                lnWidth = loControlSize.getWidth();
                lnLeft = loControlLocation.getX();
                lnTop = loControlLocation.getY() - loDrawerSize.getHeight();
            }
            else if (loPosition == Position.BOTTOM_CENTER())
            {
                lnWidth = loControlSize.getWidth();
                lnLeft = loControlLocation.getX();
                lnTop = loControlLocation.getY() + loControlSize.getHeight();
            }
            else if (loPosition == Position.MIDDLE_LEFT())
            {
                lnHeight = loControlSize.getHeight();
                lnLeft = loControlLocation.getX() - loDrawerSize.getWidth();
                lnTop = loControlLocation.getY();
            }
            else
            {
                lnHeight = loControlSize.getHeight();
                lnLeft = loControlLocation.getX() + loControlSize.getWidth();
                lnTop = loControlLocation.getY();
            }
            
            toDrawer.setMinSize(lnWidth, lnHeight);
            toDrawer.setPreferredSize(new Dimension(Math.min(loDrawerSize.getWidth(), lnWidth), 
                             Math.min(loDrawerSize.getHeight(), lnHeight)));
            toDrawer.setLocation(lnLeft, lnTop);

        }
    }
    
    /****
     * Start of EventHandlers for the drawer
     ****/
    private void onVisibleChanged(Event<IControl> toEvent)
    {
        List<Drawer> loDrawers = getDrawersFor(toEvent.getTarget());
        for (Drawer loDrawer : loDrawers)
        {
            loDrawer.setVisible(toEvent.getTarget().isVisible() && !toEvent.getTarget().isInvalidated());
            updateSizeAndLocation(toEvent.getTarget(), loDrawer);
        }
    }
    
    private void onMinimised(Event<IControl> toEvent)
    {
        List<Drawer> loDrawers = getDrawersFor(toEvent.getTarget());
        for (Drawer loDrawer : loDrawers)
        {
            loDrawer.minimise();
        }
    }
    
    private void onRestored(Event<IControl> toEvent)
    {
        List<Drawer> loDrawers = getDrawersFor(toEvent.getTarget());
        for (Drawer loDrawer : loDrawers)
        {
            loDrawer.restore();
            updateSizeAndLocation(toEvent.getTarget(), loDrawer);
        }
    }
    
    private void onSizeChanged(Event<IControl> toEvent)
    {
        List<Drawer> loDrawers = getDrawersFor(toEvent.getTarget());
        for (Drawer loDrawer : loDrawers)
        {
            updateSizeAndLocation(toEvent.getTarget(), loDrawer);
        }
    }
    
    private void onPositionChanged(Event<IControl> toEvent)
    {
        List<Drawer> loDrawers = getDrawersFor(toEvent.getTarget());
        for (Drawer loDrawer : loDrawers)
        {
            updateSizeAndLocation(toEvent.getTarget(), loDrawer);
        }
    }
    
    private void onClosing(Event<IControl> toEvent)
    {
        List<Drawer> loDrawers = getDrawersFor(toEvent.getTarget());
        for (Drawer loDrawer : loDrawers)
        {
            loDrawer.close();
        }
    }
    
    private void onOpened(Event<IControl> toEvent)
    {
        List<Drawer> loDrawers = getDrawersFor(toEvent.getTarget());
        for (Drawer loDrawer : loDrawers)
        {
            loDrawer.setVisible(toEvent.getTarget().isVisible());
            updateSizeAndLocation(toEvent.getTarget(), loDrawer);
        }
    }
    
    private void onActivated(Event<IControl> toEvent)
    {
        List<Drawer> loDrawers = getDrawersFor(toEvent.getTarget());
        for (Drawer loDrawer : loDrawers)
        {
            loDrawer.sendToFront();
        }
    }
    
    
    /****
     * End of EventHandlers for the drawer
     ****/
    
    

}
