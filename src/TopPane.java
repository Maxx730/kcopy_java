import javax.swing.*;
import java.awt.*;

public class TopPane extends JPanel {

    private GridBagConstraints constraints;
    private JPanel innerPannel;

    public TopPane () {
        super();
        setLayout( new GridBagLayout() );
        setBorder( BorderFactory.createMatteBorder(1,0,1,0,Color.decode("#C4C4C4")) );
        setBackground( Color.WHITE );

        this.innerPannel = new JPanel();
        this.innerPannel.setLayout( new GridBagLayout() );
        this.innerPannel.setBorder( BorderFactory.createEmptyBorder( 10,10,10,10 ));

        this.constraints = new GridBagConstraints();
        this.constraints.weightx = 1;
        this.constraints.fill = GridBagConstraints.HORIZONTAL;
        this.add( this.innerPannel,this.constraints );
    }

    public void Add ( Component c ) {
        this.innerPannel.add( c,this.constraints );
    }
}
