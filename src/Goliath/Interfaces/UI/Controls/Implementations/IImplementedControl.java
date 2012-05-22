/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls.Implementations;

import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Constants.UIEventType;
import java.awt.Graphics2D;

/**
 *
 * @author kmchugh
 */
public interface IImplementedControl
{

    /**
     * Gets the base (Goliath.UI.Control) version of the control
     * @return
     */
    IControl getControlBase();

    /**
     * Gets the property with the specified name.
     * @param <T>
     * @param tcProperty the name of the property to get
     * @return the value of the property, or null if the property has not been set
     */
    <T extends Object> T getProperty(String tcProperty);

    /**
     * Gets the property with the specified name.  If the property has not been set
     * previously, it will be set to the default value
     * @param <T>
     * @param tcProperty the name of the property to get
     * @param toDefault the value to use if the value had not already been set.
     * @return the value of the property, or toDefault if the property never existed
     */
    <T extends Object> T getProperty(String tcProperty, T toDefault);

    /**
     * Sets the property to the specified value
     * @param <T>
     * @param tcProperty the property to set
     * @param toValue the value of the property
     * @return the old value of the property, or null if the property was not set
     */
    <T extends Object> T setProperty(String tcProperty, T toValue);
    
    /**
     * Forces the component to paint to the graphics object passed in
     * @param toGraphics the graphics object to paint to
     */
    void paint(Graphics2D toGraphics);
    
    Dimension getImplementedSize();

    Point getImplementedLocation();

}
