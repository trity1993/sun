package cc.trity.sun.models;

/**
 * Created by TryIT on 2016/1/11.
 */
public class ProvinceModel {

    private int id;
    private String provinceName;
    private String provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String toString() {
        return new String("id="+id+",provinceName="+provinceName+",provinceCode="+provinceCode);
    }
}
