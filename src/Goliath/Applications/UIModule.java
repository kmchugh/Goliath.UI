/* =========================================================
 * UIModule.java
 *
 * Author:      kmchugh
 * Created:     30-May-2008, 16:36:48
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
 * =======================================================*/

package Goliath.Applications;

import Goliath.Interfaces.Applications.IUIModule;
import Goliath.Interfaces.Commands.IContextCommand;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 30-May-2008
 * @author      kmchugh
**/
public abstract class UIModule extends Goliath.Applications.Module
        implements IUIModule
{
    //private IMenuBar m_oMenuBar = null;
    //private IMenuBar m_oTrayBar = null;
            
    /** Creates a new instance of UIModule */
    public UIModule()
    {
    }
    
    /**
     * Items in this menu bar are added to the application menu
     * @return
     */
    /*
    @Override
    public IMenuBar getMenuBar()
    {
        return m_oMenuBar;        
    }

    @Override
    public IMenuBar getTrayMenu()
    {
        return m_oTrayBar;
    }
    
    
    public void setMenuBar(IMenuBar toMenuBar)
    {
        m_oMenuBar = toMenuBar;        
    }
    
    public void setTrayBar(IMenuBar toMenuBar)
    {
        m_oTrayBar = toMenuBar;        
    }
    */
    protected IContextCommand onGetDefaultHandler()
    {
        return null;
    }

    @Override
    public final IContextCommand getDefaultContextHandler()
    {
        return onGetDefaultHandler();
    }
    
    @Override
    protected String onGetDefaultContext()
    {
        IContextCommand loCommand = getDefaultContextHandler();
        if (loCommand != null && loCommand.getContexts().size() > 0)
        {
            return loCommand.getContexts().get(0).toString();
        }
        return super.onGetDefaultContext();
    }
    
    
    
}
