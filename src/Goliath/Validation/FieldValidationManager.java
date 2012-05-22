/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Goliath.Validation;

import Goliath.Arguments.Arguments;
import Goliath.Interfaces.IValidatable;
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.DynamicCode.Java;
import Goliath.Environment;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.UI.Controls.UserControls.InvalidFieldControl;
import Goliath.Applications.Application;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.Controls.Label;
import Goliath.UI.Controls.Layouts.BorderLayoutConstants;

/**
 *
 * @author reshusinghania
 */
public class FieldValidationManager extends Goliath.Object
        implements IValidatable
{

    private HashTable<String, IValueControl> m_oPropertyControlMap;
    private IValidatable m_oValidationObject;
    private List<String> m_oInvalidProperties;

    public FieldValidationManager(IValidatable toValidationObject)
    {
        Goliath.Utilities.checkParameterNotNull("toValidationObject", toValidationObject);
        m_oValidationObject = toValidationObject;
    }

    public void linkProperty(String tcPropertyName, IValueControl toControl)
    {
        if (m_oPropertyControlMap == null)
        {
            m_oPropertyControlMap = new HashTable<String, IValueControl>();
        }
        m_oPropertyControlMap.put(tcPropertyName.toLowerCase(), toControl);
    }

    @Override
    public boolean isValid()
    {
        boolean lnIsValid = m_oValidationObject.isValid();
        updateUI();
        return lnIsValid;
    }

    public void UpdateOnCancel()
    {
        updateCancelUI();
    }

    private void updateCancelUI()
    {
        for (String lcProperty : m_oPropertyControlMap.keySet())
        {
            IValueControl loValueControl = getValueControlForProperty(lcProperty);
            if (loValueControl != null)
            {
                IContainer loInvalidField = loValueControl.getParent();

                if (Java.isEqualOrAssignable(InvalidFieldControl.class, loInvalidField.getClass()))
                {
                    IContainer loParent = loInvalidField.getParent();
                    int lnIndex = loParent.indexOf(loInvalidField);
                    loParent.removeControl(loInvalidField);
                    loParent.addControl(lnIndex, loValueControl);
                    loValueControl.removeClass("EditorFieldError");
                }
            }

        }
    }

    private void updateUI()
    {
        if (m_oPropertyControlMap != null)
        {
            for (String lcProperty : m_oPropertyControlMap.keySet())
            {
                BrokenRulesCollection loBrokenRules = m_oValidationObject.getBrokenRules(lcProperty);
                boolean llValid = loBrokenRules == null || loBrokenRules.size() == 0;

                if (llValid)
                {
                    // Clean up the control if it had an InvalidField control
                    if (m_oInvalidProperties != null && m_oInvalidProperties.contains(lcProperty))
                    {
                        IValueControl loValueControl = getValueControlForProperty(lcProperty);
                        if (loValueControl != null)
                        {
                            IContainer loInvalidField = loValueControl.getParent();
                            if (Java.isEqualOrAssignable(InvalidFieldControl.class, loInvalidField.getClass()))
                            {
                                // TODO: Use the replace control when moved to utilities
                                IContainer loParent = loInvalidField.getParent();
                                int lnIndex = loParent.indexOf(loInvalidField);
                                loParent.removeControl(loInvalidField);
                                loParent.addControl(lnIndex, loValueControl);
                                loValueControl.removeClass("EditorFieldError");
                            }
                        }
                        m_oInvalidProperties.remove(lcProperty);
                    }
                    else
                    {
                        // This means the properties may have been okay, but there was an underlying problem that
                        // stopped this from validating
                        // TODO: Need to add a formatted error message here.
                    }
                }
                else
                {
                    // Wrap the control in an invalid field control
                    if (m_oInvalidProperties == null || !m_oInvalidProperties.contains(lcProperty))
                    {
                        addInvalidControl(lcProperty);
                    }
                }
            }
        }
    }

    private void addInvalidControl(String tcProperty)
    {
        if (m_oInvalidProperties == null)
        {
            m_oInvalidProperties = new List<String>();
        }

        if (!m_oInvalidProperties.contains(tcProperty))
        {
            m_oInvalidProperties.add(tcProperty);

            IValueControl loControl = getValueControlForProperty(tcProperty);
            if (loControl != null)
            {
                InvalidFieldControl loFieldControl = new InvalidFieldControl();
                loFieldControl.wrapControl(loControl);
                List<BrokenRule> loRules = getBrokenRules(tcProperty);
                StringBuilder loBuilder = new StringBuilder();
                for (BrokenRule loRule : loRules)
                {
                    loBuilder.append(loRule.getMessage());
                    loBuilder.append(Environment.NEWLINE());
                }

                // Added a label showing the error message
                Label loErrorMsgLabel = new Label();
                loErrorMsgLabel.setText(loBuilder.toString());
                loErrorMsgLabel.setTooltip(loBuilder.toString());
                loErrorMsgLabel.setProperty(ControlLayoutProperty.LAYOUTCONSTRAINT().getValue(), BorderLayoutConstants.PAGE_END());
                loFieldControl.addControl(loErrorMsgLabel);
                
                // TODO: Added the error msg to tooltip because the label is covered due to rendering cycle issue.
                loControl.setTooltip(loBuilder.toString());
                loControl.addClass("EditorFieldError");

            }
        }
    }

    private IValueControl getValueControlForProperty(String tcProperty)
    {
        return m_oPropertyControlMap != null ? m_oPropertyControlMap.get(tcProperty.toLowerCase()) : null;
    }

    @Override
    public boolean addValidationException(String tcRuleName, Throwable toException)
    {
        return addValidationException(tcRuleName, "", toException);
    }

    @Override
    public boolean addValidationException(String tcRuleName, String tcPropertyName, Throwable toException)
    {
        return m_oValidationObject.addValidationException(tcRuleName, tcPropertyName, toException);
    }

    @Override
    public <T extends RuleHandler> void addClassValidationRule(Class<T> toRuleClass, String tcProperty, Arguments toArgs)
    {
        try
        {
            m_oValidationObject.addClassValidationRule(toRuleClass, tcProperty, toArgs);
        }
        catch (Goliath.Exceptions.InvalidPropertyException ex)
        {
            Application.getInstance().log(ex);
        }
    }

    @Override
    public <T extends RuleHandler> void addValidationRule(Class<T> toRuleClass, String tcProperty, Arguments toArgs)
    {
        try
        {
            m_oValidationObject.addValidationRule(toRuleClass, tcProperty, toArgs);
        }
        catch (Goliath.Exceptions.InvalidPropertyException ex)
        {
            Application.getInstance().log(ex);
        }
    }

    @Override
    public BrokenRulesCollection getBrokenRules()
    {
        return m_oValidationObject.getBrokenRules();
    }

    @Override
    public BrokenRulesCollection getBrokenRules(String tcProperty)
    {
        return m_oValidationObject.getBrokenRules(tcProperty);
    }

    @Override
    public boolean isValid(boolean tlClearBrokenRules)
    {
        return m_oValidationObject.isValid(tlClearBrokenRules);
    }
}
