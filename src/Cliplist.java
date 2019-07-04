import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;

public class Cliplist extends JList< JSONObject > {
    private DefaultListModel< JSONObject > model;
    private Timer time = new Timer();
    private JFrame ref;
    private JSONArray clips;
    private ChangeInterface change;
    private int clipId;

    public Cliplist ( ChangeInterface change,JFrame reference ) {
        super();
        model = new DefaultListModel<>();
        setCellRenderer( new ClipRenderer() );
        setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        this.ref = reference;
        this.change = change;

        //Create the context menu for when the user right clicks on and item.
        ItemMenu men = new ItemMenu();

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                //Make sure we find something that is actually in the list.
                if ( SwingUtilities.isRightMouseButton( mouseEvent ) ) {
                    if ( locationToIndex( mouseEvent.getPoint() ) != -1 ) {
                        //Now we want to tell the clipboard to set the value for the given locaiton.
                        try {
                            setSelectedIndex( locationToIndex( mouseEvent.getPoint() ) );
                            men.show(mouseEvent.getComponent(),mouseEvent.getX(),mouseEvent.getY());
                        } catch ( Exception e ) {
                            System.out.println( e.getMessage() );
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

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

    private class ItemMenu extends JPopupMenu {
        public ItemMenu() {
            JMenuItem copy = new JMenuItem( " Copy" );
            copy.setIcon(new ImageIcon(getClass().getResource("images/29.png")));
            JMenuItem delete = new JMenuItem( " Delete" );
            delete.setIcon(new  ImageIcon(getClass().getResource("images/04.png")));

            //Copies the selected value into the clipboard
            copy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        JSONObject obj = model.get( getSelectedIndex() );
                        change.BoardChanged(obj.getString("value"),false);
                    } catch( Exception err ){
                        System.out.println(err.getMessage());
                    }
                }
            });

            add( copy );
            add( delete );
        }

        public void CopyItem( String value ) {

        }
    }
}
