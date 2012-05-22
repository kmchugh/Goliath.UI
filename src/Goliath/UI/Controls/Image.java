/* ========================================================
 * Image.java
 *
 * Author:      kmchugh
 * Created:     Aug 2, 2010, 8:00:07 PM
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

package Goliath.UI.Controls;

import Goliath.Constants.EventType;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Exceptions.FileNotFoundException;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Effects.TintEffect;
import Goliath.Graphics.Effects.TintEffectArguments;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IImage;
import Goliath.Interfaces.UI.Controls.Implementations.IImageControlImpl;
import java.awt.Color;
import java.io.File;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 2, 2010
 * @author      kmchugh
**/
public class Image extends Control
        implements IImage
{
    
    private Goliath.Graphics.Image m_oImage;
    private Color m_oTint;
    private float m_nTintStrength;
    private IDelegate<Event<Image>> m_oImageChangedHandler;

    /**
     * Helper function to get the implementation control for the button
     * @return
     */
    protected IImageControlImpl getImageImplementation()
    {
        return (IImageControlImpl)getImplementation();
    }
    
    /**
     * Creates a new image control
     * @param tcSource the source image
     * @param tcAlternateText the text to use as the tooltip for this image
     * @param tlScale true if this source image is allowed to scale
     * @param tlMaintainRatio true to maintainte ratio when scaling this image
     * @throws FileNotFoundException if the source is not found
     */
    public Image(String tcSource, String tcAlternateText, boolean tlScale, boolean tlMaintainRatio)
            throws FileNotFoundException
    {
        this(new Goliath.Graphics.Image(tcSource), tcAlternateText, tlScale, tlMaintainRatio);
    }

    /**
     * Creates a new image control
     * @param tcSource the source image
     * @param tcAlternateText the text to use as the tooltip for this image
     * @throws FileNotFoundException if the source is not found
     */
    public Image(String tcSource, String tcAlternateText)
            throws FileNotFoundException
    {
        this(new File(tcSource), tcAlternateText);
    }

    /**
     * Creates a new image control
     * @param toSource the source image
     * @param tcAlternateText the text to use as the tooltip for this image
     * @throws FileNotFoundException if the source is not found
     */
    public Image(File toSource, String tcAlternateText)
            throws FileNotFoundException
    {
        this(new Goliath.Graphics.Image(toSource), tcAlternateText);
    }

    /**
     * Creates a new image control
     * @param toImage the source image
     * @param tcAlternateText the text to use as the tooltip for this image
     * @throws FileNotFoundException if the source is not found
     */
    public Image(Goliath.Graphics.Image toImage, String tcAlternateText)
    {
        this(toImage, tcAlternateText, toImage.isScaleable(), toImage.maintainRatio());
    }
    
    /**
     * Creates a new image control
     * @param toImage the source image
     * @param tcAlternateText the text to use as the tooltip for this image
     * @param tlScale true if this source image is allowed to scale
     * @param tlMaintainRatio true to maintainte ratio when scaling this image
     * @throws FileNotFoundException if the source is not found
     */
    public Image(Goliath.Graphics.Image toImage, String tcAlternateText, boolean tlScale, boolean tlMaintainRatio)
    {
        toImage.setScaleable(tlScale);
        toImage.maintainRatio();
        setSource(toImage);
        setAlternateText(tcAlternateText);
    }

    
    // TODO: Remove this, effects should be used instead
    public void setImageTintColour(Color toColour, float tnStrength)
    {
        m_oTint = toColour;
        m_nTintStrength = tnStrength;
        if (m_oImage != null)
        {
            m_oImage.addEffect(new TintEffect(), new TintEffectArguments(toColour, tnStrength));
        }
    }

    @Override
    public Goliath.Graphics.Image getImage()
    {
        return m_oImage;
    }

    public final void setSource(String tcSource)
            throws FileNotFoundException
    {
        setSource(new File(tcSource));
    }

    public final void setSource(File toSource)
            throws FileNotFoundException
    {
        setSource(new Goliath.Graphics.Image(toSource));
    }

    public final void setSource(Goliath.Graphics.Image toSource)
    {
        if (m_oImage != toSource)
        {
            if (m_oImage != null)
            {
                m_oImage.removeEventListener(EventType.ONCHANGED(), getImageChangedHandler());
            }
            m_oImage = toSource;
            setImageTintColour(m_oTint, m_nTintStrength);
            getImageImplementation().setSource(m_oImage, getControl());
            if (m_oImage != null)
            {
                m_oImage.addEventListener(EventType.ONCHANGED(), getImageChangedHandler());
            }
            invalidate();
        }
    }

    private IDelegate<Event<Image>> getImageChangedHandler()
    {
        if (m_oImageChangedHandler == null)
        {
            m_oImageChangedHandler = Delegate.build(this, "onImageChanged");
        }
        return m_oImageChangedHandler;
    }

    @Override
    public boolean setSize(Dimension toDimension)
    {
        boolean llReturn = super.setSize(toDimension);
        if (llReturn && m_oImage != null)
        {
            m_oImage.setSize(toDimension);
        }
        return llReturn;
    }
    
    @Override
    public final String getAlternateText()
    {
        return getImageImplementation().getAlternateText(getControl());
    }

    @Override
    public final void setAlternateText(String tcAltText)
    {
        getImageImplementation().setAlternateText(tcAltText, getControl());
        setTooltip(tcAltText);
    }

    private void onImageChanged(Event<Goliath.Graphics.Image> toEvent)
    {
        getImageImplementation().setSource(toEvent.getTarget(), getControl());
    }


}
