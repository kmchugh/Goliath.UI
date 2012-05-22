/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Commands;

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.ExecutableXML;
import Goliath.Interfaces.UI.Controls.IControl;

/**
 *
 * @author kenmchugh
 */
public abstract class UICommand extends Goliath.Commands.Command<UICommandArgs, ExecutableXML>
        implements Goliath.Interfaces.Commands.IUICommand
{   
    private ExecutableXML m_oXML;
    private List<IControl> m_oDependencies;
    private UICommandType m_oType;
    
    /** Creates a new instance of ExecuteCommand */
    public UICommand()
    {
        super(false);
        m_oType = UICommandType.DEFAULT();
    }


    public UICommandType getCommandType()
    {
        return m_oType;
    }

    protected void setUICommandType(UICommandType toType)
    {
        m_oType = toType;
    }

    private List<IControl> getDependencyList()
    {
        if (m_oDependencies == null)
        {
            m_oDependencies = new List<IControl>(5);
        }
        return m_oDependencies;
    }

    @Override
    public void addDependency(IControl toControl)
    {
        List<IControl> loList = getDependencyList();
        if (!loList.contains(toControl))
        {
            loList.add(toControl);
        }
    }

    @Override
    public List<IControl> getControlDependencies()
    {
        return getDependencyList();
    }


    
    public final ExecutableXML doExecute() throws Throwable
    {
        m_oXML = new ExecutableXML();
        
        // set the response headers
        try
        {
            onDoExecute();
        }
        catch(Throwable ex)
        {
            // Log the error
            Application.getInstance().log(ex);
        }
        return m_oXML;
    }
    
    protected abstract void onDoExecute();
    
    protected org.w3c.dom.Node getNode()
    {
        return getArguments().getNode();
    }
    
    protected IControl getControl(String tcName)
    {
        //return getApplicationInstance().getControlByName(tcName);
        return null;
    }
    
    protected void appendScript(String tcScript)
    {
        m_oXML.appendScript(tcScript);
    }
    
    protected void appendResponseXML(String tcHTML)
    {
        m_oXML.appendResponseXML(tcHTML);
    }
    
    
    
}
