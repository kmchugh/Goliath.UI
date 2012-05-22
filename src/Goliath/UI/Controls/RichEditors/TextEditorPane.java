/* ========================================================
 * TextEditorPane.java
 *
 * Author:      kenmchugh
 * Created:     Mar 4, 2011, 9:34:16 AM
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
import Goliath.Delegate;
import Goliath.Event;
import Goliath.Graphics.Font;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Controls.Combobox;
import Goliath.UI.Controls.UserControls.FormattingDefinition;
import Goliath.UI.Constants.UIEventType;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Mar 4, 2011
 * @author      kenmchugh
 **/
public class TextEditorPane extends EditorPane<RichTextEditor> {

    protected class NoSelectionState extends EditorState {

        public NoSelectionState() {
        }

        @Override
        protected boolean canMoveToState(EditorState toState) {
            return isOneOf(toState, SelectionState.class);
        }

        @Override
        protected IList getEnabledControls() {
            return new List<String>(0);
        }

        @Override
        protected IList getVisibleControls() {
            return new List<String>(new String[]{BOLD, ITALIC, UNDERLINE,
                        FONTLIST, FONTSIZE,
                        SUPERSCRIPT, SUBSCRIPT,
                        COLOUR, BACKGROUND,
                        LEFT_JUSTIFY, CENTER_JUSTIFY, RIGHT_JUSTIFY, JUSTIFY,
                        STYLE,
                        INDENT, REMOVEINDENT, BULLETLIST, NUMBEREDLIST
                    });
        }
    }

    protected class SelectionState extends EditorState {

        public SelectionState() {
        }

        @Override
        protected boolean canMoveToState(EditorState toState) {
            return isOneOf(toState, NoSelectionState.class);
        }

        @Override
        protected IList getEnabledControls() {
            return new List<String>(new String[]{BOLD, ITALIC, UNDERLINE,
                        FONTLIST, FONTSIZE,
                        SUPERSCRIPT, SUBSCRIPT,
                        COLOUR, BACKGROUND,
                        LEFT_JUSTIFY, CENTER_JUSTIFY, RIGHT_JUSTIFY, JUSTIFY,
                        STYLE,
                        INDENT, REMOVEINDENT, BULLETLIST, NUMBEREDLIST
                    });
        }

        @Override
        protected IList getVisibleControls() {
            return new List<String>(new String[]{BOLD, ITALIC, UNDERLINE,
                        FONTLIST, FONTSIZE,
                        SUPERSCRIPT, SUBSCRIPT,
                        COLOUR, BACKGROUND,
                        LEFT_JUSTIFY, CENTER_JUSTIFY, RIGHT_JUSTIFY, JUSTIFY,
                        STYLE,
                        INDENT, REMOVEINDENT, BULLETLIST, NUMBEREDLIST
                    });
        }
    }

    private static String BOLD = "BOLD";
    private static String ITALIC = "ITALIC";
    private static String UNDERLINE = "UNDERLINE";

    private static String FONTLIST = "FONTLIST";
    private static String FONTSIZE = "FONTSIZE";

    private static String SUPERSCRIPT = "SUPERSCRIPT";
    private static String SUBSCRIPT = "SUBSCRIPT";

    private static String COLOUR = "COLOUR";
    private static String BACKGROUND = "BACKGROUND";

    private static String LEFT_JUSTIFY = "LEFT_JUSTIFY";
    private static String RIGHT_JUSTIFY = "RIGHT_JUSTIFY";
    private static String CENTER_JUSTIFY = "CENTER_JUSTIFY";
    private static String JUSTIFY = "JUSTIFY";

    private static String STYLE = "STYLE";

    private static String INDENT = "INDENT";
    private static String REMOVEINDENT = "REMOVEINDENT";
    private static String BULLETLIST = "BULLETLIST";
    private static String NUMBEREDLIST = "NUMBEREDLIST";
    private static String m_cFontSelected;
    private static String m_cFontSizeSelected;
    private static String m_cFontStyleSelected;

    public TextEditorPane(RichTextEditor toEditor) {
        super(toEditor);
        initialiseComponent();
    }

    private void initialiseComponent() {
        // TODO: Make all of the editor bars plugins
        // Creates all of the editor bars for this editor
        createEditorBars();

        updateCurrentState();
    }

    @Override
    protected EditorState getInitialState() {
        return getEditorStateByClass(NoSelectionState.class);
    }

    @Override
    protected void onSelectionChanged(Event toEvent) {
        setState(getEditorStateByClass(
                ((RichTextEditor) toEvent.getTarget()).getSelectionLength() > 0
                ? SelectionState.class : NoSelectionState.class));
    }

    /**
     * Template method for creating all of the editor bars for this editor
     */
    private void createEditorBars() {
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

    private void addFontSizeBar() {
        List<Combobox> loControls = new List<Combobox>();

        loControls.add(createEditorDropdown(FONTLIST, getFontList(), "onFontChanged"));
        loControls.add(createEditorDropdown(FONTSIZE, getFontSizeList(), "onFontSizeChanged"));

        IContainer loGroup = createEditorGroup(loControls);

        addControl(loGroup);
    }

    private void addFontDecorationBar() {
        List<IButton> loControls = new List<IButton>();

        loControls.add(createEditorButton(BOLD, "Bold", "./resources/images/rte/bold.png", "onBoldClicked"));
        loControls.add(createEditorButton(UNDERLINE, "Underline", "./resources/images/rte/underline.png", "onUnderlineClicked"));
        loControls.add(createEditorButton(ITALIC, "Italic", "./resources/images/rte/italic.png", "onItalicClicked"));

        IContainer loGroup = createEditorGroup(loControls);

        addControl(loGroup);
    }

    private void addFontScriptBar() {
        List<IButton> loControls = new List<IButton>();

        loControls.add(createEditorButton(SUPERSCRIPT, "Superscript", "./resources/images/rte/superscript.png", "onSuperScriptClicked"));
        loControls.add(createEditorButton(SUBSCRIPT, "Subscript", "./resources/images/rte/subscript.png", "onSubScriptClicked"));

        IContainer loGroup = createEditorGroup(loControls);

        addControl(loGroup);
    }

    private void addFontColourBar() {
        List<IButton> loControls = new List<IButton>();

        loControls.add(createEditorButton(COLOUR, "Colour", "./resources/images/rte/fontcolor.png", "onColourClicked"));
        loControls.add(createEditorButton(BACKGROUND, "Background", "./resources/images/rte/fontbackgroundcolor.png", "onBGColourClicked"));

        IContainer loGroup = createEditorGroup(loControls);

        addControl(loGroup);
    }

    private void addAlignmentBar() {
        List<IButton> loControls = new List<IButton>();

        loControls.add(createEditorButton(LEFT_JUSTIFY, "Left", "./resources/images/rte/left.png", "onLJustifyClicked"));
        loControls.add(createEditorButton(CENTER_JUSTIFY, "Center", "./resources/images/rte/center.png", "onCJustifyClicked"));
        loControls.add(createEditorButton(RIGHT_JUSTIFY, "Right", "./resources/images/rte/right.png", "onRJustifyClicked"));
        loControls.add(createEditorButton(JUSTIFY, "Justified", "./resources/images/rte/justified.png", "onJustifyClicked"));

        IContainer loGroup = createEditorGroup(loControls);

        addControl(loGroup);
    }

    private void addCustomStyleBar() {
        List<Combobox> loControls = new List<Combobox>();

        loControls.add(createEditorDropdown(STYLE, getStyleList(), "onStyleChanged"));

        IContainer loGroup = createEditorGroup(loControls);

        addControl(loGroup);
    }

    private void addIndentAndListBar() {
        List<IButton> loControls = new List<IButton>();

        loControls.add(createEditorButton(INDENT, "Indent", "./resources/images/rte/indent.png", "onIndentClicked"));
        loControls.add(createEditorButton(REMOVEINDENT, "Remove Indent", "./resources/images/rte/removeindent.png", "onRemoveIndentClicked"));
        loControls.add(createEditorButton(BULLETLIST, "Bullet List", "./resources/images/rte/bulletlist.png", "onBulletListClicked"));
        loControls.add(createEditorButton(NUMBEREDLIST, "Numbered List", "./resources/images/rte/numberedlist.png", "onNumberedListClicked"));

        IContainer loGroup = createEditorGroup(loControls);

        addControl(loGroup);
    }

    private void addInsertBar() {
        /*
        List<IButton> loControls = new List<IButton>();

        loControls.add(createEditorButton(INDENT, "Indent", "./resources/images/rte/indent.png", "onIndentClicked"));
        loControls.add(createEditorButton(REMOVEINDENT, "Remove Indent", "./resources/images/rte/removeindent.png", "onRemoveIndentClicked"));
        loControls.add(createEditorButton(BULLETLIST, "Bullet List", "./resources/images/rte/bulletlist.png", "onBulletListClicked"));
        loControls.add(createEditorButton(NUMBEREDLIST, "Numbered List", "./resources/images/rte/numberedlist.png", "onNumberedListClicked"));

        IContainer loGroup = createEditorGroup(loControls);

        addControl(loGroup);
         *
         */
    }

    private List<String> getFontList() {
        return Font.getFonts();
    }

    private List<String> getFontSizeList() {
        List<String> loFonts = new List<String>();
        loFonts.add("64");
        loFonts.add("32");
        loFonts.add("16");
        loFonts.add("8");
        return loFonts;
    }

    private List<String> getStyleList() {
        List<String> loFonts = new List<String>();
        loFonts.add("Normal");
        loFonts.add("Heading1");
        loFonts.add("Heading2");
        loFonts.add("Heading3");
        return loFonts;
    }

    private List<String> getInsertAddOnList() {
        List<String> loFonts = new List<String>();
        loFonts.add("Link");
        loFonts.add("Image");
        loFonts.add("Flickr Image");
        loFonts.add("YouTube Video");
        return loFonts;
    }

    /**
     * get the Tag
     * @return
     */
    public static HashTable<String, String> getTagName() {
        HashTable<String, String> loTagName = new HashTable<String, String>();
        loTagName.put(BOLD, "b");
        loTagName.put(ITALIC, "i");
        loTagName.put(UNDERLINE, "u");
        loTagName.put(SUPERSCRIPT, "sup");
        loTagName.put(SUBSCRIPT, "sub");
        loTagName.put(SUBSCRIPT, "sub");
        loTagName.put(FONTLIST, "font" + addAttributes(FONTLIST));
        loTagName.put(FONTSIZE, "font" + addAttributes(FONTSIZE));
        loTagName.put(BULLETLIST, "li");
        loTagName.put(NUMBEREDLIST, m_cFontStyleSelected + addAttributes(NUMBEREDLIST));
        loTagName.put(LEFT_JUSTIFY, "div" + addAttributes(LEFT_JUSTIFY));
        loTagName.put(CENTER_JUSTIFY, "div" + addAttributes(CENTER_JUSTIFY));
        loTagName.put(RIGHT_JUSTIFY, "div" + addAttributes(RIGHT_JUSTIFY));
        loTagName.put(JUSTIFY, "div" + addAttributes(JUSTIFY));
        loTagName.put(INDENT,"blockquote");
        return loTagName;
    }

    /**
     * Add attributes for Font Tag
     * @param tcKey
     * @return
     */
    public static String addAttributes(String tcKey) {
        StringBuilder lcAttributes = new StringBuilder();
        lcAttributes.append(" ");
        if (tcKey.equalsIgnoreCase(FONTLIST)) {
            lcAttributes.append("style=" + "\"font-family:" + m_cFontSelected + ";\"");
        }
        if (tcKey.equalsIgnoreCase(FONTSIZE)) {
            lcAttributes.append("style=" + "\"font-size:" + m_cFontSizeSelected + ";\"");
        }
        if (tcKey.equalsIgnoreCase(LEFT_JUSTIFY)) {
            lcAttributes.append("style=" + "\"text-align:left\"");
        }
        if (tcKey.equalsIgnoreCase(CENTER_JUSTIFY)) {
            lcAttributes.append("style=" + "\"text-align:center\"");
        }
        if (tcKey.equalsIgnoreCase(RIGHT_JUSTIFY)) {
            lcAttributes.append("style=" + "\"text-align:right\"");
        }
        if (tcKey.equalsIgnoreCase(JUSTIFY)) {
            lcAttributes.append("style=" + "\"text-align:justify\"");
        }

        return lcAttributes.toString();
    }

    public static String removeAttributes(String tcWholeTag) {
        String[] laAttributes = tcWholeTag.split(" ");
        return laAttributes[0];
    }

    private void onFontChanged(Event<IControl> toEvent) {
        Combobox loFont = (Combobox) getStoredControl("FONTLIST");
        m_cFontSelected = loFont.getValue().toString();
        getEditor().applyFormattingToSelection(FONTLIST);
    }

    private void onFontSizeChanged(Event<IButton> toEvent) {
        Combobox loFont = (Combobox) getStoredControl("FONTSIZE");
        m_cFontSizeSelected = loFont.getValue().toString();
        getEditor().applyFormattingToSelection(FONTSIZE);
    }

    private void onUnderlineClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(UNDERLINE);
    }

    private void onBoldClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(BOLD);
    }

    private void onItalicClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(ITALIC);
    }

    private void onSuperScriptClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(SUPERSCRIPT);
    }

    private void onSubScriptClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(SUBSCRIPT);
    }

    private void onColourClicked(Event<IButton> toEvent) {
    }

    private void onBGColourClicked(Event<IButton> toEvent) {
    }

    private void onLJustifyClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(LEFT_JUSTIFY);
    }

    private void onCJustifyClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(CENTER_JUSTIFY);
    }

    private void onRJustifyClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(RIGHT_JUSTIFY);
    }

    private void onJustifyClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(JUSTIFY);
    }

    private void onStyleChanged(Event<IControl> toEvent) {
        Combobox loFont = (Combobox) getStoredControl("STYLE");
        String lcStyle = loFont.getValue().toString();
        if (lcStyle.equalsIgnoreCase("Heading1")) {
            m_cFontStyleSelected = "h1";
        } else if (lcStyle.equalsIgnoreCase("Heading2")) {
            m_cFontStyleSelected = "h2";
        } else if (lcStyle.equalsIgnoreCase("Heading3")) {
            m_cFontStyleSelected = "h3";
        } else {
            m_cFontStyleSelected = "p";
        }
        getEditor().applyFormattingToSelection(STYLE);
    }

    private void onIndentClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(INDENT);
    }

    private void onRemoveIndentClicked(Event<IButton> toEvent) {
    }

    private void onBulletListClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(BULLETLIST);
    }

    private void onNumberedListClicked(Event<IButton> toEvent) {
        getEditor().applyFormattingToSelection(NUMBEREDLIST);

    }
}
