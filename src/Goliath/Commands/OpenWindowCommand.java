/* ========================================================
 * OpenWindowCommand.java
 *
 * Author:      kmchugh
 * Created:     Aug 4, 2010, 10:30:04 AM
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

import Goliath.Interfaces.UI.Controls.IWindow;


        
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
public class OpenWindowCommand extends UICommand
{
    IWindow m_oWindow;

    public OpenWindowCommand(IWindow toWindow)
    {
        m_oWindow = toWindow;
    }

    @Override
    protected void onDoExecute()
    {
        if (!m_oWindow.isVisible())
        {
            m_oWindow.isVisible();
        }
    }
}
