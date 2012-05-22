/* =========================================================
 * ControlImplementation.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * This is the base class for creating control implementations.
 * Implementations work as flyweights, so only one is created per
 * control type.  The control Implementation manages communication
 * between the Goliath Control, and the actual visual control
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
import Goliath.Constants.EventType;
import Goliath.Constants.LogType;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.Implementations.IControlImpl;
import Goliath.Interfaces.UI.Controls.Implementations.IImplementedControl;

/**
 *
 * @author kmchugh
 */
public abstract class ControlImplementation <T extends IControl> extends Goliath.Object
        implements IControlImpl

{
    @Override
    public abstract Class<T> getSupportedClass();

    /**
     * Factory method that creates the Implemented Control for the specified Goliath Control
     * @param toControlBase the Goliath Control to create an implementation for
     * @return the newly created implemented control
     */
    @Override
    public abstract IImplementedControl createControl(IControl toControlBase);
    
    /**
     * Gets the render types that this control supports
     * @return a list of types that this implementation knows how to handle
     */
    @Override
    public abstract List<ControlImplementationType> getSupportedTypes();
    
    /**
     * Renders the component as required.
     * @param toRenderCanvas is the canvas to render to, this could be a string, a graphics object, or a file, depending
     * on the specific implementation.  It is up to the implementation to know what this object is
     * @param toControl the control to render
     */
    @Override
    public abstract void renderComponent(java.lang.Object toRenderCanvas, IImplementedControl toControl);

    public final void setSizeAndLocation(Point toPoint, Dimension toDimension, IImplementedControl toControl)
    {
        setSize(toDimension, toControl);
        setLocation(toPoint, toControl);
    }

    @Override
    public abstract void setVisible(boolean tlVisible, IImplementedControl toControl);

    @Override
    public abstract boolean setSize(Dimension toSize, IImplementedControl toControl);

    @Override
    public abstract void setSelectable(boolean tlSelectable, IImplementedControl toControl);

    @Override
    public abstract void setName(String tcName, IImplementedControl toControl);

    @Override
    public abstract void setMinSize(Dimension toSize, IImplementedControl toControl);

    @Override
    public abstract void setMaxSize(Dimension toSize, IImplementedControl toControl);

    @Override
    public abstract void setLocation(Point toLocation, IImplementedControl toControl);

    @Override
    public abstract void setEnabled(boolean tlEnabled, IImplementedControl toControl);

    @Override
    public abstract boolean isVisible(IImplementedControl toControl);

    @Override
    public abstract boolean isSelectable(IImplementedControl toControl);

    @Override
    public abstract boolean isEnabled(IImplementedControl toControl);

    @Override
    public abstract Dimension getSize(IImplementedControl toControl);

    @Override
    public abstract String getName(IImplementedControl toControl);

    @Override
    public abstract Dimension getMinSize(IImplementedControl toControl);

    @Override
    public abstract Dimension getMaxSize(IImplementedControl toControl);

    @Override
    public abstract Point getLocation(IImplementedControl toControl);

    @Override
    public abstract String getTooltip(IImplementedControl toControl);

    @Override
    public abstract void setTooltip(String tcTooltip, IImplementedControl toControl);



    @Override
    public final void addEventListener(EventType toEvent, IImplementedControl toControl)
    {
        if (!handleEventType(toEvent, toControl))
        {
            Application.getInstance().log("The event " + toEvent.toString() + " has not been handled.", LogType.DEBUG());
        }
    }

    protected boolean handleEventType(EventType toEvent, IImplementedControl toControl)
    {
        return false;
    }





}
