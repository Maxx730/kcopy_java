import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Timer;
import java.util.TimerTask;

public class Clipboard {
    java.awt.datatransfer.Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
    private Timer timer;
    private String lastClip = "";
    private ChangeInterface changed;

    public Clipboard ( ChangeInterface changed ) {
        this.changed = changed;
        timer = new Timer();
        timer.schedule( new PollTask(),0,500 );
    }

    private class PollTask extends TimerTask {

        @Override
        public void run() {
            CheckClipboard();
        }

        private void CheckClipboard () {
            try {
                //Make sure the current clip is not the same as the last clip.
                if ( !board.getData( DataFlavor.stringFlavor ).toString().equals( lastClip ) ) {
                    //If the clip has changed then we want to run our board changed function
                    changed.BoardChanged( board.getData( DataFlavor.stringFlavor ).toString() );
                    lastClip = board.getData( DataFlavor.stringFlavor ).toString();
                }
            } catch ( Exception e ) {
                System.out.println( "ERROR: " + e.getMessage() );
            }
        }
    }
}

