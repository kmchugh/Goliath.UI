/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.UI.Controls.Implementations;

/**
 *
 * @author kmchugh
 */
public interface IWindowImpl extends IContainerImpl
{
    /**
     * Sends the window to the front, making it the top level window
     * @param toControl the window implementation
     */
    void sendToFront(IImplementedControl toControl);
    
    /**
     * Sends the window to the back, making it the bottom level window
     * @param toControl the window implementation
     */
    void sendToBack(IImplementedControl toControl);
    
    void setTitle(String tcTitle, IImplementedControl toControl);
    String getTitle(IImplementedControl toControl);

    void close(IImplementedControl toControl);

    void setShowChrome(boolean tlShow, IImplementedControl toControl);

    boolean getShowChrome(IImplementedControl toControl);

    void setAlwaysOnTop(boolean tlOnTop, IImplementedControl toControl);

    boolean getAlwaysOnTop(IImplementedControl toControl);

    void minimise(IImplementedControl toControl);

    boolean isMinimised(IImplementedControl toControl);

    void maximise(IImplementedControl toControl);

    boolean isMaximised(IImplementedControl toControl);

    void restore(IImplementedControl toControl);

    boolean isNormal(IImplementedControl toControl);

    

}
