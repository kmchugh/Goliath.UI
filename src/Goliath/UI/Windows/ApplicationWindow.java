/* =========================================================
 * ApplicationWindow.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * The Application Window is the top level window that the
 * user can interact with.  There should only be one application
 * window for each application.  When the application window closes
 * the application will also shut down
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/


package Goliath.UI.Windows;

import Goliath.Applications.Application;
import Goliath.Delegate;
import Goliath.Interfaces.UI.Controls.IApplicationWindow;
import Goliath.UI.Controls.Screen;

/**
 *
 * @author kmchugh
 */
public class ApplicationWindow extends Window
        implements IApplicationWindow
{

    /**
     * Creates an instance of the application window
     */
    public ApplicationWindow()
    {
        initialiseComponent();
    }

    /**
     * Initialises the application window
     */
    private void initialiseComponent()
    {
        // Set the title and size of this application window
        this.setTitle(Application.getInstance().getName());
        
        // Set the maximum size to the same size as the screen
        setMaxSize(new Screen().getSize());
        this.addEventListener(Goliath.UI.Constants.UIEventType.ONCLOSED(), Delegate.build(this, "onWindowClosed"));
    }

    /**
     * Shuts down the application when the window closes
     * @param toEvent the event object
     */
    private void onWindowClosed(Object toEvent)
    {
        Application.getInstance().shutdown();
    }
}
