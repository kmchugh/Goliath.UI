/* ========================================================
 * DynamicViewContainerHelper.java
 *
 * Author:      kenmchugh
 * Created:     Jan 28, 2011, 12:03:06 PM
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
import Goliath.UI.Controls.Group;


        
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
public class DynamicViewGroupHelper extends DynamicViewHelper
{
    private List<String> m_oSupportedTags;
    
    /**
     * Creates a new instance of DynamicViewContainerHelper
     */
    public DynamicViewGroupHelper()
    {
    }

    @Override
    public IControl createControl(String tcNodeName, PropertySet toProperties)
    {
        return new Group();
    }

    @Override
    public List<String> getSupportedTags()
    {
        if (m_oSupportedTags == null)
        {
            m_oSupportedTags = new List<String>(2);
            m_oSupportedTags.add("div");
            m_oSupportedTags.add("group");
        }
        return m_oSupportedTags;
    }

    @Override
    public boolean isContainer()
    {
        return true;
    }


}
