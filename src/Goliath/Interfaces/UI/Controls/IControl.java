/* =========================================================
 * IControl.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * Base control interface, all controls created in the framework
 * will conform to this interface
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/
package Goliath.Interfaces.UI.Controls;

import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Constants.EventType;
import Goliath.Event;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Graphics.Constants.Position;
import Goliath.Graphics.Rectangle;
import Goliath.Interfaces.IEventDispatcher;
import Goliath.Interfaces.UI.Controls.Implementations.IControlImpl;
import Goliath.Interfaces.UI.Controls.Implementations.IImplementedControl;
import Goliath.UI.Controls.BorderSettings;
import Goliath.UI.Controls.ControlBounds;
import Goliath.UI.Controls.ControlImplementationType;
import Goliath.UI.Controls.ControlStyle;
import java.awt.Color;

/**
 *
 * @author kmchugh
 */
public interface IControl
        extends IEventDispatcher<EventType, Event<? extends IControl>>
{
    /**
     * The gets the actual control that the user sees.  This method is public
     * as the implementation needs access to the control itself.  The Implementation
     * controls communication between the visual control and the non visual control.
     * In general there should be no need to call getControl externally as the developer
     * will never know exactly what control they will get.
     * @return the actual visual component
     */
    IImplementedControl getControl();
    
    
    /**
     * Gets the implementation of this control
     * @return the implementation of the control
     */
    IControlImpl getImplementation();

    /**
     * Checks if this controls is actually showing on the screen
     * @return true if the control is on screen
     */
    boolean isShowing();
    
    /**
     * Gets the full class string for this control
     * @return the string representation of the entire class list applied to this control
     */
    String getClassString();

    /**
     * Gets the type to use for the implementation of this control
     * @return The implementation type for this control
     */
    ControlImplementationType getImplementationType();
    
    /**
     * removes any resources and breaks any links used by this control to allow
     * it to be cleaned by the garbage collector
     */
    void dispose();
    
    /**
     * Prompts the control to render itself to the canvas specified.  The canvas
     * is based on implementation type.  Generally this method should never be
     * called manually.
     * @param toRenderCanvas the canvas to render to
     */
    void render(java.lang.Object toRenderCanvas);

    /**
     * Gets the name of the control, the name is how the control is identified
     * @return the name of the control
     */
    String getName();

    /**
     * Sets the name of the control, the name is simply used to refer to the
     * control in any dynamically generated code, or when searching a tree for
     * a control
     * @param tcName the new name of the control
     */
    void setName(String tcName);
    
    /**
     * Force the rendering of the control to occur
     */
    void forceRender();
    
    /**
     * Forces the rerendering of the control after the specified delat
     * @param tnRenderDelay the delay
     */
    void forceRender(long tnRenderDelay);
    
    /**
     * Forces the rerendering of the control starting at toStart and covering
     * only toArea
     * @param toStart the start point
     * @param toArea the area to rerender in
     */
    void forceRender(Point toStart, Dimension toArea);
    
    /**
     * Forces the rerendering of the control starting at toStart and covering after the
     * specified delay
     * @param tnRenderDelay the delay
     * @param toStart the start point
     * @param toArea the area to rerender in
     */
    void forceRender(long tnRenderDelay, Point toStart, Dimension toArea);

    /**
     * Gets the value of the specified property, this only includes a search through custom properties.
     * @param <T> The type of the value to return
     * @param tcProperty The name of the property to get.  This is not case sensitive
     * @return the value retrieved, or null if the property does not exist
     */
    <T extends Object> T getProperty(String tcProperty);

    /*
     * Gets the value of the specified property, this only includes a search through custom properties.
     * If the property does not exist, it will be set to toDefault then returned.
     * @param <T> The type of the value to return
     * @param tcProperty The name of the property to get.  This is not case sensitive
     * @param toDefault The default value of this property, if the property has not already been set, it will be set to this value
     * @return the value retrieved, or toDefault if the property does not exist
     */
    <T extends Object> T getProperty(String tcProperty, T toDefault);

    /**
     * Checks if this control has custom properties
     * @return true if custom properties have been set for this control
     */
    boolean hasProperties();

    /**
     * Gets the full list of custom properties that have been set on this control
     * @return the list of properties or null if no properties exist
     */
    PropertySet getProperties();

    /**
     * Sets the property to the specified value
     * @param <T> the type of the property
     * @param tcProperty the property to set, this is not case sensitive
     * @param toValue the value of the property
     * @return the old value of the property, or null if the property was not set
     */
    <T extends Object> T setProperty(String tcProperty, T toValue);

    /**
     * Clears the specified property from the property bag
     * @param <T> the type of the property
     * @param tcProperty the name of the property
     * @return the old value of the property or null if the property did not exist
     */
    <T extends Object> T clearProperty(String tcProperty);

    /**
     * Checks if the control is visible or not
     * @return true if visible false if not
     */
    boolean isVisible();

    /**
     * Sets the focus to the specified control
     */
    void setFocus();

    /**
     * Sets if the control is visible or not
     * @param tlVisible true to make the control visible, false otherwise
     */
    void setVisible(boolean tlVisible);

    /**
     * Checks if the size has been set using the setSize method
     * @return true if the size has been explicitly set
     */
    boolean isSizeSet();

    /**
     * Sets if the control can be modified by the user
     * @param tlVisible
     */
    void setEditable(boolean tlVisible);

    /**
     * Checks if the control can be modified by the user
     * @return true if yes, otherwise false
     */
    boolean isEditable();

    /**
     * Sets the size of the control
     * @param toDimension the new dimensions of the control
     * @return true if the size was changed
     */
    boolean setSize(Dimension toDimension);

    /**
     * Sets the size of the control
     * @param tnWidth the new width
     * @param tnHeight the new height
     * @return true if the size was changed
     */
    boolean setSize(float tnWidth, float tnHeight);

     /**
     * Gets the size of the control as a dimension
     * @return the current dimension of the control
     */
    Dimension getSize();
    
    /**
     * Sets the content size of this control, this is similar to the css box model,
     * specifying the content size will add the margin, border, and padding to the size
     * @param toDimension the content dimensions of this control
     * @return true if the size was changed as a result of this call
     */
    boolean setContentSize(Dimension toDimension);
    
    /**
     * Sets the content size of this control, this is similar to the css box model,
     * specifying the content size will add the margin, border, and padding to the size
     * @param tnWidth the content width of this control
     * @param tnHeight the content height of this control
     * @return true if the size was changed as a result of this call
     */
    boolean setContentSize(float tnWidth, float tnHeight);
    
    /**
     * Gets the size of the control not including its margin, border, or padding
     * @return the content dimension of the control
     */
    Dimension getContentSize();
    
    /**
     * Gets the content bounds of this control, this excludes the margin, border, and padding
     * @return the content bounds for this control
     */
    ControlBounds getContentBounds();

    /**
     * Gets the rectangle that the contents of this control should be drawn to
     * @return the area within this control that should be drawn to when rendering the controls
     */
    Rectangle getContentRectangle();
    
    /**
     * Sets the preferred size of this control, usually called by a layoutmanager
     * @param toDimension the preferred size of the control
     */
    boolean setPreferredSize(Dimension toDimension);

    /**
     * Gets the preferred size for this control
     * @return the preferred size of the control
     */
    Dimension getPreferredSize();

    /**
     * Sets if the control is selectable, or focusable
     * @param tlSelectable true if selectable, false if not
     */
    void setSelectable(boolean tlSelectable);
    
    /**
     * Gets if the control is selectable or not
     * @return true if selectable, false if not
     */
    boolean isSelectable();

    /**
     * Sets if the control is enabled or not
     * @param tlEnabled true if enabled, false otherwise
     */
    void setEnabled(boolean tlEnabled);

    /**
     * Gets if the control is enabled
     * @return true if it is, false if it is not
     */
    boolean isEnabled();

    /**
     * Sets the minimum size the control is allowed to be, the control should
     * never be able to be set as smaller than this size.
     * If a control is set smaller than this size, it will just silently be set
     * to this minimum size
     * @param tnWidth the height of the control
     * @param tnHeight the width of the control
     */
    void setMinSize(float tnWidth, float tnHeight);

    /**
     * Sets the minimum size the control is allowed to be, the control should
     * never be able to be set as smaller than this size.
     * If a control is set smaller than this size, it will just silently be set
     * to this minimum size
     * @param toDimension the new mimimum size of the control
     */
    void setMinSize(Dimension toDimension);

    /**
     * Gets the minimum size of this control, if no minimum size is set, this
     * will return null
     * @return the minimum size, or null if no minimum was set
     */
    Dimension getMinSize();

    /**
     * Sets the maximum size the control is allowed to be, the control should
     * never be able to be set as larger than this size.
     * If a control is set larger than this size, it will just silently be set
     * to this maximum size
     * @param tnWidth the maximum height of the control
     * @param tnHeight the maximum width of the control
     */
    void setMaxSize(float tnWidth, float tnHeight);

    /**
     * Sets the maximum size the control is allowed to be, the control should
     * never be able to be set as larger than this size.
     * If a control is set larger than this size, it will just silently be set
     * to this maximum size
     * @param toDimension the new maximum size of the control
     */
    void setMaxSize(Dimension toDimension);

    /**
     * Gets the maximum size of this control, if no maximum size is set, this
     * will return null
     * @return the maximum size, or null if no maximum was set
     */
    Dimension getMaxSize();

    /**
     * Gets the bounds of the control
     * @return the control bounds
     */
    ControlBounds getControlBounds();


    /**
    * Sets the location of the control
    * @param tnX the new x position
    * @param tnY the new y position
    */
    void setLocation(float tnX, float tnY);
    /**
     * Sets the location of the control
     * @param toPoint the new location of the control
     */
    void setLocation(Point toPoint);

    /**
    * Sets the location of the background image if there is one
    * @param tnX the new x position
    * @param tnY the new y position
    */
    void setBackgroundLocation(float tnX, float tnY);
    /**
     * Sets the location of the Background image if there is one
     * @param toPoint the new location of the Background
     */
    void setBackgroundLocation(Point toPoint);

    /**
     * Gets the location of the background
     * @return the current location of the background
     */
    Point getBackgroundLocation();

    /**
     * Gets the location of the control
     * @return the current location of the control
     */
    Point getLocation();

    /**
     * Marks a control as invalid so it will be updated when required, this will also
     * mark parent controls as invalid
     */
    void invalidate();

    /**
     * Finds the highest invalid parent and redraws from there
     */
    void update();

    /**
     * Checks if a control is displayable (initialised and not being disposed)
     * @return true if the control is displayable
     */
    boolean isDisplayable();

    /**
     * Sets the tooltip for the control
     * @param tcValue the new tooltip value
     */
    void setTooltip(String tcValue);

    /**
     * Gets the tooltip for the control
     * @return the tooltip of the control
     */
    String getTooltip();

    /**
     * Gets the parent container for this control, this could be null
     * @return the parent container
     */
    IContainer getParent();

    /**
     * Returns if the control has been invalidated and needs to be redrawn
     * @return true if this control will be redrawn at some point
     */
    boolean isInvalidated();

    /**
     * Sets the foreground colour used for this control
     * @param toColour the foreground colour for this control
     */
    void setColour(Color toColour);

    /**
     * Gets the foreground colour of this control
     * @return the foreground colour of the control
     */
    Color getColour();

    /**
     * Sets the background image for this control
     * @param toBackground the new background image for the control
     */
    void setBackground(Goliath.Graphics.Image toBackground);

    /**
     * Sets the background colour of this control
     * @param toColour
     */
    void setBackground(Color toColour);

    /**
     * Gets the background colour of this control
     * @return the background colour of the control
     */
    Color getBackgroundColour();

    /**
     * Gets the background image for this control
     * @return the background image, this could be null
     */
    Goliath.Graphics.Image getBackgroundImage();

    /**
     * Checks if this control should force clearing of the background before being drawn
     * @return true if the BG should be cleared, this is only checked if there is a background image and if this is not an opaque control
     */
    boolean clearOnPaint();

    /**
     * Sets if this control should clear it's background area before painting, used for transparent controls  with a background image
     * @param tlClear true if you want to clear the bg area of the control
     */
    void setClearOnPaint(boolean tlClear);


    /**
     * Sets the alignment type of the background image
     * @param toAlignment
     */
    void setBackgroundImageAlignment(Position toAlignment);

    /**
     * Gets the alignment type for the background image
     * @return the alignmetn type for the background image
     */
    Position getBackgroundImageAlignment();

    /**
     * Returns if this control should be participating in the layout
     * @return true if the control participates in the layout
     */
    boolean participatesInLayout();

    /**
     * Sets if this control should be participating with the layout managers layout
     * This is up to the layout manager to adhere to
     * @param tlParticipate true if this control should participate
     */
    void setParticipatesInLayout(boolean tlParticipate);

    /**
     * Sets this controls opacity value
     * @param tnAlpha the value between 0 and 1
     */
    void setOpacity(float tnAlpha);
    
    /**
     * Gets the controls alpha value
     * @return the alpha value between 0 and 1
     */
    float getOpacity();

    /**
     * Checks if the controls background is opaque or transparent
     * @return true if opaque
     */
    public boolean isOpaque();

    /**
     * Checks if this control has a border set
     * @return true if a border is set
     */
    boolean hasBorder();


    /**
     * Sets the border for this control
     * @param toBorder the border definition for the control
     */
    void setBorder(BorderSettings toBorder);

    /**
     * Gets the border definition for this control
     * @return the control border definition
     */
    BorderSettings getBorder();

    /**
     * Adds the specified class to the control
     * @param tcClass the class to add
     */
    void addClass(String tcClass);

    /**
     * Removes the specified class to the control
     * @param tcClass the class to remove
     */
    void removeClass(String tcClass);

    /**
     * Checks if a control has the specified class
     * @param tcClass the class to check for
     * @return true if the control has the class
     */
    boolean hasClass(String tcClass);
    
    /**
     * Checks if any classes are defined on this control
     * @return the classes defined on this control
     */
    boolean hasClasses();
    
    /**
     * Gets the full list of classes applied to this control
     * @return the list of classes
     */
    List<String> getClasses();

    /**
     * Clears all the classes on this control
     */
    void clearClasses();

    /**
     * Gets the window this control is sitting on, if there is one
     * @return the window this control is on
     */
    public IWindow getParentWindow();

    /**
     * Gets the location of this control in screen coordinates
     * @return the location of the control in screen coordinates
     */
    Point getScreenCoordinates();

    /**
     * Sets the font size for this control
     * @param tnSize the new font size
     */
    void setFontSize(float tnSize);
    
    /**
     * Gets the font size for this control
     * @return the font size
     */
    float getFontSize();
    
    /**
     * Gets the Styling for this control
     * @return the control style
     */
    ControlStyle getStyle();
    
    

    

}
