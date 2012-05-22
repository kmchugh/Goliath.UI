/* ========================================================
 * AbstractTableColumnDefinition.java
 *
 * Author:      kmchugh
 * Created:     Aug 5, 2010, 11:14:51 PM
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


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 5, 2010
 * @author      kmchugh
**/
public abstract class AbstractTableColumnDefinition<T> extends Goliath.Object
{
    private String m_cTitle;
    private String m_cProperty;
    private boolean m_lEditable;

    /**
     * Creates a new instance of TableColumnDefinition
     */
    public AbstractTableColumnDefinition(String tcProperty, String tcTitle)
    {
        m_cTitle = tcTitle;
        m_cProperty = tcProperty;
    }

    public void setTitle(String tcValue)
    {
        m_cTitle = tcValue;
    }

    public String getTitle()
    {
        return m_cTitle;
    }

    public void setProperty(String tcValue)
    {
        m_cProperty = tcValue;
    }

    public String getProperty()
    {
        return m_cProperty;
    }

    public boolean getEditable()
    {
        return m_lEditable;
    }

    public void setEditable(boolean tlEditable)
    {
        m_lEditable = tlEditable;
    }
    
    public boolean usesValueFunction()
    {
        return true;
    }

    public abstract java.lang.Object getValueFor(T toObject, int tnRow, int tnColumn);



}
