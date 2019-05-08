import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Cliplist extends JList< JSONObject > {
    private DefaultListModel< JSONObject > model;
    private Long start;
    private boolean detailsOpen = false;
    private Timer time = new Timer();
    private TimeCheckTask task = new TimeCheckTask();
    private JFrame ref;
    private JSONArray clips;
    private int clipID =  0;

    public Cliplist ( ChangeInterface change,JFrame reference ) {
        super();
        model = new DefaultListModel<>();
        setCellRenderer( new ClipRenderer() );
        this.ref = reference;
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                //Make sure we find something that is actually in the list.
                if ( mouseEvent.getClickCount() == 2) {
                    if ( locationToIndex( mouseEvent.getPoint() ) != -1 ) {
                        //Now we want to tell the clipboard to set the value for the given locaiton.
                        try {
                            JSONObject obj = getModel().getElementAt( locationToIndex( mouseEvent.getPoint() ) );
                            change.BoardChanged( obj.getString("value" ) );
                        } catch ( Exception e ) {
                            System.out.println( e.getMessage() );
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                //Make sure we are clicking on an actual clip.
                if( locationToIndex( mouseEvent.getPoint() ) != -1 ) {
                    try {
                        clipID = locationToIndex( mouseEvent.getPoint() );
                    } catch( Exception e ) {
                        System.out.println( e.getMessage() );
                    }
                }

                start = new Date().getTime() / 1000;

                time = new Timer();
                task = new TimeCheckTask();
                time.schedule( task,0,1 );
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                task.cancel();
                time.cancel();
                time.purge();
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }

    //Takes the JSONArray of clip objects and applies them to the list.
    public void SetClips ( JSONArray clips ) {
        this.clips = clips;
        model = new DefaultListModel<>();
        //Loop through all the clips and add them to
        //the list model.
        for ( int i = clips.length() - 1;i > -1;i-- ) {
            try {
                JSONObject obj = clips.getJSONObject( i );
                model.addElement( obj );
            } catch ( Exception e ) {
                System.out.println( e.getMessage() );
            }
        }

        System.out.println( "UPDATING LIST" );
        setModel( model );
    }

    //Task class that will run to check how long the user has
    //clicked down on the list item.
    private class TimeCheckTask extends TimerTask {

        @Override
        public void run() {
            //Now check how long the user has been holding down the click based on the time
            //since the start click.
            Long end = new Date().getTime() / 1000;
            if ( end - start > .5f ) {
                if ( !detailsOpen ) {
                    detailsOpen = true;

                    JDialog dialog = new JDialog( ref,"Clip Details" );

                    dialog.addWindowListener(new WindowListener() {
                        @Override
                        public void windowOpened(WindowEvent e) {

                        }

                        @Override
                        public void windowClosing(WindowEvent e) {
                            detailsOpen = false;
                        }

                        @Override
                        public void windowClosed(WindowEvent e) {

                        }

                        @Override
                        public void windowIconified(WindowEvent e) {

                        }

                        @Override
                        public void windowDeiconified(WindowEvent e) {

                        }

                        @Override
                        public void windowActivated(WindowEvent e) {

                        }

                        @Override
                        public void windowDeactivated(WindowEvent e) {

                        }
                    });

                    dialog.setLocationRelativeTo( ref );
                    dialog.setSize( new Dimension( 400,300 ) );
                    dialog.setLayout( new BorderLayout() );
                    dialog.setResizable( false );

                    try {
                        //Build out the dialog UI here for options on what to do with said clip.
                        JPanel clipValue = new JPanel();
                        JTextArea clipHold = new JTextArea( 10, dialog.getSize().width / 12 );
                        JScrollPane scroll = new JScrollPane( clipHold,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );

                        scroll.setViewportBorder( BorderFactory.createEmptyBorder( 0,0,0,0 ) );
                        clipHold.setLineWrap( true );
                        clipHold.setBorder( BorderFactory.createEmptyBorder( 10,10,10,10 ) );
                        clipHold.setText( clips.getJSONObject( ( clips.length() - 1) - clipID ).getString("value" ) );

                        clipValue.setBorder( BorderFactory.createEmptyBorder( 3,10,10,10 ));
                        clipValue.add( scroll );

                        //Create the action buttons below the clip textfield
                        JPanel action_panel = new JPanel( new FlowLayout() );

                        JButton close = new JButton( "Close" );
                        close.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                dialog.setVisible( false );
                                detailsOpen = false;
                            }
                        });

                        action_panel.add( new JButton("Copy") );
                        action_panel.add( new JButton("Favorite") );
                        action_panel.add( close );

                        //Add panels to the new dialog.
                        dialog.add( clipValue,BorderLayout.NORTH );
                        dialog.add( action_panel,BorderLayout.SOUTH );
                    } catch ( Exception e ) {
                        System.out.println( e.getMessage() );
                    }

                    dialog.setVisible( true );
                }
            }
        }
    }
}
