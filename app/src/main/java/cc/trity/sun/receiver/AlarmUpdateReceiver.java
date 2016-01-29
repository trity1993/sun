package cc.trity.sun.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import cc.trity.sun.db.DataBaseManager;
import cc.trity.sun.listener.HttpCallbackListener;
import cc.trity.sun.model.city.County;
import cc.trity.sun.presenter.WeatherPresenter;

/**
 * 定时6，11，18 进行刷新
 * Created by TryIT on 2016/1/28.
 */
public class AlarmUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WeatherPresenter weatherPresenter=WeatherPresenter.getInstance(context);
        DataBaseManager dataBaseManager= DataBaseManager.getInstance(context);
        List<County> countyList= dataBaseManager.loadCounties();
        if(countyList!=null&&countyList.size()>=0){
            County county=countyList.get(0);
            weatherPresenter.loadWeather(county.getWeaterCode(), new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

    }
}
