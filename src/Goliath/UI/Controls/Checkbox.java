/* =========================================================
 * Checkbox.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * A checkbox respresents a yes/no type option to present
 * to the user.  The checkbox includes an area to "check"
 * as well as a label to display information to the user.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.UI.Controls;

import Goliath.Interfaces.UI.Controls.Implementations.ILabelledControlImpl;
import Goliath.Interfaces.UI.Controls.Implementations.IValuedControlImpl;

/**
 * Basic checkbox control.  Checked value can be retrieved by either
 * checking value, or checking the state.
 * Value == FALSE = State == 0
 * Value == TRUE  = State == 1
 * Value == NULL  = State == 2 (only if allow nulls is specified in the constructor)
 * 
 * @author kmchugh
 */
public class Checkbox extends ToggleControl<Boolean>
{
    private boolean m_lAllowNulls;

    /**
     * Helper function to get the control implementation
     * @return the control implementation
     */
    protected IValuedControlImpl<Boolean> getValuedControlImplementation()
    {
        return (IValuedControlImpl<Boolean>)getImplementation();
    }

    /**
     * Helper function to get the control implementation
     * @return the control implementation
     */
    protected ILabelledControlImpl getLabelControlImplementation()
    {
        return (ILabelledControlImpl)getImplementation();
    }

    /**
     * Creates an instance of checkbox
     */
    public Checkbox()
    {
        super(2);
    }

    /**
     * Creates an instance of CheckBox
     * @param tcText the label text
     */
    public Checkbox(String tcText)
    {
        super(2);
        setText(tcText);
    }
    
    /**
     * Creates an instance of CheckBox, allowing up to 3 states, true, false, and null
     * @param tcText the text for the checkbox label
     * @param tlAllowNull if true, three states are allowed for this checkbox
     */
    public Checkbox(String tcText, boolean tlAllowNulls)
    {
        super(3);
        m_lAllowNulls = tlAllowNulls;
        setText(tcText);
    }
    
    public boolean allowsNulls()
    {
        return m_lAllowNulls;
    }

    /**
     * Gets the value of the checkbox, if it is checked or unchecked
     * @return true if checked, false if not
     */
    @Override
    public Boolean getValue()
    {
        return getState() == 1;
    }

    /**
     * Sets the value of the checkbox
     * @param tcValue true will check, false will uncheck
     */
    @Override
    public void setValue(Boolean tcValue)
    {
        // Make sure the value is correct
        if (tcValue == null && !m_lAllowNulls)
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