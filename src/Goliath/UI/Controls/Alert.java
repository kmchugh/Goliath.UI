/* ========================================================
 * Alert.java
 *
 * Author:      kenmchugh
 * Created:     Dec 14, 2010, 4:30:22 PM
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

package Goliath.UI.Controls;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.Controls.Layouts.BorderLayoutConstants;
import Goliath.UI.Controls.Layouts.BorderLayoutManager;
import Goliath.UI.Controls.Layouts.GridLayoutManager;
import Goliath.UI.Views.View;
import Goliath.UI.Windows.Window;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 14, 2010
 * @author      kenmchugh
**/
public class Alert<T> extends View
        implements IValueControl<T>
{
    private static String BUTTON_KEY = "BUTTON_KEY";

    public static String ACCEPT = "Accept";
    public static String CANCEL = "Cancel";



    public static void show(String tcTitle, String tcMessage)
    {
        Window loWindow = new Window(new Alert(tcMessage, tcTitle));
        Window.centerOnScreen(loWindow);
        loWindow.setVisible(true);
    }

    // Alert.prompt("Please give your fragment a title", "Please provide a fragment title", Textbox.class, Delegate.<Event<IButton>>build(this, "onFragmentTitleProvided"));

    public static <R, S extends IValueControl<R>> Alert<R> prompt(String tcTitle, String tcMessage, Class<S> toInputClass, IDelegate<Event<IButton>> toOkayAction)
    {
        List<String> toButtons = new List<String>(new String[]{ACCEPT, CANCEL});
        HashTable<String, IDelegate> loDelegates = new HashTable<String, IDelegate>();
        loDelegates.put(ACCEPT, toOkayAction);

        Window loWindow = new Window(
                new Alert<R>(tcMessage, tcTitle, loDelegates, toButtons, null, toInputClass));
        Window.centerOnScreen(loWindow);
        loWindow.setVisible(true);

        return (Alert<R>)loWindow.getView();
    }



    private String m_cMessage;
    private HashTable<String,IDelegate> m_oEventHandlers;
    private HashTable<String,String> m_oButtonText;
    private HashTable<String,IButton> m_oButtons;
    private Goliath.Graphics.Image m_oImage;
    private IValueControl<T> m_oEditor;


    /**
     * Creates a new instance of the alert view with the specified message
     * @param tcMessage the message to display
     */
    public Alert(String tcMessage)
    {
        this(tcMessage, Application.getInstance().getName(), (Goliath.Graphics.Image)null);
    }

    /**
     * Creates a new instance of the alert view
     * @param tcMessage the message to display
     * @param tcTitle the title of the alert
     */
    public Alert(String tcMessage, String tcTitle)
    {
        this(tcMessage, tcTitle, (Goliath.Graphics.Image)null);
    }

    /**
     * Creates a new instance of the alert view
     * @param tcMessage the message to display
     * @param tcTitle the title of the alert
     * @param toImage the image to show in the view
     */
    public Alert(String tcMessage, String tcTitle, Goliath.Graphics.Image toImage)
    {
        setTitle(tcTitle);
        m_cMessage = tcMessage;
        m_oImage = toImage;
        addButton(CANCEL.toLowerCase(), createButton(CANCEL));
        initialiseComponent();
    }

    /**
     * Creates a new instance of the alert view.  This also takes a mapping of event handlers,
     * when the button with the same key as an event handler is clicked, that event handler will
     * be exectued
     * @param tcMessage the message to display
     * @param tcTitle the title of the alert
     * @param toEventHandlers the event handlers to run when a button is clicked
     */
    public Alert(String tcMessage, String tcTitle, HashTable<String,IDelegate> toEventHandlers)
    {
        this(tcMessage, tcTitle, toEventHandlers, (IList<String>)null);
    }

    /**
     * Creates a new instance of the alert view.  This also takes a mapping of event handlers,
     * when the button with the same key as an event handler is clicked, that event handler will
     * be exectued.
     * The list of button keys allows customisation of the buttons that exist on the alert, this is an ordered
     * list of the buttons to create
     * @param tcMessage the message to display
     * @param tcTitle the title of the alert
     * @param toEventHandlers the event handlers to run when a button is clicked
     * @param toButtonKeys the list of button keys to create buttons for
     */
    public Alert(String tcMessage, String tcTitle, HashTable<String,IDelegate> toEventHandlers, IList<String> toButtonKeys)
    {
        this(tcMessage, tcTitle, toEventHandlers, toButtonKeys, (HashTable<String, String>)null);
    }

    /**
     * Creates a new instance of the alert view.  This also takes a mapping of event handlers,
     * when the button with the same key as an event handler is clicked, that event handler will
     * be exectued.
     * The list of button keys allows customisation of the buttons that exist on the alert, this is an ordered
     * list of the buttons to create
     * @param tcMessage the message to display
     * @param tcTitle the title of the alert
     * @param toEventHandlers the event handlers to run when a button is clicked
     * @param toButtonKeys the list of button keys to create buttons for
     * @param toButtonTextMap the mapping for the button text, keyed by the button key
     */
    public Alert(String tcMessage, String tcTitle, HashTable<String,IDelegate> toEventHandlers, IList<String>toButtonKeys, HashTable<String, String> toButtonTextMap)
    {
        this(tcMessage, tcTitle, toEventHandlers, toButtonKeys, toButtonTextMap, null);
    }

    /**
     * Creates a new instance of the alert view.  This also takes a mapping of event handlers,
     * when the button with the same key as an event handler is clicked, that event handler will
     * be exectued.
     * The list of button keys allows customisation of the buttons that exist on the alert, this is an ordered
     * list of the buttons to create
     * @param tcMessage the message to display
     * @param tcTitle the title of the alert
     * @param toEventHandlers the event handlers to run when a button is clicked
     * @param toButtonKeys the list of button keys to create buttons for
     * @param toButtonTextMap the mapping for the button text, keyed by the button key
     */
    public Alert(String tcMessage, String tcTitle, HashTable<String,IDelegate> toEventHandlers, IList<String>toButtonKeys, HashTable<String, String> toButtonTextMap, Class<? extends IValueControl<T>> toInputClass)
    {
        m_cMessage = tcMessage;
        if (toInputClass != null)
        {
            try
            {
                m_oEditor = toInputClass.newInstance();
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);
            }
        }
        setTitle(tcTitle);
        m_oEventHandlers = toEventHandlers;
        m_oButtonText = toButtonTextMap;

        if (toButtonKeys != null)
        {
            for (String lcKey : toButtonKeys)
            {
                addButton(lcKey.toLowerCase(), createButton(lcKey));
            }
        }
        else
        {
            // By default we will have an okay and cancel button
            addButton(ACCEPT.toLowerCase(), createButton(ACCEPT));
            addButton(CANCEL.toLowerCase(), createButton(CANCEL));
        }
        initialiseComponent();
    }

    /**
     * Initialises the component
     */
    private void initialiseComponent()
    {
        BorderLayoutManager loLayout = this.setLayoutManager(BorderLayoutManager.class);

        this.suspendLayout();

        // Create the top group for the image and message
        Group loGroup = new Group();
        loGroup.setLayoutManager(BorderLayoutManager.class);

        // Create the icon if needed
        if (m_oImage != null)
        {
            loGroup.addControl(new Image(m_oImage, m_cMessage),
                ControlLayoutProperty.LAYOUTCONSTRAINT(),
                BorderLayoutConstants.LINE_START());
        }

        // Create the message label
        loGroup.addControl(new Label(m_cMessage), ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.CENTER());

        this.addControl(loGroup, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.CENTER());

        // Create the button group
        Group loButtonGroup = new Group();
        //loGroup.setLayoutManager(new GridLayoutManager(1, m_oButtons.size()));
        loButtonGroup.setLayoutManager(GridLayoutManager.class);

        for (String lcKey : getButtons().keySet())
        {
            loButtonGroup.addControl(getButton(lcKey));
        }

        this.addControl(loButtonGroup, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.PAGE_END());
        
        this.resumeLayout();
    }

    @Override
    public T getValue()
    {
        return m_oEditor == null ? null : m_oEditor.getValue();
    }

    @Override
    public void setValue(T toValue)
    {
        if (m_oEditor != null)
        {
            m_oEditor.setValue(toValue);
        }
    }



    /**
     * Helper function to add a button to the button list
     * @param tcKey the key of the button
     * @param toButton the button
     */
    private void addButton(String tcKey, IButton toButton)
    {
        tcKey = tcKey.toLowerCase();
        if (m_oButtons == null)
        {
            m_oButtons = new HashTable<String, IButton>();
        }
        toButton.setProperty(BUTTON_KEY, tcKey);
        m_oButtons.put(tcKey, toButton);
    }

    /**
     * Gets the specified button from the list of buttons
     * @param tcKey the key for the button to get
     * @return returns the button, or null if it does not exist
     */
    private IButton getButton(String tcKey)
    {
        tcKey = tcKey.toLowerCase();
        IButton loReturn = null;
        if (m_oButtons != null && m_oButtons.containsKey(tcKey))
        {
            loReturn = m_oButtons.get(tcKey);
        }
        return loReturn;
    }

    private HashTable<String, IButton> getButtons()
    {
        if (m_oButtons == null)
        {
            m_oButtons = new HashTable<String, IButton>();
        }
        return m_oButtons;
    }

    /**
     * Creates a button for the specified key, or if that keyed button already exists,
     * returns the original button
     * @param tcKey the key to create the buton for
     * @return the button with the specified key
     */
    private IButton createButton(String tcKey)
    {
        IButton loButton = getButton(tcKey);
        if (loButton == null)
        {
            loButton = new Button(getTextForButton(tcKey), Delegate.build(this, "onButtonClick"));
        }
        return loButton;
    }

    /**
     * Gets the text for the specified button, if no text mapping exists, then the
     * key is used
     * @param tcKey the key of the button
     * @return the text to use for the button
     */
    private String getTextForButton(String tcKey)
    {
        String lcReturn = null;
        if (m_oButtonText != null)
        {
            for (String lcKey : m_oButtonText.keySet())
            {
                if (tcKey.equalsIgnoreCase(lcKey))
                {
                    lcReturn = lcKey;
                    break;
                }
            }
        }
        return Goliath.Utilities.isNull(lcReturn, tcKey);
    }

    /**
     * Gets the event handler for the specified button
     * @param tcKey the button
     * @return the event handler
     */
    private IDelegate getEventHandler(String tcKey)
    {
        tcKey = tcKey.toLowerCase();
        IDelegate loReturn = null;
        if (m_oEventHandlers != null && m_oEventHandlers.containsKey(tcKey))
        {
            loReturn = m_oEventHandlers.get(tcKey);
        }
        return loReturn;
    }

    /**
     * Adds the event handler for the button
     * @param tcKey the button
     * @param toEventHandler the event handler
     */
    private void addEventHandler(String tcKey, IDelegate toEventHandler)
    {
        tcKey = tcKey.toLowerCase();
        if (m_oEventHandlers == null)
        {
            m_oEventHandlers = new HashTable<String, IDelegate>();
        }
        m_oEventHandlers.put(tcKey, toEventHandler);
    }

    /**
     * Creates a default event handler for the button key
     * @param tcKey the key of the button
     * @return the event handler created for this button
     */
    private IDelegate createEventHandler(String tcKey)
    {
        IDelegate loHandler = Delegate.build(this, "on" + tcKey + "Clicked");
        addEventHandler(tcKey, loHandler);
        return loHandler;
    }


    /**
     * Handles when a button is clicked on the alert view.  Forwards events
     * to the event handler that was mapped, or if no event handler is mapped
     * then to the default handler for that button type
     * @param toEvent the event
     */
    private void onButtonClick(Event<IButton> toEvent)
    {
        String lcKey = toEvent.getTarget().getProperty(BUTTON_KEY);

        IDelegate loEventHandler = getEventHandler(lcKey);
        if (loEventHandler ==  null)
        {
            loEventHandler = createEventHandler(lcKey);
        }

        try
        {
            loEventHandler.invoke(toEvent);
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
    }

    public void close()
    {
        this.getParentWindow().close();
    }




    // TODO: The okay and cancel should be set up as events rather than delegates

    private void onAcceptClicked(Event<IButton> toButton)
    {
        // Close the window
        close();

        // TODO: Trace through to see if the windows are disposed when closed, they should be disposed and removed from memory
    }

    private void onCancelClicked(Event<IButton> toButton)
    {
        // Close the window
        close();

        // TODO: Trace through to see if the windows are disposed when closed, they should be disposed and removed from memory
    }
}
