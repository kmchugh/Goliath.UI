/* ========================================================
 * ITextEditorControlImpl.java
 *
 * Author:      kenmchugh
 * Created:     Dec 15, 2010, 9:29:53 PM
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

package Goliath.Interfaces.UI.Controls.Implementations;

import org.w3c.dom.Document;



/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 15, 2010
 * @author      kenmchugh
**/
public interface ITextEditorControlImpl
{
    /**
     * Checks if this control is a rich text control
     * @param toControl the control to check
     * @return true if this is a rich text control
     */
    boolean getRichText(IImplementedControl toControl);

    /**
     * Sets if this control is a rich text control or not
     * @param tlRichText true if this is a rich text control
     * @param toControl the control to set as richtext
     */
    void setRichText(boolean tlRichText, IImplementedControl toControl);
    
    long getSelectionStart(IImplementedControl toControl);

    long getSelectionEnd(IImplementedControl toControl);

}
