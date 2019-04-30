import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class HintField extends JTextField implements FocusListener {

    public HintField ( int cols ) {
        System.out.println( "CREATED NEW HINT FIELD" );

        setColumns( cols );
        this.setBorder( BorderFactory.createCompoundBorder( this.getBorder(),BorderFactory.createEmptyBorder( 5,5,5,5 ) ) );
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        System.out.println( "Focused" );
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        System.out.println( "Focused Lost" );
    }
}
