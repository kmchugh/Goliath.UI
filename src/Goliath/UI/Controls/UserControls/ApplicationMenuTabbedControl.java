/* ========================================================
 * SystemMenuAccordion.java
 *
 * Author:      kenmchugh
 * Created:     Jan 14, 2011, 2:13:36 PM
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

package Goliath.UI.Controls.UserControls;

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Constants.EventType;
import Goliath.Delegate;
import Goliath.Interfaces.UI.Controls.IApplicationPanelCommand;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.UI.Controls.Layouts.GridLayoutManager;
import Goliath.UI.Controls.ToggleButton;
import Goliath.UI.Windows.ApplicationPanelType;
import java.util.Arrays;
import java.util.Comparator;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jan 14, 2011
 * @author      kenmchugh
**/
public class ApplicationMenuTabbedControl extends TabbedControl
{
    /**
     * Creates a new instance of SystemMenuAccordion
     */
    public ApplicationMenuTabbedControl()
    {
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        this.setMaxSelectable(99);

        // Suspend the accordion layout
        suspendLayout();
        
        List<IApplicationPanelCommand> loCommands = new List<IApplicationPanelCommand>();

        try
        {
            // Get a list of all the Explorer Panel Commands that are available
            List<Class<IApplicationPanelCommand>> loCommandClasses = Application.getInstance().getObjectCache().getClasses(IApplicationPanelCommand.class);
            // Go through each command and create the panels
            for (Class<IApplicationPanelCommand> loCommandClass : loCommandClasses)
            {
                try
                {
                    IApplicationPanelCommand loCommand = Goliath.DynamicCode.Java.createObject(loCommandClass, (Object[])null);
                    if (loCommand != null && !loCommands.contains(loCommand))
                    {
                        if(loCommand.getPanelType() != ApplicationPanelType.NONE())
                        {
                            loCommands.add(loCommand);
                        }
                    }
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }
        }
        catch(Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        
        
        // Sort the commands
        IApplicationPanelCommand[] laCommands = loCommands.toArray(new IApplicationPanelCommand[0]);
        try
        { 
            Arrays.sort(laCommands, new Comparator<IApplicationPanelCommand>() {

                @Override
                public int compare(IApplicationPanelCommand toCommand1, IApplicationPanelCommand toCommand2)
                {
                    return ((Integer)toCommand1.getCommandIndex()).compareTo(toCommand2.getCommandIndex());
                }
            });
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        
        // Add the commands in order
        for (int i=0; i< laCommands.length; i++)
        {
            IApplicationPanelCommand loCommand = laCommands[i];
            if (loCommand != null)
            {
                try
                {
                    // Get the panel the command is supposed to be in and make sure it exists in the Accordion
                    ApplicationPanelType loPanelType = loCommand.getPanelType();
                    String lcKey = loPanelType.getValue();
                    // TODO: This should be using a lookup for languages
                    String lcName = loPanelType.getValue();

                    // Make sure the panel exists
                    IContainer loPanelContainer = createPanel(lcKey, lcName);

                    // Add the controls to the panel
                    if (loPanelContainer != null)
                    {
                        // Get the LayoutManager
                        GridLayoutManager loLayout = (GridLayoutManager)loPanelContainer.getLayoutManager();

                        IButton loButton = createCommandButton(loCommand);
                        if (loButton != null)
                        {
                            loButton.addClass("applicationPanelButton");
                            loPanelContainer.addControl(loButton);
                        }
                        loLayout.setRows(loPanelContainer, loPanelContainer.getChildren().size());
                        loLayout.setColumns(loPanelContainer, 1);
                    }
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }
        }
        
        // Resume the accordion layout engine
        resumeLayout();
    }

    /**
     * Creates the Buttons for each Explorer command
     * @param toCommand the command to create a button for
     * @return the Button
     */
    private IButton createCommandButton(IApplicationPanelCommand toCommand)
    {
        IButton loReturn = new ToggleButton();
        loReturn.addEventListener(EventType.ONSTATECHANGED(), Delegate.build(toCommand, "onCommandClicked"));
        
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
        loReturn.setProperty("command", toCommand);
        return loReturn;
    }
}
