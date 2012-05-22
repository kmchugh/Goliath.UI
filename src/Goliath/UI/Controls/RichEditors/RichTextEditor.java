/* ========================================================
 * RichTextEditor.java
 *
 * Author:      kenmchugh
 * Created:     Dec 14, 2010, 1:44:41 PM
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
import Goliath.Constants.EventType;
import Goliath.Constants.MimeType;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.UI.Controls.Layouts.TabControlLayoutManager;
import Goliath.Graphics.Constants.Position;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.Implementations.IValuedControlImpl;
import Goliath.UI.Controls.TextArea;
import Goliath.UI.Controls.UserControls.FormattingDefinition;
import Goliath.UI.Controls.UserControls.TabbedControl;
import Goliath.UI.Events.DataChangeType;
import Goliath.UI.Events.DataChangedEvent;
import Goliath.UI.Constants.UIEventType;
import org.w3c.dom.Document;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 14, 2010
 * @author      kenmchugh
 **/
public class RichTextEditor extends RichEditor<String, Document>
{

    private TextArea m_oTextArea;
    private TextArea m_oSourceArea;
    private TabbedControl m_oTabControl;
    private StringBuilder m_oBuilder;

    private static String VIEW_PANEL = "VIEW";
    private static String SOURCE_PANEL = "SOURCE";

    /**
     * Creates a new instance of the rich text editor
     */
    public RichTextEditor()
    {
        super(MimeType.TEXT_HTML());
        initialiseComponent();
    }

    public RichTextEditor(boolean tlShowEditor)
    {
        super(tlShowEditor, MimeType.TEXT_HTML());
        initialiseComponent();
    }

    /**
     * Sets up the component on creation
     */
    private void initialiseComponent()
    {
        // Listen for data changes so we can update the text areas
        this.addEventListener(UIEventType.ONCHANGED(), Delegate.build(this, "onRawDataChanged"));

        // Listen for changes on both text areas
        getSourceArea().addEventListener(EventType.ONCHANGED(), Delegate.build(this, "onSourceDataChanged"));
        getTextArea().addEventListener(EventType.ONCHANGED(), Delegate.build(this, "onViewDataChanged"));

        // Finally listen for the cursor position changes (only on the Data)
        getTextArea().addEventListener(UIEventType.ONCARETCHANGED(), Delegate.build(this, "onCaretChanged"));

    }

    /**
     * Creates the content area with the actual editors
     * @param toContainer the container to create the content in
     */
    @Override
    protected void onPopulateEditor(IContainer toContainer)
    {
        m_oTabControl = new TabbedControl();
        //TabControlLayoutManager loManager = new TabControlLayoutManager();
        //loManager.setTabAlignment(Position.BOTTOM_CENTER());
        m_oTabControl.setLayoutManager(TabControlLayoutManager.class);
        m_oTabControl.setMaxSelectable(1);

        IContainer loPanel = m_oTabControl.createPanel(VIEW_PANEL, "View");
        loPanel.addControl(getTextArea());

        loPanel = m_oTabControl.createPanel(SOURCE_PANEL, "Source");
        loPanel.addControl(getSourceArea());

        // Make sure the view tab is selected
        m_oTabControl.selectPanel(VIEW_PANEL);
        m_oTabControl.addEventListener(UIEventType.ONSELECTIONCHANGED(), Delegate.build(this, "onPanelChanged"));

        toContainer.addControl(m_oTabControl);
    }

    @Override
    protected EditorPane onGetEditorPane()
    {
        return new TextEditorPane(this);
    }

    @Override
    protected long getRawDataLength()
    {
        String lcData = getRawData();
        return lcData == null ? 0 : lcData.length();
    }

    /**
     * Event occurs when the internal data has been changed
     * @param toEvent the event data
     */
    private void onRawDataChanged(Event<RichTextEditor> toEvent)
    {
        if (m_oTabControl.getSelectedPanels().contains(SOURCE_PANEL))
        {
            getSourceArea().suppressEvents(true);
            getSourceArea().setValue(getFormattedData(getRawData()));
            getSourceArea().suppressEvents(false);
        }
        else
        {
            getTextArea().suppressEvents(true);
            getTextArea().setValue(getFormattedData(getRawData()));
            getTextArea().suppressEvents(false);
        }
    }

    /**
     * Event occurs when the source view data changes
     * @param toEvent the event data
     */
    private void onSourceDataChanged(DataChangedEvent<String> toEvent)
    {
        //setValue(Goliath.XML.Utilities.toXML(getSourceArea().getValue()));
    }

    /**
     * Event occurs when the data view data changes
     * @param toEvent the event data
     */
    private void onViewDataChanged(DataChangedEvent<String> toEvent)
    {
        if (toEvent.getType().equals(DataChangeType.DELETION()))
        {
            removeAt(toEvent.getStartIndex(), toEvent.getChangeLength());
        }
        else if (toEvent.getType().equals(DataChangeType.INSERTION()))
        {
            insertAt(toEvent.getStartIndex(), toEvent.getData());
        }
        else
        {
            replaceAt(toEvent.getStartIndex(), toEvent.getChangeLength(), toEvent.getData());
        }
    }

    /**
     * Event occurs when the caret view data changes
     * @param toEvent the event data
     */
    private void onCaretChanged(Event<TextArea> toEvent)
    {
        // Update the caret
        boolean llEvent = clearSelection();

        long lnStart = toEvent.getTarget().getSelectionStart();
        long lnEnd = toEvent.getTarget().getSelectionEnd();

        setCurrentIndex(lnEnd);
        if (lnStart != lnEnd)
        {
            // TODO: Update this to allow multiple selection areas
            llEvent = addSelection(lnStart, lnEnd) || llEvent;
        }

        if (llEvent)
        {
            fireEvent(UIEventType.ONSELECTIONCHANGED());
        }
    }

    /**
     * update Source data Area
     */
    private void updateSourceData()
    {

        boolean llTextEventsSuppressed = getTextArea().areEventsSuppressed();
        boolean llEventsSuppressed = areEventsSuppressed();
        getTextArea().suppressEvents(true);
        suppressEvents(true);
        getSourceArea().setValue(parseData(getValue()));
        getTextArea().suppressEvents(llTextEventsSuppressed);
        suppressEvents(llEventsSuppressed);
    }

    /**
     * update view data area
     */
    private void updateViewData()
    {
        boolean llTextEventsSuppressed = getTextArea().areEventsSuppressed();
        boolean llEventsSuppressed = areEventsSuppressed();
        getTextArea().suppressEvents(true);
        suppressEvents(true);
        getTextArea().setValue(getFormattedData(getRawData()).toString());
        getTextArea().suppressEvents(llTextEventsSuppressed);
        suppressEvents(llEventsSuppressed);
    }

    /**
     * Event occurs when the tab controls selected panel changes
     * @param toEvent the event data
     */
    private void onPanelChanged(Event<TabbedControl> toEvent)
    {
        if (toEvent.getTarget().isPanelSelected(SOURCE_PANEL))
        {
            updateSourceData();
        }
        if (toEvent.getTarget().isPanelSelected(VIEW_PANEL))
        {
            updateViewData();
        }
    }

    /**
     * Helper function to get the control as a IValuedControlImpl
     * @return the Control implementation
     */
    protected IValuedControlImpl<String> getValuedControlImplementation()
    {
        return (IValuedControlImpl<String>) getImplementation();
    }

    /**
     * Helper function for getting the text area, if it does not exist, it will be created here
     * @return the text area
     */
    private TextArea getTextArea()
    {
        if (m_oTextArea == null)
        {
            m_oTextArea = new TextArea();
            m_oTextArea.setRichText(true);

        }
        return m_oTextArea;
    }

    /**
     * Helper function for getting the source area, if it does not exist, it will be created here
     * @return the text area
     */
    private TextArea getSourceArea()
    {
        if (m_oSourceArea == null)
        {
            m_oSourceArea = new TextArea();
            m_oSourceArea.setEditable(false);
        }
        return m_oSourceArea;
    }

    private String getFormattedData(String toRawData)
    {
        StringBuilder loBuilder = new StringBuilder("<html>   <head>    </head>   <body>     <p style=\"margin-top: 0\">");

            if (!hasFormatting())
            {
                loBuilder.append(toRawData);
            }
            else
            {
                List<FormattingDefinition> loStarted = new List<FormattingDefinition>();
                HashTable<String, Long> loIndex = new HashTable<String, Long>();
                HashTable<String, List<FormattingDefinition>> loFormats = getFormatting();
                for (long i=0, lnLength = toRawData.length(); i<lnLength; i++)
                {
                    // Loop through all of the formatting to see if any starts here
                    for (String lcKey : loFormats.keySet())
                    {
                        List<FormattingDefinition> loDefs = loFormats.get(lcKey);

                        long lnIndex = Goliath.Utilities.isNull(loIndex.get(lcKey), 0L);
                        if (lnIndex < loDefs.size())
                        {
                            for (long j=lnIndex, lnCount = loDefs.size(); j<lnCount; j++)
                            {
                                FormattingDefinition loDef = loDefs.get((int)j);
                                if (loDef.getStartIndex() == i && loDef.getEndIndex() > i)
                                {
                                    // Found a format that needs to be applied
                                    loStarted.add(loDef);
                                    loIndex.put(lcKey, i);

                                    loBuilder.append("<");
                                    loBuilder.append(getTagNameFor(lcKey));
                                    loBuilder.append(">");
                                }
                            }
                        }
                    }

                    // Look for any ending formats
                    for (int j=loStarted.size()-1, lnCount=0; j >= lnCount; j--)
                    {
                        FormattingDefinition loDef = loStarted.get(j);
                        if (loDef.getEndIndex() ==i)
                        {
                            loStarted.remove(loDef);
                            loBuilder.append("</");
                            loBuilder.append(removeAttributesWhileClosingTag(getTagNameFor(loDef.getFormatType())));
                            loBuilder.append(">");
                        }
                    }

                    loBuilder.append(toRawData.charAt((int)i));
                }
            }

            loBuilder.append("</p>   </body> </html>");
            
            return loBuilder.indexOf("\n") >= 0 ? loBuilder.toString().replaceAll("\n", "<br/>") : loBuilder.toString();
    }

    @Override
    protected Document formatRaw(String toRawData)
    {
        try
        {
            return Goliath.XML.Utilities.toXML(getFormattedData(toRawData).toString());
        }
        catch (Throwable ex)
        {
            return null;
        }
    }

        /**
     * get the Tag name for style
     * @param tcType
     * @return
     */
    private String getTagNameFor(String tcType)
    {
        HashTable<String,String> loGetTag=TextEditorPane.getTagName();
        if(loGetTag.get(tcType)!=null)
            return loGetTag.get(tcType);
        else
            return "H1";

    }
    /**
     * remove tag attributes
     * @param tcWholeTag
     * @return
     */
    private String removeAttributesWhileClosingTag(String tcWholeTag)
    {
        String lcTag = TextEditorPane.removeAttributes(tcWholeTag);
        return lcTag;
    }

    /**
     * Parse the Document data to String
     * @param toData
     * @return
     */
    @Override
    protected String parseData(Document toData)
    {
        return Goliath.XML.Utilities.toString(toData);
    }

    @Override
    protected void onInsertAt(long tnStartIndex, String toData)
    {
        boolean llTextEventsSuppressed = getTextArea().areEventsSuppressed();
        boolean llEventsSuppressed = areEventsSuppressed();
        getTextArea().suppressEvents(true);
        suppressEvents(true);
        if (m_oBuilder == null)
        {
            m_oBuilder = new StringBuilder();
        }
        m_oBuilder.insert((int)tnStartIndex, toData);
        super.setRawData(m_oBuilder.toString());
        suppressEvents(llEventsSuppressed);
        getTextArea().suppressEvents(llTextEventsSuppressed);


        //addToFormatting(tnStartIndex, toData.length());
    }

    @Override
    protected void onRemoveAt(long tnStartIndex, long tnLength)
    {
        boolean llTextEventsSuppressed = getTextArea().areEventsSuppressed();
        boolean llEventsSuppressed = areEventsSuppressed();
        getTextArea().suppressEvents(true);
        suppressEvents(true);

        if (m_oBuilder != null)
        {
            m_oBuilder.delete((int)tnStartIndex, (int)(tnStartIndex + tnLength));
            super.setRawData(m_oBuilder.toString());

            //removeFromFormatting(tnStartIndex, tnLength);
        }
        super.setRawData(m_oBuilder.toString());
        suppressEvents(llEventsSuppressed);
        getTextArea().suppressEvents(llTextEventsSuppressed);
    }
    
}
