/* ========================================================
 * Menu.java
 *
 * Author:      kmchugh
 * Created:     Aug 18, 2010, 11:05:57 AM
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

import Goliath.Exceptions.InvalidParameterException;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.ControlPropertySet;


        
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
public class Menu extends Container
    implements IContainer
{
    /**
     * Creates a new instance of Menu
     */
    public Menu()
    {
    }

    public Menu(ControlImplementationType toImplementationType)
    {
        super(toImplementationType);
    }

    @Override
    public boolean addControl(IControl toControl)
    {
        if (!Goliath.DynamicCode.Java.isEqualOrAssignable(MenuItem.class, toControl.getClass()))
        {
            throw new InvalidParameterException("MenuItems must be added to a Menu, a " + toControl.getClass().getName() + " can not be added.", "toControl");
        }
        return super.addControl(toControl);
    }

    @Override
    public boolean addControl(IControl toControl, ControlPropertySet toLayoutParameters)
    {
        if (!Goliath.DynamicCode.Java.isEqualOrAssignable(MenuItem.class, toControl.getClass()))
        {
            throw new InvalidParameterException("MenuItems must be added to a Menu, a " + toControl.getClass().getName() + " can not be added.", "toControl");
        }
        return super.addControl(toControl, toLayoutParameters);
    }

    @Override
    public boolean addControl(int tnIndex, IControl toControl)
    {
        if (!Goliath.DynamicCode.Java.isEqualOrAssignable(MenuItem.class, toControl.getClass()))
        {
            throw new InvalidParameterException("MenuItems must be added to a Menu, a " + toControl.getClass().getName() + " can not be added.", "toControl");
        }
        return super.addControl(tnIndex, toControl);
    }


}
