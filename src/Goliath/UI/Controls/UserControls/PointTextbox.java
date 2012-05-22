/* ========================================================
 * LocationTextbox.java
 *
 * Author:      christinedorothy
 * Created:     Aug 2, 2011, 11:08:55 AM
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

import Goliath.Graphics.Point;
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
public class PointTextbox extends Group implements IValueControl<Point>
{
    private Textbox m_oX;
    private Textbox m_oY;

    /**
     * Creates a new instance of LocationTextbox
     */
    public PointTextbox()
    {
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        if (m_oX == null)
        {
            m_oX = new Textbox();
        }
        if (m_oY == null)
        {
            m_oY = new Textbox();
        }

        setLayoutManager(FlowLayoutManager.class);
        addControl(new Label("X"));
        addControl(m_oX);
        addControl(new Label("Y"));
        addControl(m_oY);
    }

    @Override
    public Point getValue()
    {
        if (Goliath.Utilities.isNullOrEmpty(m_oX.getValue()) || Goliath.Utilities.isNullOrEmpty(m_oY.getValue()))
        {
            return null;
        }
        return new Point(Float.valueOf(m_oX.getValue()), Float.valueOf(m_oY.getValue()));
    }

    @Override
    public void setValue(Point toValue)
    {
        if (toValue == null)
        {
            m_oX.setValue("");
            m_oY.setValue("");
        }
        else
        {
            m_oX.setValue(String.valueOf(toValue.getX()));
            m_oY.setValue(String.valueOf(toValue.getY()));
        }
    }
}
