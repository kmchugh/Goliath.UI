/* ========================================================
 * IScreen.java
 *
 * Author:      kmchugh
 * Created:     Aug 2, 2010, 4:07:34 PM
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

package Goliath.Interfaces.UI.Controls;

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
public interface IScreen
{
    /**
     * Gets the number of screens available
     * @return the number of screens available to the system
     */
    int getCount();

    /**
     * Gets the size of the specified screen
     * @param tnScreen the index of the screen to get the size of
     * @return the size of the specified screen
     */
    Dimension getSize(int tnScreen);

    /**
     * Gets the pixel depth for the primary monitor
     * @return the pixel depth
     */
    long getPrimaryBitDepth();

    /**
     * gets the pixel depth for the specified monitor
     * @param tcScreen the index of the screen to get the depth for
     * @return the pixel depth
     */
    long getBitDepth(int tnScreen);

    Dimension getClientSize();

    Dimension getClientSize(int tnScreen);

    Point getLocation();

    Point getLocation(int tnScreen);
}
