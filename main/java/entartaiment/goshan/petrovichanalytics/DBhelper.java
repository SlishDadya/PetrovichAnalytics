package entartaiment.goshan.petrovichanalytics;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DBhelper extends SQLiteOpenHelper {
    public DBhelper(Context context) {

        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "UUID text,"
                + "name text,"
                + "price text,"
                + "number text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
