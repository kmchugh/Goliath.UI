/* ========================================================
 * OpenApplicationWindowCommand.java
 *
 * Author:      kmchugh
 * Created:     Aug 4, 2010, 10:31:28 AM
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

package Goliath.Commands;

import Goliath.Applications.UIApplicationSettings;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.UI.Skin.SkinManager;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 4, 2010
 * @author      kmchugh
**/
public class OpenApplicationWindowCommand extends UICommand
{
    UIApplicationSettings m_oSettings;

    public OpenApplicationWindowCommand(UIApplicationSettings toSettings)
    {
        m_oSettings = toSettings;
    }

    @Override
    protected void onDoExecute()
    {
        IWindow loWindow = m_oSettings.getApplicationWindow();
        if (!loWindow.isVisible())
        {
            loWindow.setVisible(true);
            SkinManager.getInstance().getCurrentSkin().invalidateStyle(loWindow);
            loWindow.update();
        }
    }
}
