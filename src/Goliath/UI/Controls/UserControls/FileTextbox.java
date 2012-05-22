/* ========================================================
 * FileTextBox.java
 *
 * Author:      christinedorothy
 * Created:     Jul 21, 2011, 1:16:49 PM
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

import Goliath.Delegate;
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Controls.Button;
import Goliath.UI.Controls.Group;
import Goliath.UI.Controls.Layouts.BorderLayoutConstants;
import Goliath.UI.Controls.Textbox;
import Goliath.UI.UIEvent;
import java.awt.Component;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jul 21, 2011
 * @author      christinedorothy
**/
public class FileTextbox extends Group
        implements IValueControl<File>
{
    private File m_oSelectedFile;
    private Textbox m_oTextbox;
    private FileFilter m_oFileFilter;

    /**
     * Creates a new instance of FileTextBox
     */
    public FileTextbox()
    {
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        m_oTextbox = new Textbox();
        m_oTextbox.setEditable(false);
        m_oTextbox.setProperty(ControlLayoutProperty.LAYOUTCONSTRAINT().getValue(), BorderLayoutConstants.CENTER());
        addControl(m_oTextbox);

        Button loButton = new Button("...");
        loButton.setProperty(ControlLayoutProperty.LAYOUTCONSTRAINT().getValue(), BorderLayoutConstants.LINE_END());
        addControl(loButton);
        loButton.addEventListener(UIEventType.ONCLICK(), Delegate.build(this, "onButtonClicked"));
    }

    private void onButtonClicked(UIEvent toEvent)
    {
        JFileChooser loChooser;
        if (m_oSelectedFile != null)
        {
            loChooser = new JFileChooser(m_oSelectedFile);
        }
        else
        {
            loChooser = new JFileChooser();
        }
        if (m_oFileFilter != null)
        {
            loChooser.setFileFilter(m_oFileFilter);
        }
        int returnVal = loChooser.showOpenDialog((Component) getControl());
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            m_oSelectedFile = loChooser.getSelectedFile();
            m_oTextbox.setValue(loChooser.getSelectedFile().getPath());
        }
    }

    @Override
    public File getValue()
    {
        return m_oSelectedFile;
    }

    @Override
    public void setValue(File toValue)
    {
        m_oSelectedFile = toValue;
        m_oTextbox.setValue(toValue == null ? "" : toValue.getPath());
    }

    public FileFilter getFileFilter()
    {
        return m_oFileFilter;
    }

    public void setFileFilter(FileFilter toFilter)
    {
        m_oFileFilter = toFilter;
    }

    @Override
    public void setEditable(boolean tlEditable)
    {
        super.setEditable(tlEditable);
        m_oTextbox.setEditable(false);
    }
}
