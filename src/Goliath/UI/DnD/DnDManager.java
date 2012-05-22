/* ========================================================
 * DnDManager.java
 *
 * Author:      kenmchugh
 * Created:     Feb 22, 2011, 1:11:00 PM
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

package Goliath.UI.DnD;

import Goliath.Applications.Application;
import Goliath.Applications.UIApplicationSettings;
import Goliath.Collections.List;
import Goliath.Constants.MimeType;
import Goliath.Exceptions.ObjectNotCreatedException;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.DnD.IDnDManagerImpl;


        
/**
 * The DnDManager class controls the DnD system for the application
 *
 * @see         Related Class
 * @version     1.0 Feb 22, 2011
 * @author      kenmchugh
**/
public class DnDManager extends Goliath.Object
{
    private static DnDManager g_oDnDManager = null;

    private List<IControl> m_oTargets;
    private IDnDManagerImpl m_oImpl;

    /**
     * Singleton of the DnDManager
     */
    public static DnDManager getInstance()
    {
        if (g_oDnDManager == null)
        {
            g_oDnDManager = new DnDManager();
        }
        return g_oDnDManager;
    }

    /**
     * Creates a new instance of DnDManager
     * not publically creatable, use getInstance instead
     */
    private DnDManager()
    {
    }

    /**
     * Gets the implementation of the DnD manager, created it if needed
     * @return the implementation of the manager
     */
    protected IDnDManagerImpl getImpl()
    {
        if (m_oImpl == null)
        {
            // Load the implementation
            List<Class<IDnDManagerImpl>> loImplementationClasses = Application.getInstance().getObjectCache().getClasses(IDnDManagerImpl.class);
            for (Class<IDnDManagerImpl> loClass : loImplementationClasses)
            {
                try
                {
                    IDnDManagerImpl loImpl = loClass.newInstance();

                    // Check if it is the right supporting type
                    if (loImpl.supports(((UIApplicationSettings)Application.getInstance().getApplicationSettings()).getDefaultImplementationType()))
                    {
                        m_oImpl = loImpl;
                        break;
                    }
                }
                catch(Throwable ex)
                {
                    Application.getInstance().log(ex);
                }

                if (m_oImpl == null)
                {
                    throw new ObjectNotCreatedException("Could not create a DnDManager for the implementation type " + ((UIApplicationSettings)Application.getInstance().getApplicationSettings()).getDefaultImplementationType().getValue());
                }
            }
        }
        return m_oImpl;
    }



    /**
     * Allows a user to register a drop target
     * @param toControl the control to register as a drop target
     * @param toAccepts the MimeTypes this control will accept
     * @return true if this control is added to the list of drop targets
     */
    public boolean registerDropTarget(IControl toControl, List<MimeType> toAccepts)
    {
        if (m_oTargets == null)
        {
            m_oTargets = new List<IControl>();
        }
        if (!m_oTargets.contains(toControl))
        {
            if (m_oTargets.add(toControl))
            {
                if (!getImpl().registerTarget(toControl, toAccepts))
                {
                    return false;
                }
            }
            return m_oTargets.add(toControl);
        }
        return false;
    }
}
