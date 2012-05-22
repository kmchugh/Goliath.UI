/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls;


/**
 *
 * @author kmchugh
 */
public interface ILabel 
        extends IControl
{
    String getText();
    void setText(String tcText);

    /**
     * Sets the image that is displayed by this control
     * @param toImage the image to display
     */
    void setImage(Goliath.Graphics.Image toImage);

    /**
     * Gets the image that is to be used for this control
     * @return the image being used for the control
     */
    Goliath.Graphics.Image getImage();


}
