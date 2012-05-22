/* ========================================================
 * ControlManager.java
 *
 * Author:      archana
 * Created:     Aug 16, 2011, 3:44:34 PM
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

import Goliath.ChildCountChangedEvent;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Constants.EventType;
import Goliath.Delegate;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.Interfaces.IDelegate;
import Goliath.Event;
import Goliath.EventDispatcher;
import Goliath.Interfaces.IEventDispatcher;
import Goliath.UI.Constants.UIEventType;

/**
 * Global class that takes action over the controls that have been registered with the manager.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 16, 2011
 * @author      archana
 **/
public abstract class ControlManager extends Goliath.Object
        implements IEventDispatcher<EventType, Event>
{

    private HashTable<IControl, List<IControl>> m_oControlHandles;
    private List<IControl> m_oSuspendedList;
    private boolean m_lIsManagerSuspended;
    private EventDispatcher<EventType, Event> m_oEventDispatcher;
    private List<IControl> m_oRegisteredControls;
    private List<IControl> m_oActiveControls;
    private HashTable<String, IDelegate> m_oDelegates;
    private List<IControl> m_oActionControls;
    

    /**
     * Registers a control with the manager, returns true if the control was not registered before,
     * No op if the control was not previously registered
     * @param toRegister the control to register, this control also becomes the handle
     * @return true if the registered controls list changed
     */
    public final boolean registerControl(IControl toRegister)
    {
        return registerControl(toRegister, new List<IControl>(new IControl[]{toRegister}));
    }

    /**
     * Registers a control with the manager, returns true if the control was not registered before,
     * No op if the control was not previously registered
     * @param toRegister the control to register
     * @param toRegisterHandles the handles that can be used to manipulated the registered control
     * @return true if the registered controls list changed
     */
    public final synchronized boolean registerControl(IControl toRegister, List<IControl> toRegisterHandles)
    {
        boolean llReturn = false;
        if (m_oControlHandles == null)
        {
            m_oControlHandles = new HashTable<IControl, List<IControl>>();
        }

        for (IControl loControl : toRegisterHandles)
        {
            llReturn = registerControlHandle(toRegister, loControl) || llReturn;
        }
        return llReturn;
    }

    /**
     * Adds a control handle to the control specified
     * @param toRegisterControl the control to register
     * @param toHandle the new handle for the control
     * @return true if the collection has been changed
     */
    public final boolean registerControlHandle(IControl toRegisterControl, IControl toHandle)
    {
        boolean llReturn = false;
        if (m_oControlHandles != null)
        {
            if (!m_oControlHandles.containsKey(toHandle))
            {
                m_oControlHandles.put(toHandle, new List<IControl>(1));
            }

            if (!m_oControlHandles.get(toHandle).contains(toRegisterControl))
            {
                // The storage may seem a little backwards, but this is done for speed on the events
                // as the events will generally be accessing by the handle
                llReturn = m_oControlHandles.get(toHandle).add(toRegisterControl);
                // If a handle has been added then we need to add event listeners
                if (llReturn)
                {
                    addListeners(toHandle);
                    m_oRegisteredControls = null;
                    m_oActiveControls = null;
                    fireEvent(UIEventType.ONCHILDADDED(), new ChildCountChangedEvent<ControlManager, IControl>(this, toRegisterControl, true));
                }
            }
        }
        return llReturn;
    }
    
    /**
     * Unregisters the specified control
     * Returns true if the control existed and was removed, no op if the control does not exist within this manager
     * @param toUnregister the control to unregister
     * @return
     */
    public final boolean unRegisterControl(IControl toUnregister)
    {
        return unRegisterControl(toUnregister, new List<IControl>(new IControl[]{toUnregister}));
    }

    /**
     * Unregisters the specified control
     * Returns true if the control existed and was removed, no op if the control does not exist within this manager
     * @param toUnregister the control to unregister
     * @param toUnregisterHandles the handles that can be used to unregister the control
     * @return
     */
    private synchronized boolean unRegisterControl(IControl toUnregister, List<IControl> toUnregisterHandles)
    {
        boolean llReturn = false;

        for (IControl loControl : toUnregisterHandles)
        {
            llReturn = unRegisterControlHandle(toUnregister, loControl) || llReturn;
        }
        return llReturn;
    }

    /**
     * Unregisters the specified control handle
     * @param toUnregisterControl the control to unregister
     * @param toHandle the handle for the control to unregister
     * @return true if the collection has been changed
     */
    private synchronized boolean unRegisterControlHandle(IControl toUnregisterControl, IControl toHandle)
    {
        boolean llReturn = false;
        if (m_oControlHandles.containsKey(toHandle) && m_oControlHandles.get(toHandle).contains(toUnregisterControl))
        {
            llReturn = m_oControlHandles.get(toHandle).remove(toUnregisterControl);
            if (llReturn)
            {
                removeListeners(toHandle);
                resume(toUnregisterControl);
                m_oRegisteredControls = null;
                m_oActiveControls = null;
            }
            
            // If toUnregisteredControl is no longer a registered control, then fire an event
            if (!getRegisteredControls().contains(toUnregisterControl))
            {
                fireEvent(UIEventType.ONCHILDREMOVED(), new ChildCountChangedEvent<ControlManager, IControl>(this, toUnregisterControl, false));
            }

            // Do some clean up for memory
            if (m_oControlHandles.get(toHandle).size() == 0)
            {
                clearControlHandle(toHandle);
            }
        }
        return llReturn;
    }
    
    /**
     * Helper function for getting the specified delegate, if the delegate does not
     * exist, this will create a delegate and cache for future reference.  The created
     * delegate will expect a method named "on" + tcType.  For example, if getDelegate("MouseDown") is
     * called, then the delegate will expect a method with the following signature:
     * private void onMouseDown(Event<IControl> toEvent);
     * @return the specified delegate
     */
    public IDelegate<Event<IControl>> getDelegate(String tcType) 
    {
        if (m_oDelegates == null)
        {
            m_oDelegates = new HashTable<String, IDelegate>();
        }
        
        if (!m_oDelegates.containsKey(tcType.toLowerCase()))
        {
            m_oDelegates.put(tcType.toLowerCase(), Delegate.build(this, "on" + tcType));
        }
        return m_oDelegates.get(tcType.toLowerCase());
    }
    
    /**
     * Sets the "Action controls" or the controls that this manager is currently acting over
     * @param toControls The controls this manager is acting over
     */
    public void setActionControls(List<IControl> toControls)
    {
        m_oActionControls = toControls;
    }
    
    /**
     * Clears the controls that this manager is acting on
     */
    public void clearActionControls()
    {
        m_oActionControls = null;
    }
    
    /**
     * Gets the list of controls that this manager is acting on
     * @return the list of controls
     */
    public List<IControl> getActionControls()
    {
        return m_oActionControls == null ? null : new List(m_oActionControls);
    }
    
    /**
     * Helper function to add the event listeners to the handle
     * @param toControl the handle to all to
     */
    protected abstract void addListeners(IControl toControl);

    /**
     * Helper function to remove the event listeners from the handle
     * @param toHandle the toControl to remove the listeners from
     */
    protected abstract void removeListeners(IControl toControl);

    /**
     * Clears the handle, stops the control being a handle
     * @param toHandle the handle to clear
     * @return true if the internal collections were changed due to this call
     */
    public synchronized boolean clearControlHandle(IControl toHandle)
    {
        boolean llReturn = false;
        if (m_oControlHandles != null && m_oControlHandles.containsKey(toHandle))
        {
            llReturn = m_oControlHandles.remove(toHandle) != null;
            if (llReturn)
            {
                removeListeners(toHandle);
            }
        }
        return llReturn;
    }

    /**
     * Checks if the specified control is registered
     * @param toControl the control to check
     * @return true if this control is registered, false otherwise
     */
    public final boolean isRegistered(IControl toControl)
    {
        boolean llReturn = false;
        if (m_oControlHandles != null)
        {
            for (List<IControl> loControlList : m_oControlHandles.values())
            {
                for (IControl loResizeable : loControlList)
                {
                    if (loResizeable == toControl)
                    {
                        // return as soon as we find the control
                        return true;
                    }
                }
            }
        }
        return llReturn;
    }
    
    /**
     * Checks if all of the controls specified are registered to this manager, this
     * will return false if there are no controls in the list
     * @param toControls the controls to check
     * @return false if any of the controls are not registered
     */
    public final boolean isRegistered(List<IControl> toControls)
    {
        for (IControl loControl : toControls)
        {
            if (!isRegistered(loControl))
            {
                return false;
            }
        }
        return toControls.size() > 0 ? true : false;
    }

    /**
     * Check if this manager is suspended. If yes then all control is not resizeable.
     * @return true of the manager is suspended, false otherwise
     */
    public final boolean isManagerSuspended()
    {
        return m_lIsManagerSuspended;
    }

    /**
     * Suspends the manager so that all control is not resizeable
     */
    public final void suspendManager()
    {
        m_lIsManagerSuspended = true;
    }

    /**
     * Resume the manager so that all control is resizeable
     */
    public final void resumeManager()
    {
        m_lIsManagerSuspended = false;
    }
    
    /**
     * Suspends the action on the control
     * @param toControl the control to suspend
     */
    public final void suspend(IControl toControl)
    {
        if (isRegistered(toControl) && !getSuspendedControls().contains(toControl))
        {
            getSuspendedControls().add(toControl);
            onControlSuspended(toControl);
        }
    }
    
    /**
     * Suspends the action on the list of controls provided
     * @param toControls the list of controls
     */
    public final void suspend(List<IControl> toControls)
    {
        for(IControl loControl : toControls)
        {
            suspend(loControl);
        }
    }

    /**
     * Check if the given control is suspended from the action of this manager
     * @param toControl the control to check
     * @return true if the control is suspended, false otherwise
     */
    public final boolean isSuspended(IControl toControl)
    {
        return getSuspendedControls().contains(toControl);
    }
    
    /**
     * Checks if any of the controls specified are suspended, if there are no
     * controls in the list, this will return true
     * @param toControls the controls to check
     * @return true if any of the controls are suspended
     */
    public final boolean isSuspended(List<IControl> toControls)
    {
        for (IControl loControl : toControls)
        {
            if (isSuspended(loControl))
            {
                return true;
            }
        }
        return toControls.size() > 0 ? false : true;
    }

    /**
     * Resume the action on the control specified
     * @param toControl the control to resume
     */
    public final void resume(IControl toControl)
    {
        if (getSuspendedControls().contains(toControl))
        {
            getSuspendedControls().remove(toControl);
            onControlResumed(toControl);
        }
    }
    
    /**
     * Resumes the action on the list of controls specified
     * @param toControls the list of controls
     */
    public final void resume(List<IControl> toControls)
    {
        for(IControl loControl : toControls)
        {
            resume(loControl);
        }
    }
    
    /**
     * Gets the list of registered controls on this manager
     * @return the list of registered control handles
     */
    protected List<IControl> getRegisteredControls()
    {
        if (m_oRegisteredControls == null)
        {
            m_oRegisteredControls = new List<IControl>();
            synchronized(m_oRegisteredControls)
            {
                if (m_oControlHandles != null)
                {
                    for (IControl loHandle : m_oControlHandles.keySet())
                    {
                        List<IControl> loControls = m_oControlHandles.get(loHandle);
                        if (loControls != null)
                        {
                            for (IControl loRegistered : loControls)
                            {
                                if (!m_oRegisteredControls.contains(loRegistered))
                                {
                                    m_oRegisteredControls.add(loHandle);
                                }
                            }
                        }
                    }
                }
            }
        }
        return m_oRegisteredControls;
    }
    
    /**
     * Gets the list of registered controls for the specified handle.  This will be
     * and empty list if the control specified is not a handle
     * @param toHandle the handle to get the registered controls for
     * @return the list of controls that are registered using the specified handle
     */
    protected List<IControl> getRegisteredControls(IControl toHandle)
    {
        return isControlHandle(toHandle) ? m_oControlHandles.get(toHandle) : new List<IControl>(0);
    }

    /**
     * Gets the list of active controls (controls that are not suspended)
     * @return the list of controls that are not suspended
     */
    protected List<IControl> getActiveControls()
    {
        if (m_oActiveControls == null)
        {
            m_oActiveControls = new List<IControl>(getRegisteredControls());
            m_oActiveControls.removeAll(getSuspendedControls());
        }
        return m_oActiveControls;
    }

    /**
     * Helper function to get the list of suspended controls
     * @return the list of suspended controls
     */
    protected List<IControl> getSuspendedControls()
    {
        if (m_oSuspendedList == null)
        {
            m_oSuspendedList = new List<IControl>();
        }
        return m_oSuspendedList;
    }
    
    /**
     * Gets the handles for the specified control, this is a heavy function that does not
     * cache, so if using multiple times in a method, it is best to call once and store the
     * result
     * @param toControl the registered control to get the handles for
     * @return all of the handles for the registered control
     */
    protected List<IControl> getHandles(IControl toControl)
    {
        List<IControl> loReturn = new List<IControl>();
        if (m_oControlHandles != null && isRegistered(toControl))
        {
            List<IControl> loControls = new List<IControl>(m_oControlHandles.keySet());
            for (IControl loHandle : loControls)
            {
                List<IControl> loRegisteredControls = new List<IControl>(m_oControlHandles.get(loHandle));
                for (IControl loRegistered : loRegisteredControls)
                {
                    if (loRegistered == toControl)
                    {
                        loReturn.add(loHandle);
                        break;
                    }
                }
            }
        }
        return loReturn;
    }
    
    /**
     * Checks if the control is a handle for another control
     * @param toHandle the control to check
     * @return true if this control is a event handle
     */
    public boolean isControlHandle(IControl toHandle)
    {
        return m_oControlHandles != null && m_oControlHandles.containsKey(toHandle);
    }
    
    /**
     * Hook to allow interaction when a control has been suspended
     * @param toControl the suspended control
     */
    protected void onControlSuspended(IControl toControl)
    {
    }
    
    /**
     * Hook to allow interaction when a control has been resumed
     * @param toControl the control resumed
     */
    protected void onControlResumed(IControl toControl)
    {   
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /****
     * IEventDispatcher Implementation below here
     ****/
    

    

    @Override
    public void suppressEvents(boolean tlSuppress)
    {
        if (m_oEventDispatcher != null)
        {
            m_oEventDispatcher.suppressEvents(tlSuppress);
        }
    }

    @Override
    public boolean removeEventListener(EventType toEvent, IDelegate toCallback)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.removeEventListener(toEvent, toCallback) : false;
    }

    @Override
    public boolean hasEventsFor(EventType toEvent)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.hasEventsFor(toEvent) : false;
    }

    @Override
    public final void fireEvent(EventType toEventType, Event toEvent)
    {
        if (m_oEventDispatcher != null)
        {
            m_oEventDispatcher.fireEvent(toEventType, toEvent);
        }
    }

    @Override
    public boolean clearEventListeners(EventType toEvent)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.clearEventListeners(toEvent) : false;
    }

    @Override
    public boolean clearEventListeners()
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.clearEventListeners() : false;
    }

    @Override
    public boolean areEventsSuppressed()
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.areEventsSuppressed() : false;
    }

    @Override
    public boolean addEventListener(EventType toEvent, IDelegate toCallback)
    {
        if (m_oEventDispatcher == null)
        {
            m_oEventDispatcher = new EventDispatcher<EventType, Event>();
        }
        return m_oEventDispatcher.addEventListener(toEvent, toCallback);
    }
}
