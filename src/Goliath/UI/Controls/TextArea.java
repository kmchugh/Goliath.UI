/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.UI.Controls;

import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.Interfaces.UI.Controls.Implementations.ITextEditorControlImpl;
import Goliath.Interfaces.UI.Controls.Implementations.IValuedControlImpl;


/**
 *
 * @author kmchugh
 */
public class TextArea extends Control
    implements IValueControl<String>
{

    protected IValuedControlImpl<String> getValuedControlImplementation()
    {
        return (IValuedControlImpl<String>)getImplementation();
    }

    protected ITextEditorControlImpl getTextEditorControlImplementation()
    {
        return (ITextEditorControlImpl)getImplementation();
    }

    public TextArea()
    {
        initialiseComponent();
    }
    
    public TextArea(ControlImplementationType toType)
    {
        super(toType);
        initialiseComponent();
    }
    
    private void initialiseComponent()
    {
        setClearOnPaint(true);
    }
    
    

    @Override
    public String getValue()
    {
    return getValuedControlImplementation().getValue(getControl());
    }

    @Override
    public void setValue(String tcValue)
    {
        String lcValue = getValue();
        if ((lcValue != null && !lcValue.equals(tcValue)) || lcValue == null && tcValue != null)
        {
            getValuedControlImplementation().setValue(tcValue, getControl());
            invalidate();
        }
    }

    public long getSelectionStart()
    {
        return getTextEditorControlImplementation().getSelectionStart(getControl());
    }

    public long getSelectionEnd()
    {
        return getTextEditorControlImplementation().getSelectionEnd(getControl());
    }

    public boolean getRichText()
    {
        return getTextEditorControlImplementation().getRichText(getControl());
    }

    public void setRichText(boolean tlRichText)
    {
        getTextEditorControlImplementation().setRichText(tlRichText, getControl());
    }
    
}