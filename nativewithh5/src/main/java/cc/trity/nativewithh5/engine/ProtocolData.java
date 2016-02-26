package cc.trity.nativewithh5.engine;

/**
 * Created by TryIT on 2016/2/25.
 */
public class ProtocolData {
    private String key;
    private String target;//目的类的包名

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
