/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.UI.Controls.UserControls;

import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.UI.Controls.Group;
import java.awt.Color;

/**
 *
 * @author reshusinghania
 */
public class InvalidFieldControl extends Group
{
    private IValueControl m_oValueControl;


    /**
     * Creates a new instance of InvalidFieldControl
     * @param toValueControl the value control that is invalid
     */
    public InvalidFieldControl()
    {
        initialiseComponent();
    }

    /**
     * Wraps the specified control in this invalid field control.  This will replace toValue with this control
     * and add toValueControl as contents of this control
     * @param toValueControl the value control to replace
     * @return true if the replacement was successful
     */
    public boolean wrapControl(IValueControl toValueControl)
    {
        Goliath.Utilities.checkParameterNotNull("toValueControl", toValueControl);
        m_oValueControl = toValueControl;

        // Replace the value control with the invalid field control
        boolean llReturn = replaceControl(toValueControl, this);
        
        // Add the value control to the invalid field control
        this.addControl(toValueControl);

        return llReturn;
    }

    public IValueControl getValueControl()
    {
        return m_oValueControl;
    }

    private void initialiseComponent()
    {
        //this.setOpaque(true);
        this.setBackground(Color.red);
    }

    // TODO: refactor this into the control utilities


    /**
     * Replaces toDestination with toFieldControl if possible.
     * @param toDestination the control to replace
     * @param toFieldControl the replacement control
     * @return true if the control was replaced, false otherwise
     */
    private boolean replaceControl(IValueControl toDestination, InvalidFieldControl toFieldControl)
    {
        IContainer loParent = toDestination.getParent();
        if (loParent != null)
        {
            int lnIndex = loParent.indexOf(toDestination);
            loParent.removeControl(toDestination);
            loParent.addControl(lnIndex, toFieldControl);
            loParent.update();
            return true;
        }
        return false;
    }
}


