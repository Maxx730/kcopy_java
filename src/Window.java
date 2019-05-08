
import com.boxysystems.jgoogleanalytics.FocusPoint;
import com.boxysystems.jgoogleanalytics.JGoogleAnalyticsTracker;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Window {
    private JFrame frame;
    private JMenuBar menu;
    private JMenu edit,file,help;
    private Container container;
    private JMenuItem about,preferences,imp,exp;
    private JPanel searchPanel,clipPanel,favoritesPanel;
    private TopPane topPanel;
    private Cliplist list;
    private GridBagConstraints constraints;
    private Data data;
    private JScrollPane scrollpane;
    private Timer timout;
    private JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker("kCopy","1.0.0","UA-85720731-3");

    //List of clip objects
    private JSONArray clips;
    private Clipboard clipboard;
    private NotificationPanel notif_panel;

    public Window () {
        Preferences.ClearPrefs();
        //Test Google analytics tracking...
        FocusPoint focus = new FocusPoint( "kCopy Open" );
        tracker.trackAsynchronously( focus );

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
                    newClip.put( "date",new Date().toString() );

                    clips.put( newClip );
                    list.SetClips( clips );
                    data.UpdateData( clips );

                    timout.schedule( new TimerTask() {
                        @Override
                        public void run() {
                            ShowNotificationPanel( value );
                        }
                    },0);
                    FocusPoint focus = new FocusPoint( "Clip Copied" );
                    tracker.trackAsynchronously( focus );
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
            Preferences.SavePrefBoolean( "show-notification", true );
            Preferences.SavePrefString( "notification-length","short" );
        }

        //Initialize the timer used for showing and hiding the notification panel.
        timout = new Timer();

        //Next we want to read the data from the database and store the JSONObjects into an array.
        this.list = new Cliplist(new ChangeInterface() {
            @Override
            public void BoardChanged(String value) {
                clipboard.SetClip( value );
            }
        },frame );
        clips = data.GetClips();
        this.list.SetClips( clips );

        //Create the actual application frame.
        this.frame = new JFrame("kCopy" );
        this.frame.setMinimumSize( new Dimension( 400,400 ) );
        this.menu = new JMenuBar();

        Border spacing = BorderFactory.createEmptyBorder( 10,16,10,16 );

        //Create our top level menu items.
        this.file = new JMenu( "File" );
        this.file.setBorder( spacing );
        this.imp = new JMenuItem( "  Save",
                new ImageIcon( new ImageIcon( this.getClass().getResource( "images/round_save_black_18dp.png" )).getImage().getScaledInstance( 18,18,Image.SCALE_SMOOTH )) );
        this.imp.setBorder( BorderFactory.createEmptyBorder( 8,10,8,10 ));
        this.exp = new JMenuItem( "  Open",new ImageIcon( new ImageIcon( this.getClass().getResource( "images" +
                "/round_folder_black_18dp.png" )).getImage().getScaledInstance( 18,18,Image.SCALE_SMOOTH )) );
        this.exp.setBorder( BorderFactory.createEmptyBorder( 8,10,8,10 ));
        this.file.add( imp );
        this.file.add( exp );

        //Add the edit menu with submenu items.
        this.edit = new JMenu( "Edit" );
        this.edit.setBorder( spacing );
        this.about = new JMenuItem( "  About",new ImageIcon( new ImageIcon( this.getClass().getResource(
                "images" +
                        "/round_person_black_18dp.png" )).getImage().getScaledInstance( 18,18,Image.SCALE_SMOOTH )) );
        this.about.setBorder( BorderFactory.createEmptyBorder( 8,10,8,10 ));
        this.about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutWindow about = new AboutWindow( frame );
            }
        });
        this.preferences = new JMenu( "  Preferences" );
        BuildPreferenceMenu( this.preferences );
        this.preferences.setBorder( BorderFactory.createEmptyBorder( 8,10,8,10 ));
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
                        FocusPoint focus = new FocusPoint( "Preferences Opened" );
                        tracker.trackAsynchronously( focus );
                    }
                },frame );
            }
        });

        //Create the help menu
        this.help = new JMenu("Help" );
        this.help.setBorder( spacing );

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

        this.scrollpane.setViewportView( this.list );
        this.help.add( this.about );
        this.edit.add( this.preferences );
        this.menu.add( this.file );
        this.menu.add( this.edit );
        this.menu.add( this.help );

        this.container.setLayout( new BorderLayout( 10,0 ) );

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

    //Takes in the root menu item and builds out all the preference options.
    private void BuildPreferenceMenu( JMenuItem prefs ) {
        Border space = BorderFactory.createEmptyBorder( 8,10,8,10 );
        JCheckBoxMenuItem show_notif = new JCheckBoxMenuItem( "Notifications" );
        JMenuItem clear_hist = new JMenuItem( "Clear History" );
        JMenu notification_length = new JMenu( "Notification Duration" );
        ButtonGroup group = new ButtonGroup();

        //Create length radio buttons.
        JRadioButtonMenuItem shorter = new JRadioButtonMenuItem( "Short" );
        JRadioButtonMenuItem medium = new JRadioButtonMenuItem( "Medium" );
        JRadioButtonMenuItem longer = new JRadioButtonMenuItem( "Long" );

        group.add( shorter );
        group.add( medium );
        group.add( longer );

        //Check our prefs here to display ticked on settings etc.
        if ( Preferences.GetPrefBool( "show-notification" ) ) {
            show_notif.setState( true );
        }

        switch ( Preferences.GetPrefString( "notification-length" ) ) {
            case "medium":
                medium.setSelected( true );
                break;
            case "longer":
                longer.setSelected( true );
                break;
                default:
                    shorter.setSelected( true );
                    break;
        }

        show_notif.setBorder( space );
        clear_hist.setBorder( space );
        shorter.setBorder( space );
        medium.setBorder( space );
        longer.setBorder( space );
        notification_length.setBorder( space );

        //Add Sub menu items.
        notification_length.add( shorter );
        notification_length.add( medium );
        notification_length.add( longer );

        prefs.add( show_notif );
        prefs.add( notification_length );
        prefs.add( new JSeparator(SwingConstants.HORIZONTAL) );
        prefs.add( clear_hist );
    }
}
