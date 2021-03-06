package cc.trity.sun.engine;

/**
 * Created by TryIT on 2016/1/14.
 */
public class AppConstants {

    public static final String URL_WEATHER="http://open.weather.com.cn/data/";
    public static final String URL_SHARE_APP="http://www.pgyer.com/T_sun";
    public static final String APP_ID="02031c9cf50aca5b";
    public static final String PRIVATE_KEY="2037ff_SmartWeatherAPI_bfadbba";
    public static final String ASSETS_CITY="city.xml";
    public static final String MATCH_DATE_MINUTE="yyyyMMddHHmm";
    public static final String BROADCAST_START_SERVICE="cc.trity.receiver.START_SERVICE";
    public static final String SHARE_PREF_SERVICE="isStartService";
    public static final String IS_LOACTE_SUCCESS="isLocateSuccess";
    public static final String COUNTRY_NAME="county_name";
    public static final String COUNTRY_CODE="county_code";
    public static final String DEFAULT_STRING_VALUE="";
    public static final int DEFAULT_INTEGER_VALUE=-1;
    public static int pageLength=1;
    public static int REQUEST_ALARM_RECEIVER=2;
    public static boolean isStartService=true;//监控多个fragment的情况

    public static final String INTENT_WEATHER_MSG="weather_message";
    public static final String INTENT_BG_COLOR="resBgColor";


    public final static String Email = "Email";
    public final static String NeedCallback = "NeedCallback";
}
