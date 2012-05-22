/* =========================================================
 * IImageControlImpl.java
 *
 * Author:      kenmchugh
 * Created:     Nov 1, 2010, 12:45:04 PM
 *
 * Description
 * --------------------------------------------------------
 * <Description>
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Interfaces.UI.Controls.Implementations;

/**
 *
 * @author kenmchugh
 */
public interface IImageControlImpl
    extends IControlImpl
{
    /**
     * Gets the text used for screenreaders and other accessibility devices
     * @return the text
     */
    String getAlternateText(IImplementedControl toControl);

    /**
     * Sets the alt text for the image
     * @param tcAltText the alternate text that is used by accessibility devices
     */
    void setAlternateText(String tcAltText, IImplementedControl toControl);

    /**
     * Gets the source of this image
     * @return the image source
     */
    Goliath.Graphics.Image getSource(IImplementedControl toControl);

    /**
     * Sets the source of this image
     * @param tcSource the source
     * @param toControl the control to set the image source for
     */
    void setSource(Goliath.Graphics.Image toSource, IImplementedControl toControl);
}