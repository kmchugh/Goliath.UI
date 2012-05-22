/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls.Implementations;

import Goliath.Collections.List;
import Goliath.Constants.EventType;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Controls.ControlImplementationType;
import java.awt.Color;

/**
 *
 * @author kmchugh
 */
public interface IControlImpl
{
    /**
     * Gets the Goliath.UI.Control class that this Implementation is to be used for
     * @return the class that is supported by this implementation
     */
    Class getSupportedClass();

    /**
     * Creates the implemented control and sets the base control to this control for communication
     * between the logic and the implementation
     * @param toControlBase the base control
     * @return the newly created implementation of the control
     */
    IImplementedControl createControl(IControl toControlBase);

    /**
     * Gets the render types that this control supports
     * @return a list of types that this implementation knows how to handle
     */
    List<ControlImplementationType> getSupportedTypes();
    
    /**
     * Renders the compent as required.
     * @param toRenderCanvas is the canvas to render to, this could be a string, a graphics object, or a file, depending
     * on the specific implementation.  It is up to the implementation to know what this object is
     * @param toControl the control to render
     */
    void renderComponent(java.lang.Object toRenderCanvas, IImplementedControl toControl);
    
    void setVisible(boolean tlVisible, IImplementedControl toControl);

    boolean setSize(Dimension toSize, IImplementedControl toControl);

    void setSelectable(boolean tlSelectable, IImplementedControl toControl);

    void setName(String tcName, IImplementedControl toControl);

    void setMinSize(Dimension toSize, IImplementedControl toControl);

    void setMaxSize(Dimension toSize, IImplementedControl toControl);

    void setLocation(Point toLocation, IImplementedControl toControl);

    void setEnabled(boolean tlEnabled, IImplementedControl toControl);

    boolean isVisible(IImplementedControl toControl);

    void setFocus(IImplementedControl toControl);
    
    /**
     * Forces rendering of the control after the specified delay.  The re render will occur at
     * toPoint and will redraw the area within toDimension
     * @param toControl the control to re paint
     * @param tnDelayMillis the delay before the repaint
     * @param toPoint the point to start the repaint at
     * @param toDimension the area of the repaint
     */
    void forceRender(IImplementedControl toControl, long tnDelayMillis, Point toPoint, Dimension toDimension);

    void setFontSize(float tnSize, IImplementedControl toControl);

    /**
     * Checks if this controls is actually showing on the screen
     * @return true if the control is on screen
     */
    boolean isShowing(IImplementedControl toControl);

    boolean isSelectable(IImplementedControl toControl);

    boolean isEnabled(IImplementedControl toControl);

    Dimension getSize(IImplementedControl toControl);

    Dimension getPreferredSize(IImplementedControl toControl);

    String getName(IImplementedControl toControl);

    String getTooltip(IImplementedControl toControl);

    void setTooltip(String tcTooltip, IImplementedControl toControl);

    Dimension getMinSize(IImplementedControl toControl);

    Dimension getMaxSize(IImplementedControl toControl);

    Point getLocation(IImplementedControl toControl);

    void addEventListener(EventType toEvent, IImplementedControl toControl);

    void invalidate(IImplementedControl toControl);

    void update(IImplementedControl toControl);

    boolean isDisplayable(IImplementedControl toControl);

    void setEditable(boolean tlVisible, IImplementedControl toControl);

    boolean isEditable(IImplementedControl toControl);

    IContainer getParent(IImplementedControl toControl);

    void setBorderSize(float tnTop, float tnRight, float tnBottom, float tnLeft, IImplementedControl toControl);

    void setColour(Color toColour, IImplementedControl toControl);

    Color getColour(IImplementedControl toControl);

    void setOpacity(float tnOpacity, IImplementedControl toControl);

    float getOpacity(IImplementedControl toControl);


    /**
     * Sets the background image for this control
     * @param toBackground the new background image for the control
     */
    void setBackground(Goliath.Graphics.Image toBackground, IImplementedControl toControl);

    /**
     * Sets the background image for this control
     * @param toBackground the new background colour for the control
     */
    void setBackground(Color toBackground, IImplementedControl toControl);

    /**
     * Gets the background image for this control
     * @return the background image, this could be null
     */
    Goliath.Graphics.Image getBackgroundImage(IImplementedControl toControl);

    /**
     * Gets the background colour for this control
     * @return the background colour, this could be null
     */
    Color getBackgroundColour(IImplementedControl toControl);

    /**
     * Sets if the controls background is opaque or transparent
     * @param tlOpaque true if this should be opaque
     */
    void setOpaque(boolean tlOpaque, IImplementedControl toControl);

    /**
     * Checks if the controls background is opaque or transparent
     * @return true if opaque
     */
    boolean isOpaque(IImplementedControl toControl);
}
