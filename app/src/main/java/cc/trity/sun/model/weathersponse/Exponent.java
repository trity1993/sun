
package cc.trity.sun.model.weathersponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 返回该城市当天指数数据
 * 包括：晨练指数，舒适度，穿衣指数
 */
public class Exponent {

    @SerializedName("i1")
    @Expose
    private String exponentShortName;
    @SerializedName("i2")
    @Expose
    private String exponentName;
    @SerializedName("i3")
    @Expose
    private String exponentNickName;
    @SerializedName("i4")
    @Expose
    private String exponentLevel;
    @SerializedName("i5")
    @Expose
    private String exponentDescribe;

    public String getExponentShortName() {
        return exponentShortName;
    }

    public void setExponentShortName(String exponentShortName) {
        this.exponentShortName = exponentShortName;
    }

    public String getExponentName() {
        return exponentName;
    }

    public void setExponentName(String exponentName) {
        this.exponentName = exponentName;
    }

    public String getExponentNickName() {
        return exponentNickName;
    }

    public void setExponentNickName(String exponentNickName) {
        this.exponentNickName = exponentNickName;
    }

    public String getExponentLevel() {
        return exponentLevel;
    }

    public void setExponentLevel(String exponentLevel) {
        this.exponentLevel = exponentLevel;
    }

    public String getExponentDescribe() {
        return exponentDescribe;
    }

    public void setExponentDescribe(String exponentDescribe) {
        this.exponentDescribe = exponentDescribe;
    }
}
