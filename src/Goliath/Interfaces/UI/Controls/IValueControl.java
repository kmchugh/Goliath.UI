/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls;

/**
 *
 * @author kmchugh
 */
public interface IValueControl<T extends Object>
        extends IControl
{
    T getValue();
    void setValue(T toValue);
}
