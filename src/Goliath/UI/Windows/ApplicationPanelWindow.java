/* ========================================================
 * ApplicationPanelWindow.java
 *
 * Author:      kmchugh
 * Created:     Dec 8, 2010, 3:13:06 PM
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

package Goliath.UI.Windows;

import Goliath.UI.Controls.UserControls.TabbedControl;
import Goliath.UI.Controls.UserControls.ApplicationMenuTabbedControl;
import Goliath.UI.Views.View;


        
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
public class ApplicationPanelWindow extends ApplicationWindow
{
    /**
     * Creates a new instance of ApplicationWindow
     */
    public ApplicationPanelWindow()
    {
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        // Pause the layout engine while we are setting up
        this.suspendLayout();

        // Create the accordion for the window
        TabbedControl loTabControl = new ApplicationMenuTabbedControl();
        configureTabbedControl(loTabControl);

        // Create a view as the base of the window
        View loWindowView = new View();

        loWindowView.addControl(loTabControl);
        this.addControl(loWindowView);

        // Restart the layout engine
        this.resumeLayout();
    }

    /**
     * Hook method to allow configuration of the tabbed control that is added to the application window
     * @param toTabbedControl the tabbed control to allow configuration of
     */
    protected void configureTabbedControl(TabbedControl toTabbedControl)
    {

    }
}
