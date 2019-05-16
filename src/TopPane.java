import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TopPane extends JPanel {

    private GridBagConstraints constraints;
    private JPanel innerPannel,lowerPanel,mainPanel;
    private ImageIcon search_icon;
    private JTextField search_field;
    private JButton clear_search,refresh_list;
    private JLabel title;

    public TopPane ( SearchInterface search ) {
        super();
        setLayout( new BorderLayout() );
        setBorder( BorderFactory.createMatteBorder(1,0,1,0,Color.decode("#C4C4C4")) );

        this.lowerPanel = new JPanel( );
        this.lowerPanel.setLayout( new BoxLayout( lowerPanel,BoxLayout.X_AXIS ) );
        this.lowerPanel.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createMatteBorder( 1,0,0,0,
                Color.decode("#C4C4C4") ),BorderFactory.createEmptyBorder( 6,8,6,10 ) ) );

        //Panel that contains the actual search field.
        this.innerPannel = new JPanel();
        this.innerPannel.setLayout( new GridBagLayout() );
        this.innerPannel.setBorder( BorderFactory.createEmptyBorder( 10,0,10,10 ));

        this.constraints = new GridBagConstraints();
        this.constraints.weightx = 1;
        this.constraints.fill = GridBagConstraints.HORIZONTAL;

        //Create the search field.
        this.search_field = new JTextField();
        this.search_field.setBorder( BorderFactory.createCompoundBorder( this.search_field.getBorder(),
                BorderFactory.createEmptyBorder( 6,8,6,8 ) ));
        this.search_field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if ( search_field.getText().equals( "Search" ) ) {
                    search_field.setText( "" );
                    search_field.setForeground( Color.BLACK );
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                search_field.setText( "Search" );
                search_field.setForeground( Color.decode( "#999999" ) );
            }
        });

        this.search_field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                search.OnSearch( search_field.getText() );
            }
        });

        ImageIcon search_icon = new ImageIcon( getClass().getResource("images/49.png"));


        this.add( this.innerPannel,BorderLayout.NORTH );
        this.innerPannel.add( new JLabel( search_icon ),this.constraints );
        this.constraints.weightx = 12;
        this.innerPannel.add( this.search_field,this.constraints );
    }
}
