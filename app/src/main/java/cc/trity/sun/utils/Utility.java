package cc.trity.sun.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cc.trity.library.utils.FileUtils;
import cc.trity.library.utils.GsonUtils;
import cc.trity.library.utils.LogUtils;
import cc.trity.sun.R;
import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.model.city.City;
import cc.trity.sun.model.city.County;
import cc.trity.sun.model.city.Province;
import cc.trity.sun.model.weathersponse.WeatherContainer;
import cc.trity.sun.model.city.BasePlace;
import cc.trity.sun.sdk.PlaceSaxParseHandler;

public class Utility {
	private static final String TAG="Utility";

	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地。
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
					weatherDesp, publishTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
	 */
	public static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}

	public static void saveCountyWeatherInfo(Context context,String countyCode,String county3WeatherInfo){
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putString(countyCode,county3WeatherInfo);
		editor.commit();
	}

	/**
	 * 从sharePre中将key=countyCode对应的值value=WeatherContainer，以json的格式进行取出
	 * @param context
	 * @param countyCode
	 * @return
	 */
	public static WeatherContainer getWeatherContainer(Context context,String countyCode){
		if(countyCode==null)
			return null;
		SharedPreferences sharePre=PreferenceManager.getDefaultSharedPreferences(context);
		String jsonStr=sharePre.getString(countyCode, "");
		LogUtils.d(TAG, jsonStr);
		if(!TextUtils.isEmpty(jsonStr)){
			return GsonUtils.getClass(jsonStr, WeatherContainer.class);
		}
		return null;
	}


	public static List<BasePlace> getPlace(String xmlStr,String flagArgs){
		XmlPullParser xmlPullParser= FileUtils.getXmlPullParser(xmlStr);
		BasePlace place=null;
		List<BasePlace> listPlace=null;
		try{
			int eventType=xmlPullParser.getEventType();
			while (eventType!=XmlPullParser.END_DOCUMENT){
				String nodeName=xmlPullParser.getName();
				switch (eventType){
					case XmlPullParser.START_DOCUMENT:
						place=new BasePlace();
						listPlace=new ArrayList<>();
						break;
					case XmlPullParser.START_TAG:
						if(nodeName.equals(flagArgs)){
							place.setId(xmlPullParser.getAttributeValue(null, "id"));
							place.setPlaceName(xmlPullParser.getAttributeValue(null, "name"));
						}
						break;
					case XmlPullParser.END_TAG:
						listPlace.add(place);
						place=null;
						break;
				}
				eventType=xmlPullParser.next();
			}
		}catch (Exception e){
			LogUtils.e(TAG, Log.getStackTraceString(e));
			return null;
		}
		return listPlace;
	}

	public static String getCountryCode(String provinceName, String cityName, String countyName, Context context) {
		List<Province> listProvince;
		try {
			listProvince = PlaceSaxParseHandler.getProvicneModel
					(context.getAssets().open(AppConstants.ASSETS_CITY));
		} catch (Exception e) {
			LogUtils.e("ChooseAreaPresent", Log.getStackTraceString(e));
			return null;
		}
		String countyTmpCode=null;//一种替代方式，对应的区或者县没有code，只能这么替代了。
		if (listProvince != null) {
			for (Province provinceTmp : listProvince) {
				if (provinceTmp.getPlaceName().equals(provinceName)) {
					List<City> listCity = provinceTmp.getCityList();
					for (City cityTmp : listCity) {
						if (cityTmp.getPlaceName().equals(cityName)) {
							List<County> listCounty = cityTmp.getCountyList();
							for (County countyTmp : listCounty) {
								if(countyTmp.getPlaceName().equals(cityName)){
									countyTmpCode=countyTmp.getWeaterCode();
								}
								if (countyTmp.getPlaceName().equals(countyName)) {
									return countyTmp.getWeaterCode();
								}
							}
						}
					}
				}
			}
		}
		if(countyTmpCode!=null){
			return countyTmpCode;
		}

		return null;
	}

	/**
	 * 返回摄氏度的匹配字符串
	 * @param context
	 * @return
	 */
	public static String getTempMatch(Context context){
		String degreeStr = context.getResources().getString(R.string.degree);
		return "%s"+degreeStr;
	}

}