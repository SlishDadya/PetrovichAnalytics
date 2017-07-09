package entartaiment.goshan.petrovichanalytics;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class ShareArray {

    private DBhelper mDBhelper;
    private SettingsHelper mSettingsHelper;
    private static ShareArray mShareArray;
    private ShareArray(Context context)
    {
        mDBhelper=new DBhelper(context.getApplicationContext());
        mSettingsHelper=new SettingsHelper(context.getApplicationContext());
        ContentValues cv = new ContentValues();
        SQLiteDatabase db=mSettingsHelper.getWritableDatabase();

    }
    public static ShareArray getShareArray(Context context)
    {
        if(mShareArray==null) mShareArray=new ShareArray(context);
        return mShareArray;
    }
    public void updatePercent(String percent)
    {
        ContentValues cv=new ContentValues();
        SQLiteDatabase db=mSettingsHelper.getWritableDatabase();

        cv.put("percent",percent);
        db.update("setiDB",cv,"id = ?",new String[]{"1"});
        db.close();
    }
    public void updateNotificcation(String percent)
    {
        ContentValues cv=new ContentValues();
        SQLiteDatabase db=mSettingsHelper.getWritableDatabase();

        cv.put("notification",percent);
        db.update("setiDB",cv,"id = ?",new String[]{"1"});
        db.close();
    }
    public Bundle getSettings()
    {
        Bundle bundle=new Bundle();
        SQLiteDatabase db=mSettingsHelper.getWritableDatabase();
        Cursor c = db.query("setiDB", null, null, null, null, null, null);
            c.moveToFirst();
            String percent = c.getString(c.getColumnIndex("percent"));
            String notification = c.getString(c.getColumnIndex("notification"));
            bundle.putSerializable("percent", percent);
            bundle.putSerializable("notification", notification);
            return bundle;

    }
    public ArrayList<Share> getArray()
    {

        SQLiteDatabase database=mDBhelper.getWritableDatabase();

        Cursor c = database.query("mytable", null, null, null, null, null, null);
        ArrayList<Share> array=new ArrayList<>();

        if((c.moveToFirst())) {
        }
        int UUIDcolomnIndex=c.getColumnIndex("UUID");
        int NameColomnIndex=c.getColumnIndex("name");
        int PriceColomnIndex=c.getColumnIndex("price");
        int NumColomnIndex=c.getColumnIndex("number");
        while(c.moveToNext()){
            Share share=new Share(UUID.fromString(c.getString(UUIDcolomnIndex)));
            share.setName(c.getString(NameColomnIndex));
            //  share.setUUID(UUID.fromString(c.getString(UUIDcolomnIndex)));
            share.setPrice(Double.parseDouble(c.getString(PriceColomnIndex)));
            share.setNum(Integer.parseInt(c.getString(NumColomnIndex)));
            array.add(share);
        }

             return array;
    }
    public Share getShare(UUID id)
    {
        ArrayList<Share> list=new ArrayList<>();
        list=getArray();
        if(list!=null) {
            for (Share share : list) {
                if (id.toString().equals(share.getUUID().toString())) {
                    return share;
                }
            }
        }
        return new Share(id);
    }
    public void addShare(Share share)
    {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db=mDBhelper.getWritableDatabase();

        cv.put("UUID",share.getUUID().toString());
        cv.put("name",share.getName());
        cv.put("price",share.getPrice());
        cv.put("number",share.getNum());

        db.insert("mytable",null,cv);
        db.close();
    }
    public void updateShare(Share share,UUID id)
    {
        ContentValues cv=new ContentValues();
        SQLiteDatabase db=mDBhelper.getWritableDatabase();

        cv.put("UUID",id.toString());
        cv.put("name",share.getName());
        cv.put("price",share.getPrice());
        cv.put("number",share.getNum());

        db.update("mytable",cv,"UUID = ?",new String[]{id.toString()});
        db.close();
    }
    public void deleteShare(Share share)
    {
        SQLiteDatabase database=mDBhelper.getWritableDatabase();

        database.delete("mytable","UUID= '"+ share.getUUID() +"'",null);

        database.close();
    }

}

