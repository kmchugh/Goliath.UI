/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls;

import Goliath.Collections.List;
import Goliath.Graphics.Dimension;
import Goliath.Interfaces.UI.Controls.Layouts.ILayoutManager;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.ControlPropertySet;
import Goliath.UI.Controls.ControlBounds;
import java.util.Comparator;

/**
 *
 * @author kmchugh
 */
public interface IContainer
        extends IControl
{
    /**
     * Adds a control as a child of this control
     * @param toControl the control to add
     * @return true if the child control collection was changed as a result of this call
     */
    boolean addControl(IControl toControl);

    /**
     * Adds a control using the specified layout properties, this can be used for setting multiple
     * layout parameters at once
     * @param toControl the control to add
     * @param toLayoutParameters the layout parameters 
     * @return true if the child control collection was changed as a result of this call
     */
    boolean addControl(IControl toControl, ControlPropertySet toLayoutParameters);
    
    
    /**
     * Adds a control specifying a single layout property
     * @param toControl the control to add
     * @param toLayoutProperty the layout property
     * @param toLayoutValue the value of the layout property
     * @return true if the child control collection was changed as a result of this call
     */
    boolean addControl(IControl toControl, ControlLayoutProperty toLayoutProperty, Object toLayoutValue);

    /**
     * Adds a control to a specific index of this control
     * @param tnIndex the index of the control
     * @param toControl the control to add
     * @return true if the child control collection was changed as a result of this call
     */
    boolean addControl(int tnIndex, IControl toControl);

    /**
     * Adds a control at the specified index using the specified layout properties, 
     * this can be used for setting multiple properties.
     * @param tnIndex the index to add the control at
     * @param toControl the control to add
     * @param toLayoutParameters the layout parameters 
     * @return true if the child control collection was changed as a result of this call
     */
    boolean addControl(int tnIndex, IControl toControl, ControlPropertySet toLayoutParameters);

    /**
     * Adds a control at the specified index also specifying a single layout property
     * @param tnIndex the index to add the control at
     * @param toControl the control to add
     * @param toLayoutProperty the property 
     * @param toLayoutValue the value of the layout property
     * @return true if the child control collection was changed as a result of this call
     */
    boolean addControl(int tnIndex, IControl toControl, ControlLayoutProperty toLayoutProperty, Object toLayoutValue);

    /**
     * Removes the specified control from this controls contents
     * @param toControl the control to add
     * @return true if the control was removed due to this call
     */
    boolean removeControl(IControl toControl);
    
    /**
     * Removes the control at the specified index
     * @param tnIndex the index to remove the control at
     * @return true if the control was removed due to this call
     */
    boolean removeControl(int tnIndex);
    
    /**
     * Sorts the children in this control according to the rules of the comparator
     * @param toComparator the comparator to use to sort the children
     * @return true if the child list was sorted without error
     */
    boolean sortChildren(Comparator<IControl> toComparator);
    
    /**
     * Suspends the layout of this container
     */
    void suspendLayout();
    
    /**
     * Resumes the layout of this container
     */
    void resumeLayout();
    
    /**
     * Checks if the layout of this container is suspended
     * @return true if this layout is suspended
     */
    boolean isLayoutSuspended();
    
    /**
     * Gets the layout manager for this control
     * @return the layout manager
     */
    ILayoutManager getLayoutManager();
    
    /**
     * Sets the layout manager for this control
     * @param toLayoutManager the class of the layout manager
     * @return the layout manager that was added to the control
     */
    <K extends ILayoutManager> K setLayoutManager(Class<K> toManagerClass);
    
    int indexOf(IControl toControl);
    
    void setTextRotation(float tnDegrees);
    float getTextRotation();
    

    

    List<IControl>getChildren();
    void clearChildren();

    
    IControl getChildControlByName(String tcName);

}
