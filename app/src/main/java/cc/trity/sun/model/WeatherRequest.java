package cc.trity.sun.model;

import cc.trity.sun.engine.AppConstants;

/**
 * 天气数据请求的参数类
 * Created by TryIT on 2016/1/19.
 */
public class WeatherRequest {
    private String areaid;
    private String type;
    private String date;
    private String appid;
    private String key;

    public WeatherRequest() {
        type = "index_v";//常规数据的接口
        appid = AppConstants.APP_ID;
    }

    public WeatherRequest(String areaid, String date, String key) {
        this.areaid = areaid;
        this.date = date;
        this.key = key;
    }

    /**
     * 生成公密，用于加密
     * @return
     */
    public String generatePubliKey() {
        return new String(AppConstants.URL_WEATHER + "?" +
                "areaid=" + areaid + "&" + "type=" + type + "&" + "date=" + date + "&" + "appid=" + appid);
    }

    /**
     * 生成请求的Url
     * @return
     */
    public String generateUrl() {
        appid=appid.substring(0,6);
        return new String(AppConstants.URL_WEATHER + "?" +
                "areaid=" + areaid + "&" + "type=" + type + "&" + "date=" + date +
                "&" + "appid=" + appid+"&"+"key="+key);
    }
    public void changeForecastType(){
        setType("forecast_v");
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
