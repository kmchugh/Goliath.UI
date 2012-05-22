/* ========================================================
 * GridLayoutManager.java
 *
 * Author:      kmchugh
 * Created:     Aug 3, 2010, 6:44:37 PM
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

package Goliath.UI.Controls.Layouts;

import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Graphics.Dimension;
import Goliath.Graphics.Point;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 3, 2010
 * @author      kmchugh
**/
public class GridLayoutManager extends LayoutManager<IContainer>
{
    private HashTable<IContainer, Integer> m_oRows;
    private HashTable<IContainer, Integer> m_oColumns;

    /**
     * Creates a new instance of the grid layout
     */
    public GridLayoutManager()
    {
    }

    /**
     * Gets the number of rows that have been specified for the container
     * @param toControl the container to get the number of rows for
     * @return the number of rows for the container, 0 if not specified
     */
    public int getRows(IContainer toControl)
    {
        return m_oRows == null || !m_oRows.containsKey(toControl) ? 0 : m_oRows.get(toControl);
    }

    /**
     * Gets the number of columns that have been specified for the container
     * @param toControl the container to get the number of columns for
     * @return the number of columns for the container, 0 if not specified
     */
    public int getColumns(IContainer toControl)
    {
        return m_oColumns == null || !m_oColumns.containsKey(toControl) ? 0 : m_oColumns.get(toControl);
    }

    /**
     * Sets the number of rows that will be used to render the specified container
     * @param toControl the container to set the rows for
     * @param tnRows the number of rows
     */
    public void setRows(IContainer toControl, int tnRows)
    {
        tnRows = tnRows < 0 ? 0 : tnRows;
        if (m_oRows == null)
        {
            m_oRows = new HashTable<IContainer, Integer>();
        }
        Integer lnRows = m_oRows.put(toControl, tnRows);
        if (lnRows != null && lnRows.intValue() != tnRows)
        {
            toControl.invalidate();
        }
    }

    /**
     * Sets the number of columns that will be used to render the specified container
     * @param toControl the container to set the columns for
     * @param tnRows the number of columns
     */
    public void setColumns(IContainer toControl, int tnColumns)
    {
        tnColumns = tnColumns < 0 ? 0 : tnColumns;
        if (m_oColumns == null)
        {
            m_oColumns = new HashTable<IContainer, Integer>();
        }
        Integer lnColumns = m_oColumns.put(toControl, tnColumns);
        if (lnColumns != null && lnColumns.intValue() != tnColumns)
        {
            toControl.invalidate();
        }
    }

    /**
     * Removes the controls from the settings collections
     * @param toContainer the container that has been removed
     */
    @Override
    protected void onControlRemoved(IContainer toContainer)
    {
        if (m_oRows != null && m_oRows.containsKey(toContainer))
        {
            m_oRows.remove(toContainer);
        }
        if (m_oColumns != null && m_oColumns.containsKey(toContainer))
        {
            m_oColumns.remove(toContainer);
        }
    }
    
    

    /**
     * Loops through each child and lays them out
     */
    @Override
    protected void layoutChildren(IContainer toContainer)
    {
        float lnHeight = 0;
        float lnWidth = 0;

        List<IControl> loChildren = toContainer.getChildren();

        Dimension loSize = toContainer.getContentSize();

        int lnColumns = getColumnCount(toContainer, loChildren);
        int lnRows = getRowCount(toContainer, loChildren);
        
        lnHeight = loSize.getHeight() / lnRows;
        lnWidth = loSize.getWidth() / lnColumns;

        int lnCurrentColumn = 0;
        float lnX = 0;
        float lnY = 0;

        // Set the location and position for all the controls
        for (IControl loControl : loChildren)
        {
            setContainedControlSize(toContainer, loControl, lnWidth, lnHeight);
            setContainedControlLocation(toContainer, loControl,  lnX, lnY);

            Dimension loActualSize = loControl.getSize();

            // Note: We shouldn't update lnX with loLocation.getX() because it has been added with margin, padding and border.
            // When we pass the lnX to the next control, it'll increase the location with another ste of margin, padding and border.
//            Point loLocation = loControl.getLocation();
//            lnX = loLocation.getX() + loActualSize.getWidth();
//            lnY = loLocation.getY();
            lnX += loActualSize.getWidth();

            lnCurrentColumn++;
            if (lnCurrentColumn >= lnColumns)
            {
                lnX = 0;
//                lnY = loLocation.getY() + loActualSize.getHeight();
                lnY += loActualSize.getHeight();
                lnCurrentColumn = 0;
            }
        }
    }

    /**
     * Helper function to get the number of rows that should be used to calculate sizes
     * @param toChildren
     * @return the number of rows
     */
    private int getRowCount(IContainer toContainer, List<IControl> toChildren)
    {
        return getRows(toContainer) <= 0 ?
                    getColumns(toContainer) <= 0 ? 1 :
                    (int)Math.ceil(toChildren.size() / (float)getColumns(toContainer))
                : getRows(toContainer);
    }

    /**
     * Helper function to get the number of columns that should be used to calculate sizes
     * @param toChildren
     * @return the number of columns
     */
    private int getColumnCount(IContainer toContainer, List<IControl> toChildren)
    {
        return getColumns(toContainer) <= 0 ?
                    getRows(toContainer) <= 0 ? toChildren.size() :
                    (int)Math.ceil(toChildren.size() / (float)getRows(toContainer))
                : getColumns(toContainer);
    }

    /**
     * Calculates the size this control should be based on it's contents
     * @return the size the control should be
     */
    @Override
    protected Dimension onCalculateDimension(IContainer toContainer, Dimension toContainerSize)
    {
        float lnHeight = 0;
        float lnWidth = 0;

        List<IControl> loChildren = toContainer.getChildren();

        // Find the max dimension for all of the controls
        for (IControl loControl : loChildren)
        {
            Dimension loSize = getContainedControlDimensions(loControl);

            lnHeight = Math.max(lnHeight, loSize.getHeight());
            lnWidth = Math.max(lnWidth, loSize.getWidth());
        }
        
        lnWidth = lnWidth * getColumnCount(toContainer, loChildren);
        
        lnHeight = lnHeight * getRowCount(toContainer, loChildren);

        return new Dimension(lnWidth, lnHeight);
    }
}
