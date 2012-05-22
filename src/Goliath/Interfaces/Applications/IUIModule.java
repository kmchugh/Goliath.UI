/* =========================================================
 * IUIModule.java
 *
 * Author:      kmchugh
 * Created:     30-May-2008, 16:37:01
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

package Goliath.Interfaces.Applications;

import Goliath.Interfaces.Commands.IContextCommand;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 30-May-2008
 * @author      kmchugh
**/
public interface IUIModule extends Goliath.Interfaces.Applications.IModule
{
    //IMenuBar getMenuBar();
    //IMenuBar getTrayMenu();
    IContextCommand getDefaultContextHandler();
}
