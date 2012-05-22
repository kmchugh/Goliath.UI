/* ========================================================
 * EditorPane.java
 *
 * Author:      kmchugh
 * Created:     Feb 17, 2011, 11:53:46 AM
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
package Goliath.UI.Controls.UserControls;

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Exceptions.FileNotFoundException;
import Goliath.Graphics.Constants.Orientation;
import Goliath.Graphics.Font;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Constants.ControlLayoutProperty;
import Goliath.UI.Controls.Button;
import Goliath.UI.Controls.Combobox;
import Goliath.UI.Controls.Group;
import Goliath.UI.Controls.Label;
import Goliath.UI.Controls.Layouts.BorderLayoutConstants;
import Goliath.UI.Controls.Layouts.BorderLayoutManager;
import Goliath.UI.Controls.Layouts.FlowLayoutManager;
import Goliath.UI.Controls.Layouts.GridLayoutManager;
import Goliath.UI.Controls.TextArea;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Feb 17, 2011
 * @author      kmchugh
 **/
public class EditorView extends Group
{

    private TextArea m_oEditor;

    /**
     * Creates a new instance of EditorPane
     */
    public EditorView()
    {
        initialiseComponent();
    }

    private void initialiseComponent()
    {
        //this.setLayoutManager(new FlowLayoutManager(Orientation.HORIZONTAL()));
        this.setLayoutManager(FlowLayoutManager.class);


        // TODO: Make all of the editor bars plugins
        createEditorBars();

        // Creates all of the editor bars for this editor


    }

    /**
     * Template method for creating all of the editor bars for this editor
     */
    private void createEditorBars()
    {
        // Font bar
        addFontSizeBar();

        // Font decoration bar
        addFontDecorationBar();

        // Font script bar
        addFontScriptBar();

        // Font Colour bar
        addFontColourBar();

        // Alignment bar
        addAlignmentBar();

        // Stored Style bar
        addCustomStyleBar();

        // Indenting and list bar
        addIndentAndListBar();

        // insert bar
        addInsertBar();

    }

    private void addFontSizeBar()
    {
        Group loGroup = createEditorGroup();

        loGroup.addControl(new Combobox<String>(getFontList()));
        loGroup.addControl(new Combobox<String>(getFontSizeList()));

        addControl(loGroup);
    }

    private void addFontDecorationBar()
    {
        Group loGroup = createEditorGroup();

        loGroup.addControl(createNewButtons("./resources/images/BoldButton.png", "Bold"));
        loGroup.addControl(createNewButtons("./resources/images/UnderLineButton.png", "UnderLine"));
        loGroup.addControl(createNewButtons("./resources/images/ItalicButton.png", "Italic"));

        addControl(loGroup);
    }

    private void addFontScriptBar()
    {
        Group loGroup = createEditorGroup();

        loGroup.addControl(createNewButtons("./resources/images/BoldButton.png", "Superscript"));
        loGroup.addControl(createNewButtons("./resources/images/UnderLineButton.png", "Subscript"));

        addControl(loGroup);
    }

    private void addFontColourBar()
    {
        Group loGroup = createEditorGroup();

        loGroup.addControl(createNewButtons("./resources/images/ColorButton.png", "Color"));
        loGroup.addControl(createNewButtons("./resources/images/ColorButton.png", "BackgroundColor"));

        addControl(loGroup);
    }

    private void addAlignmentBar()
    {
        Group loGroup = createEditorGroup();

        loGroup.addControl(createNewButtons("./resources/images/LeftButton.png", "Left"));
        loGroup.addControl(createNewButtons("./resources/images/CenterButton.png", "Center"));
        loGroup.addControl(createNewButtons("./resources/images/RightButton.png", "Right"));
        loGroup.addControl(createNewButtons("./resources/images/JustifiedButton.png", "Justified"));

        loGroup.addControl(createNewButtons("./resources/images/BulletButton.png", "BulletList"));
        loGroup.addControl(createNewButtons("./resources/images/BulletButton.png", "NumberedList"));

        addControl(loGroup);
    }

    private void addCustomStyleBar()
    {
        Group loGroup = createEditorGroup();

        loGroup.addControl(new Combobox<String>(getStyleList()));

        addControl(loGroup);
    }

    private void addIndentAndListBar()
    {
        Group loGroup = createEditorGroup();

        loGroup.addControl(createNewButtons("./resources/images/BulletButton.png", "Indent"));
        loGroup.addControl(createNewButtons("./resources/images/BulletButton.png", "RemoveIndent"));

        loGroup.addControl(createNewButtons("./resources/images/BulletButton.png", "BulletList"));
        loGroup.addControl(createNewButtons("./resources/images/BulletButton.png", "NumberedList"));

        addControl(loGroup);
    }

    private void addInsertBar()
    {
        Group loGroup = createEditorGroup();

        for (String lcInsert : getInsertAddOnList())
        {
            loGroup.addControl(createNewButtons("./resources/images/LeftButton.png", lcInsert));
        }

        addControl(loGroup);
    }

    /**
     * Helper function for creating the editor groups
     * @param tnMinWidth the minimum width of the editor group
     * @param tnMinHeight the minimum height of the editor group
     * @return
     */
    private Group createEditorGroup()
    {
        Group loGroup = new Group();
        //loGroup.setBorderSize(2, 2, 2, 2);
        //loGroup.setLayoutManager(new FlowLayoutManager(Orientation.HORIZONTAL()));
        loGroup.setLayoutManager(FlowLayoutManager.class);

        return loGroup;
    }

    /**
     * Registers the editor with this pane for event listening
     */
    public void registerEditor(TextArea toEditor)
    {
        // TODO: For now this is limited to the TextArea, we want to allow for other Editable controls as well.

        if (m_oEditor != null)
        {
            unRegisterEditor();
        }
        m_oEditor = toEditor;

        // Hook up all the event listening
    }

    /**
     * Unregisters the current editor from this pane, this will unhook all events and remove the reference to the editor
     */
    public void unRegisterEditor()
    {
        // Unhook all the event listeners

        m_oEditor = null;
    }

    private IContainer createEditorGroup1()
    {
        Group loReturn = new Group();
        loReturn.setLayoutManager(BorderLayoutManager.class);

        Group loModifiers = new Group();
        loModifiers.setLayoutManager(GridLayoutManager.class);

        // Create the font dropdown
        loModifiers.addControl(new Combobox(getFontList()));

        loModifiers.addControl(new Combobox(getFontSizeList()));

        loModifiers.addControl(createNewButtons("./resources/images/BoldButton.png", "Bold"));
        loModifiers.addControl(createNewButtons("./resources/images/UnderLineButton.png", "UnderLine"));
        loModifiers.addControl(createNewButtons("./resources/images/ItalicButton.png", "Italic"));

        loModifiers.addControl(createNewButtons("./resources/images/ColorButton.png", "Color"));

        loModifiers.addControl(createNewButtons("./resources/images/LeftButton.png", "Left"));
        loModifiers.addControl(createNewButtons("./resources/images/CenterButton.png", "Center"));
        loModifiers.addControl(createNewButtons("./resources/images/RightButton.png", "Right"));
        loModifiers.addControl(createNewButtons("./resources/images/JustifiedButton.png", "Justified"));

        loModifiers.addControl(createNewButtons("./resources/images/BulletButton.png", "Bullet"));


        Group loInserts = new Group();
        loInserts.setLayoutManager(GridLayoutManager.class);

        loInserts.addControl(new Label("Insert"));
        loInserts.addControl(new Combobox(getInsertAddOnList()));
        loInserts.addControl(new Button("Insert"));


        loReturn.addControl(loModifiers, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.PAGE_START());
        loReturn.addControl(loInserts, ControlLayoutProperty.LAYOUTCONSTRAINT(), BorderLayoutConstants.PAGE_END());

        return loReturn;
    }

    private List<String> getFontList()
    {
        return Font.getFonts();
    }

    private List<String> getFontSizeList()
    {
        List<String> loFonts = new List<String>();
        loFonts.add("64");
        loFonts.add("32");
        loFonts.add("16");
        loFonts.add("8");
        return loFonts;
    }

    private List<String> getStyleList()
    {
        List<String> loFonts = new List<String>();
        loFonts.add("Normal");
        loFonts.add("Heading1");
        loFonts.add("Heading2");
        loFonts.add("Heading3");
        return loFonts;
    }

    private List<String> getInsertAddOnList()
    {
        List<String> loFonts = new List<String>();
        loFonts.add("Link");
        loFonts.add("Image");
        loFonts.add("Flickr Image");
        loFonts.add("YouTube Video");
        return loFonts;
    }

    private Button createNewButtons(String loImageSource, String loButtonFunction)
    {
        Button loTextEditorButtons;
        Goliath.Graphics.Image loImage = null;
        try
        {
            loImage = new Goliath.Graphics.Image(loImageSource);
        }
        catch (FileNotFoundException ex)
        {
            Application.getInstance().log(ex);
        }
        loTextEditorButtons = new Button(Delegate.build(this, loButtonFunction.toLowerCase()));

        // System.out.println ((Container)loTextEditorButtons.getControl().);
        loTextEditorButtons.setImage(loImage);
        loTextEditorButtons.setTooltip(loButtonFunction);
        loTextEditorButtons.setSize(loImage.getSize());
        return loTextEditorButtons;

    }

    /*  private void bold(Event<IControl> toEvent){
    updateInputMap();
    Action action= m_oTextArea.getTextCompDocument().getActionMap( ).get("font-bold");
    action.putValue(Action.NAME, "Bold");
    System.out.println(action.getValue(Action.NAME));

    }
    protected void updateInputMap() {
    // Extend the input map used by our text component.
    InputMap map = m_oTextArea.getInputMap( );
    int mask = Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( );
    KeyStroke bold = KeyStroke.getKeyStroke(KeyEvent.VK_B, mask, false);
    KeyStroke italic = KeyStroke.getKeyStroke(KeyEvent.VK_I, mask, false);
    KeyStroke under = KeyStroke.getKeyStroke(KeyEvent.VK_U, mask, false);
    map.put(bold, "font-bold");
    map.put(italic, "font-italic");
    map.put(under, "font-underline");
    }*/
    private void bold(Event<IControl> toEvent)
    {
        /*        System.out.println(m_oTextArea.getSelectedHTMLText());
        String loString="<b>"+m_oTextArea.getSelectedHTMLText()+"</b>";
        System.out.println("After appending"+loString);
        m_oTextArea.setValue(m_oTextArea.getValue().replace(m_oTextArea.getSelectedHTMLText(),loString ));
        System.out.println(m_oTextArea.getValue());*/
    }
    /* private void underLine(Event<IControl> toEvent) {
    HTMLDocument  loHtmlDoc = m_oTextArea.getDocument();
    int start = m_oTextArea.getSelectionStart();
    int end = m_oTextArea.getSelectionEnd();
    SimpleAttributeSet  newType = new SimpleAttributeSet();
    newType.addAttribute(AttributeSet.NameAttribute,HTML.Tag.U );
    loHtmlDoc.setParagraphAttributes(start, end-start, newType, false );
    m_oTextArea.setDocument(loHtmlDoc);
    }

    private void italic(Event<IControl> toEvent) {
    HTMLDocument  loHtmlDoc = m_oTextArea.getDocument();
    int start = m_oTextArea.getSelectionStart();
    int end = m_oTextArea.getSelectionEnd();
    SimpleAttributeSet  newType = new SimpleAttributeSet();
    newType.addAttribute(AttributeSet.NameAttribute,HTML.Tag.I );
    loHtmlDoc.setParagraphAttributes(start, end-start, newType, false );
    m_oTextArea.setDocument(loHtmlDoc);
    }
    private void left(Event<IControl> toEvent) throws IOException, BadLocationException {
    HTMLDocument  loHtmlDoc = m_oTextArea.getDocument();
    int start = m_oTextArea.getSelectionStart();
    int end = m_oTextArea.getSelectionEnd();
    SimpleAttributeSet  newType = new SimpleAttributeSet();
    newType.addAttribute("align", );
    loHtmlDoc.setParagraphAttributes(start, end-start, newType, false );
    m_oTextArea.setDocument(loHtmlDoc);

    }
    private void center(Event<IControl> toEvent) {
    String loHTMLdoc= m_oTextArea.getValue();
    m_oTextArea.setValue(loHTMLdoc.replace("<p", "<p align=\"center\""));
    System.out.println(m_oTextArea.getValue());
    }
    private void right(Event<IControl> toEvent) {
    String loHTMLdoc= m_oTextArea.getValue();
    m_oTextArea.setValue(loHTMLdoc.replace("<p", "<p align=\"right\""));
    System.out.println(m_oTextArea.getValue());
    }
    private void checkAlignment()
    {


    }
    private HTMLDocument getDocument() throws IOException, BadLocationException
    {
    HTMLEditorKit htmlKit = new HTMLEditorKit();
    HTMLDocument loHtmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
    htmlKit.read(new StringReader(m_oTextArea.getValue()), loHtmlDoc, 0);
    return loHtmlDoc;
    }*/
}
