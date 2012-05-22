/* =========================================================
 * Window.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * A window is a control that has chrome.  A window can only
 * contain views and a window has a WindowController which
 * controls things like current view and actions that can be taken.
 * Menus can be associated with windows.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.UI.Windows;

import Goliath.UI.Controls.ControlBounds;
import Goliath.UI.Controls.ControlDragManager;
import Goliath.UI.Controls.ControlResizeManager;
import Goliath.Collections.List;
import Goliath.UI.Views.View;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Exceptions.InvalidParameterException;
import Goliath.Graphics.BoxDimension;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IView;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.Interfaces.UI.Controls.Implementations.IWindowImpl;
import Goliath.UI.ControlPropertySet;
import Goliath.UI.Controls.Container;
import Goliath.UI.Controls.ControlImplementationType;
import Goliath.UI.Controls.Screen;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Controls.BorderSettings;
import Goliath.UI.Controls.Layouts.WindowLayoutManager;
import Goliath.UI.Skin.Skin;
import Goliath.UI.Skin.SkinManager;
import Goliath.UI.Views.ChromeView;

/**
 *
 * @author kmchugh
 */
public class Window extends Container
        implements IWindow
{
    private static IDelegate<Event<IWindow>> g_oModalWindowDeactivated;

    private boolean m_lActualChrome;
    private boolean m_lIsModal;
    private ChromeView m_oChromeView;
    private Dimension m_oContentSize;
    private ControlBounds m_oContentBounds;


    // TODO: Fix the invalidate with explicity sized windows

    /**
     * Centers the window on the screen
     * @param toWindow the window to center
     */
    public static void centerOnScreen(IWindow toWindow)
    {
        centerOnScreen(toWindow, 0);
    }

    /**
     * Centers the window on the specified screen
     * @param toWindow the window to center
     * @param tnScreen the screen to center on
     */
    public static void centerOnScreen(IWindow toWindow, int tnScreen)
    {
        Screen loScreen = new Screen(toWindow.getImplementationType());
        Dimension loSize = loScreen.getSize(tnScreen);
        Dimension loWindowSize = toWindow.getSize();

        toWindow.setLocation((loSize.getWidth() - loWindowSize.getWidth()) /2,
                (loSize.getHeight() - loWindowSize.getHeight()) /2);
    }

    /**
     * Centers toWindow against toCenter
     * @param toWindow the window to position
     * @param toCenter the window to use to calculate the position
     */
    public static void centerWithWindow(IWindow toWindow, IWindow toCenter)
    {
        Dimension loWindowSize = toCenter.getSize();
        Point loWindowLocation = toCenter.getLocation();
        float lnCenterX = loWindowLocation.getX() + (loWindowSize.getWidth() /2f);
        float lnCenterY = loWindowLocation.getY() + (loWindowSize.getHeight() /2f);

        Dimension loPositionSize = toWindow.getSize();


        toWindow.setLocation(lnCenterX - (Math.max(0, loPositionSize.getWidth()/2f)), Math.max(0, lnCenterY - (loPositionSize.getHeight()/2f)));
    }

    /**
     * Creates a new instance of Window
     */
    public Window()
    {
        initialiseComponent();
    }

    /**
     * Creates a window with the specified view
     * @param toView the view to create the window with
     */
    public Window(IView toView)
    {
        initialiseComponent();
        addControl(toView);
    }

    public Window(Goliath.Graphics.Image toBackground)
    {
        initialiseComponent();
        setBackground(toBackground);
    }

    public Window(ControlImplementationType toType)
    {
        super(toType);
        initialiseComponent();
    }

    public Window(Goliath.Graphics.Image toBackground, ControlImplementationType toType)
    {
        super(toType);
        initialiseComponent();
        setBackground(toBackground);
    }

    /**
     * Initialises the window
     */
    private void initialiseComponent()
    {
        // By default windows are opaque
        m_lActualChrome = true;
        setClearOnPaint(true);
        setLayoutManager(WindowLayoutManager.class);
        this.addEventListener(Goliath.UI.Constants.UIEventType.ONCLOSING(), Delegate.build(this, "onWindowClosing"));
    }
    
    /**
     * Sends the window to the front, making it a top level window.
     */
    public void sendToFront()
    {
        getWindowImplementation().sendToFront(getControl());
    }
    
    /**
     * Sends the window to the back, making it a bottom level window.
     */
    public void sendToBack()
    {
        getWindowImplementation().sendToBack(getControl());
    }

    /**
     * The window content size does not use the margin for calculation
     * @return the content dimension of the control
     */
    @Override
    public Dimension getContentSize()
    {
        if (m_oContentSize == null)
        {
            m_oContentSize = getControlBounds().getSize();
            BoxDimension loPadding = getStyle().getPadding();
            BorderSettings loBorder = getBorder();

            if (loPadding != null || loBorder != null)
            {
                m_oContentSize = new Dimension(
                        m_oContentSize.getWidth() - 
                            (loPadding == null ? 0 : loPadding.getLeft() + loPadding.getRight()) -
                            (loBorder == null ? 0 : loBorder.getLeft() + loBorder.getRight()), 
                        m_oContentSize.getHeight() -
                            (loPadding == null ? 0 : loPadding.getTop() + loPadding.getBottom()) -
                            (loBorder == null ? 0 : loBorder.getTop() + loBorder.getBottom()));
            }
        }
        return m_oContentSize;
    }
    
    /**
     * Gets the content bounds of this control, this excludes the border and padding
     * @return the content bounds for this control
     */
    @Override
    public ControlBounds getContentBounds()
    {
        if (m_oContentBounds == null)
        {
            BoxDimension loPadding = getStyle().getPadding();
            BorderSettings loBorder = getBorder();
            Dimension loSize = getContentSize();
            m_oContentBounds = new ControlBounds(
                    (loPadding == null ? 0 : loPadding.getLeft()) +
                    (loBorder == null ? 0 : loBorder.getLeft()),
                    
                    (loPadding == null ? 0 : loPadding.getTop()) +
                    (loBorder == null ? 0 : loBorder.getTop()), 
                    
                    loSize.getWidth(),loSize.getHeight());
        }
        return m_oContentBounds;
    }
    
    /**
     * Sets the content size of this control, this is similar to the css box model,
     * specifying the content size will add the border and padding to the size
     * @param toDimension the content dimensions of this control
     * @return true if the size was changed as a result of this call
     */
    @Override
    public boolean setContentSize(Dimension toDimension)
    {
        BoxDimension loPadding = getStyle().getPadding();
        BorderSettings loBorder = getBorder();
        
        if (loPadding != null || loBorder != null)
        {
            toDimension = new Dimension(
                    toDimension.getWidth() + 
                        (loPadding == null ? 0 : loPadding.getLeft() + loPadding.getRight()) +
                        (loBorder == null ? 0 : loBorder.getLeft() + loBorder.getRight()), 
                    toDimension.getHeight() + 
                        (loPadding == null ? 0 : loPadding.getTop() + loPadding.getBottom()) +
                        (loBorder == null ? 0 : loBorder.getTop() + loBorder.getBottom()));
        }
        
        if (getControlBounds().setSize(toDimension))
        {
            // Set the implementation bounds
            getImplementation().setSize(getControlBounds().getSize(), getControl());
            invalidate();
            m_oContentBounds = null;
            m_oContentSize = null;
            return true;
        }
        return false;
    }

    /**
     * Checks if this window is a modal window or not
     * @return true if modal
     */
    @Override
    public boolean getModal()
    {
        return m_lIsModal;
    }
    
    /**
     * Sets if this window should be modal
     * @param tlModal true if modal, false otherwise
     */
    @Override
    public void setModal(boolean tlModal)
    {
        if (m_lIsModal != tlModal)
        {
            // TODO: Currently this works until a user clicks on another application, then back to another winodw in this app.  We need to create a window manager to force modal windows
            m_lIsModal = tlModal;
            setAlwaysOnTop(tlModal);
            if (m_lIsModal)
            {
                setFocus();
                addEventListener(UIEventType.ONDEACTIVATED(), getModalWindowDeactivatedDelegate());
            }
            else
            {
                removeEventListener(UIEventType.ONDEACTIVATED(), getModalWindowDeactivatedDelegate());
            }
        }
    }

    @Override
    protected void onUpdate()
    {
        // Update the location to include the margin
        BoxDimension loDim = getStyle().getMargin();
        if (loDim != null)
        {
            if (loDim.isAuto())
            {
                Dimension loSize = getSize();
                Dimension loScreenSize = Screen.getScreenSize(getImplementationType());
                setLocation((loScreenSize.getWidth() - loSize.getWidth()) /2, (loScreenSize.getHeight() - loSize.getHeight()) /2);
            }
            else
            {
                //getImplementation().setLocation(new Point(loLocation.getIntX() + loDim.getLeft(), loLocation.getIntY() + loDim.getTop()), getControl());
            }
        }
        if (!isLayoutSuspended())
        {
            super.onUpdate();
        }
    }

    @Override
    public boolean setSize(Dimension toDimension)
    {
        m_oContentBounds = null;
        m_oContentSize = null;
        return super.setSize(toDimension);
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
        else if (!isSizeSet())
        {
            // This means that the size has been manually changed
            fireEvent(UIEventType.ONSIZECHANGING());
        }
        return llReturn;
    }
    
    /**
     * Sets the window up to use custom chrome rendering
     * @param tlUseCustom true to use custom chrome
     */
    @Override
    public void useCustomChrome(boolean tlUseCustom)
    {
        boolean llChrome = m_lActualChrome;
        setShowChrome(!tlUseCustom ? llChrome : !tlUseCustom);
        m_lActualChrome = llChrome;
        if (!tlUseCustom)
        {
            if (m_oChromeView != null)
            {
                View loView = m_oChromeView.getView();
                m_oChromeView = null;
                if (loView != null)
                {
                    addControl(loView);
                }
            }
        }
        else
        {
            createChromeView();
        }
    }
    
    /**
     * Helper function to add dragging capabilities to this window 
     */
    private void addDragManager()
    {
        if (m_oChromeView != null)
        {
            ControlDragManager.getInstance().registerControl(this, new List<IControl>(new IControl[]{this, m_oChromeView}));
        }
    }
    
    private void createChromeView()
    {
        if (m_oChromeView == null)
        {
            ChromeView loView = new ChromeView();

            List<IControl> loChildren  = getChildren();
            if(loChildren.size() > 0)
            {
                loView.addControl(loChildren.get(0));
            }
            addControl(0, loView, null);
            m_oChromeView = loView;
            ControlResizeManager.getInstance().registerControl(this, new List<IControl>(new IControl[]{this, m_oChromeView}));
            addDragManager();
        }
    }

    @Override
    public final boolean usesCustomChrome()
    {
        return m_oChromeView != null;
    }

    @Override
    public final boolean minimise()
    {
        if (!isMinimised())
        {
            getWindowImplementation().minimise(getControl());
            return true;
        }
        return false;
    }

    @Override
    public final boolean isMinimised()
    {
        return getWindowImplementation().isMinimised(getControl());
    }

    @Override
    public final boolean maximise()
    {
        if (!isMaximised())
        {
            getWindowImplementation().maximise(getControl());
            return true;
        }
        return false;
    }

    @Override
    public final boolean isMaximised()
    {
        return getWindowImplementation().isMaximised(getControl());
    }

    @Override
    public final boolean restore()
    {
        if (!isNormal())
        {
            getWindowImplementation().restore(getControl());
            return true;
        }
        return false;
    }

    @Override
    public final boolean isNormal()
    {
        return getWindowImplementation().isNormal(getControl());
    }

    private void onWindowDeactivated(Event<IWindow> toEvent)
    {
        toEvent.getTarget().setFocus();
    }

    // TODO: Change both modal event delegate methods to static methods
    /**
     * Helper function for creating the deactivation listener for modal windows
     * @return the deactivation listener
     */
    private IDelegate<Event<IWindow>> getModalWindowDeactivatedDelegate()
    {
        if (g_oModalWindowDeactivated == null)
        {
            g_oModalWindowDeactivated = Delegate.build(this, "onWindowDeactivated");
        }
        return g_oModalWindowDeactivated;
    }
    
    @Override
    public boolean addControl(int tnIndex, IControl toControl, ControlPropertySet toLayoutParameters)
    {
        boolean llReturn = false;
        if (!Goliath.DynamicCode.Java.isEqualOrAssignable(View.class, toControl.getClass()))
        {
            throw new InvalidParameterException("Only Views are allowed to be added to Windows, a " + toControl.getClass().getName() + " can not be added.", "toControl");
        }
        
        // Only one view can be contained and displayed at a time
        if (usesCustomChrome())
        {
            View loView = m_oChromeView.getView();
            if (loView != null)
            {
                m_oChromeView.removeControl(loView);
            }
            llReturn = m_oChromeView.addControl(0, toControl, toLayoutParameters);
        }
        else
        {
            clearChildren();
            llReturn = super.addControl(0, toControl, toLayoutParameters);
        }
        return llReturn;
    }

    /**
     * Event that occurs when the window is about to close, closing can be cancelled here.
     * @param toEvent the event that occurred
     */
    private void onWindowClosing(Object toEvent)
    {
        // TODO: hooks for prompting before closing the window.

        close();
    }

    /**
     * Helper function to get the window implementation
     * @return the window implementation
     */
    protected IWindowImpl getWindowImplementation()
    {
        return (IWindowImpl)getImplementation();
    }


    /**
     * Gets the title of the window
     * @return
     */
    @Override
    public String getTitle()
    {
        return getWindowImplementation().getTitle(getControl());
    }

    /**
     * Sets the new window title
     * @param tcTitle the new title
     */
    @Override
    public void setTitle(String tcTitle)
    {
        getWindowImplementation().setTitle(tcTitle, getControl());
        if (usesCustomChrome())
        {
            m_oChromeView.setTitle(tcTitle);
        }
    }

    /**
     * Checks if this window draws all of its chrome as well
     * @return true if the chrome is drawn, false if not
     */
    @Override
    public boolean getShowChrome()
    {
        return this.usesCustomChrome() ? m_lActualChrome : getWindowImplementation().getShowChrome(getControl());
    }

    /**
     * Sets if the window should always be on top of other windows
     * @param tlOnTop true if it should always be on top
     */
    @Override
    public void setAlwaysOnTop(boolean tlOnTop)
    {
        getWindowImplementation().setAlwaysOnTop(tlOnTop, getControl());
    }

    /**
     * Sets if this windows should draw it's chrome or not
     * @param tlShow true to draw the chrome
     */
    @Override
    public final void setShowChrome(boolean tlShow)
    {
        m_lActualChrome = tlShow;
        getWindowImplementation().setShowChrome(usesCustomChrome() ? false : tlShow, getControl());
        if (usesCustomChrome())
        {
            ControlResizeManager.getInstance().registerControl(this);
            addDragManager();
        }
    }

    /**
     * Checks if this window will always be displayed on top of other windows
     * @return true if it is always to be displayed on top of other windows.
     */
    @Override
    public boolean getAlwaysOnTop()
    {
        return getWindowImplementation().getAlwaysOnTop(getControl());
    }

    @Override
    public void setVisible(boolean tlVisible)
    {
        if (tlVisible)
        {
            Skin loSkin = SkinManager.getInstance().getCurrentSkin();
            if (loSkin != null)
            {
                loSkin.styleControl(this);
            }
        }
        super.setVisible(tlVisible);
    }
    
    


    /**
     * Gets the view that is contained by this window, if there is no view, this
     * will return null
     * @return the view for this window
     */
    @Override
    public View getView()
    {
        // If this uses a chrome view, then return the chrome views view
        if (this.usesCustomChrome())
        {
            return (m_oChromeView != null) ? m_oChromeView.getView() : null;
        }
        else
        {
            List<IControl> loControls = getChildren();
            return (View)(loControls != null && loControls.size() > 0 ? loControls.get(0) : null);
        }
    }
    
    /**
     * Gets the chrome view for the window if it exists
     * @return the chrome view for the window
     */
    public ChromeView getChromeView()
    {
        return m_oChromeView;
    }

    /**
     * Sets the window view to this view, this is a shortcut to addControl(0, toView, null);
     * @param toView the view to set
     */
    @Override
    public void setView(View toView)
    {
        addControl(0, toView, null);
    }



    /**
     * Closes the window
     */
    @Override
    public final void close()
    {
        if (m_lIsModal)
        {
            removeEventListener(UIEventType.ONDEACTIVATED(), getModalWindowDeactivatedDelegate());
        }
        /**
         * TODO: Implement a prompt if the window (or window view) is modified
         */
        getWindowImplementation().close(getControl());
    }

    /**
     * A mutator method for UsingCustomChrome as a class property
     * @param tlUsingCustomChrome   true for using custom chrome, and false otherwise.
     */
    public void setUsingCustomChrome(boolean tlUsingCustomChrome)
    {
        // Note: this method is created as a helper for translating window object to xml
        useCustomChrome(tlUsingCustomChrome);
    }

    /**
     * An accessor method for UsingCustomChrome as a class property
     * @return  true if the window uses custom chrome, false otherwise.
     */
    public boolean getUsingCustomChrome()
    {
        // Note: this method is created as a helper for translating window object to xml
        return usesCustomChrome();
    }
}
