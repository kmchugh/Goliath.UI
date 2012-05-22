/* =========================================================
 * ControlNameExistsException.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:13:40
 * 
 * Description
 * --------------------------------------------------------
 * Used when a control is added to a container where that 
 * controls name already exists in the container
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Exceptions;

/**
 * Used when a control is added to a container where that 
 * controls name already exists in the container
 *
 * @version     1.0 28-Jan-2008
 * @author      kmchugh
**/
public class ControlNameExistsException extends Goliath.Exceptions.UncheckedException
{
    
     /**
    * Creates a new instance of ControlNameExistsException
    *
    * @param tcMessage   The error message
    */
    public ControlNameExistsException(String tcMessage)
    {
        super(tcMessage);
    }

    /**
    * Creates a new instance of ControlNameExistsException
    *
    * @param tcMessage   The error message
    * @param toInnerException The inner exception
    */
    public ControlNameExistsException(String tcMessage, java.lang.Exception toInnerException)
    {
        super(tcMessage, toInnerException);
    }
}
