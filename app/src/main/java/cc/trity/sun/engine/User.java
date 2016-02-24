package cc.trity.sun.engine;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

import cc.trity.library.utils.FileUtils;

/**
 * 用以保存用户信息（用户名,登陆状态等）
 * Created by TryIT on 2016/2/23.
 */
public class User implements Serializable,Cloneable{
    private static final long serialVersionUID = 1L;

    public final static String TAG = "User";
    private static String CACHEDIR;

    private String loginName;
    private String userName;
    private int score;
    private boolean loginStatus;

    private static User instance;
    private Context context;

    private User(Context context) {
        this.context=context;
    }

    public static User getInstance(Context context) {
        if (instance == null) {
            synchronized (User.class){
                CACHEDIR=FileUtils.getDiskCacheDirPath(context)+ File.separator + TAG;
                Object object = FileUtils.restoreObject(CACHEDIR);
                if (object == null) { // App第一次启动，文件不存在，则新建之
                    object = new User(context);
                    FileUtils.saveObject(CACHEDIR, object);
                }

                instance = (User) object;
            }
        }

        return instance;
    }

    public void save() {
        FileUtils.saveObject(CACHEDIR, this);
    }
    /**
     * 注销重置操作
     */
    public void reset() {
        loginName = null;
        userName = null;
        score = 0;
        loginStatus = false;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    // -----------以下3个方法用于序列化-----------------
    public User readResolve() throws ObjectStreamException, CloneNotSupportedException {
        instance = (User) this.clone();
        return instance;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }

    public Object Clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
