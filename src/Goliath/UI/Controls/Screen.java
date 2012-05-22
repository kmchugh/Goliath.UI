/* ========================================================
 * Screen.java
 *
 * Author:      kmchugh
 * Created:     Aug 2, 2010, 2:45:07 PM
 *
 * Description
 * --------------------------------------------------------
 * The screen control represents the area that the application
 * is allowed to occupy, for example in a web application,
 * it would be the client size of the browser, in a desktop
 * application, it would be the size of the screen space
 * available.
 *
 * This is really a helper to allow the developer to access
 * the height, width, and pixel depth of the rendering area
 *
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
import Goliath.Interfaces.UI.Controls.IScreen;
import Goliath.Interfaces.UI.Controls.Implementations.IScreenImpl;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 2, 2010
 * @author      kmchugh
**/
public class Screen extends Control
        implements IScreen
{

    private static Dimension g_oScreenSize;
    public static Dimension getScreenSize(ControlImplementationType toType)
    {
        if (g_oScreenSize == null)
        {
            Screen loScreen = new Screen(toType);
            g_oScreenSize = loScreen.getSize();
        }
        return g_oScreenSize;
    }
    
    private static Dimension g_oScreenClientSize;
    public static Dimension getScreenClientSize(ControlImplementationType toType)
    {
        if (g_oScreenClientSize == null)
        {
            Screen loScreen = new Screen(toType);
            g_oScreenClientSize = loScreen.getClientSize();
        }
        return g_oScreenClientSize;
    }

    private static Point g_oScreenLocation;
    public static Point getScreenLocation(ControlImplementationType toType)
    {
        if (g_oScreenLocation == null)
        {
            Screen loScreen = new Screen(toType);
            g_oScreenLocation = loScreen.getLocation();
        }
        return g_oScreenLocation;
    }

    protected IScreenImpl getScreenImplementation()
    {
        return (IScreenImpl)getImplementation();
    }

    public Screen()
    {
    }

    public Screen(ControlImplementationType toType)
    {
        super(toType);
    }

    @Override
    public int getCount()
    {
        return getScreenImplementation().getCount(getControl());
    }

    @Override
    public long getBitDepth(int tnScreen)
    {
        return getScreenImplementation().getBitDepth(tnScreen, getControl());
    }

    @Override
    public final long getPrimaryBitDepth()
    {
        return getBitDepth(0);
    }

    @Override
    public final Dimension getSize()
    {
        return getSize(0);
    }

    @Override
    public Dimension getSize(int tnScreen)
    {
        return getScreenImplementation().getSize(tnScreen, getControl());
    }

    public Dimension getClientSize()
    {
        return getClientSize(0);
    }

    public Dimension getClientSize(int tnScreen)
    {
        return getScreenImplementation().getClientSize(tnScreen, getControl());
    }

    @Override
    public Point getLocation()
    {
        return getLocation(0);
    }

    @Override
    public Point getLocation(int tnScreen)
    {
        return getScreenImplementation().getLocation(tnScreen, getControl());
    }
}
