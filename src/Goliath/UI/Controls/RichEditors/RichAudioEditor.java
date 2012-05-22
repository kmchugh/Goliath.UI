/* ========================================================
 * RichAudioEditor.java
 *
 * Author:      kenmchugh
 * Created:     Mar 4, 2011, 9:49:30 AM
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

import Goliath.Constants.MimeType;
import Goliath.Interfaces.UI.Controls.IContainer;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Mar 4, 2011
 * @author      kenmchugh
**/
public class RichAudioEditor extends RichEditor<Object, Object>
{
    /**
     * Creates a new instance of RichAudioEditor
     */
    public RichAudioEditor()
    {
        super(MimeType.TEXT_HTML());
        initialiseComponent();
    }
    
    private void initialiseComponent()
    {
        
    }

    @Override
    protected EditorPane onGetEditorPane()
    {
        return new AudioEditorPane(this);
    }



    @Override
    protected Object formatRaw(Object toRawData)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected long getRawDataLength()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object parseData(Object toData)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void onInsertAt(long tnStartIndex, Object toData)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void onRemoveAt(long tnStartIndex, long tnLength)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }




}
