import javax.swing.*;
import java.awt.*;

//Class that creates the tabbed pane that will hold different
//contents based on the given selected tab.
public class ClipTabs extends JTabbedPane {

    public ClipTabs ( int pos ) {
        super( pos );
        setBounds( 150,150,200,200 );
        setBorder( BorderFactory.createEmptyBorder( 5,0,0,0 ) );

    }

    public void AddTab(String name, Component comp ) {
        add( name,comp );
    }
}
