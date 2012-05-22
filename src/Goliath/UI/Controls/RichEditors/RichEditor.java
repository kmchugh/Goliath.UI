/* ========================================================
 * RichEditor.java
 *
 * Author:      mmajumdar
 * Created:     Feb 25, 2011, 5:24:49 PM
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
package Goliath.UI.Controls.RichEditors;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Constants.MimeType;
import Goliath.Event;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IValueControl;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.Controls.Group;
import Goliath.UI.Controls.Label;
import Goliath.UI.Controls.Layouts.BorderLayoutConstants;
import Goliath.UI.Controls.Layouts.BorderLayoutManager;
import Goliath.UI.Controls.UserControls.FormattingDefinition;
import Goliath.UI.Constants.UIEventType;
import Goliath.UI.Views.View;

/**
 *
 * @author manamimajumdar
 */
public abstract class RichEditor<R, D> extends View
        implements IValueControl<D>
{
    private R m_oRawData;
    private D m_oUserData;

    private MimeType m_oMimeType;
    private boolean m_lShowEditorPane;
    private boolean m_lShowStatusBar;
    private boolean m_lShowEditor;

    private long m_nCurrentIndex;

    private Label m_oStatusBar;
    private EditorPane m_oEditorPane;
    private IContainer m_oEditor;

    private HashTable<String, List<FormattingDefinition>> m_oFormatting;
    private List<IndexedValue> m_oSelection;

    /**
     * Creates a new RichEditor that will edit documents with the
     * specified MimeType
     * @param toMimeType the mime type for this editor
     */
    public RichEditor(MimeType toMimeType)
    {
        super(BorderLayoutManager.class);
        // Mime type can not be null
        Goliath.Utilities.checkParameterNotNull("toMimeType", toMimeType);
        m_oMimeType = toMimeType;
        showEditorPane();
        showEditor();
        showStatusBar();
    }

    public RichEditor(boolean tlShowEditor, MimeType toMimeType)
    {
        super(BorderLayoutManager.class);
        // Mime type can not be null
        Goliath.Utilities.checkParameterNotNull("toMimeType", toMimeType);
        m_oMimeType = toMimeType;

        if (tlShowEditor)
        {
            showEditorPane();
        }

        showEditor();

        if (tlShowEditor)
        {
            showStatusBar();
        }
    }

    /**
     * Creates the content area with the actual editors
     * @param toContainer the container to create the content in
     */
    protected void onPopulateEditor(IContainer toContainer)
    {
        toContainer.addControl(getEditorPane());
    }


    /**
     * Sets the raw data for this editor
     * @param tcRawData The raw data
     */
    public final void setRawData(R tcRawData)
    {
        if ((m_oRawData != null && !m_oRawData.equals(tcRawData)) || m_oRawData == null && tcRawData != null)
        {
            m_oRawData=tcRawData;
            dataChanged();
        }
    }

    /**
     * Extracts the raw data from the editor
     * @return the raw data
     */
    public final R getRawData()
    {
        return m_oRawData;
    }

    /**
     * Adds the selection to the list
     * @param tnStart the start index
     * @param tnEnd the end index
     * @return true if the selection list is changed by this call
     */
    public final boolean addSelection(long tnStart, long tnEnd)
    {
        boolean llReturn = false;
        if (m_oSelection == null)
        {
            m_oSelection = new List<IndexedValue>();
        }

        IndexedValue loValue = new IndexedValue(tnStart, tnEnd);
        if (!m_oSelection.contains(loValue))
        {
            llReturn = m_oSelection.add(loValue);
            optimiseSelection();
        }
        return llReturn;
    }

    /**
     * Removes the specified area from the selection list
     * @param tnStart the start of the selection
     * @param tnEnd the end of the selection
     * @return true if the selection was modified as a result of this call
     */
    public final boolean removeSelection(long tnStart, long tnEnd)
    {
        // TODO: needs to be implemented
        return false;
    }

    /**
     * Gets the current length of the selection
     * @return the selection length
     */
    public final long getSelectionLength()
    {
        long lnReturn = 0;

        if (m_oSelection != null)
        {
            for (IndexedValue loValue : m_oSelection)
            {
                lnReturn += loValue.getLength();
            }
        }
        return lnReturn;
    }

    /**
     * Helper function for getting the status area, if it does not exist, it will be created here
     * @return the text area
     */
    private Label getStatusBar()
    {
        if (m_oStatusBar == null)
        {
            m_oStatusBar = new Label();
            m_oStatusBar.setMinSize(100, 20);
        }
        return m_oStatusBar;
    }

    /**
     * Helper function for getting the status area, if it does not exist, it will be created here
     * @return the text area
     */
    protected final EditorPane getEditorPane()
    {
        if (m_oEditorPane == null)
        {
            m_oEditorPane = onGetEditorPane();
        }
        return m_oEditorPane;
    }

    /**
     * Helper function for getting the status area, if it does not exist, it will be created here
     * @return the text area
     */
    public final IContainer getEditorContainer()
    {
        if (m_oEditor == null)
        {
            m_oEditor = new Group();
            onPopulateEditor(m_oEditor);
        }
        return m_oEditor;
    }

    protected abstract EditorPane onGetEditorPane();

    /**
     * Cleans up the selection list so there are no overlapping selections
     */
    private void optimiseSelection()
    {
        // TODO: Implement this
    }

    private void optimiseFormatting()
    {
        // TODO: Implement this


        dataChanged();
    }

    /**
     * Clears all of the selection values
     * @return true if the selection was modified due to this call
     */
    public boolean clearSelection()
    {
        if (m_oSelection == null)
        {
            return false;
        }
        m_oSelection = null;
        return true;
    }

    public List<IndexedValue> getSelection()
    {
        return m_oSelection;
    }

    /**
     * Shows the editor pane if it is hidden
     */
    public final void showEditorPane()
    {
        if (!m_lShowEditorPane)
        {
            m_lShowEditorPane = true;
            EditorPane loPane = getEditorPane();
            this.addControl(loPane, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.PAGE_START());
        }
    }

    /**
     * Shows the editor pane if it is hidden
     */
    public final void showEditor()
    {
        if (!m_lShowEditor)
        {
            m_lShowEditor = true;
            IContainer loEditor = getEditorContainer();
            this.addControl(loEditor, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.CENTER());
        }
    }

    /**
     * Shows the status bar if it is hidden
     */
    public final void showStatusBar()
    {
        if (!m_lShowStatusBar)
        {
            m_lShowStatusBar = true;
            Label loStatus = getStatusBar();
            this.addControl(loStatus, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.PAGE_END());
        }
    }

    /**
     * Hides the editor pane if it is shown
     */
    public final void hideEditor()
    {
        if (m_lShowEditor)
        {
            m_lShowEditor = false;
            if (m_oEditor != null)
            {
                m_oEditor.getParent().removeControl(m_oEditor);
            }
        }
    }

    /**
     * Hides the editor pane if it is shown
     */
    public final void hideEditorPane()
    {
        if (m_lShowEditorPane)
        {
            m_lShowEditorPane = false;
            if (m_oEditorPane != null)
            {
                m_oEditorPane.getParent().removeControl(m_oEditorPane);
            }
        }
    }

    /**
     * Hides the status bar if it is shown
     */
    public final void hideStatusBar()
    {
        if (m_lShowStatusBar)
        {
            m_lShowStatusBar = false;
            if (m_oStatusBar != null)
            {
                m_oStatusBar.getParent().removeControl(m_oStatusBar);
            }
        }
    }

    /**
     * Gets the current editing point of the data
     * @return the editing point for the data
     */
    public long getCurrentIndex()
    {
        return m_nCurrentIndex;
    }

    /**
     * Sets the current editing point of the data
     * @param tnIndex the new editing point of the data
     */
    public final void setCurrentIndex(long tnIndex)
    {
        if (m_nCurrentIndex != tnIndex && tnIndex >= 0 && tnIndex < getRawDataLength())
        {
            m_nCurrentIndex = tnIndex;
        }
    }


    /**
     * Sets the Data for this editor, this is the formatted data type
     * @param toData the formatted type
     */
    @Override
    public final void setValue(D toData)
    {
        // Parse the data so we can store the raw data type
        setRawData(parseData(toData));
    }

    /**
     * Converts the user data to the raw data
     * @param toData the user data
     * @return the raw parsed data
     */
    protected abstract R parseData(D toData);

    /**
     * Converts the raw data type into the user data type by formatting the raw
     * @param toRawData the raw data
     * @return the user data
     */
    protected abstract D formatRaw(R toRawData);

    /**
     * Gets the length of the raw data
     * @return the length of the raw data
     */
    protected abstract long getRawDataLength();

    /**
     * Should be called when ever somthing changes with the control that adjusts the
     * raw data as this will cause the cached user data to become invalid
     */
    private void dataChanged()
    {
        m_oUserData = null;
        fireEvent(UIEventType.ONCHANGED(), new Event<RichEditor<R, D>>(this));
    }

    /**
     * Gets the user data this is the formatted representation of the raw data
     * @return the user data
     */
    @Override
    public final D getValue()
    {
        if (m_oUserData == null)
        {
            m_oUserData = formatRaw(m_oRawData);
        }
        return m_oUserData;
    }

    /**
     * Gets the mime type of this editor
     * @return the mime type of the editor
     */
    public MimeType getMimeType()
    {
        return m_oMimeType;
    }

    /**
     * Inserts the specified data at the specified index
     * @param tnStartIndex the index to insert at
     * @param toData the data to insert
     */
    protected final void insertAt(long tnStartIndex, R toData)
    {
        onInsertAt(tnStartIndex, toData);
    }

    protected abstract void onInsertAt(long tnStartIndex, R toData);

    /**
     * Removes the specified length of data from the start index
     * @param tnStartIndex the start index
     * @param tnLength the length of the data to remove
     */
    protected final void removeAt(long tnStartIndex, long tnLength)
    {
        if (m_oRawData != null)
        {
            onRemoveAt(tnStartIndex, tnLength);
        }
    }

    protected abstract void onRemoveAt(long tnStartIndex, long tnLength);

    /**
     * Replaces the data from start index for tnLength with the data specified
     * @param tnStartIndex the index to start at
     * @param tnLength the length of the data to replace
     * @param toData the data to replace with
     */
    protected final void replaceAt(long tnStartIndex, long tnLength, R toData)
    {
        removeAt(tnStartIndex, tnLength);
        insertAt(tnStartIndex, toData);
    }


    public void applyFormattingToSelection(String tcFormat)
    {
        List<IndexedValue> loSelections = getSelection();
        if (loSelections != null && loSelections.size() > 0)
        {
            for (IndexedValue loValue : getSelection())
            {
                addFormatting(new FormattingDefinition(tcFormat, loValue.getStart(), loValue.getEnd()), false);
            }
            optimiseFormatting();
        }
    }

    protected boolean hasFormatting()
    {
        return m_oFormatting != null && m_oFormatting.size() > 0;
    }

    protected HashTable<String, List<FormattingDefinition>> getFormatting()
    {
        return m_oFormatting;
    }

    public boolean addFormatting(FormattingDefinition toFormat)
    {
        return addFormatting(toFormat, true);
    }



    public boolean addFormatting(FormattingDefinition toFormat, boolean tlOptimise)
    {
        boolean llReturn = false;
        if (m_oFormatting == null)
        {
            m_oFormatting = new HashTable<String, List<FormattingDefinition>>();
        }

        String lcKey = toFormat.getFormatType();

        if (!m_oFormatting.containsKey(lcKey))
        {
            m_oFormatting.put(lcKey, new List<FormattingDefinition>());
        }

        // Add the formatting by order of start index to make it easier to optimise
        List<FormattingDefinition> loFormats = m_oFormatting.get(lcKey);
        if (!loFormats.contains(toFormat))
        {
            if (loFormats.size() == 0)
            {
                llReturn = loFormats.add(toFormat);
            }
            else
            {
                for (int i=0, lnLength = loFormats.size(); i<lnLength; i++)
                {
                    if (loFormats.get(i).getStartIndex() >= toFormat.getStartIndex() || i == lnLength -1)
                    {
                        llReturn = loFormats.add(toFormat);
                    }
                }
            }
        }

        if (tlOptimise)
        {
            optimiseFormatting();
        }

        return llReturn;
    }

    /* protected void  removeFormatting(FormattingType loFormat)
    {

    }

    protected void  removeFormatting(FormattingType loFormat, int lnStartIndex,int lnEndIndex)
    {

    }*/

    protected void clearFormatting()
    {

    }

    protected void clearFormatting(int lnStartIndex,int lnEndIndex)
    {

    }


}
