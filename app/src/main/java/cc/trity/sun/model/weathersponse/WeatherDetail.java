
package cc.trity.sun.model.weathersponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherDetail {

    @SerializedName("fa")
    @Expose
    public String dayNum;
    @SerializedName("fb")
    @Expose
    public String nightNum;
    @SerializedName("fc")
    @Expose
    public String dayTemp;
    @SerializedName("fd")
    @Expose
    public String nightTemp;
    @SerializedName("fe")
    @Expose
    public String dayWindDirectNum;
    @SerializedName("ff")
    @Expose
    public String nightWindDirectNum;
    @SerializedName("fg")
    @Expose
    public String dayWindPowerNum;
    @SerializedName("fh")
    @Expose
    public String nightWindPowerNum;
    @SerializedName("fi")
    @Expose
    public String sunSet;//日出日落时间(中间用|分割) 06:44|18:21

    public String getDayNum() {
        return dayNum;
    }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum;
    }

    public String getNightNum() {
        return nightNum;
    }

    public void setNightNum(String nightNum) {
        this.nightNum = nightNum;
    }

    public String getDayTemp() {
        return dayTemp;
    }

    public void setDayTemp(String dayTemp) {
        this.dayTemp = dayTemp;
    }

    public String getNightTemp() {
        return nightTemp;
    }

    public void setNightTemp(String nightTemp) {
        this.nightTemp = nightTemp;
    }

    public String getDayWindDirectNum() {
        return dayWindDirectNum;
    }

    public void setDayWindDirectNum(String dayWindDirectNum) {
        this.dayWindDirectNum = dayWindDirectNum;
    }

    public String getNightWindDirectNum() {
        return nightWindDirectNum;
    }

    public void setNightWindDirectNum(String nightWindDirectNum) {
        this.nightWindDirectNum = nightWindDirectNum;
    }

    public String getDayWindPowerNum() {
        return dayWindPowerNum;
    }

    public void setDayWindPowerNum(String dayWindPowerNum) {
        this.dayWindPowerNum = dayWindPowerNum;
    }

    public String getNightWindPowerNum() {
        return nightWindPowerNum;
    }

    public void setNightWindPowerNum(String nightWindPowerNum) {
        this.nightWindPowerNum = nightWindPowerNum;
    }

    public String getSunSet() {
        return sunSet;
    }

    public void setSunSet(String sunSet) {
        this.sunSet = sunSet;
    }
}
