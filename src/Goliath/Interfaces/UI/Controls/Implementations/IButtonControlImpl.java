/* ========================================================
 * IButtonControlImpl.java
 *
 * Author:      kmchugh
 * Created:     Feb 18, 2011, 11:48:17 AM
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


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Feb 18, 2011
 * @author      kmchugh
**/
public interface IButtonControlImpl extends ILabelledControlImpl
{

    /**
     * Checks if the border will be painted for this button
     * @return true to paint the borders
     */
    boolean isBorderPainted(IImplementedControl toControl);

    /**
     * Sets if the borders will be painted for this control
     * @param tlPaintBorder to paint or not to paint, that is the question
     */
    void setBorderPainted(boolean tlPaintBorder, IImplementedControl toControl);
}