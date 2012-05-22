/* ========================================================
 * LayoutManager.java
 *
 * Author:      kmchugh
 * Created:     Aug 3, 2010, 6:29:14 PM
 *
 * Description
 * --------------------------------------------------------
 * General Class Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.UI.Controls.Layouts;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.Implementations.IContainerImpl;
import Goliath.Interfaces.UI.Controls.Layouts.ILayoutManager;
import Goliath.UI.Controls.ControlBounds;
import Goliath.Collections.List;
import Goliath.DynamicCode.Java;
import Goliath.Graphics.BoxDimension;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.UI.Controls.BorderSettings;
import java.util.Comparator;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 3, 2010
 * @author      kmchugh
**/
public abstract class LayoutManager<T extends IContainer> extends Goliath.Object
        implements ILayoutManager<T>
{
    // Look in to inheriting from Control Manager
    
    private static HashTable<Class<?extends ILayoutManager>, ILayoutManager> g_oLayoutManagers;
    
    
    /**
     * Helper function for creating the specified layout manager
     * @param <K> the type of layout manager
     * @param toManagerClass the class for the layout manager to create
     * @return the layout manager, or null if it does not exist
     */
    public static <K extends ILayoutManager> K getLayoutManager(Class<K> toManagerClass)
    {
        if (g_oLayoutManagers == null)
        {
            g_oLayoutManagers = new HashTable<Class<? extends ILayoutManager>, ILayoutManager>();
        }
        
        if (!g_oLayoutManagers.containsKey(toManagerClass))
        {
            try
            {
                ILayoutManager loManager = Java.createObject(toManagerClass, null);
                if (loManager != null)
                {
                    g_oLayoutManagers.put(toManagerClass, loManager);
                }
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);
            }
        }
        return (K)(g_oLayoutManagers.containsKey(toManagerClass) ? g_oLayoutManagers.get(toManagerClass) : null);
    }
    
    private HashTable<T, ControlBounds> m_oControls;
    private List<T> m_oSuspendedControls;
    private boolean m_lSuspended;
    
    
    private List<IControl> m_oOrderedControls;
    private Comparator m_oControlComparator;
    
    // TODO: Implement Control Padding by adding in methods for adding padding to each layed out control in the manager
    // TODO: Implement Control margins by adding in methods for calculating padding
    // TODO: Implement the ability to add a scroll bar if the controls being layed out are larger than the area allowed

    /**
     * Creates a new instance of LayoutManager
     */
    protected LayoutManager()
    {
        resumeLayout();
    }
    /**
     * Creates a new instance of LayoutManager with a passed Comparator
     * @param toComparator - Comparator to use in the ordering of the layout controls for this layout manager
     */
    protected LayoutManager(Comparator<IControl> toComparator)
    {
        m_oControlComparator = toComparator;
    }

    /**
     * Sets a comparator for this layout manager
     * @param toComparator - Comparator to use in the ordering of the layout controls for this layout manager
     */
    @Override
    public final void setComparator(Comparator<IControl> toComparator)
    {
        m_oControlComparator = toComparator;
    }

    /**
     * Checks if the control specified is laying out, this will be reported as suspended
     * if the layout manager is suspended, the control is not managed by this layout manager
     * or the control is suspended
     * @param toContainer the control to check
     * @return true if layouts are suspended on this control
     */
    @Override
    public final boolean isLayoutSuspended(T toContainer)
    {
        return m_lSuspended || 
                (manages(toContainer) && m_oSuspendedControls != null ? m_oSuspendedControls.contains(toContainer) : false);
    }

    /**
     * Resumes the layout on the container specified
     * @param toContainer the container to resume the layout on
     */
    @Override
    public final void resumeLayout(T toContainer)
    {
        if (m_oSuspendedControls != null)
        {
            m_oSuspendedControls.remove(toContainer);
        }
    }

    /**
     * suspends the layout on the container specified
     * @param toContainer the container to suspend the layout on
     */
    @Override
    public final void suspendLayout(T toContainer)
    {
        if (manages(toContainer))
        {
            if (m_oSuspendedControls == null)
            {
                m_oSuspendedControls = new List<T>();
            }
            if (!m_oSuspendedControls.contains(toContainer))
            {
                m_oSuspendedControls.add(toContainer);
            }
        }
    }
    
    /**
     * Checks if this layout manager manages the control specified
     * @param toContainer the container to check
     */
    @Override
    public final boolean manages(T toContainer)
    {
        return m_oControls != null && m_oControls.containsKey(toContainer);
    }

    /**
     * Adds the specified control to the layout manager
     * @param toContainer the container to add to the layout manager
     * @return true if this control collection was changed
     */
    @Override
    public final boolean addControl(T toContainer, ControlBounds loBounds)
    {
        if (m_oControls == null)
        {
            m_oControls = new HashTable<T, ControlBounds>();
        }
        if (!m_oControls.containsKey(toContainer))
        {
            m_oControls.put(toContainer, loBounds == null ? new ControlBounds() : loBounds);
            onControlAdded(toContainer);
            return true;
        }
        return false;
    }

    /**
     * Removes the specified control from the layout manager
     * @param toContainer the control to remove
     * @return true if the control collection was updated as a result of this call
     */
    @Override
    public final boolean removeControl(T toContainer)
    {
        boolean llReturn = false;
        if (m_oControls != null)
        {
            llReturn = m_oControls.containsKey(toContainer);
            if (llReturn)
            {
                m_oControls.remove(toContainer);
                onControlRemoved(toContainer);
                if (m_oSuspendedControls != null)
                {
                    m_oSuspendedControls.remove(toContainer);
                }
            }
        }
        return llReturn;
    }
    
    /**
     * Allows a subclass to interact when a control has been added to the layout manager
     * @param toContainer the control that has been removed
     */
    protected void onControlAdded(T toContainer)
    {
    }
    
    /**
     * Allows a subclass to interact when a control has been removed from the layout manager
     * @param toContainer the control that has been removed
     */
    protected void onControlRemoved(T toContainer)
    {
    }
    
    /**
     * Checks if this layout manager is suspended.  The layout manager can be suspended without the
     * control layout being suspended
     * @return true if the layout manager is suspended
     */
    @Override
    public final boolean isLayoutSuspended()
    {
        return m_lSuspended;
    }

    /**
     * Resumes layout of this layout manager, calling this when layout is not suspeneded will have no effect
     */
    @Override
    public final void resumeLayout()
    {
        m_lSuspended = false;
    }

    /**
     * Suspends layout of this layout manager, calling this when layout is suspended will have no effect
     */
    @Override
    public final void suspendLayout()
    {
        m_lSuspended = true;
    }

    /**
     * Gets the control bounds that are being used by this layout manager for this
     * control.  If the control is not managed by this layout manager, then returns 
     * null
     * @param toContainer the container to get the control bounds for
     * @return the control bounds for this control
     */
    @Override
    public ControlBounds getControlBounds(T toContainer)
    {
        return manages(toContainer) ? m_oControls.get(toContainer) : null;
    }
    
    /**
     * recalculate the layout
     */
    @Override
    public final void update(T toContainer)
    {
        if (!isLayoutSuspended(toContainer))
        {
            if (toContainer.isShowing())
            {
                // Suspend layout on this container so that multiple threads do not attempt to draw
                suspendLayout(toContainer);
                
                // We will ask the implemented layout manager to do it's work first
                ((IContainerImpl)toContainer.getImplementation()).doLayout(toContainer.getControl());
                
                // The do our layout
                onUpdate(toContainer);

                // Resume layout on the container
                resumeLayout(toContainer);
            }
        }
    }
    
    /**
     * Invalidates all of the cached data in the control
     */
    @Override
    public void invalidate(T toContainer)
    {
        if (!toContainer.isSizeSet())
        {
            m_oControls.put(toContainer, new ControlBounds());
        }
        for (IControl loControl : toContainer.getChildren())
        {
            if (Goliath.DynamicCode.Java.isEqualOrAssignable(IContainer.class, loControl.getClass()))
            {
                ((IContainer)loControl).getLayoutManager().invalidate(((IContainer)loControl));
            }
        }
    }
    
    /**
     * Hook function to allow subclasses to override the layout, this allows you to override the entire
     * onUpdate algorithm, most likely this will not need to be overridden, instead you can override the following
     *
     * - calculateControlBounds, called for every control when it is being updated
     * - calculatePosition, called from calculateControlBounds
     * - calculateSize, called from calculateControlBounds
     */
    protected void onUpdate(T toContainer)
    {
        // loop through each child and lay them out
        layoutChildren(toContainer);
    }
    
    /**
     * Calculates the bounds of this control, the calculation will be based
     * on the fixed size and location, or the sizes of the internal controls.
     * This will not layout or position child controls
     * @return the control bounds of this control
     */
    @Override
    public ControlBounds calculateControlBounds(T toContainer)
    {
        ControlBounds loBounds = getControlBounds(toContainer);
        Dimension loSize = loBounds != null ? loBounds.getActualSize() : null;
        if (loSize == null || loSize.equals(Dimension.EMPTYSIZE()))
        {
            loBounds.setLocation(calculatePosition(toContainer));
            // Get the required content area
            Dimension loContentSize = calculateDimension(toContainer);
            BoxDimension loMargin = Java.isEqualOrAssignable(IWindow.class, toContainer.getClass()) ? null : toContainer.getStyle().getMargin();
            BoxDimension loPadding = toContainer.getStyle().getPadding();
            BorderSettings loBorder = toContainer.getBorder();
            // Then add the margin, border, and padding if needed
            if (loMargin != null || loPadding != null || loBorder != null)
            {
                loContentSize = new Dimension(
                        loContentSize.getIntWidth() +
                        (loMargin == null ? 0 : (loMargin.getLeft() + loMargin.getRight())) +
                        (loPadding == null ? 0 : (loPadding.getLeft() + loPadding.getRight())) +
                        (loBorder == null ? 0 : (loBorder.getLeft() + loBorder.getRight())),
                        
                        loContentSize.getIntHeight() +
                        (loMargin == null ? 0 : (loMargin.getTop() + loMargin.getBottom())) +
                        (loPadding == null ? 0 : (loPadding.getTop() + loPadding.getBottom())) +
                        (loBorder == null ? 0 : (loBorder.getTop() + loBorder.getBottom())));
            }
            loBounds.setPreferredSize(loContentSize);
        }
        return loBounds;
    }
    
    /**
     * Calculates the position of the control based on the layout
     * @param toContainer the layout position
     * @return the position of the control according to the layout
     */
    protected Point calculatePosition(T toContainer)
    {
        ControlBounds loBounds = getControlBounds(toContainer);
        if (loBounds.getLocation() == null)
        {
            Point loPosition = toContainer.getLocation();
            
            IContainer loParent = toContainer.getParent();
            if (loParent != null)
            {
                BoxDimension loMargin = Java.isEqualOrAssignable(IWindow.class, loParent.getClass()) ? null : loParent.getStyle().getMargin();
                BoxDimension loPadding = loParent.getStyle().getPadding();
                BorderSettings loBorder = loParent.getBorder();
                if (loMargin != null || loPadding != null || loBorder != null)
                {
                    loPosition = new Point(
                            loPosition.getIntX() -
                            (loMargin == null ? 0 : loMargin.getLeft()) -
                            (loPadding == null ? 0 : loPadding.getLeft()) -
                            (loBorder == null ? 0 : loBorder.getLeft()), 

                            loPosition.getIntY() -
                            (loMargin == null ? 0 : loMargin.getTop()) -
                            (loPadding == null ? 0 : loPadding.getTop()) -
                            (loBorder == null ? 0 : loBorder.getTop()));
                }
            }
            loBounds.setLocation(loPosition);
        }
        return loBounds.getLocation();
    }

    /**
     * Calculates the size this control should be based on its contents, this will
     * be the minimum size required to fit this control without clipping contents
     * @return the size the control should be to prevent clipping of contents
     */
    protected final Dimension calculateDimension(T toContainer)
    {
        // If this is a container, and if the size is empty, the calculate based on the child sizes
        ControlBounds loBounds = getControlBounds(toContainer);
        Dimension loDimension = loBounds.getSize();
        if (!toContainer.isSizeSet() || (loDimension.equals(Dimension.EMPTYSIZE()) && Goliath.DynamicCode.Java.isEqualOrAssignable(IContainer.class, toContainer.getClass())))
        {
            loDimension = onCalculateDimension(toContainer, loDimension);
        }
        return loDimension;
    }

    /**
     * Calculates the dimensions of the control, including the border size.  This is called when a layout does not
     * know thie size of the control to render, if a value is passed in toContainerSize, this is the size the parent container
     * currently thinks the control should be, this method can return that size or calculate a new size based on the contents of the
     * container being layed out
     * @param toContainerSize the full current size of the container being calculated
     * @return the minimum size this container will take up
     */
    protected abstract Dimension onCalculateDimension(T toContainer, Dimension toContainerSize);
    
    /**
     * Helper function to get the size of the control specified.
     * If the control size is set, this will get the fixed size, if the 
     * control size is not set then this will attempt to calculate the size based
     * on it's contents
     * @param toControl the control to get the size for
     * @return the size of the control
     */
    protected Dimension getContainedControlDimensions(IControl toControl)
    {
        return getContainedControlBounds(toControl).getSize();
    }

    /**
     * Helper function to get the control bounds of the control specified.
     * If the control size is set, this will get the fixed size, if the 
     * control size is not set then this will attempt to calculate the size based
     * on it's contents
     * @param toControl the control to get the size for
     * @return the size of the control
     */
    protected ControlBounds getContainedControlBounds(IControl toControl)
    {
        return Goliath.DynamicCode.Java.isEqualOrAssignable(IContainer.class, toControl.getClass()) ?
                // This is a container control
                toControl.isSizeSet() ? toControl.getControlBounds() : ((IContainer)toControl).getLayoutManager().calculateControlBounds((IContainer)toControl) :
                
                // This is not a container class, so just report as it stands
                toControl.getControlBounds();
    }
    
    /**
     * Helper function to set the size of the contained control, this will only set the preferred size of the control
     * This will only work on controls that are children to toContainer
     * @param toControl the control to set the size of
     * @param tnWidth the width of the control
     * @param tnHeight the height of the control
     * @return true if the size was actually changed
     */
    protected boolean setContainedControlSize(T toContainer, IControl toControl, float tnWidth, float tnHeight)
    {
        if (toControl != null)
        {
            return setContainedControlSize(toContainer, toControl, new Dimension(tnWidth, tnHeight));
        }
        return false;
    }
    
    /**
     * Helper function to set the size of the contained control, this will only set the preferred size of the control and
     * will only work with controls that are contained by the control being layed out (toContainer)
     * @param toControl the control to set the size of
     * @param toSize the size of the control
     * @return true if the size was actually changed
     */
    protected boolean setContainedControlSize(T toContainer, IControl toControl, Dimension toSize)
    {
        if (toControl.getParent() == toContainer)
        {
            if (toControl != null && !toControl.isSizeSet())
            {
                // Ensure the size is at least 0x0
                if (toSize != null && (toSize.getWidth() < 0 || toSize.getHeight() < 0))
                {
                    toSize = new Dimension(toSize.getWidth() < 0 ? 0 : toSize.getWidth(), toSize.getHeight() < 0 ? 0 : toSize.getHeight());
                }

                // Set the preferred size, if it changed anything, invalidate the control
                if (toControl.setPreferredSize(toSize))
                {
                    toControl.invalidate();
                    if (Java.isEqualOrAssignable(IContainer.class, toControl.getClass()))
                    {
                         ((LayoutManager)((IContainer)toControl).getLayoutManager()).getControlBounds((IContainer)toControl).setPreferredSize(toSize);
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Helper function for setting the location of a contained control.
     * This will only work on controls that are contained by toContainer
     * @param toControl the control to set the location for
     * @param tnX the new x position
     * @param tnY the new y position
     */
    protected void setContainedControlLocation(T toContainer, IControl toControl, float tnX, float tnY)
    {   
        if (toControl != null && toControl.getParent() == toContainer)
        {
            BoxDimension loMargin = Java.isEqualOrAssignable(IWindow.class, toContainer.getClass()) ? null : toContainer.getStyle().getMargin();
            BoxDimension loPadding = toContainer.getStyle().getPadding();
            BorderSettings loBorder = toContainer.getBorder();
            if (loMargin != null || loPadding != null || loBorder != null)
            {
                toControl.setLocation(
                        tnX +
                        (loMargin == null ? 0 : loMargin.getLeft()) +
                        (loPadding == null ? 0 : loPadding.getLeft()) +
                        (loBorder == null ? 0 : loBorder.getLeft()), 

                        tnY +
                        (loMargin == null ? 0 : loMargin.getTop()) +
                        (loPadding == null ? 0 : loPadding.getTop()) +
                        (loBorder == null ? 0 : loBorder.getTop()));
            }
            else
            {
                toControl.setLocation(tnX, tnY);
            }
        }
    }

    /**
     * Helper function for setting the location of a contained control,
     * this will only work on controls that are children of toContainer
     * @param toControl the control to set the location for
     * @param toLocation the new location of the control
     */
    protected void setContainedControlLocation(T toContainer, IControl toControl, Point toLocation)
    {
        setContainedControlLocation(toContainer, toControl, toLocation.getX(), toLocation.getY());
    }
    
    
    
    
    
    
    
    
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    protected List<IControl> getControls(IContainer toContainer)
    {
        if (m_oOrderedControls == null)
        {
            m_oOrderedControls = toContainer.getChildren();
            if (m_oControlComparator != null)
            {
                java.util.Collections.sort(m_oOrderedControls, m_oControlComparator);
            }
        }
        
        return m_oOrderedControls;       
    }


    



    

    /**
     * Loops through each child and lays them out
     */
    protected abstract void layoutChildren(T toContainer);

    
}
