/* ========================================================
 * DimensionTextbox.java
 *
 * Author:      christinedorothy
 * Created:     Aug 2, 2011, 10:52:45 AM
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

import Goliath.Graphics.Dimension;
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.UI.Controls.Group;
import Goliath.UI.Controls.Label;
import Goliath.UI.Controls.Layouts.FlowLayoutManager;
import Goliath.UI.Controls.Textbox;


        
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
public class DimensionTextbox extends Group implements IValueControl<Dimension>
{
    private Textbox m_oWidth;
    private Textbox m_oHeight;

    /**
     * Creates a new instance of DimensionTextbox
     */
    public DimensionTextbox()
    {
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        if (m_oWidth == null)
        {
            m_oWidth = new Textbox();
        }
        if (m_oHeight == null)
        {
            m_oHeight = new Textbox();
        }

        setLayoutManager(FlowLayoutManager.class);
        addControl(new Label("W"));
        addControl(m_oWidth);
        addControl(new Label("H"));
        addControl(m_oHeight);
    }

    @Override
    public Dimension getValue()
    {
        if (Goliath.Utilities.isNullOrEmpty(m_oWidth.getValue()) || Goliath.Utilities.isNullOrEmpty(m_oWidth.getValue()))
        {
            return null;
        }
        return new Dimension(Float.valueOf(m_oWidth.getValue()), Float.valueOf(m_oHeight.getValue()));
    }

    @Override
    public void setValue(Dimension toValue)
    {
        if (toValue == null)
        {
            m_oWidth.setValue("");
            m_oHeight.setValue("");
        }
        else
        {
            m_oWidth.setValue(String.valueOf(toValue.getWidth()));
            m_oHeight.setValue(String.valueOf(toValue.getHeight()));
        }
        
    }

}
