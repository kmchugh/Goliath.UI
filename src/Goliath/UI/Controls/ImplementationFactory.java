/* =========================================================
 * ImplementationFactory.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 *
 * Description
 * --------------------------------------------------------
 * The Implementation Factory loads all ControlImplemntations
 * and creates the correct implementation based on the control
 * that is being created
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/


package Goliath.UI.Controls;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.DynamicCode.Java;
import Goliath.Interfaces.UI.Controls.IControl;
import java.util.List;

/**
 *
 * @author kmchugh
 */
public class ImplementationFactory
{
    private static HashTable<Class, HashTable<ControlImplementationType, ControlImplementation>> g_oImplementations;
    private static HashTable<ControlImplementationType, ControlImplementation> g_oDefaultImplementations;
    private static boolean g_lLoaded;


    /**
     * Gets the implementation for the specified control
     * @param <T> Can only load implementations for controls from the goliath framework
     * @param toControl the control to get the implementation for
     * @return the control implementation for the control
     */
    public static <T extends IControl> ControlImplementation getImplementation(T toControl)
    {
        if (g_oImplementations == null)
        {
            loadImplementations();
        }

        Class loControlClass = toControl.getClass();

        while (!g_lLoaded)
        {
            try
            {
                Thread.sleep(50);
            }
            catch (Throwable ex)
            {}
        }
        
        if (!g_oImplementations.containsKey(loControlClass) || !g_oImplementations.get(loControlClass).containsKey(toControl.getImplementationType()))
        {
            int lnDistance = 99999;
            Class loUsableClass = null;
            for (Class loClass : g_oImplementations.keySet())
            {
                int lnNewDistance = Java.getPropinquity(loControlClass, loClass);
                if (lnNewDistance < lnDistance && lnNewDistance > -1)
                {
                    loUsableClass = loClass;
                    lnDistance = lnNewDistance;
                    // If the distance is zero, the classes can not be closer, so break
                    if (lnNewDistance == 0)
                    {
                        break;
                    }
                }
            }
            if (loUsableClass != null)
            {
                g_oImplementations.put(loControlClass, new HashTable<ControlImplementationType, ControlImplementation>());
                g_oImplementations.get(loControlClass).put(toControl.getImplementationType(), g_oImplementations.get(loUsableClass).get(toControl.getImplementationType()));
            }
        }

        return (g_oImplementations.containsKey(loControlClass) && g_oImplementations.get(loControlClass).containsKey(toControl.getImplementationType()))
                ? g_oImplementations.get(loControlClass).get(toControl.getImplementationType())
                : g_oDefaultImplementations.get(toControl.getImplementationType());
    }

    /**
     * Loads all of the implementations available
     */
    private static void loadImplementations()
    {
        List<Class<ControlImplementation>> loImplementations = Application.getInstance().getObjectCache().getClasses(ControlImplementation.class);
        g_oImplementations = new HashTable<Class, HashTable<ControlImplementationType, ControlImplementation>>();

        for (Class<ControlImplementation> loClass : loImplementations)
        {
            try
            {
                ControlImplementation loImpl = loClass.newInstance();
                if (loImpl.getSupportedClass().equals(Control.class))
                {
                    for (Object loType : loImpl.getSupportedTypes())
                    {
                        if (g_oDefaultImplementations == null)
                        {
                            g_oDefaultImplementations = new HashTable<ControlImplementationType, ControlImplementation>();
                        }
                        g_oDefaultImplementations.put((ControlImplementationType)loType, loImpl);
                    }
                }
                else
                {
                    if (!g_oImplementations.containsKey(loImpl.getSupportedClass()))
                    {
                        g_oImplementations.put(loImpl.getSupportedClass(), new HashTable<ControlImplementationType, ControlImplementation>());
                    }
                    for (Object loType : loImpl.getSupportedTypes())
                    {
                        g_oImplementations.get(loImpl.getSupportedClass()).put((ControlImplementationType)loType, loImpl);
                    }
                }
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);

            }
        }
        g_lLoaded = true;
    }

}
