package cc.trity.sun.model;

/**
 * Created by TryIT on 2016/1/14.
 */
public class Global {

    public static final String URL_WEATHER="http://open.weather.com.cn/data/";
    public static final String APP_ID="02031c9cf50aca5b";
    public static final String PRIVATE_KEY="2037ff_SmartWeatherAPI_bfadbba";
    public static final String ASSETS_CITY="city.xml";
    public static final String INTENT_WEATHER_MSG="weather_message";
    public static final String MATCH_DATE_MINUTE="yyyyMMddHHmm";
    public static final String BROADCAST_START_SERVICE="cc.trity.receiver.START_SERVICE";
    public static final String SHARE_PREF_SERVICE="isStartService";
    public static int pageLength=1;
    public static int REQUEST_ALARM_RECEIVER=2;
    public static boolean isStartService=true;//监控多个fragment的情况
}
