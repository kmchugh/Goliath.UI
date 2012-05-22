/* ========================================================
 * FormattingType.java
 *
 * Author:      mmajumdar
 * Created:     Mar 01, 2011, 11:53:46 AM
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

/**
 *
 * @author manamimajumdar
 */
public class FormattingDefinition
{
    private String m_cFormatType;
    private long m_nStartIndex;
    private long m_nEndIndex;

    public FormattingDefinition(String tcType, long tnStart, long tnEnd)
    {
        m_cFormatType = tcType.toUpperCase();
        m_nStartIndex = tnStart;
        m_nEndIndex = tnEnd;
    }

    public String getFormatType()
    {
        return m_cFormatType;
    }

    public long getStartIndex()
    {
        return m_nStartIndex;
    }

    public long getEndIndex()
    {
        return m_nEndIndex;
    }

}
