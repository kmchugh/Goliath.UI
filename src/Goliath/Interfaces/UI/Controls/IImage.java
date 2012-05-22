/* ========================================================
 * IImage.java
 *
 * Author:      kmchugh
 * Created:     Aug 3, 2010, 5:29:20 PM
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



/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 3, 2010
 * @author      kmchugh
**/
public interface IImage
        extends IControl
{
    /**
     * Gets the text used for screenreaders and other accessibility devices
     * @return the text
     */
    String getAlternateText();

    /**
     * Sets the alt text for the image
     * @param tcAltText the alternate text that is used by accessibility devices
     */
    void setAlternateText(String tcAltText);

    /**
     * Gets the image that is being displayed by this control
     * @return the image
     */
    Goliath.Graphics.Image getImage();
}
