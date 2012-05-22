/* ========================================================
 * DynamicViewLabelHelper.java
 *
 * Author:      kenmchugh
 * Created:     Jan 28, 2011, 12:05:34 PM
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
import Goliath.UI.Controls.Label;


        
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
public class DynamicViewLabelHelper extends DynamicViewHelper
{
    private List<String> m_oSupportedTags;

    /**
     * Creates a new instance of DynamicViewLabelHelper
     */
    public DynamicViewLabelHelper()
    {
    }

    @Override
    public IControl createControl(String tcNodeName, PropertySet toProperties)
    {
        return new Label();
    }

    @Override
    public List<String> getSupportedTags()
    {
        if (m_oSupportedTags == null)
        {
            m_oSupportedTags = new List<String>(1);
            m_oSupportedTags.add("label");
        }
        return m_oSupportedTags;
    }

    @Override
    public boolean isContainer()
    {
        return false;
    }


}
