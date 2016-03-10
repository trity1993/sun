package cc.trity.sun.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用于Act与Service的传递
 * Created by TryIT on 2016/1/27.
 */
public class WeatherMsg implements Parcelable {
    private String weatherDetail;
    private String weatherTemp;
    private String weatherLocation;
    private int weatherImage;
    private int weatherLittleImage;
    private int weatherBackground;

    public WeatherMsg(){}

    WeatherMsg(Parcel source){
        this.weatherDetail=source.readString();
        this.weatherTemp=source.readString();
        this.weatherLocation=source.readString();

        this.weatherImage=source.readInt();
        this.weatherLittleImage=source.readInt();
        this.weatherBackground=source.readInt();
    }

    public static final Parcelable.Creator<WeatherMsg> CREATOR
            =new Parcelable.Creator<WeatherMsg>(){

        @Override
        public WeatherMsg createFromParcel(Parcel source) {
            return new WeatherMsg(source);
        }

        @Override
        public WeatherMsg[] newArray(int size) {
            return new WeatherMsg[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(weatherDetail);
        dest.writeString(weatherTemp);
        dest.writeString(weatherLocation);

        dest.writeInt(weatherImage);
        dest.writeInt(weatherLittleImage);
        dest.writeInt(weatherBackground);

    }

    public int getWeatherLittleImage() {
        return weatherLittleImage;
    }

    public void setWeatherLittleImage(int weatherLittleImage) {
        this.weatherLittleImage = weatherLittleImage;
    }

    public String getWeatherDetail() {
        return weatherDetail;
    }

    public void setWeatherDetail(String weatherDetail) {
        this.weatherDetail = weatherDetail;
    }

    public String getWeatherTemp() {
        return weatherTemp;
    }

    public void setWeatherTemp(String weatherTemp) {
        this.weatherTemp = weatherTemp;
    }

    public int getWeatherImage() {
        return weatherImage;
    }

    public void setWeatherImage(int weatherImage) {
        this.weatherImage = weatherImage;
    }

    public String getWeatherLocation() {
        return weatherLocation;
    }

    public void setWeatherLocation(String weatherLocation) {
        this.weatherLocation = weatherLocation;
    }

    public int getWeatherBackground() {
        return weatherBackground;
    }

    public void setWeatherBackground(int weatherBackground) {
        this.weatherBackground = weatherBackground;
    }
}
