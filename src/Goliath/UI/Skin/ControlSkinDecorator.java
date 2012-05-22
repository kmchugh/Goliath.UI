/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Goliath.UI.Skin;

import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Constants.EventType;
import Goliath.DynamicCode.Java;
import Goliath.Event;
import Goliath.Graphics.Constants.Position;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Image;
import Goliath.Graphics.Point;
import Goliath.Graphics.Rectangle;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.Interfaces.UI.Controls.Implementations.IControlImpl;
import Goliath.Interfaces.UI.Controls.Implementations.IImplementedControl;
import Goliath.UI.Controls.BorderSettings;
import Goliath.UI.Controls.ControlBase;
import Goliath.UI.Controls.ControlBounds;
import Goliath.UI.Controls.ControlImplementationType;
import Goliath.UI.Controls.ControlStyle;
import java.awt.Color;

/**
 * A SkinDecorator is meant to wrap a control class and add styling features.
 * If this class does not explicitly specify a property, then the property from 
 * the contained control class would be used
 * 
 * @author kmchugh
 */
public abstract class ControlSkinDecorator extends ControlBase
{
    private IControl m_oControl;
    private ControlSkinDecorator m_oDecorating;
    private ControlSkinDecorator m_oDecoratedBy;
    
    /**
     * Creates a new decorator and wraps toControl with it
     * @param toControl the control to wrap
     */
    public ControlSkinDecorator(IControl toControl)
    {
        this(true, toControl);
    }
    
    /**
     * Gets a reference to the control that is being decorated.  This will
     * get the base control no matter how many decorators are being used
     * @return the control being decorated
     */
    protected IControl getDecoratedControl()
    {
        return m_oControl;
    }
    
    
    /**
     * Wraps the specified IControl with this decorator, this will return either
     * the control passed in or the new wrapped control depending on if the wrapping
     * was successful.  This allows chained calls.
     * @param toDecorator the decorator that will be used to wrap the control
     * @param toControl the control to wrap
     * @return the control that should be referenced after calls to the decorator
     */
    public static IControl wrap(ControlSkinDecorator toDecorator, IControl toControl)
    {
        IControl loReturn = toControl;
        // Ensure we can wrap the control, and ensure the control is not already 
        // decorated with this decorator
        if (toDecorator.canWrap(toControl) && !toDecorator.isWrapping(toControl))
        {
            // TODO: Wrap the control here
            loReturn = toControl;
        }
        return loReturn;
    }
    
    /**
     * Checks if the control can be wrapped by this decorator
     * @param toControl the control that we are checking
     * @return true if we can decorate this control, false otherwise
     */
    public boolean canWrap(IControl toControl)
    {
        // Checks if we can wrap the control specified
        return true;
    }
    
    /**
     * Checks if this decorator is already wrapping the control specified
     * @param toControl the control to check
     * @return true if we are wrapping this control, false otherwise
     */
    public boolean isWrapping(IControl toControl)
    {
        return false;
    }
    
    /**
     * Dispases of all resources used by this skin decorator
     */
    @Override
    public final void dispose()
    {
        
    }

    @Override
    public IControlImpl getImplementation()
    {
        return null;
    }

    @Override
    public String getClassString()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    @Override
    public void forceRender()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ControlBounds getContentBounds()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dimension getContentSize()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setContentSize(Dimension toDimension)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setContentSize(float tnWidth, float tnHeight)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    

    @Override
    public void forceRender(long tnRenderDelay)
    {
    }

    @Override
    public void forceRender(Point toStart, Dimension toArea)
    {
    }

    @Override
    public void forceRender(long tnRenderDelay, Point toStart, Dimension toArea)
    {
    }

    @Override
    public BorderSettings getBorder()
    {
        return null;
    }

    @Override
    public void setBorder(BorderSettings toBorder)
    {
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
            
    /**
     * Creates a new decorator and wraps toControl with it
     * @param toControl the control to wrap
     */
    public ControlSkinDecorator(boolean tlMerge, IControl toControl)
    {
        if (Java.isEqualOrAssignable(ControlSkinDecorator.class, toControl.getClass()))
        {
            m_oDecorating = (ControlSkinDecorator)toControl;
            ((ControlSkinDecorator)toControl).m_oDecoratedBy = this;
            m_oControl = (IControl)((ControlSkinDecorator)toControl).m_oControl;
            
            // If this control is decorated by this class already, then we need to merge
            if (isDecoratedBy((Class<ControlSkinDecorator>)this.getClass()))
            {
                ControlSkinDecorator loDecorator = removeDecoration((Class<ControlSkinDecorator>)this.getClass());
                if (tlMerge)
                {
                    mergeWith(loDecorator);
                }
            }
        }
        else
        {
            m_oControl = (IControl)toControl;
        }
    }
    
    /**
     * Helper function to merge the lower level decoration with this one.  Both decorations will be of the same type.
     * This is being done in the constructor before any values for the current constructor are set.  So the method should be
     * - Overwrite the variables from the merged decoration (toDecoration)
     * - Allow the constructor to complete, writing more values over the values from the merged decoration
     * @param toDecoration the decoration to merge with this one.
     */
    protected abstract void mergeWith(ControlSkinDecorator toDecoration);
    
    /**
     * Helper function to find and remove the decoration specified by the class type
     * @param toClass the class to remove the decoration
     * @return the decoration that has been removed, or null if no decoration has been removed
     */
    public final ControlSkinDecorator removeDecoration(Class<ControlSkinDecorator> toClass)
    {
        if (m_oDecorating.getClass().equals(toClass))
        {
            // need to remove the previous decoration
            ControlSkinDecorator loReturn = m_oDecorating;
            m_oDecorating = loReturn.m_oDecorating;
            if (m_oDecorating != null)
            {
                m_oDecorating.m_oDecoratedBy = this;
            }
            
            loReturn.m_oDecorating = null;
            loReturn.m_oDecoratedBy = null;
            return loReturn;
        }
        else
        {
            return m_oDecorating.removeDecoration(toClass);
        }
    }
    
    /**
     * Helper function to determine if this Control is already decorated by a decorator of this type
     * @param toClass the decorator class to check for
     * @return true if the control is already decorated, false otherwise
     */
    public final boolean isDecoratedBy(Class<ControlSkinDecorator> toClass)
    {
        return m_oDecorating == null ? false : m_oDecorating.getClass().equals(toClass) || m_oDecorating.isDecoratedBy(toClass);
    }
            
    
    /**
     * Gets the base control that is being decorated, this will return null if
     * the control is not of the type expected by the decorator
     * @return the base control being decorated
     */
    public final IControl getBaseControl()
    {
        return Java.isEqualOrAssignable(Java.getClassFromType(Java.getMethod(this.getClass(), "getBaseControl", null).getGenericReturnType()), m_oControl.getClass()) ? m_oControl : null;
    }
    
    /**
     * Gets the object that is being decorated by this decorator
     * @return 
     */
    protected final IControl getDecorating()
    {
        return m_oDecorating;
    }


    @Override
    public <T> T clearProperty(String tcProperty)
    {
        return m_oControl.<T>clearProperty(tcProperty);
    }

    @Override
    public void fireEvent(EventType toEventType, Event<? extends IControl> toEvent)
    {
        m_oControl.fireEvent(toEventType, toEvent);
    }

    @Override
    public boolean addEventListener(EventType toEvent, IDelegate toCallback)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean areEventsSuppressed()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean clearEventListeners()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean clearEventListeners(EventType toEvent)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getFontSize()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ControlStyle getStyle()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    
    

    @Override
    public Dimension getSize()
    {
        return m_oControl.getSize();
    }

    @Override
    public Color getColour()
    {
        return m_oControl.getColour();
    }

    @Override
    public Dimension getMaxSize()
    {
        return m_oControl.getMaxSize();
    }

    @Override
    public Dimension getMinSize()
    {
        return m_oControl.getMinSize();
    }

    @Override
    public void setColour(Color toColour)
    {
        m_oControl.setColour(toColour);
    }

    @Override
    public void setMaxSize(Dimension toDimension)
    {
        m_oControl.setMaxSize(toDimension);
    }

    @Override
    public void setMaxSize(float tnWidth, float tnHeight)
    {
        m_oControl.setMaxSize(tnWidth, tnHeight);
    }

    @Override
    public void setMinSize(Dimension toDimension)
    {
        m_oControl.setMinSize(toDimension);
    }

    @Override
    public void setMinSize(float tnWidth, float tnHeight)
    {
        m_oControl.setMinSize(tnWidth, tnHeight);
    }
    
    

    @Override
    public boolean setSize(Dimension toDimension)
    {
        return m_oControl.setSize(toDimension);
    }

    @Override
    public boolean setSize(float tnWidth, float tnHeight)
    {
        return m_oControl.setSize(tnWidth, tnHeight);
    }
    
    

    @Override
    public ControlImplementationType getImplementationType()
    {
        return m_oControl.getImplementationType();
    }

    @Override
    public String getName()
    {
        return m_oControl.getName();
    }
    
    @Override
    public List<String> getClasses()
    {
        return m_oControl.getClasses();
    }

    @Override
    public Image getBackgroundImage()
    {
        return m_oControl.getBackgroundImage();
    }

    @Override
    public void setBackground(Color toColour)
    {
        m_oControl.setBackground(toColour);
    }

    @Override
    public void setBackground(Image toBackground)
    {
        m_oControl.setBackground(toBackground);
    }

    @Override
    public void addClass(String tcClass)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearClasses()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean clearOnPaint()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color getBackgroundColour()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Position getBackgroundImageAlignment()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Point getBackgroundLocation()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Rectangle getContentRectangle()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IImplementedControl getControl()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ControlBounds getControlBounds()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Point getLocation()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IWindow getParentWindow()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dimension getPreferredSize()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T getProperty(String tcProperty, T toDefault)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Point getScreenCoordinates()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasBorder()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasClass(String tcClass)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDisplayable()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEditable()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEnabled()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isOpaque()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSelectable()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isShowing()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isVisible()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean participatesInLayout()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeClass(String tcClass)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(Object toRenderCanvas)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBackgroundImageAlignment(Position toAlignment)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBackgroundLocation(float tnX, float tnY)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBackgroundLocation(Point toPoint)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setClearOnPaint(boolean tlClear)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEditable(boolean tlVisible)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEnabled(boolean tlEnabled)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFocus()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFontSize(float tnSize)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLocation(float tnX, float tnY)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLocation(Point toPoint)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getOpacity()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOpacity(float tnAlpha)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    @Override
    public void setParticipatesInLayout(boolean tlParticipate)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setPreferredSize(Dimension toDimension)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSelectable(boolean tlSelectable)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setVisible(boolean tlVisible)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    

    @Override
    public IContainer getParent()
    {
        return m_oControl.getParent();
    }

    @Override
    public PropertySet getProperties()
    {
        return m_oControl.getProperties();
    }

    @Override
    public <T> T getProperty(String tcProperty)
    {
        return m_oControl.<T>getProperty(tcProperty);
    }

    @Override
    public String getTooltip()
    {
        return m_oControl.getTooltip();
    }

    @Override
    public boolean hasClasses()
    {
        return m_oControl.hasClasses();
    }

    @Override
    public boolean hasEventsFor(EventType toEvent)
    {
        return m_oControl.hasEventsFor(toEvent);
    }

    @Override
    public boolean hasProperties()
    {
        return m_oControl.hasProperties();
    }

    @Override
    public boolean isInvalidated()
    {
        return m_oControl.isInvalidated();
    }

    @Override
    public boolean isSizeSet()
    {
        return m_oControl.isSizeSet();
    }
    
    @Override
    public void invalidate()
    {
        m_oControl.invalidate();
    }

    @Override
    public void update()
    {
        m_oControl.update();
    }

    @Override
    public boolean removeEventListener(EventType toEvent, IDelegate toCallback)
    {
        return m_oControl.removeEventListener(toEvent, toCallback);
    }

    @Override
    public void setName(String tcName)
    {
        m_oControl.setName(tcName);
    }

    @Override
    public <T> T setProperty(String tcProperty, T toValue)
    {
        return m_oControl.setProperty(tcProperty, toValue);
    }

    @Override
    public void setTooltip(String tcValue)
    {
        m_oControl.setTooltip(tcValue);
    }

    @Override
    public void suppressEvents(boolean tlSuppress)
    {
        m_oControl.suppressEvents(tlSuppress);
    }

    @Override
    public final boolean equals(java.lang.Object toObject)
    {
        return m_oControl.equals(toObject);
    }

    @Override
    public final int hashCode() 
    {
        return m_oControl.hashCode();
    }
    
    
}
