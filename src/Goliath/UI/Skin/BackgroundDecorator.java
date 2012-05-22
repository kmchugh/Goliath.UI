/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Goliath.UI.Skin;

import Goliath.Exceptions.FileNotFoundException;
import Goliath.Graphics.Image;
import Goliath.Interfaces.UI.Controls.IControl;
import java.awt.Color;
import java.io.File;

/**
 *
 * @author admin
 */
public class BackgroundDecorator extends ControlSkinDecorator 
{
    private Color m_oBackgroundColour;
    private Boolean m_lIsOpaque;
    private File m_oBGImage;
    
    
    public BackgroundDecorator(IControl toControl, File toFile, boolean tlIsOpaque)
    {
        this(toControl, toFile, true, false, 0, 0, 0, 0, tlIsOpaque);
        
    }
    
    public BackgroundDecorator(IControl toControl, File toBGImage, 
            boolean tlScaleable,
            boolean tlMaintainRatio,
            int tnTop, int tnRight, int tnBottom, int tnLeft, 
            boolean tlIsOpaque, 
            float tnXOffset, float tnYOffset)
    {
        this(toControl, toBGImage, tlScaleable, tlMaintainRatio, tnTop, tnRight, tnBottom, tnLeft, tlIsOpaque);
        getBaseControl().setBackgroundLocation(tnXOffset, tnYOffset);
    }
    
    
    public BackgroundDecorator(IControl toControl, File toBGImage, 
            boolean tlScaleable,
            boolean tlMaintainRatio,
            int tnTop, int tnRight, int tnBottom, int tnLeft, 
            boolean tlIsOpaque)
    {
        super(toControl);
        m_oBGImage = toBGImage;
        m_lIsOpaque = tlIsOpaque;
        
        Image loImage = null;
        
        if (m_oBGImage != null)
        {
            try
            {
                loImage = new Image(m_oBGImage);
            }
            catch (FileNotFoundException toEx)
            {
                // TODO: If the file is not found, use a default image
            }
            if (loImage != null)
            {
                loImage.setScale9Grid(tnTop, tnRight, tnBottom, tnLeft);
                loImage.setScaleable(tlScaleable);
                loImage.setMaintainRatio(tlMaintainRatio);
                getBaseControl().setBackground(loImage);
            }
        }
        else
        {
            getBaseControl().setBackground((Image)null);
        }
        //getBaseControl().setOpaque(tlIsOpaque);
    }
    
    public BackgroundDecorator(IControl toControl, Color toBackgroundColour)
    {
        super(toControl);
        m_oBackgroundColour = toBackgroundColour;
        getBaseControl().setBackground(toBackgroundColour);
    }
    
    public BackgroundDecorator(IControl toControl, boolean tlIsOpaque)
    {
        super(toControl);
        m_lIsOpaque = tlIsOpaque;
        //getBaseControl().setOpaque(tlIsOpaque);
    }
    
    public BackgroundDecorator(IControl toControl, Color toBackgroundColour, boolean tlIsOpaque)
    {
        super(toControl);
        m_lIsOpaque = tlIsOpaque;
        m_oBackgroundColour = toBackgroundColour;
        getBaseControl().setBackground(toBackgroundColour);
        //getBaseControl().setOpaque(tlIsOpaque);
    }

    @Override
    public Color getBackgroundColour() 
    {
        return m_oBackgroundColour != null ? m_oBackgroundColour : Goliath.Utilities.isNull(getDecorating(), getBaseControl()).getBackgroundColour();
    }

    @Override
    public void setBackground(Color toColour) 
    {
        // Do nothing.
    }
    
    @Override
    public void setBackground(Image toImage) 
    {
        // Do nothing.
    }
    
    @Override
    protected void mergeWith(ControlSkinDecorator toDecoration) 
    {
        m_oBackgroundColour = ((BackgroundDecorator)toDecoration).m_oBackgroundColour;
        m_lIsOpaque = ((BackgroundDecorator)toDecoration).m_lIsOpaque;
    }
    
    
    
    
}
