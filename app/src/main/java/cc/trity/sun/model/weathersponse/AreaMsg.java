
package cc.trity.sun.model.weathersponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 当前预报地点的详细信息
 */
public class AreaMsg {

    @SerializedName("c1")
    @Expose
    public String areaId;
    @SerializedName("c2")
    @Expose
    public String countyName;
    @SerializedName("c3")
    @Expose
    public String countyChinaName;
    @SerializedName("c4")
    @Expose
    public String cityName;
    @SerializedName("c5")
    @Expose
    public String cityChinaName;
    @SerializedName("c6")
    @Expose
    public String provinceName;
    @SerializedName("c7")
    @Expose
    public String provinceChinaName;
    @SerializedName("c8")
    @Expose
    public String countryName;
    @SerializedName("c9")
    @Expose
    public String countryChinaName;
    @SerializedName("c10")
    @Expose
    public String countyLevel;
    @SerializedName("c11")
    @Expose
    public String countyNum;
    @SerializedName("c12")
    @Expose
    public String postCode;
    @SerializedName("c13")
    @Expose
    public Float longitude;
    @SerializedName("c14")
    @Expose
    public Float latitude;
    @SerializedName("c15")
    @Expose
    public String altitude;
    @SerializedName("c16")
    @Expose
    public String radar;
    @SerializedName("c17")
    @Expose
    public String forcecastTime;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyChinaName() {
        return countyChinaName;
    }

    public void setCountyChinaName(String countyChinaName) {
        this.countyChinaName = countyChinaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityChinaName() {
        return cityChinaName;
    }

    public void setCityChinaName(String cityChinaName) {
        this.cityChinaName = cityChinaName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceChinaName() {
        return provinceChinaName;
    }

    public void setProvinceChinaName(String provinceChinaName) {
        this.provinceChinaName = provinceChinaName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryChinaName() {
        return countryChinaName;
    }

    public void setCountryChinaName(String countryChinaName) {
        this.countryChinaName = countryChinaName;
    }

    public String getCountyLevel() {
        return countyLevel;
    }

    public void setCountyLevel(String countyLevel) {
        this.countyLevel = countyLevel;
    }

    public String getCountyNum() {
        return countyNum;
    }

    public void setCountyNum(String countyNum) {
        this.countyNum = countyNum;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getRadar() {
        return radar;
    }

    public void setRadar(String radar) {
        this.radar = radar;
    }

    public String getForcecastTime() {
        return forcecastTime;
    }

    public void setForcecastTime(String forcecastTime) {
        this.forcecastTime = forcecastTime;
    }
}
