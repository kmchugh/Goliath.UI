/* ========================================================
 * Skin.java
 *
 * Author:      kenmchugh
 * Created:     Nov 26, 2010, 9:38:06 AM
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

package Goliath.UI.Skin;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Constants.LogType;
import Goliath.Delegate;
import Goliath.DynamicCode.Java;
import Goliath.DynamicEnum;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Controls.Button;
import Goliath.UI.Controls.Container;
import Goliath.UI.Controls.Control;
import Goliath.UI.Controls.ControlImplementationType;
import Goliath.UI.Controls.ControlStyle;
import Goliath.UI.Controls.Label;
import Goliath.UI.UIEvent;
import Goliath.UI.Views.ChromeView;
import Goliath.UI.Views.View;
import Goliath.UI.Windows.ApplicationWindow;
import Goliath.UI.Windows.Window;
import java.awt.Color;
import java.io.File;
import java.util.Collections;
        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Nov 26, 2010
 * @author      kenmchugh
**/
public abstract class Skin extends DynamicEnum
{
    private static final String SKIN_CACHE_PROPERTY = "skin_cache";
    
    private static boolean g_lLocked;
    
    
    private static IDelegate g_oStyleChangedDelgate;
    private static HashTable<ControlImplementationType, PropertySet> g_oStyleDelegates;
    
    public static Skin getSkin(ControlImplementationType toType)
    {
        return getEnumerations(Skin.class).get(0);
    }
    
    /**
     * Creates the skin
     * @param tcValue the value for this skin
     */
    protected Skin(String tcValue)
    {
        super(tcValue);
    }
    
    
    public void renderBorder(IControl toControl, Object toRenderCanvas, ControlStyle loStyle)
    {
        
    }
    
    public void renderContent(IControl toControl, Object toRenderCanvas, ControlStyle loStyle)
    {
        
    }
    
    public void renderBackground(IControl toControl, Object toRenderCanvas, ControlStyle loStyle)
    {
        
    }
    
    
    
    
    
    
    
    /**
     * Renders the control according to the rules of the skin
     * @param toControl the control being rendered
     * @param toRenderCanvas the canvas that is being rendered to
     */
    public final void render(IControl toControl, Object toRenderCanvas)
    {
        onRender(toControl, toRenderCanvas);
    }
    
    /**
     * The render action for the skin
     * @param toControl the control to render
     * @param toRenderCanvas the canvas to render to
     * @param toClasses the list of classes in the order they are to be rendered in
     */
    protected void onRender(IControl toControl, Object toRenderCanvas)//, List<String> toClasses)
    {
        
    }
    
    /*
     * Applies the styles to the control specified
     */
    public final void styleControl(IControl toControl)
    {
        
    }
    
    /**
     * Marks the styles on the control as invalid
     * @param toControl the control to invalidate
     */
    public final void invalidateStyle(IControl toControl)
    {
        if (toControl.setProperty(SKIN_CACHE_PROPERTY, null) != null &&
            (Java.isEqualOrAssignable(IContainer.class, toControl.getClass())))
        {
            for (IControl loControl :((IContainer)toControl).getChildren())
            {
                invalidateStyle(loControl);
            }
            toControl.invalidate();
        }
    }
    
    
    
    
    /**
     * Gets the style delegate for this style and control implementation type
     * @param tcStyleClass the style class
     * @param toType the implementation type that we are looking for
     * @return the delegate that can be used for rendering
     */
    protected IDelegate getStyleDelegate(String tcStyleClass, ControlImplementationType toType)
    {
        tcStyleClass = tcStyleClass.toLowerCase();
        if (g_oStyleDelegates == null)
        {
            g_oStyleDelegates = new HashTable<ControlImplementationType, PropertySet>();
        }
        
        if (!g_oStyleDelegates.containsKey(toType))
        {
            g_oStyleDelegates.put(toType, new PropertySet());
        }
        
        if (!g_oStyleDelegates.get(toType).containsKey(tcStyleClass))
        {
            g_oStyleDelegates.get(toType).setProperty(tcStyleClass, createStyleDelegateFor(tcStyleClass, toType));
        }
        return g_oStyleDelegates.get(toType).getProperty(tcStyleClass, false);
    }
    
    /**
     * Creates a style delegate for the specified type
     * @param tcStyleClass the class to create the delegate for
     * @param toType the type of rendering
     * @return the rendering method
     */
    protected IDelegate createStyleDelegateFor(String tcStyleClass, ControlImplementationType toType)
    {
        return null;
    }
    
    
    
    /**
     * Gets the list of style classes from this control, in the order they should be rendered
     * @param toControl the control list
     * @return the list of style classes in the order of rendering
     */
    protected List<String> getClassList(IControl toControl)
    {
        List<String> loReturn = toControl.getProperty(SKIN_CACHE_PROPERTY);
        if (loReturn == null)
        {
            // Create and cache the list of classes
            loReturn = new List<String>();
            IControl loControl = toControl;
            while(loControl != null)
            {
                loReturn.add(loControl.getClass().getName());
                loControl = loControl.getParent();
            }
            Collections.reverse(loReturn);
            
            // Add the list of style classes
            loReturn.addAll(toControl.getClasses());
            
            toControl.setProperty(SKIN_CACHE_PROPERTY, loReturn);
            
            // If the control style classes are changed, clear the skin cache
            toControl.addEventListener(UIEventType.ONSTYLECHANGED(), getStyleChangedDelegate());
        }
        return loReturn;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Helper function to gets the style change delegate
     * @return the delegate
     */
    private IDelegate getStyleChangedDelegate()
    {
        if (g_oStyleChangedDelgate == null)
        {
            g_oStyleChangedDelgate = Delegate.build(this, "onStyleChanged");
        }
        return g_oStyleChangedDelgate;
    }
    
    /**
     * Called when the style on a control has been changed
     * @param toEvent the event that has occurred
     */
    private void onStyleChanged(UIEvent toEvent)
    {
        toEvent.getTarget().clearProperty(SKIN_CACHE_PROPERTY);
        toEvent.cancelBubble();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Helper function to extract the class of the actual control
     * @param toControl
     * @return 
     */
    protected Class getControlClass(IControl toControl)
    {
        return (Java.isEqualOrAssignable(ControlSkinDecorator.class, toControl.getClass()) ?
                ((ControlSkinDecorator)toControl).getBaseControl() : toControl).getClass();
    }
    
    /**
     * Should be called when a control is initialized or when a control needs to be redecorated
     * @param toControl the control being decorated
     */
    public final IControl decorateControl(IControl toControl)
    {
        Class loControlClass = getControlClass(toControl);
        
        if (Java.isEqualOrAssignable(Control.class, loControlClass))
        {
            // Set up the base decorator for this skin, all controls get the base decorators
            toControl = decorateCore(toControl);
        }
        
        if (Java.isEqualOrAssignable(Label.class, loControlClass))
        {
            // Set up the base decorator for this skin, all controls get the base decorators
            toControl = decorateLabelCore(toControl);
        }
        
        if (Java.isEqualOrAssignable(Button.class, loControlClass))
        {
            // Set up the base decorator for this skin, all controls get the base decorators
            toControl = decorateButtonCore(toControl);
        }
        
        if (Java.isEqualOrAssignable(Container.class, loControlClass))
        {
            // Set up the base decorator for this skin, all windows get the base decorators
            toControl = decorateContainerCore(toControl);
        }
        
        if (Java.isEqualOrAssignable(View.class, loControlClass))
        {
            // Set up the base decorator for this skin, all windows get the base decorators
            toControl = decorateViewCore(toControl);
        }
        
        if (Java.isEqualOrAssignable(ChromeView.class, loControlClass))
        {
            // Set up the base decorator for this skin, all windows get the base decorators
            toControl = decorateChromeViewCore(toControl);
        }
        
        if (Java.isEqualOrAssignable(Window.class, loControlClass))
        {
            // Set up the base decorator for this skin, all windows get the base decorators
            toControl = decorateWindowCore(toControl);
        }
        
        if (Java.isEqualOrAssignable(ApplicationWindow.class, loControlClass))
        {
            // Set up the base decorator for this skin, all application windows get the base decorators
            toControl = decorateApplicationWindowCore(toControl);
        }
        
        if (toControl.hasClasses())
        {
            List<String> loClasses = new List<String>(toControl.getClasses());
            // Decorate for the control classes
            for (String lcClass : loClasses)
            {
                Application.getInstance().log("Decorating control " + toControl.getClass().getName() + " with class " + lcClass, LogType.TRACE());
                toControl = decorateForClass(toControl, lcClass);
            }
        }
        
        return onDecorateControl(toControl);
    }
    
    private IControl decorateForClass(IControl toControl, String tcClass)
    {
        return onDecorateForClass(toControl, tcClass);
    }
    
    protected IControl onDecorateForClass(IControl toControl, String tcClass)
    {
        return toControl;
    }
    
    protected IControl onDecorateControl(IControl toControl)
    {
        return toControl;
    }
    
    private IControl decorateCore(IControl toControl)
    {
        return onDecorateCore(toControl);
    }
    
    protected IControl onDecorateCore(IControl toControl)
    {
        toControl = new ForegroundDecorator(toControl, new Color(0, 0, 0));
        return toControl;
    }
    
    private IControl decorateContainerCore(IControl toControl)
    {
        return onDecorateContainerCore(toControl);
    }
    
    protected IControl onDecorateContainerCore(IControl toControl)
    {
        toControl = new BackgroundDecorator(toControl, false);
        
        return toControl;
    }
    
    private IControl decorateWindowCore(IControl toControl)
    {
        return onDecorateWindowCore(toControl);
    }
    
    protected IControl onDecorateWindowCore(IControl toControl)
    {
        return toControl;
    }
    
    private IControl decorateApplicationWindowCore(IControl toControl)
    {
        return onDecorateApplicationWindowCore(toControl);
    }
    
    protected IControl onDecorateApplicationWindowCore(IControl toControl)
    {
        return toControl;
    }
    
    private IControl decorateViewCore(IControl toControl)
    {
        return onDecorateViewCore(toControl);
    }
    
    protected IControl onDecorateViewCore(IControl toControl)
    {
        return toControl;
    }
    
    private IControl decorateChromeViewCore(IControl toControl)
    {
        return onDecorateChromeViewCore(toControl);
    }
    
    protected IControl onDecorateChromeViewCore(IControl toControl)
    {
        toControl = new BackgroundDecorator(toControl, (File)null, false);
        return toControl;
    }
    
    private IControl decorateLabelCore(IControl toControl)
    {
        return onDecorateLabelCore(toControl);
    }
    
    protected IControl onDecorateLabelCore(IControl toControl)
    {
        toControl = new BorderDecorator(toControl, 5, 5, 5, 5);
        
        return toControl;
    }
    
    private IControl decorateButtonCore(IControl toControl)
    {
        return onDecorateButtonCore(toControl);
    }
    
    protected IControl onDecorateButtonCore(IControl toControl)
    {
        return toControl;
    }
    
    
    
    /*
    private HashTable<Class, Color> m_oBGColours;

    private HashTable<String, Color> m_oColourList;

    public Skin()
    {
    }

    public final <K extends IControl> Color getHighlightColour(Class<K> toClass)
    {
        String lcKey = "highlightColour_" + toClass.getName();
        if (m_oColourList != null && m_oColourList.containsKey(lcKey))
        {
            return m_oColourList.get(lcKey);
        }
        return getDefaultHighlightColour();
    }

    public final <K extends IControl> void setHighlightColour(Class<K> toClass, Color toColour)
    {
        setColour("highlightColour_" + toClass.getName(), toColour);
    }

    public final <K extends IControl> Color getBorderColour(Class<K> toClass)
    {
        String lcKey = "borderColour_" + toClass.getName();
        if (m_oColourList != null && m_oColourList.containsKey(lcKey))
        {
            return m_oColourList.get(lcKey);
        }
        return getDefaultBorderColour();
    }

    public final <K extends IControl> void setBorderColour(Class<K> toClass, Color toColour)
    {
        setColour("borderColour_" + toClass.getName(), toColour);
    }

    public final <K extends IControl> Color getTitleColor(Class<K> toClass)
    {
        String lcKey = "titleColour_" + toClass.getName();
        if (m_oColourList != null && m_oColourList.containsKey(lcKey))
        {
            return m_oColourList.get(lcKey);
        }
        return getDefaultTitleColour();
    }

    public final <K extends IControl> void setTitleColour(Class<K> toClass, Color toColour)
    {
        setColour("titleColour_" + toClass.getName(), toColour);
    }

    public final <K extends IControl> Color getBackgroundColour(Class<K> toClass)
    {
        String lcKey = "backgroundColour_" + toClass.getName();
        if (m_oColourList != null && m_oColourList.containsKey(lcKey))
        {
            return m_oColourList.get(lcKey);
        }
        return getDefaultBackgroundColour();
    }

    public final <K extends IControl> void setBackgroundColour(Class<K> toClass, Color toColour)
    {
        setColour("backgroundColour_" + toClass.getName(), toColour);
    }

    public final <K extends IControl> Color getForegroundColour(Class<K> toClass)
    {
        String lcKey = "foregroundColour_" + toClass.getName();
        if (m_oColourList != null && m_oColourList.containsKey(lcKey))
        {
            return m_oColourList.get(lcKey);
        }
        return getDefaultForegroundColour();
    }

    public final <K extends IControl> void setForegroundColour(Class<K> toClass, Color toColour)
    {
        setColour("foregroundColour_" + toClass.getName(), toColour);
    }

    public final Color getColour(String tcColour)
    {
        Color loReturn = null;
        String tcKey = tcColour.toLowerCase();

        if (m_oColourList != null)
        {
            loReturn = m_oColourList.get(tcKey);
        }
        return loReturn == null ? getDefaultColour() : loReturn;
    }

    public final void setColour(String tcColour, Color toColour)
    {
        String lcKey = tcColour.toLowerCase();

        if (m_oColourList == null)
        {
            m_oColourList = new HashTable<String, Color>();
        }
        m_oColourList.put(lcKey, toColour);
    }

    public Color getDefaultForegroundColour()
    {
        return getDefaultColour();
    }
    
    public Color getDefaultBackgroundColour()
    {
        return Color.WHITE;
    }

    public Color getDefaultBorderColour()
    {
        return getDefaultForegroundColour();
    }

    public Color getDefaultTitleColour()
    {
        return getDefaultForegroundColour();
    }

    public Color getDefaultHighlightColour()
    {
        return getDefaultBorderColour();
    }


    public Color getDefaultColour()
    {
        return Color.BLACK;
    }

    public final void setupSkin(IControl toControl)
    {
        Class loClass = toControl.getClass();
        toControl.setBackground(getBackgroundColour(loClass));
        toControl.setColour(getForegroundColour(loClass));
        onSetupSkin(toControl);

        for (String lcClass : toControl.getClasses())
        {
            onSetupSkinForClass(toControl, lcClass.toLowerCase());
        }
    }

    public final void setupSkinForClass(IControl toControl, String tcClass)
    {
        onSetupSkinForClass(toControl, tcClass.toLowerCase());
    }

    protected void onSetupSkin(IControl toControl)
    {

    }

    protected void onSetupSkinForClass(IControl toControl, String tcClassName)
    {

    }
     * 
     */

}
