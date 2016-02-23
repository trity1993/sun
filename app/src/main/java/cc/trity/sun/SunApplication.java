package cc.trity.sun;

import android.content.Context;
import android.os.StrictMode;

import cc.trity.library.BaseApplication;

/**
 * Created by TryIT on 2016/1/11.
 */
public class SunApplication extends BaseApplication {
    private static final boolean DEVELOPER_MODE=true;

    @Override
    public void onCreate() {
        super.onCreate();
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

}
