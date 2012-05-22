/* =========================================================
 * Button.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * A Buytton represents a clickable control.  Normally
 * clicking on a button would take some sort of action
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.UI.Controls;

import Goliath.Collections.HashTable;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Graphics.Effects.EffectArguments;
import Goliath.Graphics.Effects.TintEffect;
import Goliath.Graphics.Effects.TintEffectArguments;
import Goliath.Graphics.Image;
import Goliath.Interfaces.Graphics.IEffect;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.Implementations.IButtonControlImpl;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.UIEvent;
import java.awt.Color;

/**
 *
 * @author kmchugh
 */
public class Button extends Label
    implements IButton
{
    private static HashTable<String, IDelegate> g_oDelegates;
    private static HashTable<String, IEffect> g_oEffects;
    private static HashTable<String, EffectArguments> g_oEffectArgs;


    /**
     * Helper function to get the control implementation
     * @return
     */
    protected IButtonControlImpl getButtonImplementation()
    {
        return (IButtonControlImpl)getImplementation();
    }


    /**
     * Creates a new instance of Button
     */
    public Button()
    {
        initialiseComponent();
    }

    /**
     * Creates a new instance of Button
     * @param tcLabel The labe of the button
     */
    public Button(String tcLabel)
    {
        super();
        setText(tcLabel);
        initialiseComponent();
    }

    public Button(Goliath.Graphics.Image toImage)
    {
        super(toImage);
        initialiseComponent();
    }

    public Button(String tcLabel, Goliath.Graphics.Image toImage)
    {
        setTooltip(tcLabel);
        setImage(toImage);
        initialiseComponent();
    }

    public Button(String tcLabel, Goliath.Graphics.Image toImage, IDelegate toClickCallback)
    {
        this(tcLabel, toImage);
        if (toClickCallback != null)
        {
            addEventListener(UIEventType.ONCLICK(), toClickCallback);
        }
        initialiseComponent();

    }

    // TODO: Implement a constructor that takes a command object

    public Button(IDelegate toClickCallback)
    {
        super();
        addEventListener(UIEventType.ONCLICK(), toClickCallback);
        initialiseComponent();
    }

    /**
     * Creates a new instance of Button
     * @param tcLabel the label of the button
     * @param toClickCallback the event to take when the button is clicked
     */
    public Button(String tcLabel, IDelegate toClickCallback)
    {
        super();
        setText(tcLabel);
        addEventListener(UIEventType.ONCLICK(), toClickCallback);
        initialiseComponent();
    }

    /**
     * Creates a new instance of Button
     * @param tcLabel the label of the button
     * @param toClickCallback the event to take when the button is clicked
     */
    public Button(ControlImplementationType toType, String tcLabel, IDelegate toClickCallback)
    {
        super(toType);
        setText(tcLabel);
        addEventListener(UIEventType.ONCLICK(), toClickCallback);
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        addEventListener(UIEventType.ONMOUSEOVER(), getDelegate("mouseover"));
        addEventListener(UIEventType.ONMOUSEOUT(), getDelegate("mouseout"));
        addEventListener(UIEventType.ONMOUSEDOWN(), getDelegate("mousedown"));
        addEventListener(UIEventType.ONMOUSEUP(), getDelegate("mouseup"));
    }

    @Override
    public void setImage(Image toImage)
    {
        super.setImage(toImage);
        setBorderPainted(getImage() == null && getBackgroundImage() == null);
    }

    @Override
    public void setBackground(Image toBackground)
    {
        super.setBackground(toBackground);
        setBorderPainted(getImage() == null && getBackgroundImage() == null);
        //setOpaque(getBackgroundImage() == null);
    }
    
    
    @Override
    public boolean isBorderPainted()
    {
        return getButtonImplementation().isBorderPainted(getControl());
    }

    @Override
    public void setBorderPainted(boolean tlPaintBorder)
    {
        getButtonImplementation().setBorderPainted(tlPaintBorder, getControl());
    }
    
    
    
    // TODO: This should be moved to the skin
    /**
     * Helper function to get the specified delegate
     * @param tcDelegate the delegate to get
     * @return the delegate for this named event
     */
    private IDelegate<Event<IButton>> getDelegate(String tcDelegate)
    {
        if (g_oDelegates == null)
        {
            g_oDelegates = new HashTable<String, IDelegate>();
        }
        
        tcDelegate = tcDelegate.toLowerCase();
        
        if (!g_oDelegates.containsKey(tcDelegate))
        {
            g_oDelegates.put(tcDelegate, Delegate.build(this, "on" + tcDelegate));
        }
        return g_oDelegates.get(tcDelegate);
    }

    /**
     * Mousee out handler, this does not follow the correct naming conventions because
     * it is dynamically used
     * @param toEvent 
     */
    private void onmouseout(UIEvent toEvent)
    {
        toEvent.getComponent().removeClass("highlighted");
        toEvent.getComponent().update();
        removeEffect((Button)toEvent.getComponent(), "highlight");
    }
    

    /**
     * Mouse over handler, this does not follow the correct naming conventions because
     * it is dynamically used
     * @param toEvent 
     */
    private void onmouseover(UIEvent toEvent)
    {
        toEvent.getComponent().addClass("highlighted");
        toEvent.getComponent().update();
        addEffect((Button)toEvent.getComponent(), "highlight");
    }
    
    /**
     * Mouse up handler, this does not follow the correct naming conventions because
     * it is dynamically used
     * @param toEvent 
     */
    private void onmouseup(UIEvent toEvent)
    {
        
        removeEffect((Button)toEvent.getComponent(), "lowlight");
    }
    
    /**
     * Mouse down handler, this does not follow the correct naming conventions because
     * it is dynamically used
     * @param toEvent 
     */
    private void onmousedown(UIEvent toEvent)
    {
        addEffect((Button)toEvent.getComponent(), "lowlight");
    }

    @Override
    public void setEnabled(boolean tlEnabled)
    {
        super.setEnabled(tlEnabled);
        if (tlEnabled)
        {
            removeEffect(this, "lowlight");
        }
        else
        {
            addEffect(this, "lowlight");
        }
    }
    
    /**
     * Helper function to apply an effect to the button
     * @param tcEffect the effect to apply
     */
    private void addEffect(IButton toControl, String tcEffect)
    {
        Goliath.Graphics.Image loIcon = toControl.getImage();
        Goliath.Graphics.Image loBG = toControl.getBackgroundImage();

        if (loIcon != null || loBG != null)
        {
            if (loBG != null)
            {
                loBG.addEffect(getEffect(tcEffect), getEffectArguments(tcEffect));
            }
            else
            {
                loIcon.addEffect(getEffect(tcEffect), getEffectArguments(tcEffect));
            }
            toControl.invalidate();
            toControl.update();
        }
    }
    
    /**
     * Helper function to remove an effect to the button
     * @param tcEffect the effect to apply
     */
    private void removeEffect(IButton toControl, String tcEffect)
    {
        Goliath.Graphics.Image loIcon = toControl.getImage();
        Goliath.Graphics.Image loBG = toControl.getBackgroundImage();

        if (loIcon != null || loBG != null)
        {
            if (loBG != null)
            {
                loBG.removeEffect(getEffect(tcEffect));
            }
            else
            {
                loIcon.removeEffect(getEffect(tcEffect));
            }
            toControl.invalidate();
            toControl.update();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    // TODO: any effects should be taken from the skin
    private IEffect getEffect(String tcEffect)
    {
        if (g_oEffects == null)
        {
            g_oEffects = new HashTable<String, IEffect>();
        }
        
        tcEffect = tcEffect.toLowerCase();
        
        if (!g_oEffects.containsKey(tcEffect))
        {
            g_oEffects.put(tcEffect, createEffectFor(tcEffect));
        }
        return g_oEffects.get(tcEffect);
    }
    
    // TODO: any effects should be taken from the skin
    private EffectArguments getEffectArguments(String tcEffect)
    {
        if (g_oEffectArgs == null)
        {
            g_oEffectArgs = new HashTable<String, EffectArguments>();
        }
        
        tcEffect = tcEffect.toLowerCase();
        
        if (!g_oEffectArgs.containsKey(tcEffect))
        {
            g_oEffectArgs.put(tcEffect, createEffectArgumentsFor(tcEffect));
        }
        return g_oEffectArgs.get(tcEffect);
    }

    private IEffect createEffectFor(String tcEffect)
    {
        return new TintEffect();
    }
    
    private EffectArguments createEffectArgumentsFor(String tcEffect)
    {
        if (tcEffect.equalsIgnoreCase("highlight"))
        {
            return new TintEffectArguments(Color.white, .5f);
        }
        else if (tcEffect.equalsIgnoreCase("lowlight"))
        {
            return new TintEffectArguments(Color.black, .5f);
        }
        else
        {
            return new TintEffectArguments(Color.black, .5f);
        }
    }

    // TODO: Implement accessor keys on labels, buttons and menues


}