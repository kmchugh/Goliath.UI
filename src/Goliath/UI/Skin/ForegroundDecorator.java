/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Goliath.UI.Skin;

import Goliath.Interfaces.UI.Controls.IControl;
import java.awt.Color;

/**
 *
 * @author admin
 */
public class ForegroundDecorator extends ControlSkinDecorator
{
    private Color m_oColour;
    
    public ForegroundDecorator(IControl toControl, Color toColour)
    {
        super(toControl);
        m_oColour = toColour;
        getBaseControl().setColour(m_oColour);
    }

    @Override
    public Color getColour() 
    {
        return m_oColour != null ? m_oColour : Goliath.Utilities.isNull(getDecorating(), getBaseControl()).getColour();
    }

    @Override
    public void setColour(Color toColour) 
    {
        // Do nothing
    }
    
    

    @Override
    protected void mergeWith(ControlSkinDecorator toDecoration) 
    {
        m_oColour = ((ForegroundDecorator)toDecoration).m_oColour;
    }
    
    
}
