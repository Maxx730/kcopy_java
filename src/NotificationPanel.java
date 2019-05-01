import javax.swing.*;
import java.awt.*;

//Panel that will display infomation at the bottom of the app such as
//when a clipboard item has been copied back into the clipboard.
public class NotificationPanel extends JPanel {

    JLabel message;
    ImageIcon notif_image,scaled,icon;

    public NotificationPanel () {
        message = new JLabel( "TEST MESSAGE" );
        message.setBorder( BorderFactory.createEmptyBorder( 5,5,5,5 ) );
        notif_image = new ImageIcon(new ImageIcon( this.getClass().getResource("/images" +
                "/round_notification_important_black_18dp.png") ).getImage().getScaledInstance(16,16,
                Image.SCALE_SMOOTH));


        setLayout( new FlowLayout( FlowLayout.LEFT ) );
        setBorder( BorderFactory.createMatteBorder( 1,0,0,0,Color.RED) );
        add( new JLabel( notif_image ) );
        add( message );
    }

    public void SetMessage ( String value ) {

    }
}
