/* ========================================================
 * EditorPane.java
 *
 * Author:      kmchugh
 * Created:     Feb 17, 2011, 11:53:46 AM
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
package Goliath.UI.Controls.RichEditors;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Delegate;
import Goliath.DynamicCode.Java;
import Goliath.Event;
import Goliath.Exceptions.FileNotFoundException;
import Goliath.Graphics.Constants.Orientation;
import Goliath.Graphics.Image;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Controls.Button;
import Goliath.UI.Controls.Combobox;
import Goliath.UI.Controls.Group;
import Goliath.UI.Controls.Layouts.FlowLayoutManager;
import Goliath.UI.Constants.UIEventType;
import java.util.List;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Feb 17, 2011
 * @author      kmchugh
 **/
public abstract class EditorPane<T extends RichEditor> extends Group
{
    public abstract class EditorState
    {

        public EditorState()
        {
        }

        public final String getStateName()
        {
            return getClass().getSimpleName();
        }

        public final boolean moveToState(EditorState toState)
        {
            return canMoveToState(toState);
        }

        public final void updateState()
        {
            if (m_oEditorControls != null && m_oEditorControls.size() > 0)
            {
                IList<String> loEnabled = getEnabledControls();
                IList<String> loVisible = getVisibleControls();



                for (String lcKey : m_oEditorControls.keySet())
                {
                    suspendLayout();
                    IControl loControl = m_oEditorControls.get(lcKey);
                    loControl.setEnabled(loEnabled.contains(lcKey));
                    loControl.setVisible(loVisible.contains(lcKey));
                    loControl.setParticipatesInLayout(loControl.isVisible());
                    resumeLayout();
                }
            }
        }

        protected boolean isOneOf(EditorState toState, Class<? extends EditorState>... taEditors)
        {
            for (int i=0, lnLength = taEditors.length; i<lnLength; i++)
            {
                if (taEditors[i].equals(toState.getClass()))
                {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final EditorState other = (EditorState) obj;
            if ((this.getStateName() == null) ? (other.getStateName() != null) : !this.getStateName().equals(other.getStateName()))
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 59 * hash + (this.getStateName() != null ? this.getStateName().hashCode() : 0);
            return hash;
        }






        protected abstract IList<String> getEnabledControls();
        protected abstract IList<String> getVisibleControls();

        protected abstract boolean canMoveToState(EditorState toState);


    }

    private T m_oEditor;
    private EditorState m_oCurrentState;
    private HashTable<String, IControl> m_oEditorControls;

    /**
     * Creates a new instance of EditorPane
     */
    public EditorPane(T toEditor)
    {
        registerEditor(toEditor);
        m_oEditor = toEditor;
        initialiseComponent();
    }

    /**
     * Initialises the component, sets the initial state
     */
    private void initialiseComponent()
    {
        //this.setLayoutManager(new FlowLayoutManager(Orientation.HORIZONTAL()));
        this.setLayoutManager(FlowLayoutManager.class);
    }

    /**
     * Sets the internal state for this editor
     * @param toState the new state for the editor
     * @return true if the state changed as a result of this call
     */
    protected final boolean setState(EditorState toState)
    {
        if (m_oCurrentState == null)
        {
            m_oCurrentState = toState;
            m_oCurrentState.updateState();
            fireEvent(UIEventType.ONSTATECHANGED(), new Event<EditorPane>(this));
            return true;
        }
        else if (!m_oCurrentState.equals(toState) && m_oCurrentState.canMoveToState(toState))
        {
            m_oCurrentState = toState;
            m_oCurrentState.updateState();
            fireEvent(UIEventType.ONSTATECHANGED(), new Event<EditorPane>(this));
            return true;
        }
        return false;
    }

    protected EditorState getCurrentState()
    {
        return m_oCurrentState;
    }

    protected void updateCurrentState()
    {
        if (m_oCurrentState != null)
        {
            m_oCurrentState.updateState();
        }
    }

    /**
     * Gets the editor that has been registered to this control
     * @return the editor registered to this control
     */
    public T getEditor()
    {
        return m_oEditor;
    }

    /**
     * Registers the editor with this pane for event listening
     */
    private void registerEditor(T toEditor)
    {
        if (m_oEditor != null)
        {
            unRegisterEditor();
        }
        m_oEditor = toEditor;

        // Hook up all the event listening
        toEditor.addEventListener(UIEventType.ONSELECTIONCHANGED(), Delegate.build(this, "selectionChanged"));

        setState(getInitialState());
    }

    protected abstract EditorState getInitialState();

    protected <K extends EditorState> K getEditorStateByClass(Class<K> toClass)
    {
        try
        {
            return Java.createObject(toClass, new Object[]{this});
        }
        catch (Throwable ex)
        {
            return null;
        }
    }

    private void selectionChanged(Event<T> toEvent)
    {
        onSelectionChanged(toEvent);
    }

    protected void onSelectionChanged(Event<T> toEvent)
    {

    }

    /**
     * Unregisters the current editor from this pane, this will unhook all events and remove the reference to the editor
     */
    public void unRegisterEditor()
    {
        // Unhook all the event listeners

        m_oEditor = null;
    }

    /**
     * Helper function to create editor buttons for the editor pane
     * @param tcKey the key to store the button as
     * @param tcTitle the title of the button
     * @param tcIconURL the icon for the button
     * @param tcCallback the callback for the button
     * @return the button created
     */
    protected final IButton createEditorButton(String tcKey, String tcTitle, String tcIconURL, String tcCallback)
    {
        Button loButton = null;
        Image loImage = null;

        try
        {
            loImage = new Goliath.Graphics.Image(tcIconURL);
            loImage.setScaleable(true);
        }
        catch (FileNotFoundException ex)
        {
            Application.getInstance().log(ex);
        }

        if (loImage != null)
        {
            loButton = new Button(tcTitle, loImage, Delegate.build(this, tcCallback));
        }
        else
        {
            loButton = new Button(tcTitle, Delegate.build(this, tcCallback));
        }
        storeControl(tcKey, loButton);
        return loButton;
    }

    protected final <T> Combobox<T> createEditorDropdown(String tcKey, IList<T> toData, String tcCallback)
    {
        Combobox<T> loCombobox = new Combobox<T>(toData);
        loCombobox.addEventListener(UIEventType.ONCHANGED(), Delegate.build(this, tcCallback));
        storeControl(tcKey, loCombobox);
        return loCombobox;
    }

    /**
     * Helper function to create distinct editor groups for controls in the editor pane
     * @param toControls the list of controls to put in the group
     * @return the container with the controls
     */
    protected final IContainer createEditorGroup(List<? extends IControl> toControls)
    {
        Group loGroup = new Group();
        //loGroup.setBorderSize(2, 2, 2, 2);
        //loGroup.setLayoutManager(new FlowLayoutManager(Orientation.HORIZONTAL()));
        loGroup.setLayoutManager(FlowLayoutManager.class);

        Image loLBGImage = null;
        Image loRBGImage = null;
        Image loBGImage = null;

        try
        {
            loLBGImage = new Image("./resources/images/buttons/small_button_left.png");
            loRBGImage = new Image("./resources/images/buttons/small_button_right.png");
            loBGImage = new Image("./resources/images/buttons/small_button_center.png");
        }
        catch(Throwable ex)
        {
        }



        for (int i=0, lnLength = toControls.size(); i<lnLength; i++)
        {
            IControl loControl = toControls.get(i);
            loGroup.addControl(loControl);

            if (Java.isEqualOrAssignable(Button.class, loControl.getClass()))
            {
                if (i==0 && lnLength > 1)
                {
                    loControl.setBackground(loLBGImage);
                    loControl.setSize(loLBGImage.getSize());
                }
                else if (i == lnLength-1 && lnLength > 1)
                {
                    loControl.setBackground(loRBGImage);
                    loControl.setSize(loRBGImage.getSize());
                }
                else
                {
                    loControl.setBackground(loBGImage);
                    loControl.setSize(loBGImage.getSize());
                }

                Image loIcon = ((IButton)loControl).getImage();
                if (loIcon != null)
                {
                    Image loBG = loControl.getBackgroundImage();
                    if (loBG != null)
                    {
                        loIcon.setSize(loBG.getSize());
                    }
                }
            }
        }

        return loGroup;
    }

    /**
     * Stores the control so it can be retrieved at a later time
     * @param tcKey the key for the control
     * @param toControl the control to store
     */
    protected void storeControl(String tcKey, IControl toControl)
    {
        if (m_oEditorControls == null)
        {
            m_oEditorControls = new HashTable<String, IControl>();
        }
        m_oEditorControls.put(tcKey, toControl);
    }

    /**
     * Gets the cached editor control, or null if a control does not exist
     * @param tcKey the key of the control to get
     * @return the control for the key, or null
     */
    protected IControl getStoredControl(String tcKey)
    {
        return (m_oEditorControls != null && m_oEditorControls.containsKey(tcKey)) ? m_oEditorControls.get(tcKey) : null;
    }
}
