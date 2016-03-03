package cc.trity.sun.model.city;

/**
 * 城市地方的基类
 * Created by TryIT on 2016/1/21.
 */
public class BasePlace {
    private String id;

    protected String placeName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

}
