/* =========================================================
 * Textbox.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * A Textbox represents a control that allows the user to enter
 * text
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.UI.Controls;

import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.Interfaces.UI.Controls.Implementations.IValuedControlImpl;

/**
 *
 * @author kmchugh
 */
public class Textbox extends Control
    implements IValueControl<String>
{
    // TODO: Implement Action command, links an event to the enter key being pressed when this control has focus
    // TODO: Implement Cancel command, links an event to the escape key being pressed when this control has focus

    protected IValuedControlImpl<String> getValuedControlImplementation()
    {
        return (IValuedControlImpl<String>)getImplementation();
    }

    public Textbox()
    {
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        setMinSize(60, 25);
    }

    @Override
    public String getValue()
    {
        return getValuedControlImplementation().getValue(getControl());
    }

    @Override
    public void setValue(String tcValue)
    {
        getValuedControlImplementation().setValue(tcValue, getControl());
    }
}