/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls.Implementations;

import Goliath.Collections.List;
import Goliath.Graphics.Dimension;
import Goliath.Interfaces.UI.Controls.IControl;

/**
 *
 * @author kmchugh
 */
public interface IContainerImpl 
        extends IControlImpl
{
    /**
     * Adds the specifed control to this container at the index specified
     * @param tnIndex the index to add the control at
     * @param toControl the control to add
     * @param toControlBase the control that we are adding to
     * @return true if the child collection in toControlBase was changed as a result of this call
     */
    boolean addControl(int tnIndex, IControl toControl, IImplementedControl toControlBase);
    
    /**
     * Removes the specified control from the container
     * @param toControl the control to remove
     * @param toControlBase the container to remove from
     * @return true if the child collection in toControlBase was changed as a result of this call
     */
    boolean removeControl(IControl toControl, IImplementedControl toControlBase);
    
    List<IControl>getChildren(IImplementedControl toControlBase);
    void clearChildren(IImplementedControl toControlBase);

    IControl getChildControlByName(String tcName, IImplementedControl toControlBase);

    Dimension getClientSize(IImplementedControl toControlBase);
    Dimension getPreferredClientSize(IImplementedControl toControlBase);

    /**
     * Forces the implemented controls layoutmanager to do it's layout
     * @param toControlBase
     */
    void doLayout(IImplementedControl toControlBase);

    int indexOf(IControl toControl, IImplementedControl toControlBase);

}
