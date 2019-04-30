//Class used to keep user preferences, such as
//is it the first time opening said application.
public final class Preferences {
    private static java.util.prefs.Preferences prefs;

    public static void ClearPrefs () {
        prefs = java.util.prefs.Preferences.userRoot();

        //Reset all the preferences that can be set in this application.
        prefs.remove( "first-run" );
    }

    public static void SavePrefBoolean ( String name,boolean value ) {
        prefs = java.util.prefs.Preferences.userRoot();

        try {
            prefs.putBoolean( name,value );
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
        }
    }

    public static boolean GetPrefBool ( String name ) {
        prefs = java.util.prefs.Preferences.userRoot();

        try {
            System.out.println( "PREF VALUE ::: " + prefs.getBoolean( name,false ) );
            return prefs.getBoolean( name,false );
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
            return true;
        }
    }

    public static int GetPrefInt ( String name ) {
        return 0;
    }
}
