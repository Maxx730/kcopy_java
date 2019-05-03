
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Window {
    private JFrame frame;
    private JMenuBar menu;
    private JMenu edit,file;
    private Container container;
    private JMenuItem about,preferences,imp,exp;
    private HintField searchField;
    private JPanel searchPanel,clipPanel,favoritesPanel;
    private TopPane topPanel;
    private Cliplist list;
    private GridBagConstraints constraints;
    private Data data;
    private JScrollPane scrollpane;
    private Timer timout;

    //List of clip objects
    private JSONArray clips;
    private Clipboard clipboard;
    private NotificationPanel notif_panel;

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
                    newClip.put( "favorite",false );

                    clips.put( newClip );
                    list.SetClips( clips );
                    data.UpdateData( clips );

                    timout.schedule( new TimerTask() {
                        @Override
                        public void run() {
                            ShowNotificationPanel( value );
                        }
                    },0);
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

        //Initialize the timer used for showing and hiding the notification panel.
        timout = new Timer();

        //Next we want to read the data from the database and store the JSONObjects into an array.
        this.list = new Cliplist(new ChangeInterface() {
            @Override
            public void BoardChanged(String value) {
                clipboard.SetClip( value );
            }
        });
        clips = data.GetClips();
        this.list.SetClips( clips );

        //Create the actual application frame.
        this.frame = new JFrame("kCopy" );
        this.frame.setMinimumSize( new Dimension( 400,400 ) );
        this.menu = new JMenuBar();

        //Create our top level menu items.
        this.file = new JMenu( "File" );
        this.file.setBorder( BorderFactory.createEmptyBorder( 5,5,5,5 ) );
        this.imp = new JMenuItem( "Import" );
        this.exp = new JMenuItem( "Export" );
        this.file.add( imp );
        this.file.add( exp );

        //Add the edit menu with submenu items.
        this.edit = new JMenu( "Edit" );
        this.about = new JMenuItem( "About", null );
        this.about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutWindow about = new AboutWindow();
            }
        });
        this.preferences = new JMenuItem( "Preferences",null );
        this.preferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PreferenceWindow pref_window = new PreferenceWindow(new DataInterface() {
                    @Override
                    public void DataClear() {
                        //Create a new database to clear all the clips.
                        data.CreateDatabase();
                        //Clear out the clips list.
                        clips = new JSONArray();
                        data.UpdateData( clips );
                        //Empty the list in the UI
                        list.SetClips( clips );
                    }
                });
            }
        });

        //Grab the actual content in the window below the menu.
        this.container = this.frame.getContentPane();

        this.searchPanel = new JPanel();
        this.topPanel = new TopPane();
        this.clipPanel = new JPanel( new GridBagLayout() );
        this.favoritesPanel = new JPanel( new GridLayout() );

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

        //Create our tabs widget to house the favorites and clips panels
        ClipTabs tabs = new ClipTabs( JTabbedPane.TOP );
        tabs.AddTab( " Clips  ",this.scrollpane );
        tabs.AddTab( " Favorites  ",this.favoritesPanel );
        this.clipPanel.add( tabs,this.constraints );


        this.searchField = new HintField( 20 );
        this.scrollpane.setViewportView( this.list );
        this.edit.add( this.about );
        this.edit.add( this.preferences );
        this.menu.add( this.file );
        this.menu.add( this.edit );

        this.container.setLayout( new BorderLayout( 10,0 ) );

        //Logic for search entry field
        this.topPanel.Add( this.searchField );
        this.searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                //Call a search function that will rerender the list based on the
                //given value.

            }
        });

        //Add all our content to the main window
        frame.setJMenuBar( this.menu );

        frame.add( this.topPanel,BorderLayout.NORTH );
        frame.add( this.clipPanel,BorderLayout.CENTER );

        //Panel that shows notifications, we are going to need to remove it after a
        //set amount of time.
        notif_panel = new NotificationPanel();
        frame.add( notif_panel,BorderLayout.SOUTH );

        HideNotificationPanel();

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setBounds( 0,0,500,600 );
        frame.setLocation( (d.width / 2) - (frame.getSize().width / 2),(d.height / 2) - (frame.getSize().height/2) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setVisible( true );
    }

    private void ShowNotificationPanel ( String value ) {
        System.out.println( "SHOWING NOTIFICATION PANEL" );
        notif_panel.SetMessage( value );
        frame.add( notif_panel,BorderLayout.SOUTH );
        frame.revalidate();

        timout.schedule(new TimerTask() {
            @Override
            public void run() {
                HideNotificationPanel();
            }
        },1500 );
    }

    private void HideNotificationPanel () {
       frame.remove( notif_panel );
       frame.revalidate();
    }
}
