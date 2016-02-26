package cc.trity.nativewithh5.engine;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import cc.trity.library.utils.LogUtils;

/**
 * 处理html页面事件的事件分发
 * 对url所包含的不同值，执行不同的操作，包括跳转方式。
 * Created by TryIT on 2016/2/25.
 */
public class Dispatcher {

    private static final String TAG="Dispatcher";

    public static void gotoAnyWhere(String url, final Activity activity) {
        String findKey = null;

        Intent intent = new Intent();

        int pos = url.indexOf(":");
        if (pos == -1) {
            findKey = url;
        } else {
            findKey = url.substring(0, pos);

            //得到对应的键值
            String strParams = url.substring(pos);
            String[] pairs = strParams.split("&");
            for (String strKeyAndValue : pairs) {
                String[] arr = strKeyAndValue.split("=");
                String key = arr[0];
                String value = arr[1];
                if (value.startsWith("(int)")) {
                    intent.putExtra(key, Integer.valueOf(value.substring(5)));
                } else if (value.startsWith("(Double)")) {
                    intent.putExtra(key, Double.valueOf(value.substring(8)));
                } else {
                    intent.putExtra(key, value);
                }
            }
        }

        //得到对应的类
        ProtocolData protocol = ProtocolManager.findProtocol(findKey, activity);
        try {
            intent.setClass(activity, Class.forName(protocol.getTarget()));
        } catch (ClassNotFoundException e) {
            LogUtils.e(TAG, Log.getStackTraceString(e));
            return;
        }

        activity.startActivity(intent);
    }
}
