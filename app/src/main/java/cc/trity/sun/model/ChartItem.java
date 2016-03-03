package cc.trity.sun.model;

/**
 * Created by TryIT on 2016/3/2.
 */
public class ChartItem {
    private String date;
    private int tempDay;
    private int tempNight;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTempDay() {
        return tempDay;
    }

    public void setTempDay(int tempDay) {
        this.tempDay = tempDay;
    }

    public int getTempNight() {
        return tempNight;
    }

    public void setTempNight(int tempNight) {
        this.tempNight = tempNight;
    }
}
