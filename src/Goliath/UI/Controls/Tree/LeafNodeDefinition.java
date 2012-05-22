/* ========================================================
 * LeafNodeDefinition.java
 *
 * Author:      kmchugh
 * Created:     Aug 6, 2010, 8:45:22 PM
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
public class LeafNodeDefinition<T> extends TreeNodeDefinition<T>
{
    /**
     * Creates a new instance of LeafNodeDefinition
     */
    public LeafNodeDefinition(String tcTitleProperty)
    {
        super(tcTitleProperty);
    }

    @Override
    public Object getChildAt(T toParent, int tnIndex)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount(T toObject)
    {
        return 0;
    }
}
