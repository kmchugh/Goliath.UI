/* =========================================================
 * Container.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * All controls that are able to contain other controls should override this class
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.UI.Controls;

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.DynamicCode.Java;
import Goliath.Graphics.Point;
import Goliath.Graphics.Polygon;
import Goliath.Graphics.Rectangle;
import Goliath.Interfaces.UI.Controls.Layouts.ILayoutManager;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.Implementations.IContainerImpl;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.ControlPropertySet;
import Goliath.UI.Controls.Layouts.BorderLayoutManager;
import Goliath.UI.Controls.Layouts.LayoutManager;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author kmchugh
 */
public abstract class Container extends Control
        implements IContainer
{
    private ILayoutManager m_oLayoutManager;
            
    private float m_nTextRotation;
    

    /**
     * Creates a new instance of Container
     */
    public Container()
    {
        initialiseComponent();
    }
    
    // TODO: When disposing a container, it should remove itself from the layout

    /**
     * Creates the container using the specified layout manager
     * @param toManager the layout manager to use
     */
    public Container(Class<? extends ILayoutManager> toManager)
    {
        setLayoutManager(toManager);
        initialiseComponent();
    }



    /**
     * Creates a new instance of Container
     */
    public Container(ControlImplementationType toImplementationType)
    {
        super(toImplementationType);
        initialiseComponent();
    }

    /**
     * Removes this control from it's layout manager and disposes all child 
     * controls
     */
    @Override
    protected void onDispose()
    {
        if (m_oLayoutManager != null)
        {
            m_oLayoutManager.removeControl(this);
            m_oLayoutManager = null;
        }
        for (IControl loControl : getChildren())
        {
            loControl.dispose();
        }
        super.onDispose();
    }
    
    /**
     * Gets the current layout manager for this container, the layout manager controls
     * how the children are positioned in this control
     * @return
     */
    @Override
    public final ILayoutManager getLayoutManager()
    {
        return m_oLayoutManager;
    }

    /**
     * Sets the layout manager for this control
     * @param toManager the new manager for the control
     */
    @Goliath.Annotations.NotProperty
    @Override
    public final <K extends ILayoutManager> K setLayoutManager(Class<K> toManagerClass)
    {
        K loManager = LayoutManager.getLayoutManager(toManagerClass);
        if (m_oLayoutManager != loManager)
        {
            boolean llSuspended = false;
            boolean llChanged = false;
            
            ControlBounds loBounds = null;
            // check if the current layout manager is suspended
            if (m_oLayoutManager != null)
            {
                llSuspended = m_oLayoutManager.isLayoutSuspended(this);
                loBounds = m_oLayoutManager.getControlBounds(this);
                llChanged = m_oLayoutManager.removeControl(this);
            }
            
            m_oLayoutManager = loManager;
            if (loBounds != null)
            {
                // Ensure the underlying control bounds are updated if removing from a layout
                this.setControlBounds(loBounds);
            }
            
            llChanged = m_oLayoutManager.addControl(this, loBounds) || llChanged;
            if (llSuspended)
            {
                loManager.suspendLayout(this);
            }
            if (llChanged)
            {
                // Changing of the layout manager invalidates the control
                invalidate();
            }
        }
        return loManager;
    }

    /**
     * Stops the container from laying out children.  This should be used to 
     * inform the container that you are making multiple changes and it should save
     * on cycles by suspending the layout manager.
     * If the layout is already suspended this will have no effect.  Suspending the layout will
     * also cause all the child controls to suspend
     */
    @Override
    public final void suspendLayout()
    {
        if (m_oLayoutManager != null)
        {
            m_oLayoutManager.suspendLayout(this);
        }
        
        // We also want to suspend any laying out of child controls
        for (IControl loControl : getChildren())
        {
            if (Java.isEqualOrAssignable(IContainer.class, loControl.getClass()))
            {
                ((IContainer)loControl).suspendLayout();
            }
        }
    }


    /**
     * Resumes laying out in the container.  This should be called after a suspend layout.
     * If layout is not suspended, then calls to this method will have no effect, resuming the layout
     * will also resume all the layouts for the child controls
     */
    @Override
    public final void resumeLayout()
    {
        if (m_oLayoutManager != null)
        {
            m_oLayoutManager.resumeLayout(this);
        }
        
        // We also want to resume any laying out of child controls
        for (IControl loControl : getChildren())
        {
            if (Java.isEqualOrAssignable(IContainer.class, loControl.getClass()))
            {
                ((IContainer)loControl).resumeLayout();
            }
        }
    }

    /**
     * Checks if the layout is currently suspended
     * @return true if it is suspended, false if not
     */
    @Override
    public final boolean isLayoutSuspended()
    {
        return (m_oLayoutManager == null) ? false : m_oLayoutManager.isLayoutSuspended(this);
    }
    
    

    /**
     * Initialise this component
     */
    private void initialiseComponent()
    {
        if (m_oLayoutManager == null)
        {
            // Set up the default Layout for the Container
            setLayoutManager(BorderLayoutManager.class);
        }
    }


    /**
     * Helper function to the the implementation of this control
     * @return the implementation
     */
    protected IContainerImpl getContainerImplementation()
    {
        return (IContainerImpl)getImplementation();
    }
    
    /**
     * Adds a control as a child of this control
     * @param toControl the control to add
     * @return true if the child list is changed as a result of this call
     */
    @Override
    public boolean addControl(IControl toControl)
    {
        return addControl(getChildren().size(), toControl, null);
    }

    /**
     * Adds a control to a specific index of this control
     * @param tnIndex the index of the control
     * @param toControl the control to add
     * @return true if the child list is changed as a result of this call
     */
    @Override
    public boolean addControl(int tnIndex, IControl toControl)
    {
        return addControl(tnIndex, toControl, null);
    }

    /**
     * Adds a control with the specified layout parameters.  The layout parameters
     * will be used with the layout manager
     * @param toControl the control to add
     * @param toLayoutParameters the layout parameters for the control
     * @return true if the child list is changed as a result of this call
     */
    @Override
    public boolean addControl(IControl toControl, ControlPropertySet toLayoutParameters)
    {
        return addControl(getChildren().size(), toControl, toLayoutParameters);
    }

    /**
     * Adds a control to the container applying the control layout property and value
     * @param toControl the control to add
     * @param toLayoutProperty the layout property
     * @param toLayoutValue the layout value
     * @return true if the child list is changed as a result of this call
     */
    @Override
    public boolean addControl(IControl toControl, ControlLayoutProperty toLayoutProperty, Object toLayoutValue)
    {
        toControl.setProperty(toLayoutProperty.getValue(), toLayoutValue);
        return addControl(getChildren().size(), toControl, null);
    }

    /**
     * Adds a control to the container applying the control layout property and value, the control is
     * added at the specified index
     * @param tnIndex the index to add the control at
     * @param toControl the control to add
     * @param toLayoutProperty the layout property
     * @param toLayoutValue the layout value
     * @return true if the child list is changed as a result of this call
     */
    @Override
    public boolean addControl(int tnIndex, IControl toControl, ControlLayoutProperty toLayoutProperty, Object toLayoutValue)
    {
        toControl.setProperty(toLayoutProperty.getValue(), toLayoutValue);
        return addControl(tnIndex, toControl, null);
    }

    /**
     * Adds the control to the container at the specified index, applying the specified layout parameters
     * @param tnIndex the index to add the control at
     * @param toControl the control to add
     * @param toLayoutParameters the layout parameters for the control
     * @return true if the child list is changed as a result of this call
     */
    @Override
    public boolean addControl(int tnIndex, IControl toControl, ControlPropertySet toLayoutParameters)
    {
        boolean llReturn = false;
        
        if (isLayoutSuspended() && Java.isEqualOrAssignable(IContainer.class, toControl.getClass()))
        {
            ((IContainer)toControl).suspendLayout();
        }

        if (toLayoutParameters != null && toLayoutParameters.size() > 0)
        {
            for (String lcProperty : toLayoutParameters.getPropertyKeys())
            {
                toControl.setProperty(lcProperty, toLayoutParameters.getProperty(lcProperty));
            }
        }
        
        llReturn = getContainerImplementation().addControl(tnIndex, toControl, getControl());
        if (llReturn)
        {
            // Adding a control invalidates the layout
            invalidate();
            fireEvent(UIEventType.ONCHILDADDED());
        }
        return llReturn;
    }

    /**
     * Removes the specified control from the container, if the control does not
     * exist in this container then nothing happens
     * @param toControl the control to remove
     */
    @Override
    public boolean removeControl(IControl toControl)
    {
        boolean llReturn = getContainerImplementation().removeControl(toControl, getControl());
        if (llReturn)
        {
            // Removing a control invalidates the layout
            invalidate();
            fireEvent(UIEventType.ONCHILDREMOVED());
        }
        
        return llReturn;
    }

    /**
     * Removes the control at the specified index
     * @param tnIndex the index to remove the control at
     */
    @Override
    public boolean removeControl(int tnIndex)
    {
        return removeControl(getChildren().get(tnIndex));
    }
    
    /**
     * Supress or resume event listening on this control.
     * Multiple calls to this method with the same parameter will not have any effect
     * @param tlSuppress true to suppress false to resume
     */
    @Override
    public void suppressEvents(boolean tlSuppress)
    {
        super.suppressEvents(tlSuppress);
        
        List<IControl> loChildren = getChildren();
        for (IControl loChild : loChildren)
        {
            loChild.suppressEvents(tlSuppress);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    

    /**
     *
     * @param tlEditable
     */
    @Override
    public void setEditable(boolean tlEditable)
    {
        super.setEditable(tlEditable);
        
        for (IControl loControl : getChildren())
        {
            if (Java.isEqualOrAssignable(Control.class, loControl.getClass()))
            {
                ((Control)loControl).updateEditable(tlEditable);
                ((Control)loControl).updateEnabled(tlEditable);
            }
            else
            {
                loControl.setEditable(tlEditable);
            }
        }
    }
    
    /**
     * Sets the text rotation for controls within this container
     * @param tnDegrees the degrees to rotate the text
     */
    @Goliath.Annotations.NotProperty
    @Override
    public void setTextRotation(float tnDegrees)
    {
        m_nTextRotation = tnDegrees;
    }
    
    /**
     * Gets the number of degrees to rotate text within this container
     * @return the degrees to rotate text
     */
    @Override
    public float getTextRotation()
    {
        return m_nTextRotation == 0 ? getParent() != null ? getParent().getTextRotation() : m_nTextRotation : m_nTextRotation;
    }
    
    /**
     * Gets the list of controls that are at the point specified
     * @param toPoint the point to get the controls at
     * @return the list of controls at the point, or an empty list if there are no child controls at the specified point
     */
    public List<IControl> getControlsAt(Point toPoint)
    {
        List<IControl> loReturn = new List<IControl>();
        for (IControl loControl : getChildren())
        {
            if (Polygon.hitTest(loControl.getControlBounds().getRectangle(), toPoint))
            {
                loReturn.add(loControl);
            }
        }
        return loReturn;
    }
    
    /**
     * Gets the list of controls that are in the rectangle specified, this will
     * also get controls that intersect the specified area
     * @param toRect the rectangle to get the controls from
     * @return the list of controls in the rectangle, or an empty list if there are no child controls at the specified point
     */
    public List<IControl> getControlsIn(Rectangle toRect)
    {
        return getControlsIn(toRect, true);
    }
    
    /**
     * Gets the list of controls that are in the rectangle specified
     * @param toRect the rectangle to get the controls from
     * @param tlIntersection true to get controls that intersect, but are not fully contained by the rectangle
     * @return the list of controls in the rectangle, or an empty list if there are no child controls at the specified point
     */
    public List<IControl> getControlsIn(Rectangle toRect, boolean tlIntersection)
    {
        List<IControl> loReturn = new List<IControl>();
        for (IControl loControl : getChildren())
        {
            if (Polygon.hitTest(toRect, loControl.getControlBounds().getRectangle(), tlIntersection))
            {
                loReturn.add(loControl);
            }
        }
        return loReturn;
    }
    
    /**
     * Gets the index of the specified control
     * @param toControl the control to get the index of
     * @return the index of the control or -1 if the control is not on this control
     */
    @Override
    public int indexOf(IControl toControl)
    {
        return (toControl.getParent() == this) ? getContainerImplementation().indexOf(toControl, getControl()) : -1;
    }

    @Override
    public synchronized final boolean sortChildren(Comparator<IControl> toComparator)
    {
        boolean llReturn = true;
        IControl[] laControls = new IControl[0];
        laControls = getChildren().toArray(laControls);
        if (laControls.length > 0)
        {
            try
            {
                Arrays.sort(laControls, toComparator);
                
                // After the sort, put the controls back in order
                suspendLayout();
                for (int i=0; i<laControls.length; i++)
                {
                    addControl(i, laControls[i]);
                }
            }
            catch (Throwable ex)
            {
                llReturn = false;
                Application.getInstance().log(ex);
            }
        }
        return llReturn;
    }
    
    



    @Override
    protected void onUpdate()
    {
        // Get the layout manager to layout
        ILayoutManager loManager = getLayoutManager();
        if (loManager != null)
        {
            loManager.update(this);
        }

        // Then the children if needed, the children may already be layed out, so this may do nothing
        for (IControl loControl : getChildren())
        {
            if (!isLayoutSuspended())
            {
                loControl.update();
            }
        }
    }

    @Override
    protected void onInvalidate()
    {
        super.onInvalidate();
        if (m_oLayoutManager != null)
        {
            m_oLayoutManager.invalidate(this);            
        }
        for (IControl loControl : getChildren())
        {
            loControl.invalidate();
        }
    }

    /**
     * Clears all of the children from this control
     */
    @Override
    public void clearChildren()
    {
        getContainerImplementation().clearChildren(getControl());
        
        // Removing a control invalidates the layout
        invalidate();
    }

    /**
     * Gets the control specified by name, if the control does not exist, returns null.
     * This will only search the current container, not any child containers
     * @param tcName the name of the control to get
     * @return the control, or null if there was no control of that name in this container
     */
    @Override
    public IControl getChildControlByName(String tcName)
    {
        for (IControl loControl : getChildren())
        {
            if (tcName.equalsIgnoreCase(loControl.getName()))
            {
                return loControl;
            }
        }
        return null;
    }

    /**
     * Gets all of the children of this container
     * @return the list of children, or an empty list if there were no children
     */
    @Goliath.Annotations.NotProperty
    @Override
    public List<IControl> getChildren()
    {
        return getContainerImplementation().getChildren(getControl());
    }
    
    /**
     * A container is not displayable if it's layout is suspended
     * @return true if this control is ready to draw, false if not
     */
    @Override
    public boolean isDisplayable()
    {
        return !isLayoutSuspended() && super.isDisplayable();
    }

    



    
}
