import javax.swing.*;
import java.awt.*;

public class TopPane extends JPanel {

    private GridBagConstraints constraints;
    private JPanel innerPannel,titlePanel;
    private ImageIcon search_icon;
    private JTextField search_field;
    private JButton clear_search;
    private JLabel title;

    public TopPane () {
        super();
        setLayout( new GridBagLayout() );
        setBorder( BorderFactory.createMatteBorder(1,0,1,0,Color.decode("#C4C4C4")) );

        search_icon = new ImageIcon(new ImageIcon( this.getClass().getResource("/images" +
                "/round_search_black_18dp.png") ).getImage().getScaledInstance(24,24,
                Image.SCALE_SMOOTH));

        //Create the Title panel that will just label the field as search.
        this.titlePanel = new JPanel();
        this.title = new JLabel( "Search",SwingConstants.LEFT );
        this.titlePanel.setLayout( new FlowLayout() );
        this.titlePanel.add( title );

        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.weightx = 1.0;
        titleConstraints.gridy = 0;
        titleConstraints.fill = GridBagConstraints.HORIZONTAL;

        this.add( this.titlePanel,titleConstraints );

        this.innerPannel = new JPanel();
        this.innerPannel.setLayout( new GridBagLayout() );
        this.innerPannel.setBorder( BorderFactory.createEmptyBorder( 0,10,10,10 ));

        this.constraints = new GridBagConstraints();
        this.constraints.weightx = 0;
        this.constraints.fill = GridBagConstraints.HORIZONTAL;

        //Create the search field.
        this.search_field = new JTextField();
        this.search_field.setBorder( BorderFactory.createCompoundBorder( this.getBorder(),
                BorderFactory.createEmptyBorder( 5,5,5,5 ) ));

        JLabel icon = new JLabel( search_icon );
        icon.setBorder( BorderFactory.createEmptyBorder( 0,0,0,8 ));


        //Create the clear search button.
        this.clear_search = new JButton( "Clear" );

        this.constraints.weightx = 0;
        this.constraints.gridy = 1;
        this.add( this.innerPannel,this.constraints );
        this.innerPannel.add( icon,this.constraints );
        this.constraints.weightx = 1;
        this.innerPannel.add( this.search_field,this.constraints );
        this.constraints.weightx = 0;
        this.innerPannel.add( this.clear_search,this.constraints );
    }
}
