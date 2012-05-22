/* ========================================================
 * ChromeView.java
 *
 * Author:      kenmchugh
 * Created:     Jan 12, 2011, 2:35:14 PM
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

import Goliath.UI.Controls.Layouts.WindowChromeLayoutManager;
import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Delegate;
import Goliath.DynamicCode.Java;
import Goliath.Event;
import Goliath.Exceptions.FileNotFoundException;
import Goliath.Graphics.Image;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.UI.Controls.IWindow;
import Goliath.UI.ControlPropertySet;
import Goliath.UI.Controls.Button;
import Goliath.UI.Controls.Label;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Controls.BorderSettings;
import Goliath.UI.Controls.BorderType;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jan 12, 2011
 * @author      kenmchugh
**/
public class ChromeView extends View
{
    private Button m_oClose;
    private Button m_oMinimise;
    private Button m_oMaximise;
    private View m_oView;

    private Label m_oTitle;

    private List<IControl> m_oControls;
    private List<IControl> m_oControlBoxControls;

    /**
     * Creates a new instance of ChromeView
     */
    public ChromeView()
    {
        initialiseComponent();
    }
    
    private void initialiseComponent()
    {
        this.setLayoutManager(WindowChromeLayoutManager.class);
        this.setBorder(new BorderSettings(BorderType.EMPTY()));
        
        // TODO: These defaults should not be the hexi images
        m_oClose = addButton("Close", "./resources/images/icons/ChromeClose.png", Delegate.build(this, "onCloseClicked"));
        m_oMinimise = addButton("Minimise", "./resources/images/icons/ChromeMinimise.png", Delegate.build(this, "onMinimiseClicked"));
        m_oMaximise = addButton("Maximise", "./resources/images/icons/ChromeMaximise.png", Delegate.build(this, "onMaximiseClicked"));
        m_oTitle = new Label();
        addControl(m_oTitle);
        m_oClose.setBackground((Image)null);
        m_oMinimise.setBackground((Image)null);
        m_oMaximise.setBackground((Image)null);

        // Add the control box class so these controls can be picked up easily
        m_oClose.addClass("controlBox");
        m_oMinimise.addClass("controlBox");
        m_oMaximise.addClass("controlBox");
        m_oTitle.addClass("controlBox");
        
        addControlBoxControl(m_oClose);
        addControlBoxControl(m_oMinimise);
        addControlBoxControl(m_oMaximise);
    }

    public void addTitleControl(IControl toControl)
    {
        getTitleControls();
        if (!m_oControls.contains(toControl))
        {
            m_oControls.add(toControl);
            addControl(toControl);
        }
    }
    
    public void addControlBoxControl(IControl toControl)
    {
        getControlBoxControls();
        if (!m_oControlBoxControls.contains(toControl))
        {
            m_oControlBoxControls.add(toControl);
            if (!getChildren().contains(toControl))
            {
                addControl(toControl);
            }
        }
    }
    
    public List<IControl> getTitleControls()
    {
        if (m_oControls == null)
        {
            m_oControls = new List<IControl>();
        }
        return m_oControls;
    }
    
    public List<IControl> getControlBoxControls()
    {
        if (m_oControlBoxControls == null)
        {
            m_oControlBoxControls = new List<IControl>();
        }
        return m_oControlBoxControls;
    }

    @Override
    public void clearChildren()
    {
        super.clearChildren();
        addControl(m_oTitle);

        if (m_oControls != null)
        {
            for (IControl loControl : m_oControls)
            {
                addControl(loControl);
            }
        }
    }

    @Override
    public boolean addControl(int tnIndex, IControl toControl, ControlPropertySet toLayoutParameters)
    {
        boolean llReturn = super.addControl(tnIndex, toControl, toLayoutParameters);
        if (llReturn && Java.isEqualOrAssignable(View.class, toControl.getClass()))
        {
            m_oView = (View)toControl;
        }
        this.invalidate();
        return llReturn;
    }

    @Override
    public void setTitle(String tcTitle)
    {
        if (m_oTitle != null)
        {
            m_oTitle.setText(tcTitle);
        }
    }



    public Button getCloseButton()
    {
        return m_oClose;
    }
    public Button getMinimiseButton()
    {
        return m_oMinimise;
    }
    public Button getMaximiseButton()
    {
        return m_oMaximise;
    }

    public Label getTitleLabel()
    {
        return m_oTitle;
    }

    public void setTitleLabelBackground(Image toImage)
    {
        if (m_oTitle != null)
        {
            m_oTitle.setBackground(toImage);
            //m_oTitle.setOpaque(false);
        }
    }

    public View getView()
    {
        return m_oView;
    }

    private Button addButton(String tcTooltip, String tcImage, IDelegate toDelegate)
    {
        Image loImage = null;
        try
        {
            loImage = new Image(tcImage);
            loImage.setScaleable(true);
        }
        catch(FileNotFoundException ex)
        {
            Application.getInstance().log(ex);
        }

        Button loButton = new Button(loImage == null ? tcTooltip : "", loImage);
        if (toDelegate != null)
        {
            loButton.addEventListener(UIEventType.ONCLICK(), toDelegate);
        }
        loButton.setTooltip(tcTooltip);

        //loButton.setOpaque(false);

        this.addControl(loButton);

        return loButton;
    }

    private void onCloseClicked(Event<IButton> toEvent)
    {
        // TODO: Add in validation on the close method
        toEvent.getTarget().getParentWindow().close();
    }

    private void onMinimiseClicked(Event<IButton> toEvent)
    {
        // TODO: Add in validation on the close method
        toEvent.getTarget().getParentWindow().minimise();
    }

    private void onMaximiseClicked(Event<IButton> toEvent)
    {
        IWindow loWindow = toEvent.getTarget().getParentWindow();

        if (!loWindow.isMaximised())
        {
            // TODO: Add in validation on the close method
            loWindow.maximise();
        }
        else
        {
            loWindow.restore();
        }
    }


}
