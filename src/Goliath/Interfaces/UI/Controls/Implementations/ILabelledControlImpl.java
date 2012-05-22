/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls.Implementations;

import Goliath.Graphics.Image;

/**
 *
 * @author kmchugh
 */
public interface ILabelledControlImpl
        extends IControlImpl
{
    /**
     * Gets the text displayed by this control
     * @param toControl the control to get the text for
     * @return the text displayed
     */
    String getText(IImplementedControl toControl);

    /**
     * Sets the text displayed by this control
     * @param toValue the text for the control
     * @param toControl the control
     */
    void setText(String toValue, IImplementedControl toControl);

    /**
     * Gets the image displayed by this control
     * @param toControl the control
     * @return the image
     */
    Image getImage(IImplementedControl toControl);

    /**
     * Sets the image displayed by this control
     * @param toImage the image
     * @param toControl the control
     */
    void setImage(Image toImage, IImplementedControl toControl);

    void setTextRotation(float tnRotation, IImplementedControl toControl);

    float getTextRotation(IImplementedControl toControl);
}
