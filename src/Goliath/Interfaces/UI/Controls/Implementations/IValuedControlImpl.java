/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls.Implementations;

/**
 *
 * @author kmchugh
 */
public interface IValuedControlImpl<T extends Object>
        extends IControlImpl
{
    T getValue(IImplementedControl toControl);
    void setValue(T toValue, IImplementedControl toControl);
}
