/* ========================================================
 * UIEvent.java
 *
 * Author:      christinedorothy
 * Created:     Jun 15, 2011, 12:18:32 PM
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

import Goliath.Constants.EventType;
import Goliath.Date;
import Goliath.Event;
import Goliath.Graphics.Point;
import Goliath.Interfaces.UI.Controls.IControl;


        
/**
 * This is an immutable event object
 *
 * @see         Related Class
 * @version     1.0 Jun 15, 2011
 * @author      christinedorothy
**/
public class UIEvent extends Event<IControl>
{
    private Point m_oScreenLocation;
    private Point m_oPosition;
    private Date m_oTime;
    private int m_nClicks;
    private boolean m_lAlt;
    private boolean m_lAltGraph;
    private boolean m_lControl;
    private boolean m_lMeta;
    private boolean m_lShift;
    private boolean m_lPopup;
    private boolean m_lButton1;
    private boolean m_lButton2;
    private boolean m_lButton3;
    private boolean m_lButton1Down;
    private boolean m_lButton2Down;
    private boolean m_lButton3Down;
    private int m_nWheelClicks;
    private IControl m_oComponent;
    
    
    /**
     * Creates a new instance of UIEvent
     */
    public UIEvent(IControl toTarget)
    {
        super(toTarget);
        m_oComponent = toTarget;
    }

    /**
     * Creates a new instance of UIEvent
     */
    public UIEvent(IControl toTarget, 
                   Point toScreenLocation,
                   Point toLocation,
                   long tnTime,
                   int tnClicks,
                   boolean tlAlt,
                   boolean tlAltGraph,
                   boolean tlControl,
                   boolean tlMeta,
                   boolean tlShift,
                   boolean tlPopup,
                   boolean tlButton1,
                   boolean tlButton2,
                   boolean tlButton3,
                   boolean tlButton1Down,
                   boolean tlButton2Down,
                   boolean tlButton3Down,
                   int tlWheelRotationClicks)
    {
        super(toTarget);
        m_oScreenLocation = toScreenLocation;
        m_oPosition = toLocation;
        m_oTime = new Date(tnTime);
        m_nClicks = tnClicks;
        m_lAlt = tlAlt;
        m_lAltGraph = tlAltGraph;
        m_lControl = tlControl;
        m_lMeta = tlMeta;
        m_lShift = tlShift;
        m_lPopup = tlPopup;
        m_lButton1 = tlButton1;
        m_lButton2 = tlButton2;
        m_lButton3 = tlButton3;
        m_lButton1Down = tlButton1Down;
        m_lButton2Down = tlButton2Down;
        m_lButton3Down = tlButton3Down;
        m_nWheelClicks = tlWheelRotationClicks;
        m_oComponent = toTarget;
    }
    
    /**
     * Gets the number of clicks the mouse wheel has been rotated.  negative units for rotation away
     * from the user, positive for towards the user
     * @return the number of clicks the wheel has been rotated
     */
    public int getWheelRotationClicks()
    {
        return m_nWheelClicks;
    }
    
    /**
     * Gets the location of this event in screen coordinates
     * @return the event location in screen coordinates
     */
    public Point getScreenLocation()
    {
        return m_oScreenLocation;
    }
    
    /**
     * Gets the location of this event relative to the control the event occured in
     * @return the control the event occurred in
     */
    public Point getLocation()
    {
        return m_oPosition;
    }
    
    /**
     * Gets the mouse position of the event relative to the component that is handling the event
     * @return the Point of the event
     */
    public Point getComponentLocation()
    {
        if (m_oPosition != null)
        {
            float lnX = m_oPosition.getX();
            float lnY = m_oPosition.getY();
            
            IControl loControl = getTarget();
            while (loControl != m_oComponent && loControl != null)
            {
                Point loLocation = loControl.getLocation();
                lnX += loLocation.getX();
                lnY += loLocation.getY();
                loControl = loControl.getParent();
            }
            
            return new Point(lnX, lnY);
        }
        return m_oPosition;
    }
    
    /**
     * Gets the time this event occured
     * @return the event time
     */
    public Date getTime()
    {
        return m_oTime;
    }
    
    /**
     * Gets the number of clicks that are associated with this event
     * @return 
     */
    public int getClickCount()
    {
        return m_nClicks;
    }
    
    /**
     * Checks if the ALT key was pressed for this event
     * @return true if so
     */
    public boolean isAltDown()
    {
        return m_lAlt;
    }

    /**
     * Checks if the ALT GR key was pressed for this event.  AltGr is generally the 
     * right hand alt key on PC keyboard or the option key on the mac. This can also
     * be the ALT and CONTROL combination in some OS's
     * @return true if so
     */
    public boolean isAltGraphDown()
    {
        return m_lAltGraph;
    }

    /**
     * Checks if the Control key was pressed for this event
     * @return true if so
     */
    public boolean isControlDown()
    {
        return m_lControl;
    }
    
    /**
     * Checks if the meta key was pressed for this event.  The meta key is usually
     * the Windows key, or the command key on the mac
     * @return true if so
     */
    public boolean isMetaDown()
    {
        return m_lMeta;
    }

    /**
     * Checks if the shift key was pressed for this event.
     * @return true if so
     */
    public boolean isShiftDown()
    {
        return m_lShift;
    }
    
    /**
     * Checks if the mouse 1 button changed state for this event
     * @return true if it has
     */
    public boolean mouse1Changed()
    {
        return m_lButton1;
    }
    
    /**
     * Checks if the mouse 2 button changed state for this event
     * @return true if it has
     */
    public boolean mouse2Changed()
    {
        return m_lButton2;
    }
    
    /**
     * Checks if the mouse 3 button changed state for this event
     * @return true if it has
     */
    public boolean mouse3Changed()
    {
        return m_lButton3;
    }
    
    /**
     * This checks if this is a popup menu event as different os's implement this differently
     * @return true if so
     */
    public boolean isPopup()
    {
        return m_lPopup;
    }
    
    /**
     * Checks if mouse button 1 is down during this event
     * @return true if the button is down
     */
    public boolean isMouse1Down()
    {
        return m_lButton1Down;
    }
    
    /**
     * Checks if mouse button 2 is down during this event
     * @return true if the button is down
     */
    public boolean isMouse2Down()
    {
        return m_lButton2Down;
    }
    
    /**
     * Checks if mouse button 3 is down during this event
     * @return true if the button is down
     */
    public boolean isMouse3Down()
    {
        return m_lButton3Down;
    }
    
    /**
     * Gets the level this event is currently acting at, getTarget will get
     * the control where the event started
     * @return the control that is currently handling this event
     */
    public IControl getComponent()
    {
        return m_oComponent;
    }

    /**
     * Bubbles the event up to the parent control if bubbling is allowed
     * @param <K>
     * @param toType the event type we are bubbling for
     */
    @Override
    public <K extends EventType> void bubble(K toType)
    {
        if (canBubble())
        {
            IControl loParent = getComponent().getParent();
            while (loParent != null && !loParent.hasEventsFor(toType))
            {
                loParent = loParent.getParent();
            }
            if (loParent != null)
            {
                this.m_oComponent = loParent;
                loParent.fireEvent(toType, this);
            }
        }
    }
    
    


}
