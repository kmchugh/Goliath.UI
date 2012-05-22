/* ========================================================
 * ListTreeNodeDefinition.java
 *
 * Author:      kmchugh
 * Created:     Aug 6, 2010, 5:30:08 PM
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

package Goliath.UI.Controls.Tree;

import java.util.List;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 6, 2010
 * @author      kmchugh
**/
public class ListTreeNodeDefinition extends AbstractTreeNodeDefinition
{

    private String m_cTitle;
    /**
     * Creates a new instance of ListTreeNodeDefinition
     */
    public ListTreeNodeDefinition(String tcTitle)
    {
        m_cTitle = tcTitle;
    }

    @Override
    public int getChildCount(Object toObject)
    {
        return (toObject != null ) ? ((List)toObject).size() : 0;
    }

    @Override
    public String getNodeText(Object toObject)
    {
        return m_cTitle;
    }

    @Override
    public Object getChildAt(Object toParent, int tnIndex)
    {
        return (toParent != null ) ? ((List)toParent).get(tnIndex) : null;
    }

    @Override
    public int getIndexOf(Object toParent, Object toChild)
    {
        return (toParent != null ) ? ((List)toParent).indexOf(toChild) : -1;
    }

    



    @Override
    public boolean hasChildren(Object toObject)
    {
        return getChildCount(toObject) > 0;
    }


}
