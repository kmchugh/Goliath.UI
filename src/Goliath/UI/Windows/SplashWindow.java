/* ========================================================
 * SplashScreen.java
 *
 * Author:      kenmchugh
 * Created:     Nov 15, 2010, 12:18:39 PM
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

import Goliath.Graphics.Image;
import Goliath.UI.Controls.ControlImplementationType;
import Goliath.UI.Controls.Layouts.BasicLayoutManager;
import Goliath.UI.Controls.TextArea;
import Goliath.UI.Views.View;
import java.awt.Color;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Nov 15, 2010
 * @author      kenmchugh
**/
public class SplashWindow extends Window
{
    private View m_oSplashView;
    private TextArea m_oStatusLabel;

    /**
     * Creates a new instance of SplashScreen
     */
    public SplashWindow(Image toImage)
    {
        super(toImage);
        initialiseComponent();
    }
    
    /**
     * Creates a new instance of SplashScreen
     */
    public SplashWindow()
    {
        super();
        initialiseComponent();
    }

    public SplashWindow(Image toImage, ControlImplementationType toType)
    {
        super(toImage, toType);
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        suspendLayout();
        this.setSelectable(false);
        this.setShowChrome(false);

        createSplashView();
        resumeLayout();
    }

    private void createSplashView()
    {
        m_oSplashView = new View(getImplementationType());
        populateSplashView();
        this.addControl(m_oSplashView);
    }
    
    private void populateSplashView()
    {
        m_oSplashView.suspendLayout();
        m_oSplashView.setLayoutManager(BasicLayoutManager.class);

        m_oStatusLabel = new TextArea(getImplementationType());
        m_oStatusLabel.setRichText(false);
        //m_oStatusLabel.setOpaque(false);

        // TODO: The size and location of the status label should be defineable in the application settings
        m_oStatusLabel.setLocation(35, 120);
        m_oStatusLabel.setSize(430, 220);

        m_oSplashView.addControl(m_oStatusLabel);
        m_oSplashView.resumeLayout();
    }

    public TextArea getStatus()
    {
        return m_oStatusLabel;
    }

    public void setInfoText(String tcMessage, Color toColour)
    {
        setInfoText(tcMessage);
    }

    public void setInfoText(String tcMessage)
    {
        if (m_oStatusLabel != null)
        {
            m_oStatusLabel.setValue(m_oStatusLabel.getValue() + "\n" + tcMessage);
            m_oStatusLabel.update();
        }
    }
}
