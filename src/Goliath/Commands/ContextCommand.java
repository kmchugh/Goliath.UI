/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Commands;

import Goliath.Applications.Application;
import Goliath.Arguments.Arguments;
import Goliath.Collections.List;
import Goliath.Interfaces.ISession;
import Goliath.Security.User;
import Goliath.Session;

/**
 *
 * @param <A>
 * @param <V>
 * @author kenmchugh
 */
public abstract class ContextCommand<A extends Arguments, V> extends Goliath.Commands.Command<A, V>
        implements Goliath.Interfaces.Commands.IContextCommand<A, V>
{
    public ContextCommand()
    {
        super(false);
    }


    @Goliath.Annotations.NotProperty
    @Override
    public final List<String> getContexts()
    {
        return this.onGetContexts();
    }

    /**
     * Gets the first context in the list of contexts
     * @return
     */
    @Goliath.Annotations.NotProperty
    public final String getContext()
    {
        List<String> loList = getContexts();
        if (loList.size() > 0)
        {
            return loList.get(0);
        }
        return null;
    }
    
    protected List<String> onGetContexts()
    {
        List<String> loList = new List<String>(1);
        loList.add(onGetContext());
        return loList;
    }
    
    protected abstract String onGetContext();

    protected boolean hasAuthenticatedContext()
    {
        return false;
    }

    protected boolean isAuthenticatedUser()
    {
        ISession loSession = Session.getCurrentSession();
        if (!loSession.isAuthenticated())
        {
            return false;
        }

        // We also need to check if this is the anonymous user
        User loUser = loSession.getUser();
        return !loUser.isMemberOf(Application.getInstance().getSecurityManager().getGroup(Goliath.Security.GroupType.ANONYMOUS().getValue()));
    }
}