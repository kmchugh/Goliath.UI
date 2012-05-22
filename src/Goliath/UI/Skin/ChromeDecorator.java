/* ========================================================
 * ChromeDecorator.java
 *
 * Author:      kmchugh
 * Created:     Dec 1, 2010, 2:39:04 PM
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
import Goliath.Graphics.Constants.Position;
import Goliath.Graphics.Image;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.UI.Views.ChromeView;
import Goliath.UI.Controls.Layouts.WindowChromeLayoutManager;
import Goliath.UI.Windows.Window;

/**
 * Adds custom chrome to the window that has been passed in,
 * this decorator will only have an effect on windows that do not already have a 
 * chrome decorator
 *
 * @see         Related Class
 * @version     1.0 Dec 1, 2010
 * @author      kmchugh
**/
public class ChromeDecorator extends ControlSkinDecorator
{
    private Position m_oPosition;
    private Image m_oTitleBackground;
    private ChromeView m_oChromeView;

    /**
     * Adds the chrome decorator to the specified control, this will
     * wrap the control with custom chrome
     * @param toControl the window to wrap
     */
    public ChromeDecorator(IControl toControl)
    {
        this(toControl, null, Position.TOP_CENTER(), true, 30);
    }
    
    /**
     * Wraps the window in custom chrome, and can show or hide the 
     * title bar as needed
     * @param toControl the c
     * @param tlShowChrome 
     */
    public ChromeDecorator(IControl toControl, boolean tlTitleBar)
    {
        this(toControl, null, Position.TOP_CENTER(), true, 30);
        ((Window)getBaseControl()).setShowChrome(tlTitleBar);
        ChromeView loView = getChromeView();
        if (loView != null)
        {
            //loView.setBorderSize(0, 0, 0, 0);
        }
    }
    
    /**
     * Adds the chrome decorator to the specified control, this will
     * wrap the control with custom chrome
     * @param toControl the control to wrap
     * @param tcTitleBackground the background image for the title label
     * @param toTitlePosition the offset position for the title
     * @param tlShowTitle specified if the title should be shown or not
     * @param tnTitleHeight the height of the title
     */
    public ChromeDecorator(IControl toControl, String tcTitleBackground, Position toTitlePosition, boolean tlShowTitle, float tnTitleHeight)
    {
        this(true, toControl, tcTitleBackground, toTitlePosition, tlShowTitle, tnTitleHeight);
    }
    
    /**
     * Merges this Chrome Decorator with one that has already been applied, if there is one already
     * @param tlMerge true to merge
     * @param toControl the control to wrap
     * @param tcTitleBackground the background image for the title label
     * @param toTitlePosition the offset position for the title
     * @param tlShowTitle specified if the title should be shown or not
     * @param tnTitleHeight the height of the title
     */
    public ChromeDecorator(boolean tlMerge, IControl toControl, String tcTitleBackground, Position toTitlePosition, boolean tlShowTitle, float tnTitleHeight)
    {
        super(tlMerge, toControl);
        
        // Only take action if this is the right type of control
        IWindow loBase = (IWindow)getBaseControl();
        if (loBase != null)
        {
            m_oPosition = toTitlePosition;
            loBase.useCustomChrome(true);
            ChromeView loView = getChromeView();
            
            showTitleLabel(tlShowTitle);
            showMinimizeButton(true);
            showMaximizeButton(true);
            showCloseButton(true);
            
            // Update the background image for the window
            if (tcTitleBackground != null)
            {
                try
                {
                    m_oTitleBackground = new Image(tcTitleBackground);
                    m_oTitleBackground.setScaleable(true);
                    m_oTitleBackground.setMaintainRatio(false);
                    m_oTitleBackground.setScale9Grid(10, 10, 10, 10);
                    loView.setTitleLabelBackground(m_oTitleBackground);
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }
            
            // Update the border size based on the title position
            if (m_oPosition == Position.TOP_CENTER())
            {
                //loView.setBorderSize(tnTitleHeight, 0, 0, 0);
            }
            else if (m_oPosition == Position.MIDDLE_RIGHT())
            {
                //loView.setBorderSize(0, tnTitleHeight, 0, 0);
            }
            else if (m_oPosition == Position.BOTTOM_CENTER())
            {
                //loView.setBorderSize(0, 0, tnTitleHeight, 0);
            }
            else if (m_oPosition == Position.MIDDLE_LEFT())
            {
                //loView.setBorderSize(0, 0, 0, tnTitleHeight);
            }
            else
            {
                //loView.setBorderSize(0, 0, 0, 0);
            }
            
            updateControls(loView, 24);
            
            ((WindowChromeLayoutManager)loView.getLayoutManager()).setTitlePosition(loView, m_oPosition);
        }
    }
    
    public ChromeDecorator(IControl toControl, Position toTitlePosition, float tnTitleHeight)
    {
        this(toControl, null, toTitlePosition, true, tnTitleHeight);
    }
    
    /**
     * Adds the specified control to the chrome views title area
     * @param toControl 
     */
    public final void addTitleControl(IControl toControl)
    {
        getChromeView().addTitleControl(toControl);
    }
    
    /**
     * Adds the specified control to the control box area of the title
     * @param toControl 
     */
    public final void addControlBoxControl(IControl toControl)
    {
        getChromeView().addControlBoxControl(toControl);
    }
    
    /**
     * Helper function to show or hide the title label
     * @param tlShow true to show, false to hide
     */
    public final void showTitleLabel(boolean tlShow)
    {
        getChromeView().getTitleLabel().setVisible(tlShow);
    }
    
    /**
     * Helper function to show or hide the Minimise button
     * @param tlShow true to show, false to hide
     */
    public final void showMinimizeButton(boolean tlShow)
    {
        getChromeView().getMinimiseButton().setVisible(tlShow);
    }
    
    /**
     * Helper function to show or hide the Maximise button
     * @param tlShow true to show, false to hide
     */
    public final void showMaximizeButton(boolean tlShow)
    {
        getChromeView().getMinimiseButton().setVisible(tlShow);
    }
    
    /**
     * Helper function to show or hide the close button
     * @param tlShow true to show, false to hide
     */
    public final void showCloseButton(boolean tlShow)
    {
        getChromeView().getCloseButton().setVisible(tlShow);
    }
    
    /**
     * Helper function to get the chrome view for this decorated control
     * @return the chrome view
     */
    public final ChromeView getChromeView()
    {
        if (m_oChromeView == null)
        {
            m_oChromeView = ((Window)getBaseControl()).getChromeView();
        }
        return m_oChromeView;
    }
    
    /**
     * Helper function to update the internal chrome controls
     * @param toView the chrome view
     * @param tnAxisSize the minor axis size, e.g. height for a top positioned title bar
     */
    private void updateControls(ChromeView toView, float tnAxisSize)
    {
        toView.getMaximiseButton().setSize(tnAxisSize, tnAxisSize);
        toView.getMinimiseButton().setSize(tnAxisSize, tnAxisSize);
        toView.getCloseButton().setSize(tnAxisSize, tnAxisSize);
        toView.getTitleLabel().setFontSize(tnAxisSize - 4);
        //toView.getTitleLabel().setBorderSize(8, 4, 8, 4);
    }

    @Override
    protected void mergeWith(ControlSkinDecorator toDecoration) 
    {
        m_oPosition = ((ChromeDecorator)toDecoration).m_oPosition;
    }
    
}
