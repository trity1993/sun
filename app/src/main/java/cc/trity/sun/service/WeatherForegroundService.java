package cc.trity.sun.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import cc.trity.sun.R;
import cc.trity.sun.activities.MainActivity;
import cc.trity.sun.model.WeatherMsg;
import cc.trity.sun.utils.LogUtils;

/**
 * 前台线程
 * Created by TryIT on 2016/1/26.
 */
public class WeatherForegroundService extends Service {
    private static final String TAG="WeatherForegroundService";
    int id = 1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 创建前台线程。
     */
    public void createForeGround(Intent weatherIntent){
        WeatherMsg weatherMsg=weatherIntent.getParcelableExtra("weather_message");
        int resIntR=weatherMsg.getWeatherImage();
        if(resIntR<0)
            return ;

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        int requestID = (int) System.currentTimeMillis();//保证唯一
        PendingIntent pdIntent = PendingIntent.getActivity
                (this, requestID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //添加LargeIcon
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.little_icon_cloudy);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher) /*空气质量图标*/
                .setContentIntent(pdIntent)
                .setContentTitle(weatherMsg.getWeatherDetail())
                .setContentText(weatherMsg.getWeatherTemp())
                .setContentInfo("空气质量")
                .setLargeIcon(largeIcon) /*天气图标*/
                .addAction(R.mipmap.ic_share_white_24dp, "分享", pdIntent)
                .addAction(R.mipmap.ic_settings_white_24dp, "设置", pdIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("帮我分享下吧~"));

        startForeground(id, mBuilder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createForeGround(intent);

        return super.onStartCommand(intent, flags, startId);
    }

    public class WeatherUpdateBinder extends Binder{
        public void startDownload() {
            LogUtils.d(TAG, "startDownload() executed");
            // 执行具体的下载任务
        }
    }
}
