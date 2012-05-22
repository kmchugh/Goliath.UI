/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Goliath.UI.Windows;

import Goliath.Graphics.Constants.Position;

/**
 * An immutable object containing information about where and how drawers should
 * be placed
 * @author admin
 */
public class DrawerConfig extends Goliath.Object
{
    private Position m_oPosition;
    
    public DrawerConfig(Position toPosition)
    {
        setPosition(toPosition);
    }
    
    /**
     * Helper function to ensure the position is withing the limits
     * @param toPosition the position the drawer should be located at
     */
    private void setPosition(Position toPosition)
    {
        if (toPosition == Position.TOP_LEFT() ||
                toPosition == Position.TOP_RIGHT() ||
                toPosition == Position.TOP_CENTER() ||
                toPosition == Position.MIDDLE_CENTER())
        {
            toPosition = Position.TOP_CENTER();
        }
        else if(toPosition == Position.BOTTOM_LEFT()||
                toPosition == Position.BOTTOM_RIGHT() ||
                toPosition == Position.BOTTOM_CENTER())
        {
            toPosition = Position.BOTTOM_CENTER();
        }
        else if (toPosition == Position.MIDDLE_LEFT() ||
                    toPosition == Position.MIDDLE_RIGHT())
        {
            // These are correct, so stay the same
        }
        else
        {
            toPosition = Position.TOP_CENTER();
        }
        m_oPosition = toPosition;
    }
    
    /**
     * Gets the position the drawer using this configuration should be set to
     * @return the drawer position
     */
    public Position getPosition()
    {
        return m_oPosition;
        
    }
}
