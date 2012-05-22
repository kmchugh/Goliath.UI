/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.UI.Controls.Tree;


/**
 *
 * @author kmchugh
 */
public abstract class AbstractTreeNodeDefinition<T> extends Goliath.Object
{

    public abstract String getNodeText(T toObject);
    public abstract boolean hasChildren(T toObject);
    public abstract int getChildCount(T toObject);
    public abstract java.lang.Object getChildAt(T toParent, int tnIndex);
    public abstract int getIndexOf(T toParent, java.lang.Object toChild);
  
}
