/* ========================================================
 * AWTSystemMenuImpl.java
 *
 * Author:      kmchugh
 * Created:     Aug 18, 2010, 5:04:14 PM
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

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Image;
import Goliath.Graphics.Point;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.Implementations.IContainerImpl;
import Goliath.Interfaces.UI.Controls.Implementations.IImplementedControl;
import java.awt.Color;


        
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
public class AWTSystemMenuImpl extends ControlImplementation<Menu>
        implements IContainerImpl
{
    protected Goliath.UI.AWT.Menu getAsMenu(IImplementedControl toObject)
    {
        return (Goliath.UI.AWT.Menu)toObject;
    }

    @Override
    public Class<Menu> getSupportedClass()
    {
        return Menu.class;
    }

    @Override
    public List<ControlImplementationType> getSupportedTypes()
    {
        List<ControlImplementationType> loReturn = new List<ControlImplementationType>(1);
        loReturn.add(ControlImplementationType.AWTSYSTEMTRAY());
        return loReturn;
    }

    @Override
    public Dimension getPreferredSize(IImplementedControl toControl)
    {
        return Dimension.EMPTYSIZE();
    }

    @Override
    public void forceRender(IImplementedControl toControl, long tnDelayMillis, Point toPoint, Dimension toDimension)
    {
        
    }

    


    @Override
    public boolean addControl(int tnIndex, IControl toControl, IImplementedControl toControlBase)
    {
        getAsMenu(toControlBase).add((java.awt.MenuItem)toControl.getControl());
        return true;
    }

    @Override
    public boolean isShowing(IImplementedControl toControl)
    {
        return false;
    }

    @Override
    public void setFocus(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearChildren(IImplementedControl toControlBase)
    {
        getAsMenu(toControlBase).removeAll();
    }

    @Override
    public IControl getChildControlByName(String tcName, IImplementedControl toControlBase)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<IControl> getChildren(IImplementedControl toControlBase)
    {
        Goliath.UI.AWT.Menu loMenu = getAsMenu(toControlBase);
        int lnChildCount = loMenu.getItemCount();
        List<IControl> loReturn = new List<IControl>(lnChildCount);
        for (int i=0; i<lnChildCount; i++)
        {
            try
            {
                loReturn.add(((Goliath.UI.AWT.MenuItem)loMenu.getItem(i)).getControlBase());
            }
            catch(Throwable ex)
            {
                Application.getInstance().log(ex);
            }
        }
        return loReturn;
    }

    @Override
    public boolean removeControl(IControl toControl, IImplementedControl toControlBase)
    {
        return false;
    }

    @Override
    public IContainer getParent(IImplementedControl toControl)
    {
        // TODO: Check what the parents actually are when attached to an app, and return if needed
        return null;
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
    public float getOpacity(IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOpacity(float tnOpacity, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    @Override
    public IImplementedControl createControl(IControl toBaseControl)
    {
        return new Goliath.UI.AWT.Menu(toBaseControl);
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
    public String getName(IImplementedControl toControl)
    {
        return getAsMenu(toControl).getName();
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
        return getAsMenu(toControl).isEnabled();
    }

    @Override
    public boolean isSelectable(IImplementedControl toControl)
    {
        return getAsMenu(toControl).isEnabled();
    }

    @Override
    public boolean isVisible(IImplementedControl toControl)
    {
        return true;
    }

    @Override
    public void setEnabled(boolean tlEnabled, IImplementedControl toControl)
    {
        getAsMenu(toControl).setEnabled(tlEnabled);
    }

    @Override
    public void setName(String tcName, IImplementedControl toControl)
    {
        getAsMenu(toControl).setName(tcName);
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
    }

    @Override
    public Dimension getClientSize(IImplementedControl toControlBase)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dimension getPreferredClientSize(IImplementedControl toControlBase)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doLayout(IImplementedControl toControlBase)
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
        
    }

    @Override
    public int indexOf(IControl toControl, IImplementedControl toControlBase)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFontSize(float tnSize, IImplementedControl toControl)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void renderComponent(Object toRenderCanvas, IImplementedControl toControl) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }








    
}
