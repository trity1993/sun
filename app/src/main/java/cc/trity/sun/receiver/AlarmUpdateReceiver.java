package cc.trity.sun.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import cc.trity.library.utils.CommonUtils;
import cc.trity.library.utils.GsonUtils;
import cc.trity.library.utils.NetWorkUtils;
import cc.trity.sun.db.DataBaseManager;
import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.listener.AbstractRequestCallback;
import cc.trity.sun.model.WeatherMsg;
import cc.trity.sun.model.city.County;
import cc.trity.sun.model.weathersponse.ReponseForcecastWeather;
import cc.trity.sun.model.weathersponse.WeatherContainer;
import cc.trity.sun.presenter.WeatherPresenter;
import cc.trity.sun.service.WeatherForegroundService;

/**
 * 定时6，11，18 进行启动service进行刷新工作
 * Created by TryIT on 2016/1/28.
 */
public class AlarmUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        CommonUtils.showToast(context, "AlarmUpdateReceiver 已接受到");
        if(NetWorkUtils.isNetworkAvailable(context)){
            final WeatherPresenter weatherPresenter=new WeatherPresenter(context);
            DataBaseManager dataBaseManager= new DataBaseManager(context);
            List<County> countyList= dataBaseManager.loadCounties();
            if(countyList!=null&&countyList.size()>=0){
                final County county=countyList.get(0);
                weatherPresenter.loadWeather(context,county.getWeaterCode(), new AbstractRequestCallback(context) {

                    @Override
                    public void onSuccess(String content) {
                        ReponseForcecastWeather weatherData = GsonUtils.getClass(content, ReponseForcecastWeather.class);
                        WeatherContainer weatherContainer = weatherData.getWeatherContainer();
                        if(weatherPresenter!=null){
                            WeatherMsg weatherMsg=weatherPresenter.updateData(weatherContainer,county.getPlaceName());
                            if(weatherMsg!=null){
                                AppConstants.isStartService=true;
                                WeatherForegroundService.toCreateForGround(context,weatherMsg);
                            }
                        }
                    }

                    @Override
                    public void onFail(String errorMsg) {

                    }
                });
            }
        }

        //取消闹钟
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(context,getResultCode(),new Intent(context,AlarmUpdateReceiver.class),PendingIntent.FLAG_CANCEL_CURRENT));

    }
}
