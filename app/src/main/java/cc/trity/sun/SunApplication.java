package cc.trity.sun;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

/**
 * Created by TryIT on 2016/1/11.
 */
public class SunApplication extends Application {
    private static final boolean DEVELOPER_MODE=true;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
//        initCrashHandler(context);
        setUpDebug();
    }

    private void initCrashHandler(Context mContext){
        CrashHandler crashHandler=CrashHandler.getInstance();
        crashHandler.collectDeviceInfo(mContext);
    }

    private void setUpDebug(){
        if(DEVELOPER_MODE){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDialog()
                    .penaltyDeath()/*当触发违规条件时，直接Crash掉当前应用程序*/
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    public static Context getContext() {
        return context;
    }

}
