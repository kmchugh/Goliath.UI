/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls.Implementations;

import Goliath.Collections.List;
import Goliath.Collections.Tree;

/**
 *
 * @author kmchugh
 */
public interface ITreeImpl<T>
{
    void setShowRoot(boolean tlShowRoot, IImplementedControl toControl);
    boolean getShowRoot(IImplementedControl toControl);
    void rootUpdated(IImplementedControl toControl);

    Tree<T> getSelectedTree(IImplementedControl toControl);
}
