/* ========================================================
 * ListEditor.java
 *
 * Author:      kenmchugh
 * Created:     Dec 30, 2010, 5:22:06 PM
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
import Goliath.Collections.HashTable;
import Goliath.Constants.EventType;
import Goliath.Delegate;
import Goliath.DynamicCode.Java;
import Goliath.Event;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.UI.Controls.Button;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Views.TableSelectView;
import Goliath.UI.Windows.Window;
import java.lang.reflect.Method;
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 30, 2010
 * @author      kenmchugh
**/
public class ListEditor<T> extends Button
        implements IValueControl<IList<T>>
{
    private IList m_oValue;
    private IWindow m_oWindow;
    private TableSelectView<T> m_oTableEditView;   
    private boolean m_lAllowNew; 
    private String m_cTitle;
    private boolean m_lWindowOpen;
    private HashTable<String, String> m_oPropertyNames;

    /**
     * Creates a new instance of ListEditor
     */
    public ListEditor(Class<T> toClass, HashTable<String, String> toPropertyNameMap, boolean tlAllowNew, String tcTitle)
    {
        m_oPropertyNames = toPropertyNameMap;
        m_lAllowNew = tlAllowNew;
        m_cTitle = tcTitle;
        initialiseComponent();
    }

    /**
     * Creates a new instance of ListEditor
     */
    public ListEditor(Class<T> toClass, boolean tlAllowNew, String tcTitle)
    {
        m_lAllowNew = tlAllowNew;
        m_cTitle = tcTitle;
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        addEventListener(UIEventType.ONCLICK(), Delegate.build(this, "onEditorClicked"));
    }

    @Override
    public IList<T> getValue()
    {
        return m_oValue;
    }

    @Override
    public void setValue(IList<T> toValue)
    {
        m_oValue = toValue;
        if (m_oTableEditView != null)
        {
            m_oTableEditView.setData(null);
        }


        updateText();
    }

    private void updateText()
    {
        if (m_oValue == null)
        {
            setText("Click to add");
        }
        else
        {
            setText("Edit (" + (m_oValue == null ? 0 : m_oValue.size()) + " items)");
        }
    }

    private IList<T> getSelectionList()
    {
        IList<T> loReturn = new Goliath.Collections.List<T>();

        if (m_oValue != null)
        {
            try
            {
                Class loDDO = Class.forName("Goliath.Collections.DynamicDataObjectCollection");
                // TODO: This should be done using an interface rather than hardcoding
                if (Java.isEqualOrAssignable(loDDO, m_oValue.getClass()))
                {
                    IList<T> loList = (IList<T>)Java.createObject(m_oValue.getClass(), new java.lang.Object[]{Java.getContainedClass(m_oValue)});
                    Method loMethod = Java.getMethod(loList.getClass(), "loadList", null);
                    loMethod.invoke(loList, (java.lang.Object[])null);

                    return loList;
                }
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);
            }
        }

        return loReturn;
    }
    
    private IWindow getWindow()
    {
        if (m_oWindow == null)
        {
            m_oWindow = new Window();
            m_oTableEditView = new TableSelectView<T>(Java.getContainedClass(m_oValue), m_oPropertyNames, false, false, false, false, true, true);         
            m_oTableEditView.setData((IList<T>)m_oValue);
            m_oTableEditView.setSelectionListDelegate(Delegate.<IList<T>>build(this, "getSelectionList"));
            m_oTableEditView.setMaxSelection(9999);
            m_oWindow.addControl(m_oTableEditView);
            m_oWindow.setTitle(m_cTitle);
            m_oWindow.setMinSize(new Dimension(500, 100));
            m_oWindow.addEventListener(UIEventType.ONDEACTIVATED(), Delegate.build(this, "onWindowDeactivated"));
            m_oWindow.addEventListener(UIEventType.ONCLOSED(), Delegate.build(this, "onEditorClosed"));

        }
        return m_oWindow;
    }
    
    private void onEditorClosed(Event<IControl> toEvent)
    {
        // TODO: We should only be firing this event if the value has actually changed
        fireEvent(EventType.ONCHANGED(), new Event<ListEditor<T>>(this));
    }


    private void onWindowDeactivated(Event<IControl> toEvent)
    {
        Window loWindow = (Window)toEvent.getTarget();
        if (loWindow != null)
        {
            loWindow.setVisible(false);
            m_lWindowOpen = false;
        }
        this.updateText();
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
        }
    }


}
