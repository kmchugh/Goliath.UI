/* ========================================================
 * ListPicker.java
 *
 * Author:      kenmchugh
 * Created:     Mar 18, 2011, 1:45:55 PM
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

package Goliath.UI.Controls.UserControls.DynamicEditors;

import Goliath.Applications.Application;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.UI.Controls.Button;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Views.TableSelectView;
import Goliath.UI.Windows.Window;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Mar 18, 2011
 * @author      kenmchugh
**/
public class ListPicker<T> extends Button
        implements IValueControl<T>
{
    private T m_oValue;
    private TableSelectView<T> m_oTableSelectView;
    private IWindow m_oSelectionWindow;
    private Class<T> m_oClass;
    private IDelegate<IList<T>> m_oDataLoadDelegate;
    private boolean m_lWindowOpen;
    private String m_cTitle;

    public ListPicker(Class<T> toClass)
    {
        m_oClass = toClass;
        initialiseComponent();
    }

    public ListPicker(Class<T> toClass, String tcTitle)
    {
        m_oClass = toClass;
        m_cTitle = tcTitle;
        initialiseComponent();
    }

    
    public ListPicker(Class<T> toClass, IDelegate<IList<T>> toLoadDataDelegate, String tcTitle)
    {
        this(toClass);
        m_oDataLoadDelegate = toLoadDataDelegate;
        m_cTitle = tcTitle;
        initialiseComponent();
    }
    
    private void initialiseComponent()
    {
        addEventListener(UIEventType.ONCLICK(), Delegate.build(this, "onEditorClicked"));
    }

    @Override
    public T getValue()
    {
        return m_oValue;
    }

    @Override
    public void setValue(T toValue)
    {
        m_oValue = toValue;
        if (toValue == null)
        {
            setText("Click to Select");
        }
        else
        {
            setText(m_oValue.toString());
        }
    }

    public void setDataLoadDelegate(IDelegate<IList<T>> toDataLoadDelegate)
    {
        m_oDataLoadDelegate = toDataLoadDelegate;
    }

    public void setList(IList<T> toList)
    {
        if (m_oTableSelectView == null)
        {
            getWindow();
        }
        if (m_oTableSelectView != null)
        {
            m_oTableSelectView.setData(toList);
            invalidate();
        }
    }


    private IWindow getWindow()
    {
        if (m_oSelectionWindow == null)
        {
            m_oSelectionWindow = new Window();
            if (m_cTitle != null)
            {
                m_oSelectionWindow.setTitle(m_cTitle);
            }
            m_oTableSelectView = new TableSelectView(m_oClass);
            m_oTableSelectView.setMaxSelection(1);
            if (m_oDataLoadDelegate != null)
            {
                try
                {
                    m_oTableSelectView.setData(m_oDataLoadDelegate.invoke());
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }
            m_oTableSelectView.addEventListener(UIEventType.ONSELECTIONCHANGED(), Delegate.build(this, "onSelectionChanged"));
            m_oSelectionWindow.addEventListener(UIEventType.ONDEACTIVATED(), Delegate.build(this, "onWindowDeactivated"));
            m_oSelectionWindow.addControl(m_oTableSelectView);
            m_oSelectionWindow.setMinSize(new Dimension(500, 100));
        }
        return m_oSelectionWindow;
    }

    private void onWindowDeactivated(Event<IControl> toEvent)
    {
        Window loWindow = (Window)toEvent.getTarget();
        if (loWindow != null)
        {
            loWindow.setVisible(false);
            m_lWindowOpen = false;
        }
    }

    private void onSelectionChanged(Event<IControl> toEvent)
    {
        IList<T> loItems = m_oTableSelectView.getSelected();
        setValue(loItems != null && loItems.size() > 0 ? loItems.get(0) : null);
    }

    private void onEditorClicked(Event<IButton> toEvent)
    {
        if (getWindow().isVisible() || m_lWindowOpen)
        {
            getWindow().setVisible(false);
            m_lWindowOpen = false;
        }
        else
        {
            Point loLocation = toEvent.getTarget().getScreenCoordinates();
            getWindow().setLocation(loLocation.getX(), loLocation.getY() + toEvent.getTarget().getSize().getHeight());
            getWindow().setVisible(true);
            m_lWindowOpen = true;
            

            if (m_oDataLoadDelegate != null)
            {
                try
                {
                    m_oTableSelectView.setData(m_oDataLoadDelegate.invoke());
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }
            m_oTableSelectView.selectItem(getValue());
        }
    }
}
