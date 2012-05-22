/* ========================================================
 * ConfigurationPanelCommand.java
 *
 * Author:      kmchugh
 * Created:     Dec 8, 2010, 4:10:02 PM
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

package Goliath.UI.PanelCommands;

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Graphics.Dimension;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.UI.Controls.Button;
import Goliath.UI.Controls.Layouts.GridLayoutManager;
import Goliath.UI.Skin.Skin;
import Goliath.UI.Skin.SkinManager;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Views.View;
import Goliath.UI.Windows.ApplicationPanelCommand;
import Goliath.UI.Windows.ApplicationPanelType;
import Goliath.UI.Windows.Window;


        
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
public class ConfigurationPanelCommand extends ApplicationPanelCommand
{
    public ConfigurationPanelCommand()
    {
        setIndex(1);
    }

    @Override
    public String getCommandName()
    {
        return "Configuration";
    }

    @Override
    protected String getCommandImageSource()
    {
        return "./resources/Images/icons/Settings.png";
    }

    @Override
    public String getCommandTooltip()
    {
        return "Configure your application";
    }

    @Override
    public ApplicationPanelType getPanelType()
    {
        return ApplicationPanelType.SETTINGS();
    }

    @Override
    public View createCommandView()
    {
        Skin loSkin = SkinManager.getInstance().getCurrentSkin();
        View loView = new View();

        // Get a list of all the Explorer Panel Commands that are available
        List<Class<IConfigurationCommand>> loCommandClasses = Application.getInstance().getObjectCache().getClasses(IConfigurationCommand.class);
        // Go through each command and create the panels
        for (Class<IConfigurationCommand> loCommandClass : loCommandClasses)
        {
            try
            {
                IConfigurationCommand loCommand = loCommandClass.newInstance();

                // Create the button for the command
                Button loButton = createCommandButton(loCommand);
                if (loButton != null)
                {
                    loButton.addClass("configurationPanelButton");
                    loView.addControl(loButton);
                }
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);
            }
        }

        //loView.setLayoutManager(new GridLayoutManager(1, loView.getChildren().size()));
        loView.setLayoutManager(GridLayoutManager.class);
        return loView;
    }

    /**
     * Hook to allow sub class to add any event listeners required
     * @param toWindow the window to add the listeners too.
     */
    @Override
    protected void addEventListeners(IWindow toWindow)
    {
        toWindow.addEventListener(UIEventType.ONDEACTIVATED(), Delegate.build(this, "onPanelDeactivated"));
    }

    /**
     * When the configuration panel is deactivated, it will close automatically
     * @param toEvent the event that occured
     */
    private void onPanelDeactivated(Event<IControl> toEvent)
    {
        Window loWindow = (Window)toEvent.getTarget();
        if (loWindow != null)
        {
            loWindow.setVisible(false);
        }
    }

    @Override
    protected void configureWindow(Event<IControl> toEvent, IWindow toWindow)
    {
        IWindow loTargetWindow = toEvent.getTarget().getParentWindow();
        toWindow.setLocation(loTargetWindow.getSize().getWidth() + loTargetWindow.getLocation().getX(), toEvent.getTarget().getScreenCoordinates().getY());
        toWindow.setSize(new Dimension(100, 75));
    }

    /**
     * Creates the Buttons for each Explorer command
     * @param toCommand the command to create a button for
     * @return the Button
     */
    private Button createCommandButton(IConfigurationCommand toCommand)
    {
        Button loReturn = new Button(Delegate.build(toCommand, "onCommandClicked"));
        Goliath.Graphics.Image loImage = toCommand.getCommandImage();

        // If there is an image, use that for the command instead of text
        if (loImage != null)
        {
            loImage.setScaleable(true);
            loImage.setMaintainRatio(true);
            loReturn.setImage(loImage);
        }
        else
        {
            loReturn.setText(toCommand.getCommandName());
        }
        loReturn.setTooltip(toCommand.getCommandTooltip());
        return loReturn;
    }
}