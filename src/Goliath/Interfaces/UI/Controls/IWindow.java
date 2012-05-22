/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls;

import Goliath.UI.Views.View;

/**
 *
 * @author kmchugh
 */
public interface IWindow
        extends IContainer
{
    /**
     * Gets the title of the window
     * @return
     */
    String getTitle();

    /**
     * Sets the new window title
     * @param tcTitle the new title
     */
    void setTitle(String tcTitle);

    /**
     * Closes the window
     */
    void close();

    /**
     * Checks if this window draws all of its chrome as well
     * @return true if the chrome is drawn, false if not
     */
    boolean getShowChrome();

    /**
     * Sets if this windows should draw it's chrome or not
     * @param tlShow true to draw the chrome
     */
    void setShowChrome(boolean tlShow);

    /**
     * Sets if the window should always be on top of other windows
     * @param tlOnTop true if it should always be on top
     */
    void setAlwaysOnTop(boolean tlOnTop);


    /**
     * Checks if this window will always be displayed on top of other windows
     * @return true if it is always to be displayed on top of other windows.
     */
    boolean getAlwaysOnTop();
    
    /**
     * Checks if this window is a modal window or not
     * @return true if modal
     */
    boolean getModal();

    /**
     * Sets if this window should be modal
     * @param tlModal true if modal, false otherwise
     */
    void setModal(boolean tlModal);

    /**
     * Gets the view that is contained by this window, if there is no view, this
     * will return null
     * @return the view for this window
     */
    View getView();

    /**
     * Sets the window view to this view, this is a shortcut to adding the view through the add control method
     * @param toView the view to set
     */
    void setView(View toView);

    /**
     * Checks if this window uses custom chrome or the default OS chrome
     * @return true if this uses custom chrome
     */
    boolean usesCustomChrome();

    /**
     * Sets the window up to use custom chrome rendering
     * @param tlUseCustom true to use custom chrome
     */
    void useCustomChrome(boolean tlUseCustom);

    boolean minimise();

    boolean isMinimised();

    boolean maximise();

    boolean isMaximised();

    boolean restore();

    boolean isNormal();
}
