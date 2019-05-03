import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Actual window that opens when the user clicks edit preferences.
public class PreferenceWindow extends JFrame {
    private JButton clear_button,reset_button,cancel_dialog,confirm_clear;
    private JComboBox < String > notifcation_length;
    private String[] choices = { "Short","Medium","Long" };
    private JPanel notification_panel = new JPanel();

    public PreferenceWindow ( DataInterface dat ) {
        setTitle( "Preferences" );
        setBounds( 0,0,400,400 );
        setLayout( new GridLayout(10,1) );

        //Create our preferences layouts and widgets here.
        clear_button = new JButton( "Clear History" );
        notifcation_length = new JComboBox<>( choices );
        reset_button = new JButton( "Reset Preferences" );

        JFrame frame = this;

        //Actions defined here
        clear_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog confirm = new JDialog( frame,"Confirm" );
                confirm.setLayout( new FlowLayout() );
                confirm.setResizable( false );

                JPanel text = new JPanel();
                text.setBorder( BorderFactory.createEmptyBorder( 10,10,10,10 ) );
                text.add( new JLabel( "<html><div style=\"width:200px\"><center>Are you sure you would like to clear " +
                        "the " +
                        "clipboard " +
                        "history?</center></div></html>" ) );

                JPanel buttons = new JPanel( new FlowLayout() );
                cancel_dialog = new JButton( "Cancel" );
                confirm_clear = new JButton( "Confirm" );

                //Close the dialog if the cancel button is pressed.
                cancel_dialog.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        confirm.setVisible( false );
                    }
                });

                confirm_clear.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dat.DataClear();
                        confirm.setVisible( false );
                    }
                });

                buttons.add( confirm_clear );
                buttons.add( cancel_dialog );

                confirm.add( text );
                confirm.add( buttons );
                confirm.setSize( 300,160 );
                confirm.setVisible( true );
            }
        });
        reset_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog reset = new JDialog( frame,"Confirm" );

                reset.setSize( new Dimension( 300,160 ) );
                reset.setVisible( true );
            }
        });

        //Create the different panels that are going to be added to the
        //frame grid layout.
        notification_panel.setLayout( new FlowLayout() );
        notification_panel.add( new JLabel( "Notification Display Length" ) );
        notification_panel.add( notifcation_length );

        add( notification_panel );
        add( clear_button );
        add( reset_button );
        add( new JButton( "Apply" ) );
        setVisible( true );
    }
}
