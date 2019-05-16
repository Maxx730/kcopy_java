import javax.swing.*;
import java.awt.*;

//Panel that will display infomation at the bottom of the app such as
//when a clipboard item has been copied back into the clipboard.
public class NotificationPanel extends JPanel {

    JLabel message = new JLabel( "TEST MESSAGE" );;
    ImageIcon notif_image,scaled,icon;

    public NotificationPanel () {

        message.setBorder( BorderFactory.createEmptyBorder( 15,15,15,15 ) );
        notif_image = new ImageIcon(new ImageIcon( this.getClass().getResource("/images" +
                "/50.png") ).getImage().getScaledInstance(18,18,
                Image.SCALE_SMOOTH));
        JLabel icon = new JLabel( notif_image );
        icon.setBorder( BorderFactory.createEmptyBorder( 10,10,10,10 ) );

        setBackground( new Color( 0,0,0,100 ) );
        setOpaque( true );
        setLayout( new FlowLayout( FlowLayout.LEFT ) );
        add( icon );
        add( message );
    }

    public void SetMessage ( String value ) {
        message.setText( "'" + value + "' saved to history." );
    }
}
