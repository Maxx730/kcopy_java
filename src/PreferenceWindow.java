import sun.java2d.loops.GeneralRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Actual window that opens when the user clicks edit preferences.
public class PreferenceWindow extends JFrame {
    private JButton clear_button,reset_button,cancel_dialog,confirm_clear;
    private JComboBox < String > notifcation_length;
    private String[] choices = { "Short","Medium","Long" };
    private JPanel notification_panel = new JPanel();
    private JPanel show_notif = new JPanel();
    private JPanel main_panel = new JPanel();
    private JCheckBox show_notifcation = new JCheckBox("" );
    private GridBagConstraints constraints;


    public PreferenceWindow ( DataInterface dat,JFrame reference ) {
        setTitle( "Preferences" );
        setBounds( 0,0,600,400 );
        setLayout( new GridBagLayout() );
        this.constraints = new GridBagConstraints();

        //Create our preferences layouts and widgets here.
        clear_button = new JButton( "Clear History" );
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
                confirm.setLocationRelativeTo( reference );
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

        //Start building our layout with panels
        //Initialize default constraints
        this.constraints.gridx = 1;
        this.constraints.gridy = 1;
        this.constraints.gridwidth = 1;
        this.constraints.gridheight = 1;
        this.constraints.weighty = 1;
        this.constraints.weightx = 1;
        this.constraints.insets = new Insets( 5,5,5,5 );
        this.constraints.anchor = GridBagConstraints.CENTER;
        this.constraints.fill = GridBagConstraints.BOTH;


        //Panel 1
        this.constraints.gridx = 1;
        JPanel panel1 = new JPanel();
        //Add preference labels here.
        panel1.setLayout( new BoxLayout( panel1,BoxLayout.Y_AXIS ));
        panel1.add( new JLabel( "Testing" ) );
        panel1.add( new JLabel( "Testing" ) );
        panel1.add( new JLabel( "Testing" ) );
        panel1.add( new JSeparator( SwingConstants.HORIZONTAL ) );

        add( panel1,this.constraints );

        //Panel 2
        this.constraints.gridx = 2;
        this.constraints.weightx = 0.4;
        JPanel panel2 = new JPanel();
        panel2.setBackground( Color.RED );
        add( panel2,this.constraints );

        setLocationRelativeTo( frame );
        setVisible( true );
    }
}
