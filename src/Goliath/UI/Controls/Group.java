/* =========================================================
 * Group.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * A group is an area to place controls in to group them together.
 * A group has no additional features such as chrome.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.UI.Controls;

import Goliath.Interfaces.UI.Controls.Layouts.ILayoutManager;

/**
 *
 * @author kmchugh
 */
public class Group extends Container
{
    public Group()
    {
        
    }

    public Group(Class<? extends ILayoutManager> toManager)
    {
        super(toManager);
    }
    
    public Group(ControlImplementationType toType)
    {
        super(toType);
    }
}
