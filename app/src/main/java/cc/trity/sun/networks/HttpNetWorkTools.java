package cc.trity.sun.networks;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cc.trity.sun.listener.HttpCallbackListener;
import cc.trity.sun.utils.LogUtils;

/**
 * Created by TryIT on 2016/1/11.
 */
public class HttpNetWorkTools {
    private static final String TAG="HttpNetWorkTools";
    /**
     * 使用HttpURLConnection进行请求
     * @param strUrl
     * @param listener
     */
    public static void sendRequestWithHttpURLConnection(final String strUrl, final HttpCallbackListener listener) {
        //开启线程进行网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(strUrl);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    if (inputStream != null) {
                        BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        StringBuffer sbuf = new StringBuffer();
                        while ((line = bufReader.readLine()) != null) {
                            sbuf.append(line);
                        }
                        if(listener!=null)
                            listener.onFinish(sbuf.toString());
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, Log.getStackTraceString(e));
                    if(listener!=null)
                        listener.onError(e);
                } finally {
                    if (httpURLConnection != null)
                        httpURLConnection.disconnect();
                }
            }
        }).start();

    }
}
