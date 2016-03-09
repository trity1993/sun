package cc.trity.sun.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.model.city.County;

/**
 * 数据库操作的类
 * Created by TryIT on 2016/1/11.
 */
public class DataBaseManager {
    /**
     * 数据库名称
     */
    public static final String DB_NAME = "db_sun";
    public static final String DB_TABLE_COUNTY = "County";

    /**
     * 数据库版本（升级的时候，需要修改此版本）
     */
    public static final int VERSION = 2;

    private SQLiteDatabase db;

    public DataBaseManager(Context context) {
        WeatherOpenHelper weatherOpenHelper = new WeatherOpenHelper(context,
                DB_NAME, null, VERSION);
        db = weatherOpenHelper.getWritableDatabase();
    }

    /**
     * 将County实例存储到数据库。
     *
     * @param county
     * @return 当返回-1 代表插入失败
     */
    public long saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put(AppConstants.COUNTRY_NAME, county.getPlaceName());
            values.put(AppConstants.COUNTRY_CODE, county.getWeaterCode());
            return db.insert(DB_TABLE_COUNTY, null, values);
        }
        return -1;
    }

    /**
     * 通过county进行update的操作，当不存在值的时候，再进行insert操作
     * 默认page_num=1的时候为定位
     * @param county
     * @return
     */
    public boolean updateCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put(AppConstants.COUNTRY_NAME, county.getPlaceName());
            values.put(AppConstants.COUNTRY_CODE, county.getWeaterCode());
            if (db.update(DB_TABLE_COUNTY, values, "page_num = ?", new String[]{county.getId()}) > 0) {
                return true;
            } else {
                values.put("page_num", county.getId());

                if (db.insert(DB_TABLE_COUNTY, null, values) > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * 从数据库读取某城市下所有的县信息。
     */
    public List<County> loadCounties() {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query(DB_TABLE_COUNTY, null, null, null, null, null, "page_num desc");
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
        cursor = null;
        return list;
    }
}
