package cc.trity.library;

import android.app.Application;
import android.content.Context;

import cc.trity.library.cache.CacheManager;

/**
 * Created by TryIT on 2016/2/23.
 */
public class BaseApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        CacheManager.getInstance(context).initCacheDir();
    }

    public static Context getContext() {
        return context;
    }

}
