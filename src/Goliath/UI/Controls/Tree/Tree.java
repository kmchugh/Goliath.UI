/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.UI.Controls.Tree;

import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.UI.Controls.ITree;
import Goliath.Interfaces.UI.Controls.Implementations.ITreeImpl;
import Goliath.UI.Controls.Control;

/**
 *
 * @author kmchugh
 */
public class Tree<T> extends Control
    implements ITree<T>
{

    private T m_oRoot = null;
    private HashTable<Class, AbstractTreeNodeDefinition> m_oNodeDefinitions;
    private boolean m_lShowRoot;


    protected ITreeImpl getTreeControlImplementation()
    {
        return (ITreeImpl)getImplementation();
    }

    public Tree()
    {
    }

    public Tree(HashTable<Class, AbstractTreeNodeDefinition> toDefinitions)
    {
        super();
        setNodeDefinitions(toDefinitions);
    }

    @Override
    public final void setNodeDefinitions(HashTable<Class, AbstractTreeNodeDefinition> toNodeDefinitions)
    {
        m_oNodeDefinitions = toNodeDefinitions;
    }

    @Override
    public HashTable<Class, AbstractTreeNodeDefinition> getNodeDefinitions()
    {
        return m_oNodeDefinitions != null ? m_oNodeDefinitions : new HashTable<Class, AbstractTreeNodeDefinition>(0);
    }


    @Override
    public T getRoot()
    {
        return m_oRoot;
    }

    @Override
    public boolean getShowRoot()
    {
        return m_lShowRoot;
    }

    /**
     * Sets the root object of the Tree, this will populate the tree
     * @param toObject the new root object in the tree
     */
    @Override
    public void setRoot(T toObject)
    {
        if (m_oRoot != toObject)
        {
            m_oRoot = toObject;
            getTreeControlImplementation().rootUpdated(getControl());
        }
    }

    @Override
    public void setShowRoot(boolean tlShowRoot)
    {
        getTreeControlImplementation().setShowRoot(tlShowRoot, getControl());
        m_lShowRoot = getTreeControlImplementation().getShowRoot(getControl());
    }

    /**
     * Gets the list of selected nodes from the tree
     * @return the list of selected items, or an empty list if no items were selected
     */
    public IList<T> getSelectedItems()
    {
        Goliath.Collections.Tree<T> loTree = getSelectedTree();
        return loTree == null ? new List<T>() : loTree.getLeafItems();
    }

    /**
     * Gets the list of selected items, including all of the parent nodes from the selected node
     * @return the list of selected items
     */
    public Goliath.Collections.Tree<T> getSelectedTree()
    {
        return getTreeControlImplementation().getSelectedTree(getControl());
    }




}