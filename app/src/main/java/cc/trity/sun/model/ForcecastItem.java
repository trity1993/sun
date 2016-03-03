package cc.trity.sun.model;

/**
 * Created by TryIT on 2016/3/2.
 */
public class ForcecastItem {
    private int weatherLittleImage;
    private String weatherTemp,Date,weatherDayTemp,weatherNightTemp;

    public ForcecastItem() {
    }

    public ForcecastItem(int weatherLittleImage, String weatherTemp,
                         String date, String weatherDayTemp, String weatherNightTemp) {
        this.weatherLittleImage = weatherLittleImage;
        this.weatherTemp = weatherTemp;
        Date = date;
        this.weatherDayTemp = weatherDayTemp;
        this.weatherNightTemp = weatherNightTemp;
    }

    public int getWeatherLittleImage() {
        return weatherLittleImage;
    }

    public void setWeatherLittleImage(int weatherLittleImage) {
        this.weatherLittleImage = weatherLittleImage;
    }

    public String getWeatherTemp() {
        return weatherTemp;
    }

    public void setWeatherTemp(String weatherTemp) {
        this.weatherTemp = weatherTemp;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getWeatherDayTemp() {
        return weatherDayTemp;
    }

    public void setWeatherDayTemp(String weatherDayTemp) {
        this.weatherDayTemp = weatherDayTemp;
    }

    public String getWeatherNightTemp() {
        return weatherNightTemp;
    }

    public void setWeatherNightTemp(String weatherNightTemp) {
        this.weatherNightTemp = weatherNightTemp;
    }
}
