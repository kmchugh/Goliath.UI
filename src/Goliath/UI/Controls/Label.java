/* =========================================================
 * Label.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * A Label represents a control that displays static text to the
 * user.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/


package Goliath.UI.Controls;

import Goliath.Constants.EventType;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Effects.TintEffect;
import Goliath.Graphics.Effects.TintEffectArguments;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.ILabel;
import Goliath.Interfaces.UI.Controls.Implementations.ILabelledControlImpl;
import java.awt.Color;

/**
 *
 * @author kmchugh
 */
public class Label extends Control
    implements ILabel
{

    private Goliath.Graphics.Image m_oImage;
    private Color m_oTint;
    private float m_nTintStrength;
    private IDelegate<Event<Image>> m_oImageChangedHandler;

    /**
     * Helper function to get the control implementation
     * @return
     */
    protected ILabelledControlImpl getLabelledImplementation()
    {
        return (ILabelledControlImpl)getImplementation();
    }

    /**
     * Creates a new instance of Label
     */
    public Label()
    {
    }

    protected Label(ControlImplementationType toType)
    {
        super(toType);
    }

    /**
     * Creates a new instance of Label
     * @param tcLabel the text of the label
     */
    public Label(String tcLabel)
    {
        super();
        setText(tcLabel);
    }

    /**
     * Creates a new label object that displays an image
     * The image displayed is separate from the background and is displayed on top of the background
     * @param toImage the image to display
     */
    public Label(Goliath.Graphics.Image toImage)
    {
        setImage(toImage);
    }

    /**
     * Creates a new label object that displays an image
     * The image displayed is separate from the background and is displayed on top of the background
     * @param tcLabel the text of the label
     * @param toImage the image to display
     */
    public Label(String tcLabel, Goliath.Graphics.Image toImage)
    {
        this(tcLabel);
        setImage(toImage);
    }

    public void setImageTintColour(Color toColour, float tnStrength)
    {
        m_oTint = toColour;
        m_nTintStrength = tnStrength;
        if (m_oTint != null && m_oImage != null)
        {
            m_oImage.addEffect(new TintEffect(), new TintEffectArguments(toColour, tnStrength));
        }
    }

    /**
     * Gets the label text
     * @return the label text
     */
    @Override
    public final String getText()
    {
        return getLabelledImplementation().getText(getControl());
    }

    /**
     * Sets the label text
     * @param tcText the new label text
     */
    @Override
    public final void setText(String tcText)
    {
        getLabelledImplementation().setText(tcText, getControl());
        invalidate();
    }

    /**
     * Sets the image that is displayed by this control
     * @param toImage the image to display
     */
    @Override
    public void setImage(Goliath.Graphics.Image toImage)
    {
        if (m_oImage != toImage)
        {
            if (m_oImage != null)
            {
                m_oImage.removeEventListener(EventType.ONCHANGED(), getImageChangedHandler());
            }
            m_oImage = toImage;
            setImageTintColour(m_oTint, m_nTintStrength);
            getLabelledImplementation().setImage(m_oImage, getControl());
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

    private void onImageChanged(Event<Goliath.Graphics.Image> toEvent)
    {
        getLabelledImplementation().setImage(m_oImage, getControl());
    }

    @Override
    protected void onUpdate()
    {
        // Make sure the image is scaled if possible
        if (m_oImage != null)
        {
            m_oImage.setSize(getSize());
            getLabelledImplementation().setImage(m_oImage, getControl());
        }
        if (getLabelledImplementation().getTextRotation(getControl()) == 0)
        {
            setTextRotation(getParent() != null ? getParent().getTextRotation() : 0);
        }
        super.onUpdate();
    }

    public void setTextRotation(float tnRotation)
    {
        getLabelledImplementation().setTextRotation(tnRotation, getControl());
    }

    public float getTextRotation()
    {
        return getLabelledImplementation().getTextRotation(getControl());
    }



    /**
     * Gets the image that is to be used for this control
     * @return the image being used for the control
     */
    @Override
    public final Goliath.Graphics.Image getImage()
    {
        return m_oImage;
    }

    // TODO: Implement label positioning against the image using the Position
}
