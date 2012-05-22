/* ========================================================
 * PanelControl.java
 *
 * Author:      christinedorothy
 * Created:     Jun 10, 2011, 3:31:55 PM
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

import Goliath.UI.Controls.ToggleButton;
import Goliath.Delegate;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Controls.Group;
import Goliath.UI.Controls.Layouts.BorderLayoutManager;
import Goliath.UI.Controls.Layouts.GridLayoutManager;
import Goliath.UI.Controls.Layouts.PanelLayoutManager;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jun 10, 2011
 * @author      christinedorothy
**/
public class PanelControl extends Group
{
    private IContainer m_oHeader;
    private IContainer m_oContent;
    private boolean m_lHeaderVisible;

    /**
     * Creates a new instance of PanelControl
     */
    public PanelControl()
    {
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        setLayoutManager(PanelLayoutManager.class);
        setHeaderVisibility(true);
        setContent(new Group());
    }

    public void setHeader(String tcTitle)
    {
        IContainer loHeader = createHeaderControl(tcTitle);
        m_oHeader = loHeader;
        if (isHeaderVisible())
        {
            addControl(m_oHeader);
        }
    }

    public void setContent(IContainer tcPanelContent)
    {
        m_oContent = tcPanelContent;
        addControl(m_oContent);
    }

    public void addControlToContent(IControl toControl)
    {
        if (m_oContent == null)
        {
            Group loContentPanel = new Group();
            setContent(loContentPanel);

            loContentPanel.setLayoutManager(GridLayoutManager.class);

            loContentPanel.addControl(toControl);
        }
        else
        {
            m_oContent.addControl(toControl);
        }
    }

    /**
     * Creates the default panel header area
     * @param tcKey the key of the panel
     * @param tcTitle the title of the panel
     * @param toImage the image to use for the header area
     * @return the default header that was created
     */
    private IContainer createHeaderControl(String tcTitle)
    {
        Group loReturn = new Group();
        loReturn.setLayoutManager(BorderLayoutManager.class);
        loReturn.addEventListener(UIEventType.ONCLICK(), Delegate.build(this, "onPanelHeaderClicked"));

        ToggleButton loButton = new ToggleButton(tcTitle);
        //loButton.setBorderSize(0, 0, 0, 5);
        loReturn.addControl(loButton);

        return loReturn;
    }

    private void onPanelHeaderClicked()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public IContainer getHeader()
    {
        return m_oHeader;
    }

    public IContainer getContent()
    {
        return m_oContent;
    }

    public boolean isHeaderVisible()
    {
        return m_lHeaderVisible;
    }

    public void setHeaderVisibility(boolean tlVisibility)
    {
        m_lHeaderVisible = tlVisibility;
        ((PanelLayoutManager) getLayoutManager()).setHeaderVisible(tlVisibility);
        if (!isHeaderVisible())
        {
            removeControl(m_oHeader);
        }
    }


}
