/* ========================================================
 * IConfigurationCommand.java
 *
 * Author:      kmchugh
 * Created:     Dec 8, 2010, 4:23:47 PM
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

import Goliath.Event;
import Goliath.Graphics.Image;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Views.View;



/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 8, 2010
 * @author      kmchugh
**/
public interface IConfigurationCommand
{
    String getCommandName();
    String getCommandTooltip();
    Image getCommandImage();

    void onCommandClicked(Event<IControl> toEvent);

    View createCommandView();
}