/* ========================================================
 * IScreenImpl.java
 *
 * Author:      kmchugh
 * Created:     Aug 2, 2010, 4:07:48 PM
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

package Goliath.Interfaces.UI.Controls.Implementations;

import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;



/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 2, 2010
 * @author      kmchugh
**/
public interface IScreenImpl
{
    int getCount(IImplementedControl toControl);

    long getBitDepth(int tnScreen, IImplementedControl toControl);

    Dimension getSize(int tnScreen, IImplementedControl toControl);

    Dimension getClientSize(int tnScreen, IImplementedControl toControl);

    Point getLocation(int tnScreen, IImplementedControl toControl);
}
