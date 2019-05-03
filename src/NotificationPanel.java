import javax.swing.*;
import java.awt.*;

//Panel that will display infomation at the bottom of the app such as
//when a clipboard item has been copied back into the clipboard.
public class NotificationPanel extends JPanel {

    JLabel message = new JLabel( "TEST MESSAGE" );;
    ImageIcon notif_image,scaled,icon;

    public NotificationPanel () {

        message.setBorder( BorderFactory.createEmptyBorder( 5,5,5,5 ) );
        message.setPreferredSize( new Dimension( 350,30 ) );
        notif_image = new ImageIcon(new ImageIcon( this.getClass().getResource("/images" +
                "/round_title_black_18dp.png") ).getImage().getScaledInstance(18,18,
                Image.SCALE_SMOOTH));


        setLayout( new FlowLayout( FlowLayout.LEFT ) );
        add( new JLabel( notif_image ) );
        add( message );
    }

    public void SetMessage ( String value ) {
        message.setText( "'" + value + "' saved to history." );
    }
}
