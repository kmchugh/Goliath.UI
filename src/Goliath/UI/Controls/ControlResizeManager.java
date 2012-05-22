/* ========================================================
 * WindowsResizeManager.java
 *
 * Author:      christinedorothy
 * Created:     Jun 20, 2011, 10:35:03 AM
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

import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Graphics.Rectangle;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.UIEvent;
        
/**
 * Allows arbitrary controls to be resized
 *
 * @version     1.0 Jun 20, 2011
 * @author      christinedorothy
**/
public class ControlResizeManager extends ControlManager
{
    private static ControlResizeManager g_oControlResizeManager;

    /**
     * Gets the singleton instance of the control resize manager
     */
    public static ControlResizeManager getInstance()
    {
        if (g_oControlResizeManager == null)
        {
            g_oControlResizeManager = new ControlResizeManager();
        }
        return g_oControlResizeManager;
    }

    private boolean m_lResizeIsTriggered;
    private Dimension m_oTriggerAreaSize;
    private Point m_oStart;
    
    /**
     * Creates a new instance of the control resize manager, this is not
     * available to be created outside this class
     */
    private ControlResizeManager()
    {
        
    }

    /**
     * Helper function to add the event listeners to the handle
     * Implements abstract method from ControlManager
     * @param toControl
     */
    @Override
    protected void addListeners(IControl toControl)
    {
        toControl.addEventListener(UIEventType.ONMOUSEDOWN(), getDelegate("MouseDown"));
        toControl.addEventListener(UIEventType.ONMOUSEUP(), getDelegate("MouseUp"));
    }

    /**
     * Helper function to remove the event listeners from the handle
     * Implements abstract method from ControlManager
     * @param toControl
     */
    @Override
    protected void removeListeners(IControl toControl)
    {
        toControl.removeEventListener(UIEventType.ONMOUSEDOWN(), getDelegate("MouseDown"));
        toControl.removeEventListener(UIEventType.ONMOUSEUP(), getDelegate("MouseUp"));
    }

    /**
     * Override onMouseUp from ControlManager
     * @param toEvent the event object
     */
    protected void onMouseUp(UIEvent toEvent) 
    {
        if (m_lResizeIsTriggered)
        {
            Point loPoint = toEvent.getScreenLocation();
            
            for (IControl loControl : getActionControls())
            {
                Dimension loCurrentDimension = loControl.getSize();
                Dimension loNewDimension = new Dimension(loCurrentDimension.getWidth() + loPoint.getX() - m_oStart.getX(),
                                                         loCurrentDimension.getHeight() + loPoint.getY() - m_oStart.getY());
                loControl.setSize(loNewDimension);
            }

            m_lResizeIsTriggered = false;
            m_oStart = null;
            ControlDragManager.getInstance().resume(getActionControls());
        }
    }

    /**
     * Override onMouseDown from ControlManager
     * @param toEvent the event object
     */
    protected void onMouseDown(UIEvent toEvent) 
    {
        if (isControlHandle(toEvent.getTarget()))
        {
            setActionControls(getRegisteredControls(toEvent.getTarget()));
            Point loRelativePoint = toEvent.getLocation();
            Dimension loControlSize = toEvent.getTarget().getSize();

            Rectangle loRect = new Rectangle(
                    new Point(
                        loControlSize.getWidth() - getTriggerAreaSize().getWidth(),
                        loControlSize.getHeight() - getTriggerAreaSize().getHeight()),
                    getTriggerAreaSize());

            if (Rectangle.hitTest(loRect, loRelativePoint))
            {
                m_oStart = toEvent.getScreenLocation();
                m_lResizeIsTriggered = true;

                ControlDragManager.getInstance().suspend(getActionControls());
            }
        }
    }

    /**
     * Sets the trigger area size
     * @param toTriggerAreaSize The area size in which if clicked, resize will be triggered.
     */
    public void setTriggerAreaSize(Dimension toTriggerAreaSize)
    {
        m_oTriggerAreaSize = toTriggerAreaSize;
    }

    /**
     * Gets the trigger area size. By default it will be 24 x 24 pixels.
     * @return the area size in which if clicked, resize will be triggered.
     */
    public Dimension getTriggerAreaSize()
    {
        if (m_oTriggerAreaSize == null)
        {
            m_oTriggerAreaSize = new Dimension(24, 24);
        }
        return m_oTriggerAreaSize;
    }
}