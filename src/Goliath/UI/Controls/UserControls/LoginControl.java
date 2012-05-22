/* ========================================================
 * LoginControl.java
 *
 * Author:      kenmchugh
 * Created:     Jan 31, 2011, 11:52:58 AM
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

package Goliath.UI.Controls.UserControls;

import Goliath.Delegate;
import Goliath.Event;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IView;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.Session;
import Goliath.UI.Controls.Button;
import Goliath.UI.Controls.Checkbox;
import Goliath.UI.Controls.Label;
import Goliath.UI.Controls.Layouts.GridLayoutManager;
import Goliath.UI.Controls.PasswordBox;
import Goliath.UI.Controls.Textbox;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Views.View;
import Goliath.UI.Windows.Window;


        
/**
 * Control to allow sign out and sign in from the current session
 *
 * @see         Related Class
 * @version     1.0 Jan 31, 2011
 * @author      kenmchugh
**/
public class LoginControl extends Button
{
    private IWindow m_oLoginWindow;
    private IView m_oLoginView;
    /**
     * Creates a new instance of LoginControl
     */
    public LoginControl()
    {
        super();
        initialiseComponent();
    }


    private void initialiseComponent()
    {
        updateText();
        this.addEventListener(UIEventType.ONCLICK(), Delegate.build(this, "onClicked"));
    }
    
    private void onClicked(Event<IButton> toEvent)
    {
        if (Session.getCurrentSession().isAuthenticated())
        {
            // If we are authenticated, sign us out
        }
        else
        {
            // Not authenticated, so try to sign us in
            IWindow loWindow = getLoginWindow();
            loWindow.setLocation(toEvent.getTarget().getScreenCoordinates());
            loWindow.setVisible(true);
        }

        this.updateText();
    }

    private void onLoginWindowClosed(Event<IWindow> toEvent)
    {
        this.updateText();
    }
    
    private void updateText()
    {
        this.setText(Session.getCurrentSession().isAuthenticated() ? "Sign Out" : "Sign In");
    }
    
    private IWindow getLoginWindow()
    {
        if (m_oLoginWindow == null)
        {
            m_oLoginWindow = new Window(getLoginView());
            m_oLoginWindow.addEventListener(UIEventType.ONCLOSED(), Delegate.build(this, "onLoginWindowClosed"));
            m_oLoginWindow.addEventListener(UIEventType.ONDEACTIVATED(), Delegate.build(this, "onCancelLogin"));
            m_oLoginWindow.setShowChrome(false);
        }
        return m_oLoginWindow;
    }

    private IView getLoginView()
    {
        if (m_oLoginView == null)
        {
            m_oLoginView = new View();
            populateLoginView();
            m_oLoginView.setSize(300, 150);
            //m_oLoginView.setBorderSize(5, 5, 5, 5);
        }
        return m_oLoginView;
    }

    private void populateLoginView()
    {
        //m_oLoginView.setLayoutManager(new GridLayoutManager(4, 2));
        m_oLoginView.setLayoutManager(GridLayoutManager.class);
        m_oLoginView.addControl(new Label("User Name:"));
        m_oLoginView.addControl(new Textbox());
        m_oLoginView.addControl(new Label("Password:"));
        m_oLoginView.addControl(new PasswordBox());
        m_oLoginView.addControl(new Button("Cancel", Delegate.build(this, "onCancelLogin")));
        m_oLoginView.addControl(new Button("Sign In", Delegate.build(this, "onLogin")));
        m_oLoginView.addControl(new Checkbox("Remember Me"));
        m_oLoginView.addControl(new Button("Forgot my Password"));
    }
    
    private void onCancelLogin(Event<IControl> toEvent)
    {
        toEvent.getTarget().getParentWindow().close();
    }
    
    private void onLogin(Event<IControl> toEvent)
    {
        toEvent.getTarget().getParentWindow().close();
    }




}
