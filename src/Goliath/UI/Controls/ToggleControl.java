/* ========================================================
 * ToggleButton.java
 *
 * Author:      kenmchugh
 * Created:     Nov 26, 2010, 9:32:35 AM
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

import Goliath.Constants.EventType;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.UIEvent;
        
/**
 * A toggle control is a control that can go through a finite number of states
 *
 * @see         Related Class
 * @version     1.0 Nov 26, 2010
 * @author      kenmchugh
**/
public abstract class ToggleControl<T> extends Label
    implements IValueControl<T>
{
    private static IDelegate g_oClickDelegate;
    
    private int m_nCurrentState;
    private int m_nStateCount;
    private T m_oValue;
    

    // TODO : Implement state values
    // TODO : Implement state labels

    /**
     * Creates a new instance of ToggleButton with the maximum number of states
     */
    public ToggleControl(int tnStateCount)
    {
        m_nStateCount = tnStateCount;
        initialiseComponent();
    }

    /**
     * Creates a toggle control with the maximum number of states and the text
     * for the control
     * @param tnStateCount the count of states
     * @param tcText the string to display for this control
     */
    public ToggleControl(int tnStateCount, String tcText)
    {
        this(tnStateCount);
        setText(tcText);
    }

    /**
     * Sets the number of states for this control
     * @param tnStateCount the number of states
     */
    private void setStateCount(int tnStateCount)
    {
        if (m_nStateCount != tnStateCount && tnStateCount >= 1)
        {
            m_nStateCount = tnStateCount;
            invalidate();
        }
    }
    
    /**
     * initialises this component, sets the state to the initial state
     */
    private void initialiseComponent()
    {
        setState(0);
        
        addEventListener(UIEventType.ONCLICK(), getClickedDelegate());
    }
    
    /**
     * Helper function to get the on click default action
     * @return the on click default action
     */
    private IDelegate getClickedDelegate()
    {
        if (g_oClickDelegate == null)
        {
            g_oClickDelegate = Delegate.build(this, "onClicked");
        }
        return g_oClickDelegate;
    }
    
    /**
     * Clicking on the control will toggle its state, shift clicking will reverse
     * the toggle order
     * @param toEvent the event that occurred
     */
    private void onClicked(UIEvent toEvent)
    {
        ((ToggleControl)toEvent.getComponent()).toggleState(toEvent.isShiftDown());
        toEvent.cancelBubble();
    }

    /**
     * Sets the state to the specified state, if the state does not exist, this
     * will not have any action
     * @param tnState the state to move to
     */
    public void setState(int tnState)
    {
        // Adjust tnState to wrap from the current state.
        tnState = tnState < 0 ? m_nStateCount -1 :
                tnState >= m_nStateCount ? 0 : tnState;
        
        if (m_nCurrentState != tnState)
        {
            m_nCurrentState = tnState;
            fireEvent(EventType.ONSTATECHANGED(), new Event<ToggleControl>(this));
            invalidate();
        }
    }

    /**
     * Gets the current state
     * @return the current state
     */
    public int getState()
    {
        return m_nCurrentState;
    }

    /**
     * Toggles the state forward or backwards
     * @param tlReverse, move backwards instead of forwards
     */
    public void toggleState(boolean tlReverse)
    {
        setState(getState() + (tlReverse ? -1 : 1));
    }
    
    /**
     * Gets the value of the toggle control
     * @return the current value of the control
     */
    @Override
    public T getValue()
    {
        return m_oValue;
    }

    /**
     * Sets the value of the checkbox
     * @param tcValue true will check, false will uncheck
     */
    @Override
    public void setValue(T toValue)
    {
        if (toValue != m_oValue || (m_oValue != null && !m_oValue.equals(toValue)))
        {
            m_oValue = toValue;
            fireEvent(EventType.ONCHANGED(), new Event<ToggleControl>(this));
        }
    }
}
