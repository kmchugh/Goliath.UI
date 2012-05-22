/* =========================================================
 * ILayoutManager.java
 *
 * Author:      kmchugh
 * Created:     24-Jun-2008, 15:50:18
 * 
 * Description
 * --------------------------------------------------------
 * General Interface Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces.UI.Controls.Layouts;

import Goliath.Graphics.Dimension;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.UI.Controls.ControlBounds;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 24-Jun-2008
 * @author      kmchugh
**/
public interface ILayoutManager<T extends IContainer>
{
    /**
     * Gets the full bounds of this control based on the suggested size
     * @return the control bounds
     */
    ControlBounds calculateControlBounds(T toContainer);

    /**
     * recalculate the layout
     * @param toContainer 
     */
    void update(T toContainer);

    /**
     * Checks if this layout manager is suspended.  The layout manager can be suspended without the
     * control layout being suspended
     * @return true if the layout manager is suspended
     */
    boolean isLayoutSuspended();

    /**
     * Suspends layout of this layout manager, calling this when layout is suspended will have no effect
     */
    void suspendLayout();

    /**
     * Resumes layout of this layout manager, calling this when layout is not suspeneded will have no effect
     */
    void resumeLayout();
    
    /**
     * Checks if the container is suspended from layout out it's controls
     * @param toContainer the container to check
     * @return true is suspended, false otherwise
     */
    boolean isLayoutSuspended(T toContainer);
    
    /**
     * Suspends the layout of this control
     * @param toContainer  suspends the layout of this control
     */
    void suspendLayout(T toContainer);
    
    /**
     * resumes the layout of this control
     * @param toContainer resumes the layout of this control
     */
    void resumeLayout(T toContainer);
    
    /**
     * Checks if this layout manager manages the control specified
     * @param toContainer the container to check
     */
    boolean manages(T toContainer);
    
    /**
     * Adds the specified control to the layout manager
     * @param toContainer the container to add to the layout manager
     * @return true if this control collection was changed
     */
    boolean addControl(T toContainer, ControlBounds toBounds);
    
    /**
     * Removes the specified control from the layout manager
     * @param toContainer the control to remove
     * @return true if the control collection was updated as a result of this call
     */
    boolean removeControl(T toContainer);

    /**
     * Invalidates the layout manager and clears any cache values
     */
    void invalidate(T toContainer);

    /**
     * Gets the bounds for the control that is being layed out
     * @return the bounds for the control being layed out
     */
    ControlBounds getControlBounds(T toContainer);

    /**
     * Sets a comparator for the layout manager
     */
    void setComparator(java.util.Comparator<Goliath.Interfaces.UI.Controls.IControl> toComparator);
}
