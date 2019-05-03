import javax.swing.*;
import java.awt.*;

public class TopPane extends JPanel {

    private GridBagConstraints constraints;
    private JPanel innerPannel;
    private ImageIcon search_icon;

    public TopPane () {
        super();
        setLayout( new GridBagLayout() );
        setBorder( BorderFactory.createMatteBorder(1,0,1,0,Color.decode("#C4C4C4")) );

        search_icon = new ImageIcon( this.getClass().getResource( "/images/round_search_black_18dp.png" ));

        this.innerPannel = new JPanel();
        this.innerPannel.setLayout( new GridBagLayout() );
        this.innerPannel.setBorder( BorderFactory.createEmptyBorder( 10,10,10,10 ));

        this.constraints = new GridBagConstraints();
        this.constraints.weightx = 0;
        this.constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel icon = new JLabel( search_icon );
        icon.setBorder( BorderFactory.createEmptyBorder( 0,10,0,5 ));
        this.add( icon,this.constraints );

        this.constraints.weightx = 1;
        this.add( this.innerPannel,this.constraints );
    }

    public void Add ( Component c ) {
        this.innerPannel.add( c,this.constraints );
    }
}
