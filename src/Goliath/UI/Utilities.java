/* ========================================================
 * Utilities.java
 *
 * Author:      kenmchugh
 * Created:     Mar 2, 2011, 5:05:20 PM
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

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Constants.LogType;
import Goliath.Constants.XMLFormatType;
import Goliath.Exceptions.FileAlreadyExistsException;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.XML.XMLFormatter;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Mar 2, 2011
 * @author      kenmchugh
**/
public class Utilities extends Goliath.Object
{

    /**
     * Gets all of the controls from the container that inherit from the specified class, if toControl is
     * of the type specified then it will be included in the list
     * @param toClass the class or interface to find
     * @param toControl the control to start from
     * @return the list of controls of the specified type
     */
    public static <T extends IControl> List<T> getElementsByTagName(Class<? extends T> toClass, IContainer toControl)
    {
        List<T> loControls = new List<T>();

        for (IControl loControl : toControl.getChildren())
        {
            if (Goliath.DynamicCode.Java.isEqualOrAssignable(IContainer.class, loControl.getClass()))
            {
                loControls.addAll(getElementsByTagName(toClass, (IContainer)loControl));
            }
            else if (Goliath.DynamicCode.Java.isEqualOrAssignable(toClass, loControl.getClass()))
            {
                loControls.add((T)loControl);
            }
        }

        if (Goliath.DynamicCode.Java.isEqualOrAssignable(toClass, toControl.getClass()))
        {
            loControls.add((T)toControl);
        }
        
        return loControls;
    }

    public static void toGSPFile(IControl toControl, File toFile, boolean tlOveride)
    {
        try
        {
            XMLFormatter.writeXMLFile(toControl, toFile, XMLFormatType.GSP(), "GSP", tlOveride);
        }
        catch (FileAlreadyExistsException ex)
        {
            if (!tlOveride)
            {
                Application.getInstance().log("File already exists " + toFile.getName(), LogType.ERROR());
            }
        }
    }

    /**
     * Creates a new instance of Utilities
     */
    private Utilities()
    {
    }


}
