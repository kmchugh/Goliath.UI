/* ========================================================
 * AWTSystemMenuItemImpl.java
 *
 * Author:      kmchugh
 * Created:     Aug 18, 2010, 5:35:10 PM
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

import Goliath.Collections.List;
import Goliath.Constants.EventType;
import Goliath.Event;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Image;
import Goliath.Graphics.Point;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.Implementations.IImplementedControl;
import Goliath.Interfaces.UI.Controls.Implementations.ILabelledControlImpl;
import Goliath.UI.Constants.UIEventType;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 18, 2010
 * @author      kmchugh
**/
public class AWTSystemMenuItemImpl extends ControlImplementation<MenuItem>
        implements ILabelledControlImpl
{
    protected Goliath.UI.AWT.MenuItem getAsMenuItem(IImplementedControl toObject)
    {
        return (Goliath.UI.AWT.MenuItem)toObject;
    }

    @Override
    public Class<MenuItem> getSupportedClass()
    {
        return MenuItem.class;
    }

    @Override
    public Dimension getPreferredSize(IImplementedControl toControl)
    {
        return Dimension.EMPTYSIZE();
    }

    @Override
    public boolean isShowing(IImplementedControl toControl)
    {
        return true;
    }

    @Override
    public Color getColour(IImplementedControl toControl)
    {
        return Color.BLACK;
    }

    @Override
    public void setColour(Color toColour, IImplementedControl toControl)
    {
        
    }

    @Override
    public void setFocus(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void forceRender(IImplementedControl toControl, long tnDelayMillis, Point toPoint, Dimension toDimension)
    {
        
    }
    
    









    @Override
    public List<ControlImplementationType> getSupportedTypes()
    {
        List<ControlImplementationType> loReturn = new List<ControlImplementationType>(1);
        loReturn.add(ControlImplementationType.AWTSYSTEMTRAY());
        return loReturn;
    }

    @Override
    public IImplementedControl createControl(IControl toBaseControl)
    {
        return new Goliath.UI.AWT.MenuItem(toBaseControl);
    }

    @Override
    public Point getLocation(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dimension getMaxSize(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dimension getMinSize(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Goliath.Graphics.Image getImage(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setImage(Goliath.Graphics.Image toImage, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IContainer getParent(IImplementedControl toControl)
    {
        Goliath.UI.AWT.Menu loMenu = (Goliath.UI.AWT.Menu)getAsMenuItem(toControl).getParent();
        return loMenu != null ? (IContainer)loMenu.getControlBase() : null;
    }





    @Override
    public String getName(IImplementedControl toControl)
    {
        return getAsMenuItem(toControl).getName();
    }

    @Override
    public Dimension getSize(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTooltip(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEnabled(IImplementedControl toControl)
    {
        return getAsMenuItem(toControl).isEnabled();
    }

    @Override
    public boolean isSelectable(IImplementedControl toControl)
    {
        return getAsMenuItem(toControl).isEnabled();
    }

    @Override
    public boolean isVisible(IImplementedControl toControl)
    {
        return true;
    }

    @Override
    public void setEnabled(boolean tlEnabled, IImplementedControl toControl)
    {
        getAsMenuItem(toControl).setEnabled(tlEnabled);
    }

    

    @Override
    public void setName(String tcName, IImplementedControl toControl)
    {
        getAsMenuItem(toControl).setName(tcName);
    }

    @Override
    public void setSelectable(boolean tlSelectable, IImplementedControl toControl)
    {
        // Do nothing here
    }

    @Override
    public void setLocation(Point toLocation, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMaxSize(Dimension toSize, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMinSize(Dimension toSize, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setSize(Dimension toSize, IImplementedControl toControl)
    {
        return false;
    }

    @Override
    public void setTooltip(String tcTooltip, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setVisible(boolean tlVisible, IImplementedControl toControl)
    {
        // Do nothing here
    }

    @Override
    public void invalidate(IImplementedControl toControl)
    {
        // Do nothing here
    }

    @Override
    public void update(IImplementedControl toControl)
    {
        // Do nothing here
    }

    @Override
    public boolean isDisplayable(IImplementedControl toControl)
    {
        return true;
    }

    @Override
    public boolean isEditable(IImplementedControl toControl)
    {
        return false;
    }

    @Override
    public void setEditable(boolean tlVisible, IImplementedControl toControl)
    {
        // Do nothing here.
    }

    @Override
    public String getText(IImplementedControl toControl)
    {
        return getAsMenuItem(toControl).getLabel();
    }

    @Override
    public void setText(String toValue, IImplementedControl toControl)
    {
        getAsMenuItem(toControl).setLabel(toValue);
    }

    @Override
    public float getOpacity(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOpacity(float tnOpacity, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    // TODO: This should actually be converting the event object to a goliath event, this should also be refactored to use factories
    protected Event<IControl> createEvent(IImplementedControl toControl, java.lang.Object toEventObject)
    {
        Event<IControl> loReturn = new Event<IControl>(toControl.getControlBase());
//        loReturn.setEventObject(toEventObject);
        return loReturn;
    }

    @Override
    protected boolean handleEventType(EventType toEvent, IImplementedControl toControl)
    {
        final IImplementedControl loClosure = toControl;

        if (toEvent == UIEventType.ONCLICK()
                || toEvent == UIEventType.ONMOUSEDOWN()
                || toEvent == UIEventType.ONMOUSEOUT()
                || toEvent == UIEventType.ONMOUSEOVER()
                || toEvent == UIEventType.ONMOUSEUP())
        {
            getAsMenuItem(toControl).addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent toEvent)
                    {
                        // TODO: Allow for alternate actions for example when shift is held, or ctrl is pressed on the click
                        loClosure.getControlBase().fireEvent(UIEventType.ONCLICK(), createEvent(loClosure, toEvent));
                    }
                });
                
            return true;
        }
        return super.handleEventType(toEvent, toControl);
    }

    @Override
    public Image getBackgroundImage(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBackground(Image toBackground, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isOpaque(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOpaque(boolean tlOpaque, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color getBackgroundColour(IImplementedControl toControl)
    {
        return null;
    }

    @Override
    public void setBackground(Color toBackground, IImplementedControl toControl)
    {
    }

    @Override
    public void setBorderSize(float tnTop, float tnRight, float tnBottom, float tnLeft, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFontSize(float tnSize, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getTextRotation(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTextRotation(float tnRotation, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void renderComponent(Object toRenderCanvas, IImplementedControl toControl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }








}
