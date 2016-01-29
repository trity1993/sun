package cc.trity.sun.presenter;

import cc.trity.sun.listener.HttpCallbackListener;
import cc.trity.sun.networks.HttpManager;
import cc.trity.sun.networks.HttpNetWorkTools;

/**
 * 处理更新weather的逻辑
 * Created by TryIT on 2016/1/29.
 */
public class WeatherPresenter {
    private static final String TAG="WeatherPresenter";
    public void loadWeather(String countyCode,HttpCallbackListener httpCallbackListener){
        //生成url
        String url = HttpManager.getReqAdress(true, countyCode);
        HttpNetWorkTools.sendRequestWithHttpURLConnection(url, httpCallbackListener);
    }
}
