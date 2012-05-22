/* =========================================================
 * View.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * A view is a control that groups functionality together.
 * A view should encapsulate all the visual controls required for a
 * specific feature, and should be associated with a ViewController that
 * controls interaction between the view and the model.  
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/
package Goliath.UI.Views;

import Goliath.Arguments.Arguments;
import Goliath.Interfaces.IValidatable;
import Goliath.Interfaces.UI.Controls.IView;
import Goliath.Interfaces.UI.Controls.Layouts.ILayoutManager;
import Goliath.UI.Controls.Container;
import Goliath.UI.Controls.ControlImplementationType;
import Goliath.UI.Controls.Layouts.BorderLayoutManager;
import Goliath.Validation.BrokenRulesCollection;
import Goliath.Validation.FieldValidationManager;
import Goliath.Validation.RuleHandler;

/**
 *
 * @author kmchugh
 */
public class View extends Container
        implements IView, IValidatable
{
    // TODO: Create dynamic views that are definable from xml layout files (gsp)
    
    private String m_cTitle;
    private FieldValidationManager m_oFieldValidationManager;

    public View()
    {
        initialiseComponent();

    }

    public View(Class<? extends ILayoutManager> toManager)
    {
        super(toManager);
    }



    public View(ControlImplementationType toType)
    {
        super(toType);
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        setLayoutManager(BorderLayoutManager.class);
        
    }

    @Override
    public String getTitle()
    {
        return m_cTitle;
    }

    @Override
    public void setTitle(String tcTitle)
    {
        m_cTitle = tcTitle;
    }

    public void setFieldValidationManager(FieldValidationManager toFieldValidationManager)
    {
        m_oFieldValidationManager = toFieldValidationManager;
    }

    protected FieldValidationManager getFieldValidationManager()
    {
        return m_oFieldValidationManager;
    }
    
    @Override
    public <T extends RuleHandler> void addClassValidationRule(Class<T> toRuleClass, String tcProperty, Arguments toArgs)
    {
       if ( m_oFieldValidationManager != null)
       {
           m_oFieldValidationManager.addClassValidationRule(toRuleClass, tcProperty, toArgs);
       }
    }

    @Override
    public <T extends RuleHandler> void addValidationRule(Class<T> toRuleClass, String tcProperty, Arguments toArgs)
    {
        if ( m_oFieldValidationManager != null)
       {
           m_oFieldValidationManager.addValidationRule(toRuleClass, tcProperty, toArgs);
       }
    }

    @Override
    public BrokenRulesCollection getBrokenRules()
    {
        if ( m_oFieldValidationManager != null)
       {
           m_oFieldValidationManager.getBrokenRules();
       }

        return null;
    }

    @Override
    public BrokenRulesCollection getBrokenRules(String tcProperty)
    {
        if (m_oFieldValidationManager != null)
        {
            m_oFieldValidationManager.getBrokenRules();
        }
        return null;
    }

    @Override
    public boolean addValidationException(String tcRuleName, Throwable toException)
    {
        return addValidationException(tcRuleName, "", toException);
    }

    @Override
    public boolean addValidationException(String tcRuleName, String tcPropertyName, Throwable toException)
    {
        if (m_oFieldValidationManager != null)
        {
            return m_oFieldValidationManager.addValidationException(tcRuleName, tcPropertyName, toException);
        }
        return false;
    }



    @Override
    public boolean isValid()
    {
        if ( m_oFieldValidationManager != null)
        {
            m_oFieldValidationManager.isValid();
        }
        return false;
    }

    @Override
    public boolean isValid(boolean tlClearBrokenRules)
    {
        if ( m_oFieldValidationManager != null)
        {
            m_oFieldValidationManager.isValid();
        }
        return false;
    }






}
