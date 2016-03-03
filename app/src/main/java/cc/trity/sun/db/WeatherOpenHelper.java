package cc.trity.sun.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cc.trity.library.utils.LogUtils;

/**
 * Created by TryIT on 2016/1/11.
 */
public class WeatherOpenHelper extends SQLiteOpenHelper {

    /**
     * County表建表语句
     */
    public static final String CREATE_COUNTY = "create table County ("
            + "id integer primary key autoincrement, "
            + "county_name text, "
            + "county_code text,"
            + "page_num integer) ";

    public static final String ALTER_COUNTY_ADD="ALTER TABLE County ADD page_num integer";


    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COUNTY); // 创建County表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.d("WeatherOpenHelper",oldVersion+","+newVersion);
        switch (newVersion){
            case 1:
                db.execSQL(CREATE_COUNTY); // 创建County表
            case 2:
                db.execSQL(ALTER_COUNTY_ADD);
        }
    }
}
