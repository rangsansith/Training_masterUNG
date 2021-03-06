package appewtc.masterung.myvideo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 1/14/15 AD.
 */
public class ServiceTABLE {

    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String TABLE_SERVICE = "serviceTABLE";
    public static final String COLUMN_ID_SERVICE = "_id";
    public static final String COLUMN_STRORY = "Story";
    public static final String COLUMN_IMAGE = "Image";
    public static final String COLUMN_VIDEO = "Video";

    public ServiceTABLE(Context context) {

        objMyOpenHelper = new MyOpenHelper(context);
        writeSQLite = objMyOpenHelper.getWritableDatabase();
        readSQLite = objMyOpenHelper.getReadableDatabase();

    }   // Constructor

    //Read All Data
    public Cursor readAllData() {

        Cursor objCursor = readSQLite.query(TABLE_SERVICE, new String[]{COLUMN_ID_SERVICE, COLUMN_STRORY, COLUMN_IMAGE, COLUMN_VIDEO}, null, null, null, null, null);

        if (objCursor != null) {
            objCursor.moveToFirst();
        }


        return objCursor;
    }   // readAllData



    public long addValueToServie(Context context, String strStory, String strImage, String strVideo) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_STRORY, strStory);
        objContentValues.put(COLUMN_IMAGE, strImage);
        objContentValues.put(COLUMN_VIDEO, strVideo);

        return writeSQLite.insert(TABLE_SERVICE, null, objContentValues);
    }   // aaValueToServic

}   // Main Class
