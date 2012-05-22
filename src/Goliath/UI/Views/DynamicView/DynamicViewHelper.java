/* ========================================================
 * DynamicViewHelper.java
 *
 * Author:      kenmchugh
 * Created:     Jan 28, 2011, 11:59:04 AM
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

package Goliath.UI.Views.DynamicView;

import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Interfaces.UI.Controls.IControl;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jan 28, 2011
 * @author      kenmchugh
**/
public abstract class DynamicViewHelper extends Goliath.Object
{
    public abstract boolean isContainer();
    public abstract List<String> getSupportedTags();
    public abstract IControl createControl(String tcNodeName, PropertySet toProperties);
}
