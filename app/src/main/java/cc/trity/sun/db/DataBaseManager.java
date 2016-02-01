package cc.trity.sun.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cc.trity.sun.model.city.County;

/**
 * 数据库操作的类
 * Created by TryIT on 2016/1/11.
 */
public class DataBaseManager {
    /**
     * 数据库名称
     */
    public static final String DB_NAME="db_sun";
    public static final String DB_TABLE_COUNTY="County";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static DataBaseManager dataBaseManager;
    private SQLiteDatabase db;

    private DataBaseManager(Context context){
        WeatherOpenHelper weatherOpenHelper=new WeatherOpenHelper(context,
                DB_NAME, null, VERSION);
        db=weatherOpenHelper.getWritableDatabase();
    }
    public static DataBaseManager getInstance(Context context){
        if(dataBaseManager==null){
            synchronized (DataBaseManager.class){
                dataBaseManager=new DataBaseManager(context);
            }
        }
        return dataBaseManager;
    }

    /**
     * 将County实例存储到数据库。
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getPlaceName());
            values.put("county_code", county.getWeaterCode());
            db.insert(DB_TABLE_COUNTY, null, values);
        }
    }

    /**
     * 从数据库读取某城市下所有的县信息。
     */
    public List<County> loadCounties() {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query(DB_TABLE_COUNTY, null,null,null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setPlaceName(cursor.getString(cursor
                        .getColumnIndex("county_name")));
                county.setWeaterCode(cursor.getString(cursor
                        .getColumnIndex("county_code")));
                list.add(county);
            } while (cursor.moveToNext());
        }
        cursor.close();
        cursor=null;
        return list;
    }
}
