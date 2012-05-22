/* ========================================================
 * AuthenticationView.java
 *
 * Author:      kenmchugh
 * Created:     Apr 24, 2011, 6:07:18 PM
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

package Goliath.UI.Views;

import Goliath.Applications.Application;
import Goliath.Applications.ApplicationController;
import Goliath.Collections.List;
import Goliath.Constants.SessionEventType;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.ISession;
import Goliath.Interfaces.Security.ISecurityManager;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Security.User;
import Goliath.Session;
import Goliath.UI.ButtonMap;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.Controls.Group;
import Goliath.UI.Controls.Label;
import Goliath.UI.Controls.Layouts.BasicLayoutManager;
import Goliath.UI.Controls.Layouts.BorderLayoutConstants;
import Goliath.UI.Controls.Layouts.BorderLayoutManager;
import Goliath.UI.Controls.Layouts.GridLayoutManager;
import Goliath.UI.Controls.PasswordBox;
import Goliath.UI.Controls.Textbox;
import javax.swing.JOptionPane;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Apr 24, 2011
 * @author      kenmchugh
**/
public class AuthenticationView extends HeaderFooterView
{
    public static String CMDSIGNINKEY = "CMDSIGNINKEY";


    private Group m_oLoginView;
    private Group m_oLogoutView;
    private Textbox m_oUserName;
    private PasswordBox m_oPassword;
    private Label m_oLblMessages;

    private IDelegate<Event<ISession>> m_oUAHandler;
    private IDelegate<Event<ISession>> m_oAHandler;



    /**
     * Creates a new instance of AuthenticationView
     */
    public AuthenticationView()
    {
        super(true);
        updateView();
    }

    @Override
    protected List<ButtonMap> createButtonMap()
    {
        List<ButtonMap> loMap = new List<ButtonMap>();
        loMap.add(new ButtonMap(CMDSIGNINKEY, "Sign in", new List<String>(new String[]{"SigninButton"}), Delegate.build(this, "onOkayClicked")));
        loMap.add(new ButtonMap(CMDCANCELKEY, "Cancel", new List<String>(new String[]{"CancelButton"}), Delegate.build(this, "onCancelClicked")));
        return loMap;
    }

    public void setMessages(String tcMessage)
    {
        if (m_oLblMessages == null)
        {
            m_oLblMessages = new Label();
        }
        m_oLblMessages.setText(tcMessage);
        m_oLblMessages.getParent().invalidate();
        m_oLblMessages.getParent().update();
    }


    protected synchronized void updateView()
    {
        ISession loSession = Session.getCurrentSession();

        IContainer loContent = getContentContainer();

        loContent.clearChildren();

        Group loGroup = new Group();
        loGroup.setLayoutManager(BorderLayoutManager.class);

        if (m_oLblMessages == null)
        {
            m_oLblMessages = new Label();
        }
        loGroup.addControl(m_oLblMessages, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.PAGE_END());
        loGroup.addControl(loSession.isAuthenticated() ? getLogoutView() :  getLoginView(), ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.CENTER());

        loContent.addControl(loGroup);

        if (loSession.isAuthenticated())
        {
            this.setTitle("Sign out");
            loSession.addEventListener(SessionEventType.ONUNAUTHENTICATED(), getUnauthenticatedHandler());
            loSession.removeEventListener(SessionEventType.ONAUTHENTICATED(), getAuthenticatedHandler());
            IButton loButton = this.getButtonFor(CMDSIGNINKEY);
            loButton.setText("");
            loButton.setTooltip("Sign out");
        }
        else
        {
            this.setTitle("Sign in");
            loSession.addEventListener(SessionEventType.ONAUTHENTICATED(), getAuthenticatedHandler());
            loSession.removeEventListener(SessionEventType.ONUNAUTHENTICATED(), getUnauthenticatedHandler());

            IButton loButton = this.getButtonFor(CMDSIGNINKEY);
            loButton.setText("");
            loButton.setTooltip("Sign in");
        }
    }

    protected Group getLogoutView()
    {
        if (m_oLogoutView == null)
        {
            m_oLogoutView = createLogoutView();
        }
        return m_oLogoutView;
    }

    protected final Group getLoginView()
    {
        if (m_oLoginView == null)
        {
            m_oLoginView = createLoginView();
        }
        return m_oLoginView;
    }

    protected Group createLoginView()
    {
        Group loGroup = new Group();
        //loGroup.setLayoutManager(new GridLayoutManager(2, 2));
        loGroup.setLayoutManager(GridLayoutManager.class);
        loGroup.addControl(new Label("User Name:"));
        loGroup.addControl(getTxtUserName());
        loGroup.addControl(new Label("Password:"));
        loGroup.addControl(getTxtPassword());
        return loGroup;
    }

    protected Group createLogoutView()
    {
        Group loGroup = new Group();
        loGroup.setLayoutManager(BasicLayoutManager.class);
        loGroup.addControl(new Label("You are currently logged. Would you like to logout?"));
        return loGroup;
    }

    @Override
    protected void onCancelClicked(Event<IControl> toEvent)
    {
        super.onCancelClicked(toEvent);
    }

    @Override
    protected void onOkayClicked(Event<IControl> toEvent)
    {
        boolean llHandled = false;
        ISession loSession = Session.getCurrentSession();
        if (loSession.isAuthenticated())
        {
            llHandled = ApplicationController.getInstance().unauthenticate();
            if (llHandled)
            {
                super.onOkayClicked(toEvent);
                JOptionPane.showMessageDialog(null, "You have successfully logout. Thank you.", "Goodbye", JOptionPane.OK_OPTION);
            }
            else
            {
                setMessages("Failed to logout.");
            }
        }
        else
        {
            String lcUserName = m_oUserName.getValue();
            String lcPassword = m_oPassword.getValue();
            if (!Goliath.Utilities.isNullOrEmpty(lcUserName) && !Goliath.Utilities.isNullOrEmpty(lcPassword))
            {
                llHandled = ApplicationController.getInstance().authenticate(lcUserName, lcPassword, Delegate.build(this, "onAuthenticationFailed"), Delegate.build(this, "onAuthenticationSuccess"));
                if (llHandled)
                {
                    super.onOkayClicked(toEvent);
                    ISecurityManager loManager = Application.getInstance().getSecurityManager();
                    User loUser = loManager.getUser(getTxtUserName().getValue());
                    String lcDisplayName = loManager.getDisplayName(loUser);
                    JOptionPane.showMessageDialog(null, "Login is successful. Welcome " + lcDisplayName + "!", "Welcome", JOptionPane.OK_OPTION);
                }
                else
                {
                    setMessages("Failed to login.");
                }
            }
            else
            {
                setMessages("Please enter a user name and a password");
            }
        }
        
    }


    protected Textbox getTxtUserName()
    {
        if (m_oUserName == null)
        {
            m_oUserName = new Textbox();
        }
        return m_oUserName;
    }

    protected PasswordBox getTxtPassword()
    {
        if (m_oPassword == null)
        {
            m_oPassword = new PasswordBox();
        }
        return m_oPassword;
    }


    protected IDelegate<Event<ISession>> getUnauthenticatedHandler()
    {
        if (m_oUAHandler == null)
        {
            m_oUAHandler = Delegate.build(this, "onSessionUnathenticated");
        }
        return m_oUAHandler;
    }


    protected IDelegate<Event<ISession>> getAuthenticatedHandler()
    {
        if (m_oAHandler == null)
        {
            m_oAHandler = Delegate.build(this, "onSessionAuthenticated");
        }
        return m_oAHandler;
    }


    protected void onSessionUnathenticated(Event<ISession> toEvent)
    {
        //Because of the toggle between login and logout,
        //updateView() will show logout screen
        //after the user has successfully login.

        //updateView();
    }

    protected void onSessionAuthenticated(Event<ISession> toEvent)
    {
        //Because of the toggle between login and logout,
        //updateView() will show login screen
        //after the user has successfully logout.

        //updateView();
    }
}