package ir.sadeghlabs.turn.management;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseManagement extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/ir.sadeghlabs.turn/databases/";

    private static String DB_NAME = "turn.db";
    private static int DB_VERSION = 1;

    private final Context myContext;


    public DatabaseManagement(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }


    public File checkPath() throws IOException {
        String destinationPath = DB_PATH;

        File destination = new File(destinationPath);

        if (!destination.exists()) {
            destination.mkdir();
            destination.createNewFile();
        }

        File database = myContext.getDatabasePath(DB_NAME);

        if (!database.exists()) {
            CopyDatabase(myContext.getAssets().open(DB_NAME), new FileOutputStream(DB_PATH + DB_NAME));
        }

        return database;
    }

    public void CopyDatabase(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();
    }


    public boolean getDatabase() {
        //return SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        File file = new File(DB_PATH + DB_NAME);
        return file.exists();
    }

    public int getLastedRequestCode(){
        SQLiteDatabase database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

        Cursor cursor =  database.rawQuery("select max(RequestCode) From Alarm",null);

        int requestCode = 0;

        if(cursor.getCount() > 0) {
            if(cursor.moveToFirst()) {
                requestCode =  cursor.getInt(0);
            }
        }

        return requestCode + 1;
    }


    public Cursor getContact(){
        try {
            SQLiteDatabase database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

            return database.rawQuery("SELECT * FROM Contact", null);
        }catch (Exception ex){
            return null;
        }
    }






    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS Location");

        //db.execSQL("CREATE TABLE IF NOT EXISTS Location(Location_Id INTEGER NOT NULL,Name TEXT NOT NULL,Active INTEGER NOT NULL,PRIMARY KEY(Location_Id)");

        db.execSQL("PRAGMA user_version = " + newVersion);
    }
}
