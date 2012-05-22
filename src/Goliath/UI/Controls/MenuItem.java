/* ========================================================
 * MenuItem.java
 *
 * Author:      kmchugh
 * Created:     Aug 18, 2010, 5:28:32 PM
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

import Goliath.Graphics.Image;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.UI.Controls.IImage;
import Goliath.Interfaces.UI.Controls.IMenuItem;
import Goliath.Interfaces.UI.Controls.Implementations.ILabelledControlImpl;
import Goliath.UI.Constants.UIEventType;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 18, 2010
 * @author      kmchugh
**/
public class MenuItem extends Control
    implements IMenuItem
{

    /**
     * Helper function to get the implementation control for the button
     * @return
     */
    protected ILabelledControlImpl getLabelledImplementation()
    {
        return (ILabelledControlImpl)getImplementation();
    }

    /**
     * Creates a new instance of MenuItem
     */
    public MenuItem()
    {
    }

    /**
     * Creates a new instance of MenuItem
     * @param tcLabel The label of the MenuItem
     */
    public MenuItem(String tcLabel)
    {
        super();
        setText(tcLabel);
    }

    public MenuItem(IImage toImage)
    {
        super();
        // TODO: Implement Image
    }

    public MenuItem(String tcLabel, IImage toImage)
    {
        this(toImage);
        setText(tcLabel);
    }

   public MenuItem(String tcLabel, IImage toImage, IDelegate toClickCallback)
    {
        this(toImage);
        setText(tcLabel);
        addEventListener(UIEventType.ONCLICK(), toClickCallback);
    }

    /**
     * Creates a new instance of MenuItem
     * @param tcLabel the label of the MenuItem
     * @param toClickCallback the event to take when the MenuItem is clicked
     */
    public MenuItem(String tcLabel, IDelegate toClickCallback)
    {
        super();
        setText(tcLabel);
        addEventListener(UIEventType.ONCLICK(), toClickCallback);
    }

    /**
     * Creates a new instance of MenuItem
     * @param tcLabel the label of the MenuItem
     * @param toClickCallback the event to take when the MenuItem is clicked
     */
    public MenuItem(ControlImplementationType toImplementationType, String tcLabel, IDelegate toClickCallback)
    {
        super(toImplementationType);
        setText(tcLabel);
        addEventListener(UIEventType.ONCLICK(), toClickCallback);
    }


    /**
     * Gets the text of the MenuItem
     * @return the text of the MenuItem
     */
    @Override
    public String getText()
    {
        return getLabelledImplementation().getText(getControl());
    }

    /**
     * Sets the text of the MenuItem
     * @param tcText the text of the MenuItem
     */
    @Override
    public void setText(String tcText)
    {
        getLabelledImplementation().setText(tcText, getControl());
    }

    @Override
    public Image getImage()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setImage(Image toImage)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}