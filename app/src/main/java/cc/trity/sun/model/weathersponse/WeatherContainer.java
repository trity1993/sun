
package cc.trity.sun.model.weathersponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 天气的详细信息，注意接收为3天的预报，
 * 当18:00后请求时，白天的信息未空，所以要做好缓存
 */
public class WeatherContainer {

    @SerializedName("f1")
    @Expose
    public List<WeatherDetail> weatherDetailList = new ArrayList<WeatherDetail>();
    @SerializedName("f0")
    @Expose
    public String releaseTime;//发布时间

    public List<WeatherDetail> getWeatherDetailList() {
        return weatherDetailList;
    }

    public void setWeatherDetailList(List<WeatherDetail> weatherDetailList) {
        this.weatherDetailList = weatherDetailList;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }
}
