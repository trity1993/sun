
package cc.trity.sun.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回该城市当天指数数据
 */
public class ResponseWeather {

    @SerializedName("i")
    @Expose
    private List<Exponent> exponentList = new ArrayList<Exponent>();

    public List<Exponent> getExponentList() {
        return exponentList;
    }

    public void setExponentList(List<Exponent> exponentList) {
        this.exponentList = exponentList;
    }
}
