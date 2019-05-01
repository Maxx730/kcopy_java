import javax.swing.*;
import java.awt.*;

//Window that displays information about the application.
public class AboutWindow extends JFrame {

    public AboutWindow () {
        setTitle( "About" );
        setLayout( new GridLayout( 4,1 ) );
        setMinimumSize( new Dimension( 300,160 ) );

        GridBagConstraints constraints = new GridBagConstraints();

        getContentPane().add( new JLabel( "Version 1.0.0",SwingConstants.CENTER ) );
        getContentPane().add( new JLabel( "Created by John M Kinghorn",SwingConstants.CENTER ) );
        setVisible( true );
    }
}
