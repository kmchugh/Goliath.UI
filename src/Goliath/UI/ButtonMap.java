/* ========================================================
 * ButtonMap.java
 *
 * Author:      kenmchugh
 * Created:     Mar 18, 2011, 10:50:56 AM
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

package Goliath.UI;

import Goliath.UI.Constants.UIEventType;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Constants.EventType;
import Goliath.Graphics.Image;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IButton;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Mar 18, 2011
 * @author      kenmchugh
**/
public final class ButtonMap extends Goliath.Object
{
    private String m_cKey;
    private String m_cTitle;
    private List<String> m_oButtonClasses;
    private HashTable<EventType, IDelegate> m_oCallbacks;

    private Image m_oBackground;
    private Image m_oIcon;

    /**
     * Creates a new instance of ButtonMap
     */
    public ButtonMap(String tcKey, String tcTitle)
    {
        m_cKey = tcKey.toLowerCase();
        m_cTitle = tcTitle;
    }

    /**
     * Creates a new instance of ButtonMap
     */
    public ButtonMap(String tcKey, String tcTitle, List<String> toButtonClasses)
    {
        m_cKey = tcKey.toLowerCase();
        m_cTitle = tcTitle;
        m_oButtonClasses = toButtonClasses;
    }

    /**
     * Creates a new instance of ButtonMap
     */
    public ButtonMap(String tcKey, String tcTitle, IList<CallbackMap> toCallbacks)
    {
        this(tcKey, tcTitle);

        m_oCallbacks = new HashTable<EventType, IDelegate>(toCallbacks.size());
        for (CallbackMap loMap : toCallbacks)
        {
            m_oCallbacks.put(loMap.getEvent(), loMap.getCallback());
        }
    }

    /**
     * Creates a new instance of ButtonMap
     */
    public ButtonMap(String tcKey, String tcTitle, Image toBackground, Image toIcon)
    {
        this(tcKey, tcTitle);
        m_oBackground = toBackground;
        m_oIcon = toIcon;
    }

    /**
     * Creates a new instance of ButtonMap
     */
    public ButtonMap(String tcKey, String tcTitle, IList<CallbackMap> toCallbacks, Image toBackground, Image toIcon)
    {
        this(tcKey, tcTitle, toBackground, toIcon);

        m_oCallbacks = new HashTable<EventType, IDelegate>(toCallbacks.size());
        for (CallbackMap loMap : toCallbacks)
        {
            m_oCallbacks.put(loMap.getEvent(), loMap.getCallback());
        }
    }

    /**
     * Creates a new instance of ButtonMap
     */
    public ButtonMap(String tcKey, String tcTitle, IDelegate toClickCallback, Image toBackground, Image toIcon)
    {
        this(tcKey, tcTitle, toBackground, toIcon);

        m_oCallbacks = new HashTable<EventType, IDelegate>(1);
        m_oCallbacks.put(UIEventType.ONCLICK(), toClickCallback);
    }

    /**
     * Creates a new instance of ButtonMap
     */
    public ButtonMap(String tcKey, String tcTitle, IDelegate toClickCallback)
    {
        this(tcKey, tcTitle);

        m_oCallbacks = new HashTable<EventType, IDelegate>(1);
        m_oCallbacks.put(UIEventType.ONCLICK(), toClickCallback);
    }

    /**
     * Creates a new instance of ButtonMap
     */
    public ButtonMap(String tcKey, String tcTitle, List<String> toButtonClasses, IDelegate toClickCallback)
    {
        this(tcKey, tcTitle, toButtonClasses);

        m_oCallbacks = new HashTable<EventType, IDelegate>(1);
        m_oCallbacks.put(UIEventType.ONCLICK(), toClickCallback);
    }

    public String getKey()
    {
        return m_cKey;
    }

    public String getTitle()
    {
        return m_cTitle;
    }

    public Image getBackgroundImage()
    {
        return m_oBackground;
    }

    public Image getIcon()
    {
        return m_oIcon;
    }

    public List<String> getButtonClasses()
    {
        return m_oButtonClasses == null ? new List<String>(0) : m_oButtonClasses;
    }

    public List<EventType> getEventTypes()
    {
        return (m_oCallbacks == null) ?
            new List<EventType>(0) :
            new List<EventType>(m_oCallbacks.keySet());
    }

    public IDelegate getCallbackFor(EventType toEvent)
    {
        return m_oCallbacks != null ? m_oCallbacks.get(toEvent) : null;
    }
}
