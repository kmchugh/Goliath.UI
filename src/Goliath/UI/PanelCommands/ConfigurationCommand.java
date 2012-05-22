/* ========================================================
 * ConfigurationCommand.java
 *
 * Author:      kmchugh
 * Created:     Dec 7, 2010, 6:45:07 PM
 *
 * Description
 * --------------------------------------------------------
 * Represents a command that will show up in the configuration panel.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.UI.PanelCommands;

import Goliath.Event;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Image;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Controls.Screen;
import Goliath.UI.Windows.Window;
import Goliath.UI.Views.View;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 7, 2010
 * @author      kmchugh
**/
public abstract class ConfigurationCommand extends Goliath.Object
        implements IConfigurationCommand
{
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
     * Creates a new instance of ConfigurationCommand
     */
    public ConfigurationCommand()
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

    public String getCommandImageSource()
    {
        return null;
    }

    /**
     * The action that is taken when the command is clicked
     * @param toEvent the click event
     */
    @Override
    public final void onCommandClicked(Event<IControl> toEvent)
    {
        Window loWindow = new Window();

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

    /**
     * Positions and sizes the view window as required
     */
    protected void configureWindow(Event<IControl> toEvent, Window toWindow)
    {
        Screen loScreen = new Screen();
        Dimension loScreenSize = loScreen.getSize();

        float lnHeight = (loScreenSize.getWidth()/16) * 6;
        float lnWidth = (loScreenSize.getWidth()/16) * 8;

        toWindow.setSize(new Dimension(lnWidth, lnHeight));
        Window.centerOnScreen(toWindow);
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
}
