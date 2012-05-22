/* ========================================================
 * IApplicationPanelCommand.java
 *
 * Author:      kmchugh
 * Created:     Dec 8, 2010, 3:07:52 PM
 *
 * Description
 * --------------------------------------------------------
 * The Application panel command represents a command that can
 * appear on the application startup panel.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.Interfaces.UI.Controls;

import Goliath.UI.Windows.ApplicationPanelType;
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
public interface IApplicationPanelCommand
{
    /**
     * Gets the type of this panel command
     * @return the type of the panel command
     */
    ApplicationPanelType getPanelType();

    /**
     * Gets the name of the command
     * @return the name of this command
     */
    String getCommandName();

    /**
     * Gets the tooltip for this command
     * @return the tooltip for the command
     */
    String getCommandTooltip();

    /**
     * Gets the image for this command
     * @return the image to display for the command
     */
    Image getCommandImage();

    /**
     * Occurs when the command is clicked
     * @param toEvent the click event
     */
    void onCommandClicked(Event<IControl> toEvent);

    /**
     * creates the view for this command
     * @return the view to display when this command is used
     */
    View createCommandView();
    
    /**
     * Gets the index of this command for ordering in lists
     * @return the index of this command
     */
    int getCommandIndex();
}
