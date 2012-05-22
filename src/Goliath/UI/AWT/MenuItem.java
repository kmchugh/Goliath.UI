/* ========================================================
 * MenuItem.java
 *
 * Author:      kmchugh
 * Created:     Aug 18, 2010, 5:36:49 PM
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

package Goliath.UI.AWT;

import Goliath.Graphics.Dimension;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.Implementations.IImplementedControl;
import java.awt.Graphics2D;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 18, 2010
 * @author      kmchugh
**/
public class MenuItem extends java.awt.MenuItem
        implements IImplementedControl
{
    private IControl m_oControlBase;

    public MenuItem(IControl toControlBase)
    {
        super();
        m_oControlBase = toControlBase;
    }

    @Override
    public IControl getControlBase()
    {
        return m_oControlBase;
    }

    @Override
    public <T> T getProperty(String tcProperty)
    {
        return m_oControlBase.<T>getProperty(tcProperty);
    }

    @Override
    public <T> T getProperty(String tcProperty, T toDefault)
    {
        return m_oControlBase.getProperty(tcProperty, toDefault);
    }

    @Override
    public <T> T setProperty(String tcProperty, T toValue)
    {
        return m_oControlBase.setProperty(tcProperty, toValue);
    }

    @Override
    public Dimension getImplementedSize()
    {
        return new Dimension(0, 0);
    }

    @Override
    public Goliath.Graphics.Point getImplementedLocation()
    {
        return new Goliath.Graphics.Point(0, 0);
    }

    @Override
    public void paint(Graphics2D toGraphics)
    {
        
    }
    
    


}