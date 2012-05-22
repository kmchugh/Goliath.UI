/* ========================================================
 * ApplicationPanelCommand.java
 *
 * Author:      kmchugh
 * Created:     Dec 8, 2010, 3:07:12 PM
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

package Goliath.UI.Windows;

import Goliath.Applications.Application;
import Goliath.Applications.UIApplicationSettings;
import Goliath.Event;
import Goliath.Exceptions.UncheckedException;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Image;
import Goliath.Interfaces.UI.Controls.IApplicationPanelCommand;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.UI.Controls.Screen;
import Goliath.UI.Views.View;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 8, 2010
 * @author      kmchugh
**/
public abstract class ApplicationPanelCommand extends Goliath.Object
        implements IApplicationPanelCommand
{
    private IWindow m_oWindow;

    private int m_nIndex;

    public int getIndex()
    {
        return m_nIndex;
    }

    public void setIndex(int tnIndex)
    {
        m_nIndex = tnIndex;
    }
    
    /**
     * Creates a new instance of ApplicationPanelCommand
     */
    public ApplicationPanelCommand()
    {
    }

    /**
     * Gets the image that is to be used for this command
     * @return the image for the command or null if no image is specified
     */
    @Override
    public final Image getCommandImage()
    {
        if (!Goliath.Utilities.isNullOrEmpty(getCommandImageSource()))
        {
            try
            {
                return new Image(getCommandImageSource());
            }
            catch (Throwable ex)
            {
            }
        }
        return null;
    }

    /**
     * If this is false then the same window that was created last time this command was clicked
     * will be reused, otherwise, new windows will be created each time this command is clicked
     * @return true to allow multiple view to open
     */
    protected boolean allowMultiples()
    {
        return false;
    }

    /**
     * Gets the string path to the resource for the image for the command
     * @return the string to the image resource
     */
    protected String getCommandImageSource()
    {
        return null;
    }

    /**
     * Gets the name of this command, this is displayed if there is no image
     * @return the name of the command
     */
    @Override
    public abstract String getCommandName();

    /**
     * Gets the tooltip for this command
     * @return the tooltip for the command
     */
    @Override
    public String getCommandTooltip()
    {
        return getCommandName();
    }

    /**
     * Gets the section of the explorer startup panel that this command should be
     * displayed in
     * @return the section to display the command in
     */
    @Override
    public ApplicationPanelType getPanelType()
    {
        return ApplicationPanelType.SETTINGS();
    }

    /**
     * The action that is taken when the command is clicked
     * @param toEvent the click event
     */
    @Override
    public final void onCommandClicked(Event<IControl> toEvent)
    {
        try
        {
            onClickAction(toEvent);
        }
        catch(Throwable ex)
        {
            Application.getInstance().log(ex);
            throw new UncheckedException(ex);
        }
    }

    protected IWindow getCommandWindow()
    {
        if (m_oWindow == null)
        {
            m_oWindow = createCommandWindow();
        }
        return m_oWindow;
    }

    protected void onClickAction(Event<IControl> toEvent)
    {
        // Open the Auxilary views if required
        openAuxilaryViews();

        IWindow loWindow = allowMultiples() ? createCommandWindow() : getCommandWindow();

        addEventListeners(loWindow);

        loWindow.suspendLayout();

        // Size and position the window
        configureWindow(toEvent, loWindow);

        // Create the window contents
        View loCommandView = createCommandView();

        // Set the title
        String lcTitle = loCommandView.getTitle();
        lcTitle = Goliath.Utilities.isNullOrEmpty(lcTitle) ? getCommandName() : lcTitle;
        loWindow.setTitle(lcTitle);

        loWindow.addControl(loCommandView);

        loWindow.resumeLayout();

        loWindow.setVisible(true);
    }

    @Override
    public int getCommandIndex()
    {
        return 9999;
    }
    
    

    /**
     * Hook method to allow the subclass to create or reuse a window.  The window returned will be
     * cleared and the command view will be placed inside it
     * @return the window to use for this command
     */
    protected IWindow createCommandWindow()
    {
        IWindow loWindow = new Window();
        loWindow.addClass("ApplicationPanelCommandWindow");
        return loWindow;
    }

    /**
     * Hook to allow subclasses to add event listeners as required
     * @param toWindow the window object to add the listeners to
     */
    protected void addEventListeners(IWindow toWindow)
    {

    }

    

    /**
     * Positions and sizes the view window as required
     */
    protected void configureWindow(Event<IControl> toEvent, IWindow toWindow)
    {
        Screen loScreen = new Screen();
        Dimension loScreenSize = loScreen.getSize();

        float lnHeight = (loScreenSize.getWidth()/16) * 6;
        float lnWidth = (loScreenSize.getWidth()/16) * 8;

        toWindow.setSize(new Dimension(lnWidth, lnHeight));
        Window.centerOnScreen(toWindow);
    }

    /**
     * Hook to opens up the auxilary views if required
     */
    protected void openAuxilaryViews()
    {
        
    }

    

    /**
     * Creates the view to be displayed by this command
     * @return the view to display
     */
    @Override
    public View createCommandView()
    {
        return new View();
    }

    /**
     * Helper function to get the application window for this application
     * @return the application window
     */
    public final IWindow getApplicationWindow()
    {
        return ((UIApplicationSettings)Application.getInstance().getApplicationSettings()).getApplicationWindow();
    }
}
