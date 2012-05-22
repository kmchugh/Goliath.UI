/* ========================================================
 * ObjectEditor.java
 *
 * Author:      kmchugh
 * Created:     Dec 8, 2010, 10:23:42 AM
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
import Goliath.Collections.List;
import Goliath.Constants.EventType;
import Goliath.Delegate;
import Goliath.DynamicCode.Java;
import Goliath.DynamicEnum;
import Goliath.Event;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Interfaces.IParseValueMethod;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.UI.Controls.Button;
import Goliath.UI.Controls.Checkbox;
import Goliath.UI.Controls.Group;
import Goliath.UI.Controls.Label;
import Goliath.UI.Controls.Layouts.GridLayoutManager;
import Goliath.UI.Controls.Textbox;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Views.View;
import Goliath.Validation.FieldValidationManager;
import Goliath.Interfaces.IValidatable;
import Goliath.UI.Controls.UserControls.ColorChooser;
import Goliath.UI.Controls.Combobox;
import Goliath.UI.Controls.UserControls.DimensionTextbox;
import Goliath.UI.Controls.UserControls.FileTextbox;
import Goliath.UI.Controls.UserControls.PointTextbox;
import java.awt.Color;
import java.lang.reflect.Constructor;
       
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 8, 2010
 * @author      kmchugh
**/
public class ObjectEditor<T> extends View
        implements IValueControl<T>
{
    private static UIEventType g_oSavedEvent;
    public static UIEventType SAVED()
    {
        if (g_oSavedEvent == null)
        {
            g_oSavedEvent = new UIEventType("SAVED");
        }
        return g_oSavedEvent;
    }

    private static UIEventType g_oCancelledEvent;
    public static UIEventType CANCELLED()
    {
        if (g_oCancelledEvent == null)
        {
            g_oCancelledEvent = new UIEventType("CANCELLED");
        }
        return g_oCancelledEvent;
    }

    private T m_oObject;
    private Class<T> m_oClass;
    private Button m_oSave;
    private Button m_oCancel;
    private Group m_oButtonArea;
    private HashTable<String, String> m_oPropertyNameMap;
    private List<String> m_oReadOnlyProperties;
    private HashTable<String, IValueControl> m_oEditorInstances;
    private HashTable<String, Class<IValueControl>> m_oEditors;
    private HashTable<String, IParseValueMethod> m_oParseValueMethods;
    private List<String> m_oProperties;


    /**
     * Creates a new instance of ObjectEditor
     */
    public ObjectEditor()
    {
        initialiseComponent();
    }

    /**
     * Sets the value that is being editied by this editor
     * @param toObject the object that is to be displayed and edited
     */
    @Override
    public final void setValue(T toObject)
    {
        if (m_oObject != toObject)
        {
            m_oObject = toObject;
            if (m_oObject == null)
            {
               setFieldValidationManager(null);
               clearValues();
             }
            else
            {
                if(Goliath.DynamicCode.Java.isEqualOrAssignable(IValidatable.class, toObject.getClass()))
                {
                    // Sets up the field validation manager
                    setFieldValidationManager(new FieldValidationManager((IValidatable)toObject));
                }
                setClassTemplate(m_oObject.getClass());
                updateValues();
            }
            fireEvent(EventType.ONCHANGED(), new Event<ObjectEditor<T>>(this));
        }
    }

    /**
     * Gets the value that is being edited by this editor
     * @return the object being edited
     */
    @Override
    public final T getValue()
    {
        return m_oObject;
    }


    /**
     * Updates all of the value controls with the value from the object properties
     */
    private void updateValues()
    {
        if (m_oObject != null)
        {
            for (String lcProperty : getObjectProperties())
            {
                IValueControl loControl = getEditorForProperty(lcProperty);
                if (loControl != null)
                {

                    FieldValidationManager loFieldValidationManager = getFieldValidationManager();
                    if(loFieldValidationManager != null)
                    {
                        loFieldValidationManager.linkProperty(lcProperty,loControl);
                    }
                    try
                    {
                        Object loValue = Java.getPropertyValue(m_oObject, lcProperty);
                        try
                        {
                            putValueToEditor(lcProperty, loValue);
                        }
                        catch(Throwable ex)
                        {
                            loControl.setValue(loValue.toString());
                        }
                    }
                    catch(Throwable ex)
                    {
                        Application.getInstance().log(ex);
                    }
                }
            }
        }
        else
        {
            // Clear the values for any value control
            for (IValueControl loControl : m_oEditorInstances.values())
            {
                loControl.setValue(null);
            }
        }
    }

    /**
     * Clears all of the control values
     */
    private void clearValues()
    {
        if (m_oEditorInstances != null)
        {
            for (IValueControl loControl : m_oEditorInstances.values())
            {
                loControl.setValue(null);
            }
        }
    }

    /**
     * Sets the class that will be used to generate the editor fields
     * @param toClass the class of the type that will be edited
     */
    public final void setClassTemplate(Class toClass)
    {
        if (m_oClass != toClass)
        {
            m_oClass = toClass;
            generateEditor();
        }
    }

    /**
     * Creates the editor using the class specified as the template
     */
    private void generateEditor()
    {
        if (m_oClass != null)
        {
            clearEditor();
            List<String> loProperties = getObjectProperties();
            for(String lcProperty : loProperties)
            {
                generateEditorControl(lcProperty);
            }

            // Create the footer area
            int lnColumns = getColumns() * 2;
            int lnControls = loProperties.size() * 2;
            for (int i=0, lnLength = (lnColumns * (int)Math.ceil((double)lnControls/(double)lnColumns) - lnControls) + (lnColumns - 1); i<lnLength; i++)
            {
                addControl(new Label(" "));
            }

            addControl(getButtonArea());

            ((GridLayoutManager)getLayoutManager()).setRows(this, (int)Math.ceil((loProperties.size()/(double)getColumns())) + 1);
            invalidate();
        }
    }

    /**
     * Clears the controls from the editor
     */
    private void clearEditor()
    {
        clearChildren();
    }

    /**
     * Creates the Editor label and value control for the property specified
     * @param tcProperty the property to create the control for
     */
    private void generateEditorControl(String tcProperty)
    {
       // Add the value control
        IValueControl loControl = getEditorForProperty(tcProperty);
        if (loControl != null)
        {
            // Add the Label
            addControl(new Label(getPropertyName(tcProperty)));
        }

        if (isReadOnly(tcProperty))
        {
            loControl.setEditable(false);
            loControl.setEnabled(false);
        }

        addControl(loControl);
    }

    /**
     * Checks if the property specified is a read only property
     * @param tcProperty the name of the property
     * @return true if this property is read only
     */
    private boolean isReadOnly(String tcProperty)
    {
        return m_oReadOnlyProperties != null && m_oReadOnlyProperties.contains(tcProperty.toLowerCase());
    }

    /**
     * Creates or retrieves the value control editor for the property specified
     * @param tcProperty the property to get the control for
     * @return the editor control for this property
     */
    private IValueControl getEditorForProperty(String tcProperty)
    {
        tcProperty = tcProperty.toLowerCase();
        if (m_oEditorInstances == null || !m_oEditorInstances.containsKey(tcProperty))
        {
            // The editor does not exist, so create it
            
            if (m_oEditorInstances == null)
            {
                m_oEditorInstances = new HashTable<String, IValueControl>();
            }

            IValueControl loControl = null;

            // Look for an explicitly set editor class
            if (m_oEditors != null && m_oEditors.containsKey(tcProperty))
            {
                try
                {
                    Constructor<IValueControl> loConstruct = Java.getConstructor(m_oEditors.get(tcProperty), new Class[]{Class.class});
                    loControl = (loConstruct == null) ? m_oEditors.get(tcProperty).newInstance() : loConstruct.newInstance(Java.getPropertyType(m_oClass, tcProperty, false));
                }
                catch(Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }


            // If there is still no editor, then attempt to create a default
            if (loControl == null)
            {
                loControl = createEditorForType(Java.getPropertyType(m_oClass, tcProperty, false), tcProperty);
            }

            // Cache the editor
            m_oEditorInstances.put(tcProperty, loControl);
        }
        return m_oEditorInstances.get(tcProperty);
    }

    /**
     * Creates the default editor for the type specified
     * @param toValueType the type to create the editor for
     * @param tcProperty the property to link the editor to
     * @return the value control for the property
     */
    protected IValueControl createEditorForType(Class toValueType, String tcProperty)
    {
        if (toValueType == null)
        {
            return null;
        }
        Class loPrimitive = Java.getPrimitiveClass(toValueType);

        // If we are setting the value to null, we don't know what the type is so just clear
        if (loPrimitive != null)
        {
            if (loPrimitive.equals(boolean.class))
            {
                return new Checkbox();
            }
            else if (toValueType.equals(String.class))
            {
                return new Textbox();
            }
            else
            {
                return new Textbox();
            }
        }
        else if (loPrimitive == null)
        {
            // This was not a primitive type, so we need to create a full editor
            if (Java.isEqualOrAssignable(java.util.List.class, toValueType))
            {
                return new ListEditor(toValueType, false, getPropertyName(tcProperty) + " List");
            }
            else if (Java.isEqualOrAssignable(java.io.File.class, toValueType))
            {
                return new FileTextbox();
            }
            else if (Java.isEqualOrAssignable(DynamicEnum.class, toValueType))
            {
                Combobox<? extends DynamicEnum> loCombobox = new Combobox(DynamicEnum.getEnumerations(toValueType));
                return loCombobox;
            }
            else if (Java.isEqualOrAssignable(Dimension.class, toValueType))
            {
                return new DimensionTextbox();
            }
            else if (Java.isEqualOrAssignable(Point.class, toValueType))
            {
                return new PointTextbox();
            }
            else if (Java.isEqualOrAssignable(Color.class, toValueType))
            {
                return new ColorChooser();
            }
        }
        
        // If all else fails
        return new Textbox();
    }

    /**
     * Sets this property editor to readonly
     * @param tcProperty the property to set to readonly
     */
    public final void setReadonlyProperty(String tcProperty)
    {
        if (m_oReadOnlyProperties == null)
        {
            m_oReadOnlyProperties = new List<String>();
        }
        m_oReadOnlyProperties.add(tcProperty.toLowerCase());
    }
    
    /**
     * Sets the method that will be used to parse the value from and to the editor if a conversion is needed
     * @param tcProperty the property that the method will be used for
     * @param toMethod the method that is to be used
     */
    public final void setParseValueMethod(String tcProperty, IParseValueMethod toMethod)
    {
        if (m_oParseValueMethods == null)
        {
            m_oParseValueMethods = new HashTable<String, IParseValueMethod>();
        }
        m_oParseValueMethods.put(tcProperty.toLowerCase(), toMethod);
    }
    
    /**
     * Checks if the property specified has a parse method
     * @param tcProperty the property to check
     * @return true if there is a parse method
     */
    public final boolean hasParseValueMethod(String tcProperty)
    {
        return m_oParseValueMethods != null && m_oParseValueMethods.containsKey(tcProperty.toLowerCase());
    }
    
    /**
     * Gets the method used to parse data to and from the editor for the specified property
     * @param tcProperty the property to get the method for
     * @return the method, or null if no method exists
     */
    public final IParseValueMethod getParseValue(String tcProperty)
    {
        return m_oParseValueMethods != null ? m_oParseValueMethods.get(tcProperty.toLowerCase()) : null;
    }

    /**
     * Sets the property editor class for the specified property
     * @param <K>
     * @param tcProperty the property to set editor for
     * @param toEditorClass the proeprty editor class
     */
    public final <K extends IValueControl> void setPropertyEditor(String tcProperty, Class<K> toEditorClass)
    {
        tcProperty = tcProperty.toLowerCase();
        if (m_oEditors == null)
        {
            m_oEditors = new HashTable<String, Class<IValueControl>>();
        }
        m_oEditors.put(tcProperty, (Class<IValueControl>)toEditorClass);
    }
    
    /**
     * Sets the editor instance for this property
     * @param <K>
     * @param tcProperty the property to set the instance for
     * @param toEditor the property editor
     */
    public final <K extends IValueControl> void setPropertyEditor(String tcProperty, K toEditor)
    {
        tcProperty = tcProperty.toLowerCase();
        if (m_oEditorInstances == null)
        {
            m_oEditorInstances = new HashTable<String, IValueControl>();
        }
        m_oEditorInstances.put(tcProperty, toEditor);
    }

    /**
     * Gets the displayable name for the property specified
     * @param tcProperty the proeprty to get the displayable name for
     * @return the displayable name for the property
     */
    public final String getPropertyName(String tcProperty)
    {
        return (m_oPropertyNameMap != null && m_oPropertyNameMap.containsKey(tcProperty.toLowerCase())) ? m_oPropertyNameMap.get(tcProperty.toLowerCase()) : tcProperty;
    }

    /**
     * Sets the name that will be displayed for each of the properties
     * @param tcProperty the property
     * @param tcDisplay the text to display for the property
     */
    public final void setPropertyName(String tcProperty, String tcDisplay)
    {
        if (m_oPropertyNameMap == null)
        {
            m_oPropertyNameMap = new HashTable<String, String>();
        }
        m_oPropertyNameMap.put(tcProperty.toLowerCase(), tcDisplay);
    }

    /**
     * Gets the editor button area
     * @return the editor button area
     */
    protected IContainer getButtonArea()
    {
        if (m_oButtonArea == null)
        {
            m_oButtonArea = new Group();
            m_oSave = new Button("Okay", Delegate.build(this, "onSaveClicked"));
            m_oCancel = new Button("Close", Delegate.build(this, "onCancelClicked"));

            m_oSave.addClass("OkayButton");
            m_oCancel.addClass("CancelButton");

            //m_oButtonArea.setLayoutManager(new GridLayoutManager(1, 2));
            m_oButtonArea.setLayoutManager(GridLayoutManager.class);
            m_oButtonArea.addControl(m_oSave);
            m_oButtonArea.addControl(m_oCancel);
        }
        return m_oButtonArea;
    }

    /**
     * Handler for when the save button is clicked
     * @param toEvent the event
     */
    protected void onSaveClicked(Event<IControl> toEvent)
    {
        writeValuesToObject();

        FieldValidationManager loFieldValidationManager = getFieldValidationManager();

        if ( loFieldValidationManager != null)
        {
            if( !loFieldValidationManager.isValid())
            {

                return;
            }
        }
        fireEvent(SAVED(), new Event<IControl>(this));
    }

    /**
     * handler for when the cancel button is clicked
     * @param toEvent the event
     */
    protected void onCancelClicked(Event<IControl> toEvent)
    {
        updateValues();

        FieldValidationManager loFieldValidationManager = getFieldValidationManager();

        if ( loFieldValidationManager != null)
        {
            loFieldValidationManager.UpdateOnCancel();
        }
       
        fireEvent(CANCELLED(), new Event<IControl>(this));
    }
    
    /**
     * Helper method to parse the value from the editor control
     * @param tcProperty the property to get the value for
     * @return the parsed value
     */
    private Object getValueFromEditor(String tcProperty)
    {
        IValueControl loControl = getEditorForProperty(tcProperty);
        Object loValue = loControl.getValue();
        if (hasParseValueMethod(tcProperty))
        {
            loValue = getParseValue(tcProperty).parseFromValue(loValue);
        }
        return loValue;
    }
    
    /**
     * Helper method to parse the value from the editor control
     * @param tcProperty the property to get the value for
     * @return the parsed value
     */
    private void putValueToEditor(String tcProperty, Object toValue)
    {
        IValueControl loControl = getEditorForProperty(tcProperty);
        if (hasParseValueMethod(tcProperty))
        {
            toValue = getParseValue(tcProperty).parseToValue(toValue);
        }
        loControl.setValue(toValue);
    }

    /**
     * Writes the values from the editor control back to the object
     */
    private void writeValuesToObject()
    {
        for (String lcProperty : getObjectProperties())
        {
            if (!isReadOnly(lcProperty))
            {
                try
                {
                    Object loValue = getValueFromEditor(lcProperty);
                    if (loValue != null)
                    {
                        Java.setPropertyValue(m_oObject, lcProperty, loValue);
                    }
                }
                catch(Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }
        }
    }

    /**
     * Gets the properties for the template class
     * @return the list of properties on the template class
     */
    private List<String> getObjectProperties()
    {
        if (m_oProperties == null)
        {
            m_oProperties = m_oClass != null ? Java.getPropertyMethods(m_oClass) : new List<String>();
        }
        return m_oProperties;
    }

    /**
     * Adds a property to the list of current properties
     * @param tcProperty the property to add
     */
    public void addProperty(String tcProperty)
    {
        if (m_oProperties == null)
        {
            m_oProperties = new List<String>();
        }
        if (!m_oProperties.contains(tcProperty))
        {
            m_oProperties.add(tcProperty);
        }
    }

    /**
     * Initialises the component
     */
    private void initialiseComponent()
    {
        //setLayoutManager(new GridLayoutManager(1, 2));
        setLayoutManager(GridLayoutManager.class);
    }

    /**
     * Sets the number of columns in the editor
     * @param m_nColumns the number of columns that will be displayed
     */
    public void setColumns(int tnColumns)
    {
        ((GridLayoutManager)getLayoutManager()).setColumns(this, Math.max(1, tnColumns) * 2);
    }

    /**
     * gets the number of columns for the editor
     * @return the number of columns the editor will be arranged in to
     */
    public int getColumns()
    {
        return ((GridLayoutManager)getLayoutManager()).getColumns(this) /2;
    }

}
