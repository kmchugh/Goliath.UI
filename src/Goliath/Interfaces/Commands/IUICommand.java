/* =========================================================
 * IUICommand.java
 *
 * Author:      kmchugh
 * Created:     07-Jul-2008, 12:59:22
 * 
 * Description
 * --------------------------------------------------------
 * General Interface Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces.Commands;

import Goliath.Collections.List;
import Goliath.Commands.UICommandArgs;
import Goliath.Commands.UICommandType;
import Goliath.ExecutableXML;
import Goliath.Interfaces.UI.Controls.IControl;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 07-Jul-2008
 * @author      kmchugh
**/
public interface IUICommand extends ICommand<UICommandArgs, ExecutableXML>
{
    List<IControl> getControlDependencies();
    void addDependency(IControl toControl);
    UICommandType getCommandType();

}
