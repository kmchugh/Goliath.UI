/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.UI.Controls.Tree;

import Goliath.DynamicCode.Java;
import Goliath.Property;

/**
 *
 * @author kmchugh
 */
public abstract class TreeNodeDefinition<T> extends AbstractTreeNodeDefinition<T>
{
    private String m_cNameProperty;
    
    public TreeNodeDefinition(String tcTitleProperty)
    {
        m_cNameProperty = tcTitleProperty;
    }

    /**
     * Gets the index location of the child in the parent, this method is slow
     * and should be overridden in any implementations to specialise.
     * @param toParent the parent the child is contained in
     * @param toChild the child object
     * @return the index of the child, or -1 if it doesn't exist
     */
    @Override
    public int getIndexOf(T toParent, Object toChild)
    {
        for (int i=0, lnLength = getChildCount(toParent); i<lnLength; i++)
        {
            if (getChildAt(toParent, i) == toChild)
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String getNodeText(T toObject)
    {
        Object loText = Goliath.DynamicCode.Java.getPropertyValue(toObject, m_cNameProperty, false);
        return (loText != null) ? loText.toString() : null;
    }

    @Override
    public boolean hasChildren(T toObject)
    {
        return getChildCount(toObject) > 0;
    }
}
