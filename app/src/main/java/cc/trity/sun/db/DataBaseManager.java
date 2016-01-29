package cc.trity.sun.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cc.trity.sun.model.city.City;
import cc.trity.sun.model.city.County;
import cc.trity.sun.model.city.Province;

/**
 * 数据库操作的类
 * Created by TryIT on 2016/1/11.
 */
public class DataBaseManager {
    /**
     * 数据库名称
     */
    public static final String DB_NAME="db_sun";
    public static final String DB_TABLE_PROVINCE="Province";
    public static final String DB_TABLE_CITY="City";
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

    public void saveProvince(Province provinceModel){
        if(provinceModel!=null){
            ContentValues contentValues=new ContentValues();
//            contentValues.put("province_name",provinceModel.getProvinceName());
//            contentValues.put("province_code",provinceModel.getProvinceCode());
            db.insert(DB_TABLE_PROVINCE,null,contentValues);
        }
    }

    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db
                .query(DB_TABLE_PROVINCE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
//                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                province.setProvinceName(cursor.getString(cursor
//                        .getColumnIndex("province_name")));
//                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        return list;
    }
    /**
     * 将City实例存储到数据库。
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
//            values.put("city_name", city.getCityName());
//            values.put("city_code", city.getCityCode());
//            values.put("province_id", city.getProvinceId());
            db.insert(DB_TABLE_CITY, null, values);
        }
    }

    /**
     * 从数据库读取某省下所有的城市信息。
     */
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query(DB_TABLE_CITY, null, "province_id = ?",
                new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
//                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                city.setCityName(cursor.getString(cursor
//                        .getColumnIndex("city_name")));
//                city.setCityCode(cursor.getString(cursor
//                        .getColumnIndex("city_code")));
//                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        return list;
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
        return list;
    }
}
