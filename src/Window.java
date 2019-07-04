
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Window {
    private JFrame frame;
    private JMenuBar menu;
    private JMenu edit,file,help;
    private Container container;
    private JMenuItem about,preferences,imp,exp,exit;
    private JPanel searchPanel,notificationPanel;
    private TopPane topPanel;
    private Cliplist list;
    private GridBagConstraints constraints;
    private Data data;
    private JScrollPane scrollpane;
    private Timer timout;
    private JLayeredPane layered;
    private JLabel notif_text;

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
            public void BoardChanged(String value,boolean updateList) {
                JSONObject newClip = new JSONObject();
                Timer tim = new Timer();

                if (updateList) {
                    try {
                        newClip.put( "id",clips.length() );
                        newClip.put( "value",value );
                        newClip.put( "favorite",false );
                        newClip.put( "date",new Date().toString() );

                        clips.put( newClip );
                        list.SetClips(data.UpdateData( clips ));
                    } catch ( Exception e ) {
                        System.out.println( e.getMessage() );
                    }
                }

                //Always show the notification even if the list is not updated.
                tim.schedule( new TimerTask() {
                    @Override
                    public void run() {
                        if ( Preferences.GetPrefBool("show-notification" ) ) {
                            ShowNotificationPanel( value );
                        }

                        tim.cancel();
                        tim.purge();
                    }
                },0);
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
            public void BoardChanged(String value,boolean updateList) {
                clipboard.SetClip( value );
            }
        },frame );

        clips = data.GetClips();
        this.list.SetClips( clips );

        //Create the actual application frame.
        this.frame = new JFrame("kCopy" );
        this.frame.setMinimumSize( new Dimension( 400,600 ) );
        ImageIcon icon = new ImageIcon("images/icon.ico");
        this.frame.setIconImage(icon.getImage());
        this.menu = new JMenuBar();


        //Create our top level menu items.
        this.file = new JMenu( "File" );
        this.imp = new JMenuItem( " Save          Ctrl+S" );
        this.imp.setBorder( BorderFactory.createEmptyBorder( 4,6,4,6 ));
        this.imp.setIcon(new ImageIcon( new ImageIcon( getClass().getResource("images/45" +
                ".png") ).getImage().getScaledInstance( 16,16,Image.SCALE_SMOOTH )));
        this.exp = new JMenuItem( " Open          Ctrl+O");
        this.exp.setBorder( BorderFactory.createEmptyBorder( 4,6,4,6 ));
        this.exp.setIcon(new ImageIcon( new ImageIcon( getClass().getResource("images/47" +
                ".png") ).getImage().getScaledInstance( 16,16,Image.SCALE_SMOOTH )));
        this.exit = new JMenuItem( " Exit" );
        this.exit.setIcon( new ImageIcon( getClass().getResource( "images/50.png" ) ) );
        this.exit.setBorder( BorderFactory.createEmptyBorder( 4,6,4,6 ));

        //First menu action listeners.
        this.exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible( false );
                frame.dispose();
                System.exit( 0 );
            }
        });

        this.imp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Default the chooser path to whatever the home directory for the user is.
                JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory() );
                choose.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

                if ( choose.showSaveDialog( null ) == JFileChooser.APPROVE_OPTION ) {
                    File selected = choose.getSelectedFile();
                    //Now we want to write a backup of the json file to the said chosen directory.
                    data.WriteBackup( selected,choose.getName() );
                }
            }
        });

        this.exp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Default the chooser path to whatever the home directory for the user is.
                JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory() );
                choose.setFileSelectionMode( JFileChooser.FILES_ONLY );

                if ( choose.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
                    File selected = choose.getSelectedFile();

                    JDialog dia = new JDialog( frame,"Confirm" );
                    dia.setVisible( true );
                }
            }
        });

        this.file.add( imp );
        this.file.add( exp );
        this.file.add( exit );

        //Add the edit menu with submenu items.
        this.edit = new JMenu( "Edit" );
        this.about = new JMenuItem( " About" );
        this.about.setIcon(new ImageIcon( new ImageIcon( getClass().getResource("images/02" +
                ".png") ).getImage().getScaledInstance( 16,16,Image.SCALE_SMOOTH )));
        this.about.setBorder( BorderFactory.createEmptyBorder( 4,6,4,6 ));

        JDialog aboutd = new JDialog( frame,"About" );

        this.about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!aboutd.isVisible()){
                    aboutd.getContentPane().setLayout( new BorderLayout() );
                    aboutd.getContentPane().setBackground( Color.WHITE );
                    aboutd.setResizable( false );
                    aboutd.setLocationRelativeTo( frame );
                    aboutd.setSize( new Dimension( 300,115 ) );

                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
                    panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

                    JLabel version = new JLabel("<html><div style=\"width:200px;padding: 3px;\"><center>kCopy v1.0.0</center></div></html>");
                    version.setBackground(Color.RED);
                    panel.add(version);
                    panel.add(new JLabel("<html><div style=\"width:200px;padding:3px;\"><center>Created by John M Kinghorn</center></div></html>"));
                    panel.add(new JLabel("<html><div style=\"width:200px;padding:3px;\"><center>View on Github</center></div></html>"));
                    aboutd.add(new JPanel(),BorderLayout.WEST);
                    aboutd.add(new JPanel(),BorderLayout.EAST);
                    aboutd.add(panel,BorderLayout.CENTER);
                    aboutd.setVisible( true );
                    aboutd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                } else {
                    aboutd.requestFocus();
                }

            }
        });

        this.preferences = new JMenu( " Preferences" );
        this.preferences.setIcon(new ImageIcon( new ImageIcon( getClass().getResource("images/41" +
                ".png") ).getImage().getScaledInstance( 16,16,Image.SCALE_SMOOTH )));
        BuildPreferenceMenu( this.preferences );
        this.preferences.setBorder( BorderFactory.createEmptyBorder( 4,6,4,6 ));
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
                },frame );
            }
        });

        //Create the help menu
        this.help = new JMenu("Help" );

        //Grab the actual content in the window below the menu.
        this.container = this.frame.getContentPane();
        this.layered = new JLayeredPane();

        //Create the top pane that contains the search functionality.
        this.searchPanel = new JPanel();
        this.topPanel = new TopPane(new SearchInterface() {
            //What needs to be done when something has been typed into the search field.
            @Override
            public void OnSearch(String value) {
                JSONArray newClips = new JSONArray();

                for ( int i = 0;i < clips.length();i++ ) {
                    try  {
                        JSONObject obj = clips.getJSONObject( i );

                        if ( obj.getString("value").contains( value )) {
                            newClips.put( clips.getJSONObject( i ) );
                        }
                    } catch ( Exception e ) {
                        System.out.println( e.getMessage() );
                    }
                }

                list.SetClips( newClips );
            }
        });

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
        this.scrollpane.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.getColor("#F1F1F1")));
        this.scrollpane.setSize( frame.getSize().width - 15,frame.getSize().height - this.topPanel.getHeight() - 60 );
        this.list.requestFocus();

        //Create our tabs widget to house the favorites and clips panels
        ClipTabs tabs = new ClipTabs( JTabbedPane.TOP );
        tabs.AddTab( " Clips  ",this.scrollpane );

        this.layered.setBounds( 0,100,frame.getSize().width,
                frame.getSize().height );
        this.layered.add( this.scrollpane, new Integer( 0 ), 0 );

        //Creates the panel that will show the notification when it pops up.
        this.notificationPanel = new JPanel();
        this.notificationPanel.setSize( new Dimension( 100,100 ) );
        this.notificationPanel.setBackground( new Color( 0,0,0,200 ) );
        this.notificationPanel.setBounds( 0,frame.getSize().height - 75,frame.getSize().width,40 );
        this.notificationPanel.setLayout( new BorderLayout() );

        JLabel notif_frame = new JLabel(new ImageIcon( getClass().getResource("images/43.png") ) );
        notif_frame.setBorder( BorderFactory.createEmptyBorder( 0,10,0,10 ) );
        this.notif_text = new JLabel( "TESTING" );
        this.notif_text.setForeground( Color.WHITE );

        //Add the panel to the layered pane.
        this.notificationPanel.add( notif_frame,BorderLayout.WEST );
        this.notificationPanel.add( this.notif_text,BorderLayout.CENTER );
        this.layered.add( this.notificationPanel,new Integer( 1 ), 0);
        HideNotificationPanel();

        this.scrollpane.setViewportView( this.list );
        this.help.add( this.about );
        this.edit.add( this.preferences );
        this.menu.add( this.file );
        this.menu.add( this.edit );
        this.menu.add( this.help );

        this.container.setLayout( new BorderLayout( 10,0 ) );

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        //Add all our content to the main window
        frame.setJMenuBar( this.menu );
        frame.add( this.topPanel,BorderLayout.NORTH );
        frame.add( this.layered,BorderLayout.CENTER );
        frame.setBounds( 0,0,frame.getSize().width, frame.getSize().height );
        frame.setLocation( (d.width / 2) - (frame.getSize().width / 2),(d.height / 2) - (frame.getSize().height/2) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("./images/45.png"));
        frame.setVisible( true );

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                scrollpane.setSize( frame.getSize().width - 15,frame.getSize().height - topPanel.getHeight() - 60 );
                notificationPanel.setBounds( 0,frame.getSize().height - 125,frame.getSize().width,40 );
            }
        });
    }

    //Remove these two functions eventually
    private void ShowNotificationPanel ( String value ) {
        String truncated;
        this.notificationPanel.setVisible( true );

        //Truncate the copied string if it is longer than a certain amount for the notification.
        if(value.length() > 100){
            truncated = value.substring(0,50) + "...";
        } else {
            truncated = value;
        }

        this.notif_text.setText( "'" + truncated + "' copied to clip history.");

        //Grab the length the notification should show.
        int length;

        switch ( Preferences.GetPrefString("notification-length" ) ) {
            case "longer":
                length = 3000;
                break;
            case "medium":
                length = 2000;
                break;
            default:
                length = 1000;
                break;
        }

        timout.schedule(new TimerTask() {
            @Override
            public void run() {
                HideNotificationPanel();
            }
        },length );
    }

    private void HideNotificationPanel () {
        this.notificationPanel.setVisible( false );
    }

    //Takes in the root menu item and builds out all the preference options.
    private void BuildPreferenceMenu( JMenuItem prefs ) {
        Border space = BorderFactory.createEmptyBorder( 4,6,4,6 );
        JCheckBoxMenuItem show_notif = new JCheckBoxMenuItem( " Notifications" );
        show_notif.setIcon(new ImageIcon( new ImageIcon( getClass().getResource("images/12" +
                ".png") ).getImage().getScaledInstance( 16,16,Image.SCALE_SMOOTH )));

        //What happens when the checkbox is checked or not checked etc.
        show_notif.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences.SavePrefBoolean( "show-notification",show_notif.getState() );
            }
        });

        JMenuItem clear_hist = new JMenuItem( " Clear History" );
        clear_hist.setIcon(new ImageIcon( new ImageIcon( getClass().getResource("images/01" +
                ".png") ).getImage().getScaledInstance( 16,16,Image.SCALE_SMOOTH )));
        JMenu notification_length = new JMenu( " Notification Duration" );
        notification_length.setIcon(new ImageIcon( new ImageIcon( getClass().getResource("images/37" +
                ".png") ).getImage().getScaledInstance( 16,16,Image.SCALE_SMOOTH )));
        ButtonGroup group = new ButtonGroup();

        //Create length radio buttons.
        JRadioButtonMenuItem shorter = new JRadioButtonMenuItem( "Short" );
        JRadioButtonMenuItem medium = new JRadioButtonMenuItem( "Medium" );
        JRadioButtonMenuItem longer = new JRadioButtonMenuItem( "Long" );

        group.add( shorter );
        group.add( medium );
        group.add( longer );

        shorter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences.SavePrefString("notification-length","short" );
            }
        });

        medium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences.SavePrefString("notification-length","medium" );
            }
        });

        longer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences.SavePrefString("notification-length","longer" );
            }
        });

        //Clear history action event.
        clear_hist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog confirm = new JDialog( frame,"Confirm" );
                confirm.setLayout( new FlowLayout() );
                confirm.setResizable( false );
                confirm.getContentPane().setBackground( Color.WHITE );

                JPanel text = new JPanel();
                text.setBackground( Color.WHITE );
                text.setBorder( BorderFactory.createEmptyBorder( 10,10,10,10 ) );
                text.add( new JLabel( "<html><div style=\"width:200px\"><center>Are you sure you would like to clear " +
                        "the " +
                        "clipboard " +
                        "history?</center></div></html>" ) );

                JPanel buttons = new JPanel( new FlowLayout() );
                buttons.setBackground( Color.WHITE );
                JButton cancel_dialog = new JButton( "Cancel" );
                JButton confirm_clear = new JButton( "Confirm" );

                cancel_dialog.setIcon( new ImageIcon( getClass().getResource("images/cancel.png") ) );
                confirm_clear.setIcon( new ImageIcon( getClass().getResource( "images/check.png") ) );

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
                        //Create a new database to clear all the clips.
                        data.CreateDatabase();
                        //Clear out the clips list.
                        clips = new JSONArray();
                        data.UpdateData( clips );
                        //Empty the list in the UI
                        list.SetClips( clips );
                        confirm.setVisible( false );
                    }
                });

                buttons.add( confirm_clear );
                buttons.add( cancel_dialog );

                confirm.add( text );
                confirm.add( buttons );
                confirm.setSize( 300,160 );
                confirm.setLocationRelativeTo( frame );
                confirm.setVisible( true );
            }
        });

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
