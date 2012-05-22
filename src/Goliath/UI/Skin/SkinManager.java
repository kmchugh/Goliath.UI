/* ========================================================
 * SkinManager.java
 *
 * Author:      kenmchugh
 * Created:     Nov 26, 2010, 9:38:50 AM
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

package Goliath.UI.Skin;

import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Controls.ControlStyle;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Nov 26, 2010
 * @author      kenmchugh
**/
public class SkinManager extends Goliath.Object
{
    private static SkinManager g_oManager;
    public static SkinManager getInstance()
    {
        if (g_oManager == null)
        {
            g_oManager = new SkinManager();
        }
        return g_oManager;
    }
    
    private Skin m_oCurrentSkin;


    /**
     * Creates a new instance of SkinManager,
     * this is not publically creatable
     */
    protected SkinManager()
    {
    }

    /**
     * Gets the skin that is currently in use by the application, only one
     * skin can be in use a  a time
     * @return the skin that is currently applied
     */
    public Skin getCurrentSkin()
    {
        return m_oCurrentSkin;
    }

    /**
     * Sets the current skin
     * @param toSkin the new skin
     */
    public void setCurrentSkin(Skin toSkin)
    {
        if (m_oCurrentSkin != toSkin)
        {
            m_oCurrentSkin = toSkin;
            // TODO: Fire a skin changed event
        }
    }
    
    /**
     * renders the specified control passing it through the current skin, this
     * method is expected to change the render canvas
     * @param toControl the control to render
     * @param toRenderCanvas the canvas that we are rendering to
     */
    public void render(IControl toControl, Object toRenderCanvas)
    {
        // Get the current skin and render the control object through it
        Skin loSkin = getCurrentSkin();
        if (loSkin != null)
        {
            loSkin.render(toControl, toRenderCanvas);
        }
    }
    
    public void renderBorder(Object toRenderCanvas, IControl toControl, ControlStyle toStyle)
    {
        Skin loSkin = getCurrentSkin();
        if (loSkin != null)
        {
            loSkin.renderBorder(toControl, toRenderCanvas, toStyle);
        }
    }
    
    public void renderContent(Object toRenderCanvas, IControl toControl, ControlStyle toStyle)
    {
        Skin loSkin = getCurrentSkin();
        if (loSkin != null)
        {
            loSkin.renderContent(toControl, toRenderCanvas, toStyle);
        }
    }
    
    public void renderBackground(Object toRenderCanvas, IControl toControl, ControlStyle toStyle)
    {
        Skin loSkin = getCurrentSkin();
        if (loSkin != null)
        {
            loSkin.renderBackground(toControl, toRenderCanvas, toStyle);
        }
    }
    
    
    // TODO: Allow for session based theming
    // TODO: Create methods for loading skins
    // TODO: Create methods for changing skins
}
