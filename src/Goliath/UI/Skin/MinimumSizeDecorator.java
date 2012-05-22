/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Goliath.UI.Skin;

import Goliath.Interfaces.UI.Controls.IControl;


/**
 *
 * @author admin
 */
public class MinimumSizeDecorator extends ControlSkinDecorator
{
    private Float m_nWidth;
    private Float m_nHeight;
    private Float m_nBottom;
    private Float m_nLeft;
    
    public MinimumSizeDecorator(IControl toControl, float tnWidth, float tnHeight)
    {
        super(toControl);
        m_nWidth = tnWidth;
        m_nHeight = tnHeight;
        getBaseControl().setMinSize(tnWidth, tnHeight);
    }

    @Override
    public void setMinSize(float tnWidth, float tnHeight) 
    {
        // Do nothing
    }
    
    @Override
    protected void mergeWith(ControlSkinDecorator toDecoration) 
    {
        
    }
}