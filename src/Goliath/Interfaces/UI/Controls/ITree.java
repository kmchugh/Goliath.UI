/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls;

import Goliath.Collections.HashTable;
import Goliath.UI.Controls.Tree.AbstractTreeNodeDefinition;

/**
 *
 * @author kmchugh
 */
public interface ITree<T>
        extends IControl
{
    void setShowRoot(boolean tlShowRoot);
    boolean getShowRoot();

    T getRoot();
    void setRoot(T toObject);

    void setNodeDefinitions(HashTable<Class, AbstractTreeNodeDefinition> toNodeDefinitions);
    HashTable<Class, AbstractTreeNodeDefinition> getNodeDefinitions();

}
