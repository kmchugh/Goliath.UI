/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls;

/**
 *
 * @author kmchugh
 */
public interface IButton
        extends ILabel
{
    /**
     * Checks if the border will be painted for this button
     * @return true to paint the borders
     */
    boolean isBorderPainted();

    /**
     * Sets if the borders will be painted for this control
     * @param tlPaintBorder to paint or not to paint, that is the question
     */
    void setBorderPainted(boolean tlPaintBorder);

}
