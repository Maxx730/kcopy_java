import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Cliplist extends JList< JSONObject > {
    private DefaultListModel< JSONObject > model;

    public Cliplist () {
        super();
        model = new DefaultListModel<>();
        setCellRenderer( new ClipRenderer() );
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                //Make sure we find something that is actually in the list.
                if ( locationToIndex( mouseEvent.getPoint() ) != -1 ) {

                }
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
        model = new DefaultListModel<>();
        //Loop through all the clips and add them to
        //the list model.
        for ( int i = clips.length();i > -1;i-- ) {
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
}
