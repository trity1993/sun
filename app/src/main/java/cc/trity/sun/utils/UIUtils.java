package cc.trity.sun.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by TryIT on 2016/2/18.
 */
public class UIUtils {
    /**
     * 得到测量屏幕工具类
     * @param activity
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity){
        DisplayMetrics dm=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        return dm;
    }
    public static DisplayMetrics getDisplayMetrics(Context context){
        return context.getResources().getDisplayMetrics();
    }
}
