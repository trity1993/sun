package cc.trity.sun.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TryIT on 2016/3/2.
 */
public class ForcecastItem extends WeatherMsg {
    private String releaseTime;
    private String dayTemp,nightTemp;
    private int resDayImage,resNightImage;

    public ForcecastItem() {
    }

    ForcecastItem(Parcel source) {
        super(source);

        this.releaseTime=source.readString();
        this.dayTemp=source.readString();
        this.nightTemp=source.readString();

        this.resDayImage=source.readInt();
        this.resNightImage=source.readInt();
    }

    public static final Parcelable.Creator<ForcecastItem> CREATOR
            =new Parcelable.Creator<ForcecastItem>(){
        @Override
        public ForcecastItem createFromParcel(Parcel source) {
            return new ForcecastItem(source);
        }

        @Override
        public ForcecastItem[] newArray(int size) {
            return new ForcecastItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeString(releaseTime);
        dest.writeString(dayTemp);
        dest.writeString(nightTemp);

        dest.writeInt(resDayImage);
        dest.writeInt(resNightImage);
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
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

    public int getResDayImage() {
        return resDayImage;
    }

    public void setResDayImage(int resDayImage) {
        this.resDayImage = resDayImage;
    }

    public int getResNightImage() {
        return resNightImage;
    }

    public void setResNightImage(int resNightImage) {
        this.resNightImage = resNightImage;
    }
}
