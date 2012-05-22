/* =========================================================
 * UIApplicationSettings.java
 *
 * Author:      kmchugh
 * Created:     26 November 2007, 15:33
 *
 * Description
 * --------------------------------------------------------
 * Controls the application behaviours for a regular UI application
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Applications;

import Goliath.Collections.HashTable;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Interfaces.Applications.IApplicationController;
import Goliath.Interfaces.Applications.IUIApplicationSettings;
import Goliath.Interfaces.ISession;
import Goliath.Interfaces.UI.Controls.IApplicationWindow;
import Goliath.LibraryVersion;
import Goliath.Session;
import Goliath.UI.Controls.ControlImplementationType;
import Goliath.UI.Controls.Menu;
import Goliath.UI.Controls.MenuItem;
import Goliath.Utilities;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.util.TimerTask;

/**
 * Controls the application behaviours for a regular web application
 *
 * @see         Related Class
 * @version     1.0 26 November 2007
 * @author      kmchugh
 **/
public abstract class UIApplicationSettings<A extends IApplicationController> extends Goliath.Applications.ApplicationSettings<A>
        implements IUIApplicationSettings
{
    private HashTable<String, IApplicationWindow> m_oApplicationWindows;
    private Menu m_oMenu;
    private String m_cTrayName;


    
    /**
     * This method is used to get the ApplicationWindow, the application
     * window is the controlling, or top level, window for the application
     * @param toSession the session.
     * @return the application window for the specified application
     */
    @Override
    public final IApplicationWindow getApplicationWindow()
    {
        // TODO: See if this is still needed to contain a collection.  Rules should be changed so there is only one window per app
        ISession loSession = Session.getCurrentSession();
        String lcKey = loSession.getSessionID();
        if (m_oApplicationWindows == null || !m_oApplicationWindows.containsKey(lcKey))
        {
            if (m_oApplicationWindows == null)
            {
                m_oApplicationWindows = new HashTable<String, IApplicationWindow>();
            }

            if (m_oApplicationWindows.size() == 1)
            {
                return m_oApplicationWindows.values().iterator().next();
            }

            try
            {
                IApplicationWindow loWindow = createApplicationWindow();
                m_oApplicationWindows.put(lcKey, loWindow);
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);
            }
        }
        return m_oApplicationWindows.get(lcKey);
    }
    
    /**
     * Gets the default implementation type for this type of application, this can be overridden in subclasses to change the type
     * @return the default implementation type
     */
    @Override
    public abstract ControlImplementationType getDefaultImplementationType();





    /**
     * Alerts the user by popping up in the Application window
     * @param tcMessage the message to alert the user with
     * @param tcTitle the title of the message
     */
    @Override
    public final void alert(String tcMessage, String tcTitle)
    {
        // TODO: Implement proper and custom dialogs
        onAlert(tcMessage, tcTitle);
    }

    /**
     * Hook method to allow subclasses to interact with alert
     * @param tcMessage the message
     * @param tcTitle the title of the message
     */
    protected void onAlert(String tcMessage, String tcTitle)
    {   
    }


    /**
     * Displays the user interface to the user
     */
    protected abstract void showUserInterface();

    /**
     * True if this Application should show a user interface when it starts up, false otherwise
     * This should be overridden in sub classes to change the action
     * @return true to display a user interface, false otherwise
     */
    protected boolean getShowUserInterface()
    {
        return true;
    }

    /**
     * Creates the window that becomes the controlling window for the session
     * @param toSession The session to create the window for
     * @return the application window for the specified session
     */
    protected final IApplicationWindow createApplicationWindow()
    {
        try
        {
            return onCreateApplicationWindow();
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        return null;
    }

    private void onSessionExpired(Event<ISession> toEvent)
    {
        // Clean up the application windows if there is one for this session
        if (m_oApplicationWindows != null)
        {
            m_oApplicationWindows.remove(toEvent.getTarget().getSessionID());
        }
    }

    /**
     * Creates the application window for the specified session
     * @param toSession the session to create the window for
     * @return the new application window
     */
    protected abstract IApplicationWindow onCreateApplicationWindow();

    private void onCloseClicked(Event toEvent)
    {
        Application.getInstance().shutdown();
    }

    private void onReclaimMemoryClicked(Event toEvent)
    {
        System.gc();
    }

    @Override
    public void doSetup()
    {
        super.doSetup();

        
    }

    @Override
    public void applicationStateChanged(ApplicationState toNewState)
    {
        if (toNewState == ApplicationState.RUNNING())
        {
            // Check if we will be using the system tray
            if (this.getUsesSystemTray())
            {
                createSystemTray();

                // Add the default menu items to the system tray
                m_oMenu.addControl(new MenuItem(m_oMenu.getImplementationType(), "Close Application", Delegate.build(this, "onCloseClicked")));

                m_oMenu.addControl(new MenuItem(m_oMenu.getImplementationType(), "Reclaim Memory", Delegate.build(this, "onReclaimMemoryClicked")));
            }

            if (getShowUserInterface())
            {
                showUserInterface();
            }
            
        }
    }
    
    

    public void addMenuItem(Goliath.UI.Controls.Control toControl)
    {
        if (m_oMenu != null)
        {
            m_oMenu.addControl(toControl);
        }
    }

    /**
     * Determines if this application uses the system tray
     * @return true if it does use the system tray, false otherwise
     */
    @Override
    public final boolean getUsesSystemTray()
    {
        return onGetUsesSystemTray() && SystemTray.isSupported();
    }

    /**
     * Returns true if this application uses the system tray, false if not, this should be overridden by subclasses to change functionality
     * @return true to use the system tray, false otherwise
     */
    protected boolean onGetUsesSystemTray()
    {
        return true;
    }

    private void createSystemTray()
    {
        // TODO: System Tray should be dynamic not hard coded
        SystemTray tray = SystemTray.getSystemTray();
        Toolkit loToolkit = Toolkit.getDefaultToolkit();

        // TODO: Implement .ico reader
        Image loImage = loToolkit.getImage(Application.getInstance().getPropertyHandlerProperty("SystemTray.Icon", "./resources/icon.png"));

        // Create the popup menu
        // TODO: See if it is possible to create a system tray menu without using AWT
        m_oMenu = new Menu(ControlImplementationType.AWTSYSTEMTRAY());
        
        final TrayIcon m_oIcon = new TrayIcon(loImage, Application.getInstance().getName(), (PopupMenu)m_oMenu.getControl());

        m_oIcon.setToolTip(Application.getInstance().getPropertyHandlerProperty("SystemTray.Name", Application.getInstance().getName()));
        m_oIcon.setImageAutoSize(true);

        // TODO: This can be implemented better by using the mouse over event instead
        Application.getInstance().scheduleTask(new TimerTask() {

            @Override
            public void run()
            {
                // TODO: Optimise this method
                StringBuilder lcVersions = new StringBuilder();

                for (LibraryVersion loVersion :Application.getInstance().getObjectCache().getObjects(LibraryVersion.class, "getName"))
                {
                    lcVersions.append(loVersion.toString() + "\n");
                }
                Application.getInstance().getObjectCache().clearCache(LibraryVersion.class);

                if (m_cTrayName == null)
                {
                    m_cTrayName = Application.getInstance().getPropertyHandlerProperty("SystemTray.Name", Application.getInstance().getName());
                }
                StringBuilder lcToolTip = new StringBuilder(m_cTrayName);
                long lnHeap = Utilities.getCurrentHeapSize();
                long lnUsed = Utilities.getCurrentHeapSize() - Utilities.getFreeHeapSize();

                lcToolTip.append("\n (" + (lnUsed / 1024) + " kb / " + (lnHeap / 1024) + " kb)");
                lcToolTip.append("\n\n" + lcVersions.toString());

                m_oIcon.setToolTip(lcToolTip.toString());
            }
        }, 1000);

        try
        {
            tray.add(m_oIcon);
        }
        catch (AWTException e)
        {
            new Goliath.Exceptions.Exception(e);
        }
    }
    
}
