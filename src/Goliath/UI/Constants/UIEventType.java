/* =========================================================
 * Events.java
 *
 * Author:      kmchugh
 * Created:     02-Jun-2008, 08:45:41
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
 * =======================================================*/

package Goliath.UI.Constants;

import Goliath.Constants.EventType;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 02-Jun-2008
 * @author      kmchugh
**/
public class UIEventType extends EventType
{
    /**
     * Creates a new instance of an EventTypes Object 
     *
     * @param tcValue The value for the event type
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public UIEventType(String tcValue)
    {
        super(tcValue);
    }
    
    private static UIEventType g_oClick;
    public static UIEventType ONCLICK()
    {
        if (g_oClick == null)
        {
            g_oClick = createEnumeration(UIEventType.class, "ONCLICK");
        }
        return g_oClick;
    }

    private static UIEventType g_oOpened;
    public static UIEventType ONOPENED()
    {
        if (g_oOpened == null)
        {
            g_oOpened = createEnumeration(UIEventType.class, "ONOPENED");
        }
        return g_oOpened;
    }

    private static UIEventType g_oActivated;
    public static UIEventType ONACTIVATED()
    {
        if (g_oActivated == null)
        {
            g_oActivated = createEnumeration(UIEventType.class, "ONACTIVATED");
        }
        return g_oActivated;
    }

    private static UIEventType g_oDeactivated;
    public static UIEventType ONDEACTIVATED()
    {
        if (g_oDeactivated == null)
        {
            g_oDeactivated = createEnumeration(UIEventType.class, "ONDEACTIVATED");
        }
        return g_oDeactivated;
    }

    private static UIEventType g_oClosed;
    public static UIEventType ONCLOSED()
    {
        if (g_oClosed == null)
        {
            g_oClosed = createEnumeration(UIEventType.class, "ONCLOSED");
        }
        return g_oClosed;
    }

    private static UIEventType g_oClosing;
    public static UIEventType ONCLOSING()
    {
        if (g_oClosing == null)
        {
            g_oClosing = createEnumeration(UIEventType.class, "ONCLOSING");
        }
        return g_oClosing;
    }

    private static UIEventType g_oMinimised;
    public static UIEventType ONMINIMISED()
    {
        if (g_oMinimised == null)
        {
            g_oMinimised = createEnumeration(UIEventType.class, "ONMINIMISED");
        }
        return g_oMinimised;
    }

    private static UIEventType g_oRestored;
    public static UIEventType ONRESTORED()
    {
        if (g_oRestored == null)
        {
            g_oRestored = createEnumeration(UIEventType.class, "ONRESTORED");
        }
        return g_oRestored;
    }
    
    private static UIEventType g_oSubmit;
    public static UIEventType ONSUBMIT()
    {
        if (g_oSubmit == null)
        {
            g_oSubmit = createEnumeration(UIEventType.class, "ONSUBMIT");
        }
        return g_oSubmit;
    }
    
    private static UIEventType g_oLoad;
    public static UIEventType ONLOAD()
    {
        if (g_oLoad == null)
        {
            g_oLoad = createEnumeration(UIEventType.class, "ONLOAD");
        }
        return g_oLoad;
    }
    
    private static UIEventType g_oSelectionChanged;
    public static UIEventType ONSELECTIONCHANGED()
    {
        if (g_oSelectionChanged == null)
        {
            g_oSelectionChanged = createEnumeration(UIEventType.class, "ONSELECTIONCHANGED");
        }
        return g_oSelectionChanged;
    }
    
    private static UIEventType g_oStyleChanged;
    public static UIEventType ONSTYLECHANGED()
    {
        if (g_oStyleChanged == null)
        {
            g_oStyleChanged = createEnumeration(UIEventType.class, "ONSTYLECHANGED");
        }
        return g_oStyleChanged;
    }
    
    

    

    private static UIEventType g_oVisibleChanged;
    public static UIEventType ONVISIBLECHANGED()
    {
        if (g_oVisibleChanged == null)
        {
            g_oVisibleChanged = createEnumeration(UIEventType.class, "ONVISIBLECHANGED");
        }
        return g_oVisibleChanged;
    }
    
    private static UIEventType g_oSizeChanged;
    public static UIEventType ONSIZECHANGED()
    {
        if (g_oSizeChanged == null)
        {
            g_oSizeChanged = createEnumeration(UIEventType.class, "ONSIZECHANGED");
        }
        return g_oSizeChanged;
    }
    
    private static UIEventType g_oSizeChanging;
    public static UIEventType ONSIZECHANGING()
    {
        if (g_oSizeChanging == null)
        {
            g_oSizeChanging = createEnumeration(UIEventType.class, "ONSIZECHANGING");
        }
        return g_oSizeChanging;
    }
    
    
    private static UIEventType g_oPositionChanged;
    public static UIEventType ONPOSITIONCHANGED()
    {
        if (g_oPositionChanged == null)
        {
            g_oPositionChanged = createEnumeration(UIEventType.class, "ONPOSITIONCHANGED");
        }
        return g_oPositionChanged;
    }
    
    private static UIEventType g_oMouseOver;
    public static UIEventType ONMOUSEOVER()
    {
        if (g_oMouseOver == null)
        {
            g_oMouseOver = createEnumeration(UIEventType.class, "ONMOUSEOVER");
        }
        return g_oMouseOver;
    }
    
    private static UIEventType g_oMouseOut;
    public static UIEventType ONMOUSEOUT()
    {
        if (g_oMouseOut == null)
        {
            g_oMouseOut = createEnumeration(UIEventType.class, "ONMOUSEOUT");
        }
        return g_oMouseOut;
    }

    private static UIEventType g_oMouseUp;
    public static UIEventType ONMOUSEUP()
    {
        if (g_oMouseUp == null)
        {
            g_oMouseUp = createEnumeration(UIEventType.class, "ONMOUSEUP");
        }
        return g_oMouseUp;
    }

    private static UIEventType g_oMouseDown;
    public static UIEventType ONMOUSEDOWN()
    {
        if (g_oMouseDown == null)
        {
            g_oMouseDown = createEnumeration(UIEventType.class, "ONMOUSEDOWN");
        }
        return g_oMouseDown;
    }

    private static UIEventType g_oMouseDragged;
    public static UIEventType ONMOUSEDRAGGED()
    {
        if (g_oMouseDragged == null)
        {
            g_oMouseDragged = createEnumeration(UIEventType.class, "ONMOUSEDRAGGED");
        }
        return g_oMouseDragged;
    }

    private static UIEventType g_oMouseMoved;
    public static UIEventType ONMOUSEMOVED()
    {
        if (g_oMouseMoved == null)
        {
            g_oMouseMoved = createEnumeration(UIEventType.class, "ONMOUSEMOVED");
        }
        return g_oMouseMoved;
    }


    private static UIEventType g_oInvalidated;
    public static UIEventType ONINVALIDATED()
    {
        if (g_oInvalidated == null)
        {
            g_oInvalidated = createEnumeration(UIEventType.class, "ONINVALIDATED");
        }
        return g_oInvalidated;
    }
    
    private static UIEventType g_oValidated;
    public static UIEventType ONVALIDATED()
    {
        if (g_oValidated == null)
        {
            g_oValidated = createEnumeration(UIEventType.class, "ONVALIDATED");
        }
        return g_oValidated;
    }

    private static UIEventType g_oChildAdded;
    public static UIEventType ONCHILDADDED()
    {
        if (g_oChildAdded == null)
        {
            g_oChildAdded = createEnumeration(UIEventType.class, "ONCHILDADDED");
        }
        return g_oChildAdded;
    }

    private static UIEventType g_oChildRemoved;
    public static UIEventType ONCHILDREMOVED()
    {
        if (g_oChildRemoved == null)
        {
            g_oChildRemoved = createEnumeration(UIEventType.class, "ONCHILDREMOVED");
        }
        return g_oChildRemoved;
    }

    private static UIEventType g_oCaretChanged;
    public static UIEventType ONCARETCHANGED()
    {
        if (g_oCaretChanged == null)
        {
            g_oCaretChanged = createEnumeration(UIEventType.class, "ONCARETCHANGED");
        }
        return g_oCaretChanged;
    }
}
