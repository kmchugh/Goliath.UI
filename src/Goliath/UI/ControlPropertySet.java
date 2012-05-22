/* ========================================================
 * ControlPropertySet.java
 *
 * Author:      kmchugh
 * Created:     Feb 17, 2011, 9:16:09 AM
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

import Goliath.Collections.PropertySet;
import Goliath.UI.Constants.ControlLayoutProperty;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Feb 17, 2011
 * @author      kmchugh
**/
public class ControlPropertySet extends PropertySet
{
    /**
     * Creates a new instance of ControlPropertySet
     */
    public ControlPropertySet()
    {
    }

    public ControlPropertySet(ControlLayoutProperty toProperty, Object toValue)
    {
        setProperty(toProperty, toValue);
    }

    public <T> T getProperty(ControlLayoutProperty toProperty)
    {
        return (T)super.getProperty(toProperty.getValue(), false);
    }

    public <T> boolean setProperty(ControlLayoutProperty toProperty, T toValue)
    {
        return super.setProperty(toProperty.getValue(), toValue);
    }


}
