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
public class BorderDecorator extends ControlSkinDecorator
{
    private Float m_nTop;
    private Float m_nRight;
    private Float m_nBottom;
    private Float m_nLeft;
    
    public BorderDecorator(IControl toControl, float tnTop, float tnRight, float tnBottom, float tnLeft)
    {
        super(toControl);
        m_nTop = tnTop;
        m_nRight = tnRight;
        m_nBottom = tnBottom;
        m_nLeft = tnLeft;
        //getBaseControl().setBorderSize(tnTop, tnRight, tnBottom, tnLeft);
    }

    /*
    @Override
    public void setBorderSize(float tnTop, float tnRight, float tnBottom, float tnLeft) 
    {
        // Do nothing
    }
     * 
     */
    
    @Override
    protected void mergeWith(ControlSkinDecorator toDecoration) 
    {
        m_nTop = ((BorderDecorator)toDecoration).m_nTop;
        m_nRight = ((BorderDecorator)toDecoration).m_nRight;
        m_nBottom = ((BorderDecorator)toDecoration).m_nBottom;
        m_nLeft = ((BorderDecorator)toDecoration).m_nLeft;
    }
    
    
}
