package cc.trity.sun.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by TryIT on 2016/1/27.
 */
public class WeatherServiceConnection implements ServiceConnection {
    WeatherForegroundService.WeatherUpdateBinder weatherBinder;
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        weatherBinder=(WeatherForegroundService.WeatherUpdateBinder)service;
        weatherBinder.startDownload();
    }
    public WeatherForegroundService.WeatherUpdateBinder getWeatherBinder(){
        return weatherBinder;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
