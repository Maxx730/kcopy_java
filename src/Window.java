
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window {
    private JFrame frame;
    private JMenuBar menu;
    private JMenu edit,file;
    private Container container;
    private JMenuItem about,preferences;
    private HintField searchField;
    private JPanel searchPanel,clipPanel;
    private TopPane topPanel;
    private Cliplist list;
    private GridBagConstraints constraints;
    private Data data;
    private JScrollPane scrollpane;

    //List of clip objects
    private JSONArray clips;
    private Clipboard clipboard;

    public Window () {
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch ( Exception e ) {
            System.out.println( "ERROR SETTING LOOK AND FEEL, DEFAULTING TO SWING" );
        }

        //Create connection to our database.
        data = new Data();
        clipboard = new Clipboard(new ChangeInterface() {
            @Override
            public void BoardChanged(String value) {
                JSONObject newClip = new JSONObject();

                try {
                    newClip.put( "id",clips.length() );
                    newClip.put( "value",value );

                    clips.put( newClip );
                    list.SetClips( clips );
                    data.UpdateData( clips );
                } catch ( Exception e ) {
                    System.out.println( e.getMessage() );
                }
            }
        });


        //Check if this is the first time running the application,
        //if so we want to create the database for holding all our clips.
        if ( !Preferences.GetPrefBool( "first-run" ) ) {
            System.out.println( "FIRST RUN, CREATING DATABASE NOW" );
            data.CreateDatabase();
            Preferences.SavePrefBoolean( "first-run",true );
        }

        //Next we want to read the data from the database and store the JSONObjects into an array.
        this.list = new Cliplist();
        clips = data.GetClips();
        this.list.SetClips( clips );

        //Create the actual application frame.
        this.frame = new JFrame("kCopy" );
        this.frame.setMinimumSize( new Dimension( 400,400 ) );
        this.menu = new JMenuBar();

        //Create our top level menu items.
        this.file = new JMenu( "File" );
        this.file.setBorder( BorderFactory.createEmptyBorder( 5,5,5,5 ) );

        //Add the edit menu with submenu items.
        this.edit = new JMenu( "Edit" );
        this.about = new JMenuItem( "About", null );
        this.preferences = new JMenuItem( "Preferences",null );
        this.preferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PreferenceWindow pref_window = new PreferenceWindow();
            }
        });

        //Grab the actual content in the window below the menu.
        this.container = this.frame.getContentPane();

        this.searchPanel = new JPanel();
        this.topPanel = new TopPane();
        this.clipPanel = new JPanel( new GridBagLayout() );

        //LAYOUT CONSTRAINTS
        //Constraints to fill horizontally
        this.constraints = new GridBagConstraints();
        this.constraints.fill = GridBagConstraints.HORIZONTAL;
        this.constraints.gridx = 0;
        this.constraints.gridy = 0;
        this.constraints.weightx = this.constraints.weighty = 1.0;
        this.constraints.fill = GridBagConstraints.BOTH;

        this.scrollpane = new JScrollPane();
        this.scrollpane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        this.scrollpane.setViewportBorder( null );
        this.clipPanel.add( this.scrollpane,this.constraints );


        this.searchField = new HintField( 20 );
        this.scrollpane.setViewportView( this.list );
        this.edit.add( this.about );
        this.edit.add( this.preferences );
        this.menu.add( this.file );
        this.menu.add( this.edit );

        this.container.setLayout( new BorderLayout( 10,0 ) );

        this.topPanel.Add( this.searchField );

        //Add all our content to the main window
        frame.setJMenuBar( this.menu );
        frame.add( this.topPanel,BorderLayout.NORTH );
        frame.add( this.clipPanel,BorderLayout.CENTER );
        frame.setBounds( 0,0,400,600 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setVisible( true );
    }
}
