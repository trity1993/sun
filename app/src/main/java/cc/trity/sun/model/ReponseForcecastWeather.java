
package cc.trity.sun.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 包含预报天气的位置的信息及天气信息
 */
public class ReponseForcecastWeather {

    @SerializedName("c")
    @Expose
    public AreaMsg areaMsg;
    @SerializedName("f")
    @Expose
    public WeatherContainer weatherContainer;

    public AreaMsg getAreaMsg() {
        return areaMsg;
    }

    public void setAreaMsg(AreaMsg areaMsg) {
        this.areaMsg = areaMsg;
    }

    public WeatherContainer getWeatherContainer() {
        return weatherContainer;
    }

    public void setWeatherContainer(WeatherContainer weatherContainer) {
        this.weatherContainer = weatherContainer;
    }
}
