/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.Applications;

import Goliath.Interfaces.ISession;
import Goliath.Interfaces.UI.Controls.IApplicationWindow;
import Goliath.UI.Controls.ControlImplementationType;

/**
 *
 * @author kmchugh
 */
public interface IUIApplicationSettings
        extends IApplicationSettings
{
    IApplicationWindow getApplicationWindow();
    boolean getUsesSystemTray();
    ControlImplementationType getDefaultImplementationType();
}
