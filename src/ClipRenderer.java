import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Collections;

//Class that is used to custom render our clips intot he JList.
public class ClipRenderer extends JPanel implements ListCellRenderer<JSONObject> {
    //Initialize the parts of the cell here to help with performance.
    JLabel id = new JLabel( "ID" );
    JLabel value = new JLabel( "VALUE" );
    JLabel star = new JLabel("STAR");
    JSeparator vert = new JSeparator( JSeparator.VERTICAL );

    public ClipRenderer () {
        setLayout( new FlowLayout( FlowLayout.LEFT ) );
        setBorder( BorderFactory.createMatteBorder(0,0,1,0,Color.decode( "#E6E6E6" ) ) );
        setBackground( Color.WHITE );

        Font font = id.getFont();

        font = font.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT,TextAttribute.WEIGHT_BOLD));
        id.setFont( font );
    }

    //Override method that actually displays what will be rendered inside each of the list items.
    @Override
    public Component getListCellRendererComponent(JList<? extends JSONObject> jList, JSONObject clip, int i, boolean isSelected, boolean hasFocus) {
        removeAll();
        setBackground( Color.WHITE );

        if ( isSelected ) {
            setBackground( Color.decode("#EDEDED") );
        }

        try {
            id.setText( String.valueOf( clip.getInt( "id" ) + 1 ) + ".  " );
            value.setText( clip.getString("value" ) );
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
        }

        add( id );
        add( vert );
        add( value );
        add( star );

        return this;
    }
}
