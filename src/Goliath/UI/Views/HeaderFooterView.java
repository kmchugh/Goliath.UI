/* =========================================================
 * HeaderFooterView.java
 *
 * Author:      kenmchugh
 * Created:     Oct 29, 2010, 2:18:58 PM
 *
 * Description
 * --------------------------------------------------------
 * Base view for any views that contain a header and a footer
 * All container methods will redirect to the contentArea control
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.UI.Views;

import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Constants.EventType;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Graphics.Constants.Alignment;
import Goliath.Graphics.Constants.Orientation;
import Goliath.Graphics.Dimension;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.ButtonMap;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.Controls.Button;
import Goliath.UI.Controls.Group;
import Goliath.UI.Controls.Image;
import Goliath.UI.Controls.Label;
import Goliath.UI.Controls.Layouts.BorderLayoutConstants;
import Goliath.UI.Controls.Layouts.BorderLayoutManager;
import Goliath.UI.Controls.Layouts.FlowLayoutManager;
import Goliath.UI.Constants.UIEventType;

/**
 * Base view for any views that contain a header and a footer
 * All container methods will redirect to the contentArea control

 * @author kenmchugh
 */
public abstract class HeaderFooterView extends View
{
    private static UIEventType g_oOKAY;
    public static UIEventType OKAY()
    {
        if (g_oOKAY == null)
        {
            g_oOKAY = new UIEventType("OKAY");
        }
        return g_oOKAY;
    }

    private static UIEventType g_oCancelledEvent;
    public static UIEventType CANCELLED()
    {
        if (g_oCancelledEvent == null)
        {
            g_oCancelledEvent = new UIEventType("CANCELLED");
        }
        return g_oCancelledEvent;
    }


    public static String CMDOKAYKEY = "CMDOKAYKEY";
    public static String CMDCANCELKEY = "CMDCANCELKEY";

    
    private IContainer m_oHeaderArea;
    private IContainer m_oContentArea;
    private IContainer m_oFooterArea;
    private List<ButtonMap> m_oButtonMap;
    private HashTable<String, IButton> m_oButtons;
    private Image m_oImage;
    private Label m_oTitleLabel;



    /**
     * Creates a new instance of VisualisationBase
     */
    public HeaderFooterView(boolean tlCreate)
    {
        initialiseComponent(tlCreate);
    }

    /**
     * Creates a new instance of VisualisationBase
     */
    public HeaderFooterView()
    {
        this(true);
    }

    /**
     * Initialises and builds the components contents
     */
    private void initialiseComponent(boolean tlCreate)
    {
        setLayoutManager(BorderLayoutManager.class);

        if (tlCreate)
        {
            createContent();
        }
        
    }
    
    /**
     * Template method for creating the content
     */
    protected final void createContent()
    {
        // Force creation of all the areas
        getHeaderContainer();
        getContentContainer();
        getFooterContainer();
    }

    /**
     * Returns the header container, if it does not already exist, then it will be created here.
     * @return the header container
     */
    public final IContainer getHeaderContainer()
    {
        if (m_oHeaderArea == null)
        {
            m_oHeaderArea = createHeaderContainer();
            if (m_oHeaderArea != null)
            {
                super.addControl(m_oHeaderArea, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.PAGE_START());
            }
        }
        return m_oHeaderArea;
    }



    /**
     * Returns the content container, if it does not already exist, then it will be created here.
     * @return the content container
     */
    public final IContainer getContentContainer()
    {
        if (m_oContentArea == null)
        {
            m_oContentArea = createContentContainer();
            if (m_oContentArea != null)
            {
                super.addControl(m_oContentArea, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.CENTER());
            }
        }
        return m_oContentArea;
    }

    /**
     * Returns the footer container, if it does not already exist, then it will be created here.
     * @return the footer container
     */
    public final IContainer getFooterContainer()
    {
        if (m_oFooterArea == null)
        {
            m_oFooterArea = createFooterContainer();
            if (m_oFooterArea != null)
            {
                super.addControl(m_oFooterArea, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.PAGE_END());
            }
        }
        return m_oFooterArea;
    }

    /**
     * Creates the header area
     * @return the header area
     */
    private IContainer createHeaderContainer()
    {
        IContainer loReturn = new Group(BorderLayoutManager.class);

        m_oTitleLabel = new Label(this.getTitle());
        m_oTitleLabel.setFontSize(20);
        m_oTitleLabel.addClass("HeaderFooterViewTitle");
        loReturn.addControl(m_oTitleLabel, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.CENTER());
        return loReturn;
    }

    /**
     * Creates the content area
     * @return the content area
     */
    private IContainer createContentContainer()
    {
        IContainer loReturn = onCreateContentContainer();
        populateContent(loReturn);
        return loReturn;
    }

    /**
     * Creates the footer area
     * @return the footer area
     */
    private IContainer createFooterContainer()
    {
        int lnMinPositions = 4;
        // Get the buttons that will be created
        List<ButtonMap> loMap = getButtonMap();

        //IContainer loReturn = new Group(new FlowLayoutManager(Orientation.HORIZONTAL(), Alignment.RIGHT()));//new Group(new GridLayoutManager(1, Math.max(loMap.size(), lnMinPositions)));
        IContainer loReturn = new Group(FlowLayoutManager.class);

        for (int i=0, lnDiff = lnMinPositions - getButtonMap().size(); i<lnDiff; i++)
        {
            // Placeholder controls to ensure positioning
            loReturn.addControl(new Label());
        }

        m_oButtons = new HashTable<String, IButton>();
        for (ButtonMap loButton : loMap)
        {
            IButton loCommand = new Button(loButton.getTitle());
            
            loCommand.setName(loButton.getKey());
            Goliath.Graphics.Image loImage = loButton.getBackgroundImage();
            if (loImage != null)
            {
                loCommand.setBackground(loImage);
            }
            loImage = loButton.getIcon();
            if (loImage != null)
            {
                loCommand.setImage(loImage);
            }
            for (EventType loEvent : loButton.getEventTypes())
            {
                loCommand.addEventListener(loEvent, loButton.getCallbackFor(loEvent));
            }
            for (String lcClass : loButton.getButtonClasses())
            {
                loCommand.addClass(lcClass);
            }
            loCommand.setSize(50, 50);
            m_oButtons.put(loButton.getKey().toLowerCase(), loCommand);
            loReturn.addControl(loCommand);
        }
        return loReturn;
    }

    /**
     * Gets the list of all the buttons that are on this view
     * @return the list of all the button keys in the view
     */
    public final List<String> getButtonKeys()
    {
        return m_oButtons == null ? new List<String>(0) : new List<String>(m_oButtons.keySet());
    }

    /**
     * Gets the button for the specified key, returns null if the button does not exist
     * @param tcButtonKey the key to get the button for
     * @return the button
     */
    public final IButton getButtonFor(String tcButtonKey)
    {
        return m_oButtons != null ? m_oButtons.get(tcButtonKey.toLowerCase()) : null;
    }


    /**
     * Gets the existing button map, or creates one if it does not yet exist
     * @return the button map
     */
    private List<ButtonMap> getButtonMap()
    {
        if (m_oButtonMap == null)
        {
            m_oButtonMap = createButtonMap();
        }
        return m_oButtonMap;
    }
    
    protected List<ButtonMap> createButtonMap()
    {
        List<ButtonMap> loMap = new List<ButtonMap>();
        loMap.add(new ButtonMap(CMDOKAYKEY, "OK", new List<String>(new String[]{"OkayButton"}), Delegate.build(this, "onOkayClicked")));
        loMap.add(new ButtonMap(CMDCANCELKEY, "Close", new List<String>(new String[]{"CancelButton"}), Delegate.build(this, "onCancelClicked")));
        return loMap;
    }



    /**
     * Event occurs when the okay button is clicked
     * @param toEvent the event
     */
    protected void onOkayClicked(Event<IControl> toEvent)
    {
        this.getParentWindow().close();
        this.fireEvent(OKAY(), new Event<HeaderFooterView>(this));
    }

    /**
     * Event occurs when the cancel button is clicked
     * @param toEvent the event
     */
    protected void onCancelClicked(Event<IControl> toEvent)
    {
        this.getParentWindow().close();
        this.fireEvent(CANCELLED(), new Event<HeaderFooterView>(this));
    }

    /**
     * Adds the contents to the content area
     * @param toContainer the content container
     */
    private void populateContent(IContainer toContainer)
    {
        onPopulateContent(toContainer);
    }

    /**
     * Hook method for the subclass to add contents to the content
     * @param toContainer the content container
     */
    protected void onPopulateContent(IContainer toContainer)
    {
    }

    /**
     * Sets the title of the View
     * @param tcTitle the title of the view
     */
    @Override
    public final void setTitle(String tcTitle)
    {
        if (m_oTitleLabel != null)
        {
            m_oTitleLabel.setText(tcTitle);
        }
        super.setTitle(tcTitle);
    }


    /**
     * Method should be overridden in sub classes in order to populate the content area
     * @return the newly created content area
     */
    protected IContainer onCreateContentContainer()
    {
        return new Group(BorderLayoutManager.class);
    }

    /**
     * Sets the image that is displayed in the header view
     * @param toImage the image to display
     */
    public final void setImage(Goliath.Graphics.Image toImage)
    {
        if (toImage == null)
        {
            removeImageArea();
        }
        else
        {
            toImage.setScaleable(true);
            toImage.setSize(new Dimension(64, 64));

            if (m_oImage == null)
            {
                createImageArea(toImage);
            }
            m_oImage.setSource(toImage);
        }
    }

    /**
     * Removes the area that was created for the image
     */
    private void removeImageArea()
    {
        if (m_oImage != null)
        {

            getHeaderContainer().removeControl(m_oImage);
            m_oImage = null;
            invalidate();
        }
    }

    /**
     * Creates the area where the image will be displayed
     * @param toImage the image to display
     */
    private void createImageArea(Goliath.Graphics.Image toImage)
    {
        m_oImage = new Image(toImage, getTitle());
        m_oImage.addClass("HeaderFooterViewLogo");
        IContainer loContainer = getHeaderContainer();
        loContainer.addControl(m_oImage, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.LINE_START());
    }

}
