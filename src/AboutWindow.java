import javax.swing.*;
import java.awt.*;

//Window that displays information about the application.
public class AboutWindow extends JFrame {

    public AboutWindow ( JFrame reference ) {
        setTitle( "About" );
        setLayout( new GridLayout( 6,1 ) );
        setSize( new Dimension( 300,200 ) );
        setResizable( false );
        getContentPane().setBackground( Color.WHITE );

        GridBagConstraints constraints = new GridBagConstraints();

        getContentPane().add( new JPanel().add( new JLabel( new ImageIcon( this.getClass().getResource( "images" +
                "/round_person_black_18dp.png" ) ) ) ) );
        getContentPane().add( new JLabel( "Version 1.0.0",SwingConstants.CENTER ) );
        getContentPane().add( new JLabel( "Created by John M Kinghorn",SwingConstants   .CENTER ) );

        setLocationRelativeTo( reference );
        setVisible( true );
    }
}
