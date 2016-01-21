package cc.trity.sun.model.city;


import java.util.ArrayList;
import java.util.List;

public class Province extends BasePlace{
    private List<City> cityList;
    public Province(){
        cityList=new ArrayList<>();
    }
    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
