/* =========================================================
 * Control.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * Base control class, all Goliath UI should extend this class
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/
package Goliath.UI.Controls;

import Goliath.UI.Windows.Window;
import Goliath.Applications.Application;
import Goliath.Applications.UIApplicationSettings;
import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Constants.EventType;
import Goliath.DynamicCode.Java;
import Goliath.Event;
import Goliath.EventDispatcher;
import Goliath.Graphics.BoxDimension;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Graphics.Constants.Position;
import Goliath.Graphics.Rectangle;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.Interfaces.UI.Controls.Implementations.IControlImpl;
import Goliath.Interfaces.UI.Controls.Implementations.IImplementedControl;
import Goliath.UI.Skin.SkinManager;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Skin.Skin;
import java.awt.Color;

/**
 *
 * @author kmchugh
 */
public abstract class Control extends ControlBase
{
    // TODO: Use an invalid state object instead of a flag for speed when invalidating and updating, reduces if statements
    private IControlImpl m_oImplementation;
    private IImplementedControl m_oImplementationControl;
    private PropertySet m_oProperties;
    private ControlImplementationType m_oImplementationType;
    private EventDispatcher<EventType, Event<? extends IControl>> m_oEventDispatcher;
    private Dimension m_oContentSize;
    private ControlBounds m_oContentBounds;

    private boolean m_lIsUpdated;
    private boolean m_lEditable;
    private boolean m_lEnabled;
    private boolean m_lParticipateLayout;
    private List<String> m_oClasses;
    private String m_cClassString;
    private boolean m_lClearOnPaint;
    private ControlStyle m_oCoreStyle;
    private ControlStyle m_oContainedStyle;
    private boolean m_lStyleValid;

    /**
     * Creates a new instance of Control, this also sets up the implementation
     * and the communication between the implementation and the Control
     */
    protected Control()
    {
        initialiseComponent();
    }

    /**
     * Creates a new control of the specified implementation type
     * @param toType the type for the control
     */
    protected Control(ControlImplementationType toType)
    {
        m_oImplementationType = toType;
        initialiseComponent();
    }
    
    /**
     * Gets the control implementation for this type of control
     * @return the control implementation for this type of control
     */
    @Goliath.Annotations.NotProperty
    @Override
    public final IControlImpl getImplementation()
    {
        if (m_oImplementation == null)
        {
            m_oImplementation = ImplementationFactory.getImplementation(this);
            m_oImplementationControl = m_oImplementation.createControl(this);
        }
        return m_oImplementation;
    }
    
    @Goliath.Annotations.NotProperty
    private IImplementedControl getImplementationControl()
    {
        if (m_oImplementationControl == null)
        {
            getImplementation();
        }
        return m_oImplementationControl;
    }
    
    /**
     * Gets the values for the styling of this control, this will get the
     * values for the currently computed style.
     */
    @Goliath.Annotations.NotProperty
    @Override
    public final ControlStyle getStyle()
    {
        return m_oContainedStyle != null ? m_oContainedStyle : getCoreStyle(); 
    }
    
    /**
     * Gets the base styling values for this control
     * @return the base styling for this control
     */
    private ControlStyle getCoreStyle()
    {
        if (m_oCoreStyle == null)
        {
            m_oCoreStyle = new ControlStyle();
            m_oCoreStyle.setVisible(getImplementation().isVisible(getImplementationControl()));
        }
        return m_oCoreStyle;
    }
    
    /**
     * Invalidates the style, this will cause the style to be recalculated,
     * this will also cause an invalidation of the control
     */
    public final void invalidateStyle()
    {
        invalidate();
        m_lStyleValid = false;
        m_cClassString = null;
        Skin loSkin = SkinManager.getInstance().getCurrentSkin();
        if (loSkin != null)
        {
            loSkin.invalidateStyle(this);
        }
    }
    
    /**
     * Gets the control bounds, creates them if they did not already exist
     * @return the control bounds
     */
    @Override
    public ControlBounds getControlBounds()
    {
        return getStyle().getControlBounds(true);
    }
    
    /**
     * Sets the bounds for this control
     * @param toBounds the new control bounds
     */
    @Goliath.Annotations.NotProperty
    public void setControlBounds(ControlBounds toBounds)
    {
        if (getCoreStyle().setControlBounds(toBounds))
        {
            invalidateStyle();
        }
    }
    
    /**
     * disposes of the control, any resources should be cleaned up here
     */
    @Override
    public final void dispose()
    {
        onDispose();
    }
    
    /**
     * Allows subclasses to interact with the disposal of controls,
     * any resources and links should be cleaned up here.  The expectation is that
     * it the super dispose should 
     */
    protected void onDispose()
    {
        
    }
    
    /**
     * Initializes the component
     */
    private void initialiseComponent()
    {
        m_lEditable = true;
        m_lEnabled = true;
    }
    
    /**
     * Sets if the control is visible or not
     * @param tlVisible true to make the control visible, false otherwise
     */
    @Override
    public void setVisible(boolean tlVisible)
    {
        if (getStyle().setVisible(tlVisible))
        {
            getImplementation().setVisible(tlVisible, getImplementationControl());
        }
    }

    /**
     * Checks if the control is visible or not
     * @return true if visible false if not
     */
    @Override
    public boolean isVisible()
    {
        return getStyle().getVisible();
    }
    
    /**
     * Adds the specified class to the control
     * @param tcClass the class to add
     */
    @Override
    public final void addClass(String tcClass)
    {
        if (m_oClasses == null)
        {
            m_oClasses = new List<String>();
        }

        tcClass = tcClass.toLowerCase();
        boolean llChanged = false;
        for (String laClass : tcClass.split(" "))
        {
            if (!m_oClasses.contains(laClass))
            {
                llChanged = m_oClasses.add(laClass) || llChanged;
            }
        }
        if (llChanged)
        {
            invalidateStyle();
            fireEvent(UIEventType.ONSTYLECHANGED());
        }
    }

    /**
     * Removes the specified class to the control
     * @param tcClass the class to remove
     */
    @Override
    public final void removeClass(String tcClass)
    {
        if (m_oClasses != null)
        {
            if (m_oClasses.remove(tcClass.toLowerCase()))
            {
                invalidateStyle();
                fireEvent(UIEventType.ONSTYLECHANGED());
            }
        }
    }

    /**
     * Checks if a control has the specified class
     * @param tcClass the class to check for
     * @return true if the control has the class
     */
    @Override
    public final boolean hasClass(String tcClass)
    {
        return (m_oClasses == null ? false : m_oClasses.contains(tcClass.toLowerCase()));
    }

    /**
     * Gets the full list of classes applied to this control
     * @return the list of classes
     */
    @Goliath.Annotations.NotProperty
    @Override
    public List<String> getClasses()
    {
        List<String> loReturn = m_oClasses == null ? new List<String>(0) : new List<String>(m_oClasses);
        
        IContainer loParent = getParent();
        if (loParent != null && loParent.hasClasses())
        {
            loReturn.addAll(loParent.getClasses());
        }
        return loReturn;
    }
    
    /**
     * Gets the full class string for this control
     * @return the string representation of the entire class list applied to this control
     */
    @Goliath.Annotations.NotProperty
    @Override
    public String getClassString()
    {
        if (m_cClassString == null)
        {
            StringBuilder loClasses = new StringBuilder(getParent() != null ? getParent().getClassString() + " | " : "");
            
            List<Class> loClassList = Java.getClassHierarchy(this.getClass());
            for (Class loClass : loClassList)
            {
                // Add each of the class names
                for (String lcClass : getClasses())
                {
                    Goliath.Utilities.appendToStringBuilder(loClasses, 
                        loClass.getSimpleName().toLowerCase(),
                        ".",
                        lcClass.toLowerCase(),
                        " ");
                }
                
                // Add the control class
                Goliath.Utilities.appendToStringBuilder(loClasses, 
                        loClass.getSimpleName().toLowerCase(),
                        " ");
            }
            List<String> loClassNames = new List<String>(loClasses.toString().split(" "));
            loClasses = new StringBuilder();
            for (String lcClass : loClassNames)
            {
                Goliath.Utilities.appendToStringBuilder(loClasses, 
                        " ",
                        lcClass);
            }
            m_cClassString = loClasses.toString() + " ";
        }
        return m_cClassString;
    }

    /**
     * Checks if the control has any classes specified
     * @return true if this control has any classes set
     */
    @Override
    public boolean hasClasses()
    {
        return (m_oClasses != null && m_oClasses.size() > 0) || (getParent() != null && getParent().hasClasses());
    }

    /**
     * Clears all the classes on this control
     */
    @Override
    public final void clearClasses()
    {
        m_oClasses = null;
    }
    
    /**
     * Sets the focus to the specified control
     */
    @Goliath.Annotations.NotProperty
    @Override
    public final void setFocus()
    {
        getImplementation().setFocus(getControl());
    }
    
    /**
     * Renders the control to the canvas specified, do not call this from within
     * the render cycle or it will cause stack overflow, generally this should
     * not be called.
     * @param toRenderCanvas the canvas to render to
     */
    @Override
    public final void render(Object toRenderCanvas) 
    {
        if (isInvalidated() && isShowing())
        {
            update();
        }
        onRender(toRenderCanvas);
    }
    
    /**
     * Allows the sub class to interact with the render cycle.  If this method returns false
     * then the default render cycle will not happen, if the method returns true, 
     * then the default render cycle will happen after this call.  To call the default rendering
     * cycle from this method it is best to return false, and call the render manually
     * 
     * @param toRenderCanvas the canvas to render to
     */
    protected void onRender(Object toRenderCanvas)
    {
        onRenderBackground(toRenderCanvas);
        
        onRenderContent(toRenderCanvas);
        
        onRenderBorder(toRenderCanvas);
    }
    
    /**
     * Renders the background area of the control, this offsets for the margin automatically
     * @param toRenderCanvas the canvas to render to
     */
    protected void onRenderBackground(Object toRenderCanvas)
    {
        SkinManager.getInstance().renderBackground(toRenderCanvas, this, getStyle());
    }
    
    /**
     * Renders the content area of the control, this offsets for the margin automatically
     * @param toRenderCanvas the canvas to render to
     */
    protected void onRenderContent(Object toRenderCanvas)
    {
        SkinManager.getInstance().renderContent(toRenderCanvas, this, getStyle());
    }
    
    /**
     * Renders the border area of the control
     * @param toRenderCanvas the canvas to render to
     */
    protected void onRenderBorder(Object toRenderCanvas)
    {
        SkinManager.getInstance().renderBorder(toRenderCanvas, this, getStyle());
    }
    
    /**
     * Checks if this controls is actually showing on the screen
     * @return true if the control is on screen
     */
    @Override
    public final boolean isShowing()
    {
        return getImplementation().isShowing(getImplementationControl()) && (!getSize().equals(Dimension.EMPTYSIZE()) || !getImplementationControl().getImplementedSize().equals(Goliath.Graphics.Dimension.EMPTYSIZE()));
    }
    
    /**
     * Sets the foreground colour used for this control
     * @param toColour the foreground colour for this control
     */
    @Override
    public void setColour(Color toColour)
    {
        if (getCoreStyle().setColour(toColour))
        {
            getImplementation().setColour(toColour, getImplementationControl());
            invalidateStyle();
        }
    }

    /**
     * Gets the foreground colour of this control
     * @return the foreground colour of the control
     */
    @Override
    public Color getColour()
    {
        return getStyle().getColour();
    }
    
    /**
     * Sets the background colour of this control
     * @param toColour
     */
    @Override
    public void setBackground(Color toColour)
    {
        if (getCoreStyle().setBackgroundColour(toColour))
        {
            getImplementation().setBackground(toColour, getImplementationControl());
            invalidateStyle();
        }
    }

    /**
     * Gets the background colour of this control
     * @return the background colour of the control
     */
    @Override
    public Color getBackgroundColour()
    {
        return getStyle().getBackgroundColour();
    }
    
    /**
     * Sets the background image for this control
     * @param toBackground the new background image for the control
     */
    @Override
    public void setBackground(Goliath.Graphics.Image toBackground)
    {
        if (getCoreStyle().setBackgroundImage(toBackground))
        {
            getImplementation().setBackground(toBackground, getImplementationControl());
            invalidateStyle();
        }
    }

    /**
     * Gets the background image for this control
     * @return the background image, this could be null
     */
    @Override
    public Goliath.Graphics.Image getBackgroundImage()
    {
        return getStyle().getBackgroundImage();
    }
    
    @Override
    public void setBackgroundImageAlignment(Position toAlignment)
    {
        if (getCoreStyle().setBackgroundImageAlignment(toAlignment))
        {
            invalidateStyle();
        }
    }

    @Override
    public Position getBackgroundImageAlignment()
    {
        return getStyle().getBackgroundImageAlignment();
    }
    
    /**
     * The gets the actual control that the user sees.  This method is public
     * as the implementation needs access to the control itself.  The Implementation
     * controls communication between the visual control and the non visual control.
     * In general there should be no need to call getControl externally as the developer
     * will never know exactly what control they will get.
     * @return the actual visual component
     */
    // TODO: refactor this, remove this method and implement getImplementedControl as public
    @Goliath.Annotations.NotProperty
    @Override
    public IImplementedControl getControl()
    {
        return getImplementationControl();
    }


    

    /**
     * Gets the value of the specified property, this only includes a search through custom properties.
     * @param <T> The type of the value to return
     * @param tcProperty The name of the property to get.  This is not case sensitive
     * @return the value retrieved, or null if the property does not exist
     */
    @Override
    public <T> T getProperty(String tcProperty)
    {
        return (m_oProperties == null) ? null : (T)m_oProperties.getProperty(tcProperty);
    }


    /*
     * Gets the value of the specified property, this only includes a search through custom properties.
     * If the property does not exist, it will be set to toDefault then returned.
     * @param <T> The type of the value to return
     * @param tcProperty The name of the property to get.  This is not case sensitive
     * @param toDefault The default value of this property, if the property has not already been set, it will be set to this value
     * @return the value retrieved, or toDefault if the property does not exist
     */
    @Override
    public final <T> T getProperty(String tcProperty, T toDefault)
    {
        T loReturn = this.<T>getProperty(tcProperty);
        if (loReturn == null)
        {
            setProperty(tcProperty, toDefault);
            loReturn = toDefault;
        }
        return loReturn;
    }

    @Goliath.Annotations.NotProperty
    @Override
    public PropertySet getProperties()
    {
        return m_oProperties;
    }

    @Override
    public boolean hasProperties()
    {
        return m_oProperties != null;
    }



    /**
     * Sets the property to the specified value
     * @param <T> the type of the property
     * @param tcProperty the property to set, this is not case sensitive
     * @param toValue the value of the property
     * @return the old value of the property, or null if the property was not set
     */
    @Goliath.Annotations.NotProperty
    @Override
    public <T> T setProperty(String tcProperty, T toValue)
    {
        if (m_oProperties == null)
        {
            m_oProperties = new PropertySet();
        }
        T loReturn = (T)m_oProperties.getProperty(tcProperty);
        m_oProperties.setProperty(tcProperty, toValue);
        return loReturn;
    }
    
    @Override
    public <T> T clearProperty(String tcProperty)
    {
        if (m_oProperties != null)
        {
            T loReturn = (T)m_oProperties.getProperty(tcProperty);
            m_oProperties.clearProperty(tcProperty);
            return loReturn;
        }
        return null;
    }
    
    /**
     * Sets the name of the control, the name is simply used to refer to the
     * control in any dynamically generated code, or when searching a tree for
     * a control
     * @param tcName the new name of the control
     */
    @Goliath.Annotations.NotProperty
    @Override
    public void setName(String tcName)
    {
        getImplementation().setName(tcName, getImplementationControl());
    }

    /**
     * Gets the name of the control, the name is how the control is identified
     * @return the name of the control
     */
    @Override
    public String getName()
    {
        return getImplementation().getName(getImplementationControl());
    }
    
    @Goliath.Annotations.NotProperty
    @Override
    public final Rectangle getContentRectangle()
    {
        return getContentBounds().getRectangle();
    }

    @Goliath.Annotations.NotProperty
    @Override
    public ControlImplementationType getImplementationType()
    {
        return m_oImplementationType == null ?
                    ((UIApplicationSettings)Application.getInstance().getApplicationSettings()).getDefaultImplementationType() :
            m_oImplementationType;
    }
    
    /**
     * Sets if the control is selectable, or focusable
     * @param tlSelectable true if selectable, false if not
     */
    @Override
    public void setSelectable(boolean tlSelectable)
    {
        getImplementation().setSelectable(tlSelectable, getImplementationControl());
    }

    /**
     * Gets if the control is selectable or not
     * @return true if selectable, false if not
     */
    @Override
    public boolean isSelectable()
    {
        return getImplementation().isSelectable(getImplementationControl());
    }

    /**
     * Sets if the control is enabled or not
     * @param tlEnabled true if enabled, false otherwise
     */
    @Override
    public void setEnabled(boolean tlEnabled)
    {
        m_lEnabled = tlEnabled;
        getImplementation().setEnabled(m_lEnabled, getImplementationControl());
    }

    /**
     * This should be used by sub classes and control classes to set the enabled state.
     * This will take in to account the explicit settings by setEnabled
     * @param tlEnabled
     */
    protected void updateEnabled(boolean tlEnabled)
    {
        getImplementation().setEnabled(tlEnabled && m_lEnabled, getImplementationControl());
    }

    /**
     * Gets if the control is enabled
     * @return true if it is, false if it is not
     */
    @Override
    public boolean isEnabled()
    {
        return getImplementation().isEnabled(getImplementationControl());
    }

    /**
     * Returns if this control should be participating in the layout
     * @return true if the control participates in the layout
     */
    @Override
    public boolean participatesInLayout()
    {
        return m_lParticipateLayout;
    }

    /**
     * Sets if this control should be participating with the layout managers layout
     * This is up to the layout manager to adhere to
     * @param tlParticipate true if this control should participate
     */
    @Goliath.Annotations.NotProperty
    @Override
    public void setParticipatesInLayout(boolean tlParticipate)
    {
        if (m_lParticipateLayout != tlParticipate)
        {
            m_lParticipateLayout = tlParticipate;
            invalidate();
        }
    }

    @Override
    public void suppressEvents(boolean tlSuppress)
    {
        if (m_oEventDispatcher != null)
        {
            // Find out what is happening at the parent level
            boolean llParentSuppressed = getParent() == null ? false : getParent().areEventsSuppressed();
            
            // if either this control is being suppressed, or a parent control is suppressed, then suppress events
            m_oEventDispatcher.suppressEvents(tlSuppress || llParentSuppressed);
        }
    }
    
    @Override
    public boolean removeEventListener(EventType toEvent, IDelegate toCallback)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.removeEventListener(toEvent, toCallback) : false;
    }

    @Override
    public boolean hasEventsFor(EventType toEvent)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.hasEventsFor(toEvent) : false;
    }

    @Override
    public void fireEvent(final EventType toEventType, final Event<? extends IControl> toEvent)
    {
        if (m_oEventDispatcher != null && isEnabled() && !areEventsSuppressed())
        {
            m_oEventDispatcher.fireEvent(toEventType, toEvent);
        }
    }

    @Override
    public final boolean clearEventListeners(EventType toEvent)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.clearEventListeners(toEvent) : false;
    }

    @Override
    public final boolean clearEventListeners()
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.clearEventListeners() : false;
    }

    @Override
    public final boolean areEventsSuppressed()
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.areEventsSuppressed() : false;
    }

    @Override
    public final boolean addEventListener(EventType toEvent, IDelegate toCallback)
    {
        if (m_oEventDispatcher == null)
        {
            m_oEventDispatcher = new EventDispatcher<EventType, Event<? extends IControl>>();
        }
        boolean llReturn = m_oEventDispatcher.addEventListener(toEvent, toCallback);
        if (llReturn)
        {
            getImplementation().addEventListener(toEvent, getImplementationControl());
        }
        return llReturn;
    }

    /**
     * Forces the control to invalidate, causing it to redraw itself and recalculate any layouts
     */
    @Override
    public void invalidate()
    {
        if (m_lIsUpdated)
        {
            m_lIsUpdated = false;
            m_oContentBounds = null;
            m_oContentSize = null;
            fireEvent(UIEventType.ONINVALIDATED());
            onInvalidate();
        }
    }

    /**
     * Fires the specified event only if the event dispatcher exists
     * @param toType the event to fire
     */
    protected final void fireEvent(UIEventType toType)
    {
        if (m_oEventDispatcher != null)
        {
            m_oEventDispatcher.fireEvent(toType, new Event<IControl>(this));
        }
    }


    protected void onInvalidate()
    {
        getImplementation().invalidate(getImplementationControl());
    }

    @Goliath.Annotations.NotProperty
    @Override
    public IContainer getParent()
    {
       return m_oImplementation != null ? getImplementation().getParent(getImplementationControl()) : null;
    }

    @Override
    public void update()
    {
        // If this control is not invalid or is not showing on the screen, then no need to update
        if (isInvalidated() && isShowing())
        {
            IContainer loParent = this.getParent();

            // Check if we need to go up to the parent first
            if (loParent != null && loParent.isInvalidated())
            {
                // We need to process the parent, then we will come back here
                loParent.update();
            }
            else
            {
                // No need for the parent, so just process here
                Skin loSkin = SkinManager.getInstance().getCurrentSkin();
                if (loSkin != null)
                {
                    loSkin.styleControl(this);
                }
                m_lIsUpdated = true;
                onUpdate();
                fireEvent(UIEventType.ONVALIDATED());
            }
        }
    }
    
    /**
     * Force rendering of the control right now
     */
    @Override
    public void forceRender()
    {
        forceRender(0, getLocation(), getSize());
    }
    
    @Override
    public void forceRender(long tnRenderDelay)
    {
        forceRender(tnRenderDelay, getLocation(), getSize());
    }
    
    @Override
    public void forceRender(Point toStart, Dimension toArea)
    {
        forceRender(0, toStart, toArea);
    }
    
    @Override
    public void forceRender(long tnRenderDelay, Point toStart, Dimension toArea)
    {
        getImplementation().forceRender(getControl(), tnRenderDelay, toStart, toArea);
    }

    protected void onUpdate()
    {
        forceRender(0, getLocation(), getSize());
    }
    
    
    @Override
    public boolean isInvalidated()
    {
        return !m_lIsUpdated;
    }

    /**
     * Checks if a control is displayable (initialised and not being disposed)
     * @return true if the control is displayable
     */
    @Override
    public boolean isDisplayable()
    {
        return getImplementation().isDisplayable(getImplementationControl());
    }
    
    /**
     * Sets if the control can be modified by the user
     * @param tlEditable true if it can be modified
     */
    @Override
    public void setEditable(boolean tlEditable)
    {
        m_lEditable = tlEditable;
        getImplementation().setEditable(m_lEditable, getImplementationControl());
    }

    /**
     * This should be used by sub classes and control classes to set the editable state.
     * This will take in to account the explicit settings by setEditable
     * @param tlEditable
     */
    protected void updateEditable(boolean tlEditable)
    {
        getImplementation().setEditable(tlEditable && m_lEditable, getImplementationControl());
    }

    /**
     * Checks if the control can be modified by the user
     * @return true if yes, otherwise false
     */
    @Override
    public boolean isEditable()
    {
        return getImplementation().isEditable(getImplementationControl());
    }
    
    /**
     * Sets the tooltip for the control
     * @param tcValue the new tooltip value
     */
    @Override
    public void setTooltip(String tcValue)
    {
        getImplementation().setTooltip(tcValue, getImplementationControl());
    }

    /**
     * Gets the tooltip for the control
     * @return the tooltip of the control
     */
    @Override
    public String getTooltip()
    {
        return getImplementation().getTooltip(getImplementationControl());
    }
    
    // TODO: Also need to implement method with a parameter to which screen
    /**
     * Gets the location of this control in screen coordinates
     * @return the location of the control in screen coordinates
     */
    @Goliath.Annotations.NotProperty
    @Override
    public Point getScreenCoordinates()
    {
        Point loLocation = getLocation();
        float lnX=loLocation.getX();
        float lnY=loLocation.getY();

        IContainer loNode = getParent();
        while (loNode != null)
        {
            loLocation = loNode.getLocation();
            lnX+= loLocation.getX();
            lnY+= loLocation.getY();
            loNode = loNode.getParent();
        }
        return new Point(lnX, lnY);
    }

    /**
     * Gets the window this control is sitting on, if there is one
     * @return the window this control is on
     */
    @Goliath.Annotations.NotProperty
    @Override
    public final IWindow getParentWindow()
    {
        IContainer loNode = getParent() != null ? getParent() : Goliath.DynamicCode.Java.isEqualOrAssignable(IWindow.class, this.getClass()) ? (IWindow)this : null;
        while (loNode != null && !Goliath.DynamicCode.Java.isEqualOrAssignable(IWindow.class, loNode.getClass()))
        {
            loNode = loNode.getParent();
        }
        return (Window)loNode;
    }

    /**
     * Checks if this control implementation has a border
     * @return true if this implementation has a border
     */
    @Override
    public boolean hasBorder()
    {
        return getStyle().getBorder() != null;
    }
    
    /**
     * Gets the border settings for this control, this could return null if there is no
     * border for this control
     * @return the border defintion or null
     */
    @Override
    public BorderSettings getBorder()
    {
        return getStyle().getBorder();
    }
    
    /**
     * Sets the border settings for this control
     * @param toBorderSettings the control border settings
     */
    @Goliath.Annotations.NotProperty
    @Override
    public void setBorder(BorderSettings toBorderSettings)
    {
        if (getCoreStyle().setBorder(toBorderSettings))
        {
            invalidateStyle();
        }
    }
    
    /**
     * Checks if the controls background is opaque or transparent
     * @return true if opaque
     */
    @Override
    public boolean isOpaque()
    {
        return getStyle().getOpacity() >= 1f;
    }
    
    /**
     * Sets if the controls background is opaque or transparent
     * @param tlOpaque true if this should be opaque
     */
    @Goliath.Annotations.NotProperty
    @Override
    public void setOpacity(float tnAlpha)
    {
        if (getCoreStyle().setOpacity(tnAlpha))
        {
            getImplementation().setOpacity(tnAlpha, getImplementationControl());
            invalidateStyle();
        }
    }

    /**
     * Checks if the controls background is opaque or transparent
     * @return true if opaque
     */
    @Override
    public float getOpacity()
    {
        return getStyle().getOpacity();
    }
    
    /**
     * Checks if the size has been set using the setSize method
     * @return true if the size has been explicitly set
     */
    @Override
    public boolean isSizeSet()
    {
        return getStyle().isSizeSet();
    }
    
    /**
     * Sets the size of the control
     * @param toDimension the new dimensions of the control
     */
    @Override
    public boolean setSize(Dimension toDimension)
    {
        if (getControlBounds().setSize(toDimension))
        {
            // Set the implementation bounds
            getImplementation().setSize(getControlBounds().getSize(), getImplementationControl());
            invalidate();
            return true;
        }
        return false;
    }

    /**
     * Sets the size of the control
     * @param tnWidth the new width
     * @param tnHeight the new height
     */
    @Goliath.Annotations.NotProperty
    @Override
    public boolean setSize(float tnWidth, float tnHeight)
    {
        return setSize(new Dimension(tnWidth, tnHeight));
    }
    
    /**
     * Gets the size of the control as a dimension
     * @return the current dimension of the control
     */
    @Override
    public Dimension getSize()
    {
        return getControlBounds().getSize();
    }
    
    /**
     * Sets the content size of this control, this is similar to the css box model,
     * specifying the content size will add the margin, border, and padding to the size
     * @param toDimension the content dimensions of this control
     * @return true if the size was changed as a result of this call
     */
    @Override
    public boolean setContentSize(Dimension toDimension)
    {
        BoxDimension loMargin = getStyle().getMargin();
        BoxDimension loPadding = getStyle().getPadding();
        BorderSettings loBorder = getBorder();
        
        if (loMargin != null || loPadding != null || loBorder != null)
        {
            toDimension = new Dimension(
                    toDimension.getWidth() + 
                        (loMargin == null ? 0 : loMargin.getLeft() + loMargin.getRight()) +
                        (loPadding == null ? 0 : loPadding.getLeft() + loPadding.getRight()) +
                        (loBorder == null ? 0 : loBorder.getLeft() + loBorder.getRight()), 
                    toDimension.getHeight() + 
                        (loMargin == null ? 0 : loMargin.getTop() + loMargin.getBottom()) +
                        (loPadding == null ? 0 : loPadding.getTop() + loPadding.getBottom()) +
                        (loBorder == null ? 0 : loBorder.getTop() + loBorder.getBottom()));
        }
        
        if (getControlBounds().setSize(toDimension))
        {
            // Set the implementation bounds
            getImplementation().setSize(getControlBounds().getSize(), getImplementationControl());
            invalidate();
            m_oContentBounds = null;
            m_oContentSize = null;
            return true;
        }
        return false;
    }
    
    // TODO: SET/GET Content size should be refactored to ClientSize
    
    /**
     * Sets the content size of this control, this is similar to the css box model,
     * specifying the content size will add the margin, border, and padding to the size
     * @param tnWidth the content width of this control
     * @param tnHeight the content height of this control
     * @return true if the size was changed as a result of this call
     */
    @Goliath.Annotations.NotProperty
    @Override
    public boolean setContentSize(float tnWidth, float tnHeight)
    {
        return setContentSize(new Dimension(tnWidth, tnHeight));
    }
    
    /**
     * Gets the size of the control not including its margin, border, or padding
     * @return the content dimension of the control
     */
    @Override
    public Dimension getContentSize()
    {
        if (m_oContentSize == null)
        {
            m_oContentSize = getControlBounds().getSize();
            BoxDimension loMargin = getStyle().getMargin();
            BoxDimension loPadding = getStyle().getPadding();
            BorderSettings loBorder = getBorder();

            if (loMargin != null || loPadding != null || loBorder != null)
            {
                m_oContentSize = new Dimension(
                        m_oContentSize.getWidth() - 
                            (loMargin == null ? 0 : loMargin.getLeft() + loMargin.getRight()) -
                            (loPadding == null ? 0 : loPadding.getLeft() + loPadding.getRight()) -
                            (loBorder == null ? 0 : loBorder.getLeft() + loBorder.getRight()), 
                        m_oContentSize.getHeight() -
                            (loMargin == null ? 0 : loMargin.getTop() + loMargin.getBottom()) -
                            (loPadding == null ? 0 : loPadding.getTop() + loPadding.getBottom()) -
                            (loBorder == null ? 0 : loBorder.getTop() + loBorder.getBottom()));
            }
        }
        return m_oContentSize;
    }
    
    /**
     * Gets the content bounds of this control, this excludes the margin, border, and padding
     * @return the content bounds for this control
     */
    @Goliath.Annotations.NotProperty
    @Override
    public ControlBounds getContentBounds()
    {
        if (m_oContentBounds == null)
        {
            BoxDimension loMargin = getStyle().getMargin();
            BoxDimension loPadding = getStyle().getPadding();
            BorderSettings loBorder = getBorder();
            Dimension loSize = getContentSize();
            m_oContentBounds = new ControlBounds(
                    (loMargin == null ? 0 : loMargin.getLeft()) +
                    (loPadding == null ? 0 : loPadding.getLeft()) +
                    (loBorder == null ? 0 : loBorder.getLeft()),
                    
                    (loMargin == null ? 0 : loMargin.getTop()) +
                    (loPadding == null ? 0 : loPadding.getTop()) +
                    (loBorder == null ? 0 : loBorder.getTop()), 
                    
                    loSize.getWidth(),loSize.getHeight());
        }
        return m_oContentBounds;
    }
    
    /**
     * Sets the preferred size of this control, usually called by a layoutmanager
     * @param toDimension the preferred size of the control
     */
    @Override
    public boolean setPreferredSize(Dimension toDimension)
    {
        boolean llReturn = getControlBounds().setPreferredSize(toDimension);
        
        if (getImplementation().setSize(getControlBounds().getSize(), getControl()))
        {
            invalidate();
            return true;
        }
        return llReturn;
    }

    /**
     * Gets the preferred size of this control
     * @return the preferred size of the control
     */
    @Override
    public Dimension getPreferredSize()
    {

        ControlBounds loBounds = getControlBounds();
        Dimension loSize = loBounds.getPreferredSize();
        if (loSize.getHeight() == 0 || loSize.getWidth() == 0)
        {
            loBounds.setPreferredSize(getImplementation().getPreferredSize(getImplementationControl()));
        }
        return loBounds.getPreferredSize();
    }


    /**
     * Sets the minimum size the control is allowed to be, the control should
     * never be able to be set as smaller than this size.
     * If a control is set smaller than this size, it will just silently be set
     * to this minimum size
     * @param toDimension the new mimimum size of the control
     */
    @Override
    public void setMinSize(Dimension toDimension)
    {
        getControlBounds().setMinSize(toDimension);

        getImplementation().setMinSize(getControlBounds().getMinSize(), getImplementationControl());
        
        Dimension loSize = getSize();
        Dimension loMinSize = getMinSize();
        float lnWidth = loSize.getWidth();
        float lnHeight = loSize.getHeight();
        if (loSize.getWidth() < loMinSize.getWidth())
        {
            lnWidth = loMinSize.getWidth();
        }
        if (loSize.getHeight() < loMinSize.getHeight())
        {
            lnHeight = loMinSize.getHeight();
        }
        setPreferredSize(new Dimension(lnWidth, lnHeight));
    }

    /**
     * Sets the minimum size the control is allowed to be, the control should
     * never be able to be set as smaller than this size.
     * If a control is set smaller than this size, it will just silently be set
     * to this minimum size
     * @param tnWidth the minimum height of the control
     * @param tnHeight the minimum width of the control
     */
    @Goliath.Annotations.NotProperty
    @Override
    public void setMinSize(float tnWidth, float tnHeight)
    {
        setMinSize(new Dimension(tnWidth, tnHeight));
    }

    /**
     * Gets the minimum size of this control, if no minimum size is set, this
     * will return null
     * @return the minimum size, or null if no minimum was set
     */
    @Override
    public Dimension getMinSize()
    {   
        return getControlBounds().getMinSize();
    }


    /**
     * Sets the maximum size the control is allowed to be, the control should
     * never be able to be set as larger than this size.
     * If a control is set larger than this size, it will just silently be set
     * to this maximum size
     * @param toDimension the new maximum size of the control
     */
    @Override
    public void setMaxSize(Dimension toDimension)
    {
        getControlBounds().setMaxSize(toDimension);

        getImplementation().setMaxSize(getControlBounds().getMaxSize(), getImplementationControl());
        
        Dimension loSize = getSize();
        Dimension loMaxSize = getMaxSize();
        float lnWidth = loSize.getWidth();
        float lnHeight = loSize.getHeight();
        if (loSize.getWidth() > loMaxSize.getWidth())
        {
            lnWidth = loMaxSize.getWidth();
        }
        if (loSize.getHeight() > loMaxSize.getHeight())
        {
            lnHeight = loMaxSize.getHeight();
        }
        setPreferredSize(new Dimension(lnWidth, lnHeight));
    }


    /**
     * Sets the maximum size the control is allowed to be, the control should
     * never be able to be set as larger than this size.
     * If a control is set larger than this size, it will just silently be set
     * to this maximum size
     * @param tnWidth the maximum height of the control
     * @param tnHeight the maximum width of the control
     */
    @Goliath.Annotations.NotProperty
    @Override
    public void setMaxSize(float tnWidth, float tnHeight)
    {
        setMaxSize(new Dimension(tnWidth, tnHeight));
    }

    /**
     * Gets the maximum size of this control, if no maximum size is set, this
     * will return null
     * @return the maximum size, or null if no maximum was set
     */
    @Override
    public Dimension getMaxSize()
    {
        return getControlBounds().getMaxSize();
    }

    /**
     * Sets the location of the control
     * @param toPoint the new location of the control
     */
    @Override
    public void setLocation(Point toPoint)
    {
        if (getControlBounds().setLocation(toPoint))
        {
            getImplementation().setLocation(getControlBounds().getLocation(), getImplementationControl());
        }
    }

   /**
    * Sets the location of the control
    * @param tnX the new x position
    * @param tnY the new y position
    */
    @Override
    public void setLocation(float tnX, float tnY)
    {
        setLocation(new Point(tnX, tnY));
    }
    
    /**
     * Gets the location of the control
     * @return the current location of the control
     */
    @Override
    public Point getLocation()
    {
        return getImplementation().getLocation(getControl());
    }
    
    @Override
    public void setFontSize(float tnSize)
    {
        if (getCoreStyle().setFontSize(tnSize))
        {
            getImplementation().setFontSize(tnSize, getControl());
        }
    }
    
    @Override
    public float getFontSize()
    {
        return getStyle().getFontSize();
    }
    
    /**
     * Sets if this control should clear itself before it paints
     * @param tlClear true to clears
     */
    @Goliath.Annotations.NotProperty
    @Override
    public void setClearOnPaint(boolean tlClear)
    {
        if (m_lClearOnPaint != tlClear)
        {
            m_lClearOnPaint = tlClear;
            invalidate();
        }
    }
    
    
    
    /**
     * Checks if this control clears itself before painting
     * @return true if this control should clear itself when painting
     */
    @Override
    public boolean clearOnPaint()
    {
        return getImplementation().isOpaque(getControl()) || m_lClearOnPaint;
    }
    
    @Override
    public void setBackgroundLocation(Point toPosition)
    {
        if (getCoreStyle().setBackgroundLocation(toPosition))
        {
            invalidateStyle();
        }
    }
    
    @Override
    public void setBackgroundLocation(float tnX, float tnY)
    {
        setBackgroundLocation(new Point(tnX, tnY));
    }
    
    /**
     * Gets the location of the background
     * @return the current location of the background
     */
    @Override
    public Point getBackgroundLocation()
    {
        return getStyle().getBackgroundLocation();
    }

    
}
