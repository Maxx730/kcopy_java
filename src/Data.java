import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

//Class that communicates with our SQLite database to store
//and retrieve clips.
public class Data {
    private FileOutputStream output;
    //Clips as JSON Object array.
    private JSONArray clips;
    private JSONObject data;

    public void CreateDatabase () {
        try {
            this.output = new FileOutputStream( "clips.data" );
            String test = "{" +
                    "clips:[]" +
                    "}";
            byte[] bytes = test.getBytes();

            try {
                this.output.write( bytes );
            } catch ( Exception e ) {
                System.out.println( e.getMessage() );
            }
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
        }
    }

    public JSONArray GetClips () {
        JSONArray clips = new JSONArray();

        try {
            //Read the database file.
            File fil = new File( "clips.data" );
            FileReader read = new FileReader( fil );

            int i;
            String output = "";

            while ( ( i = read.read() ) != -1 ) {
                output += ( char ) i;
            }

            data = new JSONObject( output );
            clips = data.getJSONArray( "clips" );

        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
        }


        return clips;
    }

    public JSONArray UpdateData ( JSONArray clips ) {
        try {
            FileOutputStream out = new FileOutputStream( "clips.data" );
            data.put( "clips",clips );

            try {
                byte[] bytes = data.toString().getBytes();
                out.write( bytes );
            } catch ( Exception e ) {
                System.out.println( e.getMessage() );
            }
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
        }

        return clips;
    }

    //Takes in a file path and writes a backup of the json file to the given directory.
    public void WriteBackup( File output,String filename ) {
        try {
            FileOutputStream out = new FileOutputStream( output.getPath() );

            JSONArray clips = GetClips();

            try {
                byte[] bytes = clips.toString().getBytes();
                out.write( bytes );
            } catch( Exception e) {
                System.out.println( e.getMessage() );
            }
        } catch( Exception e ) {
            System.out.println( e.getMessage() );
        }
    }
}
