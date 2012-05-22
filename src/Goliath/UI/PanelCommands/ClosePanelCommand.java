/* ========================================================
 * ClosePanelCommand.java
 *
 * Author:      kenmchugh
 * Created:     Feb 21, 2011, 3:18:51 PM
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
import Goliath.Event;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Windows.ApplicationPanelCommand;
import Goliath.UI.Windows.ApplicationPanelType;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Feb 21, 2011
 * @author      kenmchugh
**/
public class ClosePanelCommand extends ApplicationPanelCommand
{
    public ClosePanelCommand()
    {
        setIndex(3);
    }

    @Override
    public String getCommandName()
    {
        return "Close";
    }

    @Override
    protected String getCommandImageSource()
    {
        return "./resources/Images/icons/Close.png";
    }

    @Override
    public String getCommandTooltip()
    {
        return "Close the Application";
    }

    @Override
    public ApplicationPanelType getPanelType()
    {
        return ApplicationPanelType.SETTINGS();
    }

    @Override
    protected void onClickAction(Event<IControl> toEvent)
    {
        Application.getInstance().shutdown();
    }
}