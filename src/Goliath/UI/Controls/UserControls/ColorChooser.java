/* ========================================================
 * ColourChooser.java
 *
 * Author:      christinedorothy
 * Created:     Aug 2, 2011, 10:53:45 AM
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

import Goliath.Delegate;
import Goliath.Event;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Controls.Button;
import Goliath.UI.Controls.Group;
import Goliath.UI.Controls.Layouts.BorderLayoutConstants;
import Goliath.UI.Controls.Layouts.BorderLayoutManager;
import Goliath.UI.Controls.Textbox;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JColorChooser;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 2, 2011
 * @author      christinedorothy
**/
public class ColorChooser extends Group implements IValueControl<Color>
{
    private Textbox m_oColorHex;

    /**
     * Creates a new instance of ColourChooser
     */
    public ColorChooser()
    {
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        if (m_oColorHex == null)
        {
            m_oColorHex = new Textbox();
            m_oColorHex.setProperty(ControlLayoutProperty.LAYOUTCONSTRAINT().getValue(), BorderLayoutConstants.CENTER());
        }

        setLayoutManager(BorderLayoutManager.class);
        addControl(m_oColorHex);

        Button loButton = new Button("...");
        loButton.setProperty(ControlLayoutProperty.LAYOUTCONSTRAINT().getValue(), BorderLayoutConstants.LINE_END());
        addControl(loButton);

        loButton.addEventListener(UIEventType.ONCLICK(), Delegate.build(this, "onShowChooser"));
    }

    private void onShowChooser(Event<IControl> toEvent)
    {
        Color loSelectedColor = JColorChooser.showDialog((Component) getControl(), "Choose a Color", getValue() == null ? Color.YELLOW : getValue());
        if (loSelectedColor != null)
        {
            setValue(loSelectedColor);
        }
    }

    @Override
    public Color getValue()
    {
        return (Goliath.Utilities.isNullOrEmpty(m_oColorHex.getValue()) ? null : Color.decode("#"+m_oColorHex.getValue()));
    }

    @Override
    public void setValue(Color toValue)
    {
        String loValue = "";
        if (toValue != null)
        {
            loValue = Integer.toHexString(toValue.getRGB());
            loValue = loValue.substring(2, loValue.length()).toUpperCase();
        }
        m_oColorHex.setValue(loValue);
    }
}
