package in.jivanmuktas.www.marg.database;


import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {
    //private static final String DATABASE_NAME = "jivanmuktadb.db";
    private static final String DATABASE_NAME = "databasesync.db";
    private static final int DATABASE_VERSION = 1;
    //public String databasePath = "";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //databasePath = context.getDatabasePath("AnthropometryDB.db").getPath();
        //Log.i("!!!!DataBase Path",databasePath);
        setForcedUpgrade(DATABASE_VERSION);
    }


}