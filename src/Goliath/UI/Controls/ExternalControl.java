/* ========================================================
 * ExternalControl.java
 *
 * Author:      kmchugh
 * Created:     Apr 19, 2011, 2:26:39 PM
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

import java.awt.BorderLayout;
import java.awt.Component;


        
/**
 * This control allows controls that do not belong to the goliath framework to
 * be placed inside goliath containers
 *
 * @see         Related Class
 * @version     1.0 Apr 19, 2011
 * @author      kmchugh
**/
public class ExternalControl extends Control
{
    private Component m_oExternalControl;
    // TODO: Need to do this properly
    public ExternalControl(Component toControl)
    {
        m_oExternalControl = toControl;
        ((java.awt.Container)this.getControl()).setLayout(new BorderLayout());
        ((java.awt.Container)this.getControl()).add(m_oExternalControl);
    }


}
