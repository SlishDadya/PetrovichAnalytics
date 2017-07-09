package entartaiment.goshan.petrovichanalytics;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SettingsHelper extends SQLiteOpenHelper {
    public SettingsHelper(Context context) {

        super(context, "setiDB", null,3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create table setiDB ("
                + "id text,"

                + "percent text,"
                + "notification text"
                + ");");
        ContentValues cv=new ContentValues();

        cv.put("percent","10");
        cv.put("notification","true");

        db.insert("setiDB",null,cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
