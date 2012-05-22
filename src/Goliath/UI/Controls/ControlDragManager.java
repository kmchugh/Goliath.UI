/* ========================================================
 * ControlDragManager.java
 *
 * Author:      kmchugh
 * Created:     Apr 14, 2011, 5:53:07 PM
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

package Goliath.UI.Controls;

import Goliath.Collections.List;
import Goliath.Graphics.Point;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.UIEvent;


        
/**
 * Allows arbitrary controls to be dragged an moved around
 *
 * @see         Related Class
 * @version     1.0 Apr 14, 2011
 * @author      kmchugh
**/
public class ControlDragManager extends ControlManager
{
    private static ControlDragManager g_oControlDragManager;
    /**
     * Gets the singleton instance of the control drag manager
     */
    public static ControlDragManager getInstance()
    {
        if (g_oControlDragManager == null)
        {
            g_oControlDragManager = new ControlDragManager();
        }
        return g_oControlDragManager;
    }

    private Point m_oStart;
    private boolean m_lLocked;
    
    
    /**
     * Creates a new instance of the control drag manager, this is not 
     * publicly creatable
     */
    private ControlDragManager()
    {
        resumeManager();
    }
    
    /**
     * Gets the handles that are used to drag this control
     * @param toRegisteredControl the control to get the handles for
     * @return the list of controls that are used as drag handles for the specified control
     */
    public final List<IControl>getDragHandles(IControl toRegisteredControl)
    {
        return getHandles(toRegisteredControl);
    }
    
     
    /**
     * Checks if the specified control is draggable
     * @param toControl the control to check
     * @return true if this control is draggable, false otherwise
     */
    public final boolean isDraggable(List<IControl> toControl)
    {
        return isRegistered(toControl) && !isSuspended(toControl) && !isManagerSuspended();
    }

    
    /**
     * Checks if the control is a drag handle for another control
     * @param toHandle the control to check
     * @return true if this control is a drag handle
     */
    public final boolean isDragHandle(IControl toHandle)
    {
        return isControlHandle(toHandle);
    }
    
    /**
     * Helper function to remove the event listeners from the handle
     * @param toHandle the handle to remove the listeners from
     */
    @Override
    protected void removeListeners(IControl toHandle)
    {
        toHandle.removeEventListener(UIEventType.ONMOUSEDOWN(), getDelegate("MouseDown"));
        toHandle.removeEventListener(UIEventType.ONMOUSEDRAGGED(), getDelegate("MouseMove"));
    }
    
    /**
     * Helper function to add the event listeners to the handle
     * @param toHandle the handle to all to
     */
    @Override
    protected void addListeners(IControl toHandle)
    {
        toHandle.addEventListener(UIEventType.ONMOUSEDOWN(), getDelegate("MouseDown"));
        toHandle.addEventListener(UIEventType.ONMOUSEDRAGGED(), getDelegate("MouseMove"));
    }
    
    
        
    /**
     * Event handler for the mouse down on a handle
     * @param toEvent the event object
     */
    protected void onMouseDown(UIEvent toEvent)
    {
        setActionControls(getRegisteredControls(toEvent.getComponent()));
        if (isDraggable(getActionControls()))
        {
            m_oStart = toEvent.getScreenLocation();
            if (m_oStart != null)
            {
                toEvent.cancelBubble();
            }
        }
    }

    /**
     * Event handler for the mouse move event
     * @param toEvent the mouse move event
     */
    protected void onMouseMove(UIEvent toEvent)
    {
        
        // This only happens if there is a start, if the mouse 1 button is down, and if the target is a handle
        if (m_oStart != null && toEvent.isMouse1Down())
        {
            if (!m_lLocked)
            {
                m_lLocked = true;
                
                Point loCurrentScreen = toEvent.getScreenLocation();
                Point loOffset = Point.subtract(loCurrentScreen, m_oStart);
                m_oStart = loCurrentScreen;
                
                // Get the list of controls for this drag handle
                for (IControl loControl : getActionControls())
                {
                    loControl.setLocation(Point.add(loControl.getLocation(), loOffset));
                }
                m_lLocked = false;
            }
        }
    }

    @Override
    protected void onControlSuspended(IControl toControl)
    {
        if (getActionControls() != null && getActionControls().contains(toControl))
        {
            clearActionControls();
            m_oStart = null;
        }
    }
    
    
}
