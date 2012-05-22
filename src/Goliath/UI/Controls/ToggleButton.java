/* ========================================================
 * ToggleButton.java
 *
 * Author:      kmchugh
 * Created:     Dec 1, 2010, 3:22:38 PM
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

import Goliath.Interfaces.UI.Controls.IButton;


        
/**
 * A Toggle Button is a control that has an on and off state
 *
 * @see         Related Class
 * @version     1.0 Dec 1, 2010
 * @author      kmchugh
**/
public class ToggleButton extends ToggleControl<Boolean>
    implements IButton
{
    private boolean m_lBorderPainted;
    
    /**
     * Creates a new instance of ToggleButton
     */
    public ToggleButton(String tcLabel)
    {
        super(2, tcLabel);
    }
    
    /**
     * Creates a new instance of toggle button
     */
    public ToggleButton()
    {
        super(2);
    }

    /**
     * Checks if this button is on or off
     * @return true if on, false if off
     */
    public boolean isOn()
    {
        return getState() == 1;
    }

    @Override
    public boolean isBorderPainted()
    {
        return m_lBorderPainted;
    }

    @Override
    public void setBorderPainted(boolean tlPaintBorder)
    {
        m_lBorderPainted = tlPaintBorder;
    }
    
    /**
     * Gets the value of the toggle button, if it is on or off
     * @return true if on, false if off
     */
    @Override
    public Boolean getValue()
    {
        return getState() == 1;
    }

    /**
     * Sets the value of the toggle button
     * @param tcValue true will turn on, false will turn off
     */
    @Override
    public void setValue(Boolean tcValue)
    {
        // Make sure the value is correct
        if (tcValue == null)
        {
            tcValue = Boolean.FALSE;
        }
        
        // Set the state to make sure we are inline
        setState(tcValue == Boolean.TRUE ? 1 :
                tcValue == Boolean.FALSE ? 0 : 2);
        
        // Finally update the value
        super.setValue(tcValue);
    }
    
    
}
