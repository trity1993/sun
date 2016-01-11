package cc.trity.sun;

import android.app.Application;
import android.content.Context;

/**
 * Created by TryIT on 2016/1/11.
 */
public class SunApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        initCrashHandler(context);
    }

    private void initCrashHandler(Context mContext){
        CrashHandler crashHandler=CrashHandler.getInstance();
        crashHandler.collectDeviceInfo(mContext);
    }

    public static Context getContext() {
        return context;
    }

}
