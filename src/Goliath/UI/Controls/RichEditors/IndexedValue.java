/* ========================================================
 * IndexedValue.java
 *
 * Author:      mmajumdar
 * Created:     Mar 4, 2010, 1:44:41 PM
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
package Goliath.UI.Controls.RichEditors;

/**
 *
 * @author manamimajumdar
 */
public class IndexedValue
{

    private long m_nStart;
    private long m_nEnd;

    public IndexedValue(long tnStart, long tnEnd)
    {
        m_nStart = Math.min(Math.abs(tnStart), Math.abs(tnEnd));
        m_nEnd = Math.max(Math.abs(tnStart), Math.abs(tnEnd));
    }

    public long getStart()
    {
        return m_nStart;
    }

    public long getEnd()
    {
        return m_nEnd;
    }

    public long getLength()
    {
        return m_nEnd - m_nStart;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final IndexedValue other = (IndexedValue) obj;
        if (this.m_nStart != other.m_nStart)
        {
            return false;
        }
        if (this.m_nEnd != other.m_nEnd)
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 79 * hash + (int) (this.m_nStart ^ (this.m_nStart >>> 32));
        hash = 79 * hash + (int) (this.m_nEnd ^ (this.m_nEnd >>> 32));
        return hash;
    }
}
