/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.Commands;

import Goliath.Arguments.Arguments;
import Goliath.Collections.List;

/**
 *
 * @author kenmchugh
 */
public interface IContextCommand<A extends Arguments, T> extends Goliath.Interfaces.Commands.ICommand<A, T>
{
    List<String> getContexts();
}
