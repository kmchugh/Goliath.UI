/* ========================================================
 * AudioEditorPane.java
 *
 * Author:      kenmchugh
 * Created:     Mar 4, 2011, 9:33:17 AM
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

import Goliath.Collections.List;
import Goliath.Event;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.UI.Controls.IButton;
import Goliath.Interfaces.UI.Controls.IContainer;

        
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
public class AudioEditorPane extends EditorPane<RichAudioEditor>
{
    protected class ReadyStateNoContent extends EditorState
    {
        public ReadyStateNoContent()
        {
        }

        @Override
        protected boolean canMoveToState(EditorState toState)
        {
            return isOneOf(toState, RecordingState.class);
        }

        @Override
        protected IList getEnabledControls()
        {
            return new List<String>(new String[]{RECORD});
        }

        @Override
        protected IList getVisibleControls()
        {
            return new List<String>(new String[]{BACK, REWIND, PLAY, STOP, FFORWARD, FORWARD, RECORD});
        }
        
    }

    protected class ReadyStateWithContent extends EditorState
    {
        public ReadyStateWithContent()
        {
        }

        @Override
        protected boolean canMoveToState(EditorState toState)
        {
            return isOneOf(toState, PlaybackState.class);
        }

        @Override
        protected IList getEnabledControls()
        {
            return new List<String>(new String[]{PLAY});
        }

        @Override
        protected IList getVisibleControls()
        {
            return new List<String>(new String[]{BACK, REWIND, PLAY, STOP, FFORWARD, FORWARD, RECORD});
        }
    }

    protected class RecordingState extends EditorState
    {
        public RecordingState()
        {
        }

        @Override
        protected boolean canMoveToState(EditorState toState)
        {
            return isOneOf(toState, ReadyStateWithContent.class,
                    PausedRecordingState.class);
        }

        @Override
        protected IList getEnabledControls()
        {
            return new List<String>(new String[]{PAUSE, STOP});
        }

        @Override
        protected IList getVisibleControls()
        {
            return new List<String>(new String[]{BACK, PAUSE, REWIND, STOP, FFORWARD, FORWARD, RECORD});
        }
    }

    protected class PausedRecordingState extends EditorState
    {
        public PausedRecordingState()
        {
        }

        @Override
        protected boolean canMoveToState(EditorState toState)
        {
            return isOneOf(toState, 
                    RecordingState.class,
                    ReadyStateWithContent.class);
        }

        @Override
        protected IList getEnabledControls()
        {
            return new List<String>(new String[]{STOP, RECORD});
        }

        @Override
        protected IList getVisibleControls()
        {
            return new List<String>(new String[]{BACK, PLAY, REWIND, STOP, FFORWARD, FORWARD, RECORD});
        }
    }

    protected class PausedPlaybackState extends EditorState
    {
        public PausedPlaybackState()
        {
        }

        @Override
        protected boolean canMoveToState(EditorState toState)
        {
            return isOneOf(toState, PlaybackState.class,
                    ReadyStateWithContent.class);
        }

        @Override
        protected IList getEnabledControls()
        {
            return new List<String>(new String[]{STOP, PLAY});
        }

        @Override
        protected IList getVisibleControls()
        {
            return new List<String>(new String[]{BACK, PLAY, REWIND, STOP, FFORWARD, FORWARD, RECORD});
        }
    }

    protected class PlaybackState extends EditorState
    {
        public PlaybackState()
        {
        }

        @Override
        protected boolean canMoveToState(EditorState toState)
        {
            return isOneOf(toState, PausedPlaybackState.class,
                    ReadyStateWithContent.class);
        }

        @Override
        protected IList getEnabledControls()
        {
            return new List<String>(new String[]{STOP, PAUSE, REWIND, FFORWARD, FORWARD, BACK});
        }

        @Override
        protected IList getVisibleControls()
        {
            return new List<String>(new String[]{BACK, PAUSE, REWIND, STOP, FFORWARD, FORWARD, RECORD});
        }
    }

    //return new List<String>(new String[]{BACK, PAUSE, REWIND, PLAY, STOP, FFORWARD, FORWARD, RECORD});


    private static String BACK = "BACK";
    private static String PAUSE = "PAUSE";
    private static String REWIND = "REWIND";
    private static String PLAY = "PLAY";
    private static String STOP = "STOP";
    private static String FFORWARD = "FFORWARD";
    private static String FORWARD = "FORWARD";
    private static String RECORD = "RECORD";
    /**
     * Creates a new instance of AudioEditorPane
     */
    public AudioEditorPane(RichAudioEditor toEditor)
    {
        super(toEditor);
        initialiseComponent();
    }

    @Override
    protected EditorState getInitialState()
    {
        return this.getEditorStateByClass(ReadyStateNoContent.class);
    }



    private void initialiseComponent()
    {
        List<IButton> loButtons = new List<IButton>();
        loButtons.add(createEditorButton(BACK, "Back", "./resources/images/icons/back.png", "onBackClicked"));
        loButtons.add(createEditorButton(REWIND, "Rewind", "./resources/images/icons/rewind.png", "onRewindClicked"));
        loButtons.add(createEditorButton(PLAY, "Play", "./resources/images/icons/play.png", "onPlayClicked"));
        loButtons.add(createEditorButton(PAUSE, "Pause", "./resources/images/icons/pause.png", "onPauseClicked"));
        loButtons.add(createEditorButton(STOP, "Stop", "./resources/images/icons/stop.png", "onStopClicked"));
        loButtons.add(createEditorButton(FFORWARD, "Fast Forward", "./resources/images/icons/fforward.png", "onFForwardClicked"));
        loButtons.add(createEditorButton(FORWARD, "Forward", "./resources/images/icons/forward.png", "onForwardClicked"));
        loButtons.add(createEditorButton(RECORD, "Record", "./resources/images/icons/record.png", "onRecordClicked"));

        IContainer loGroup = createEditorGroup(loButtons);

        addControl(loGroup);

        updateCurrentState();
    }

    private void onBackClicked(Event<IButton> toEvent)
    {

    }

    private void onRewindClicked(Event<IButton> toEvent)
    {

    }

    private void onPlayClicked(Event<IButton> toEvent)
    {
        setState(getEditorStateByClass(PlaybackState.class));
    }

    private void onPauseClicked(Event<IButton> toEvent)
    {
        setState(getCurrentState().equals(getEditorStateByClass(RecordingState.class)) ?
            getEditorStateByClass(PausedRecordingState.class) :
            getEditorStateByClass(PausedPlaybackState.class));
    }

    private void onStopClicked(Event<IButton> toEvent)
    {
        // State with content, or state with no content
        setState(getEditorStateByClass(ReadyStateWithContent.class));
    }

    private void onFForwardClicked(Event<IButton> toEvent)
    {

    }

    private void onForwardClicked(Event<IButton> toEvent)
    {

    }

    private void onRecordClicked(Event<IButton> toEvent)
    {
        setState(getEditorStateByClass(RecordingState.class));

    }

    
}
