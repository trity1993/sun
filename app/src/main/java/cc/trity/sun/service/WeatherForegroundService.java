package cc.trity.sun.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.util.Date;

import cc.trity.sun.R;
import cc.trity.sun.activities.MainActivity;
import cc.trity.sun.model.Global;
import cc.trity.sun.model.WeatherMsg;
import cc.trity.sun.receiver.AlarmUpdateReceiver;
import cc.trity.sun.utils.LogUtils;
import cc.trity.sun.utils.TimeUtils;

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null)
        createForeGround(intent);
        setAlarmManager();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 创建前台线程。
     */
    public void createForeGround(Intent weatherIntent){
        WeatherMsg weatherMsg=weatherIntent.getParcelableExtra(Global.INTENT_WEATHER_MSG);
        int resIntR=weatherMsg.getWeatherLittleImage();
        LogUtils.d(TAG, "" + resIntR);

        if(resIntR<0)
            return ;

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        int requestID = (int) System.currentTimeMillis();//保证唯一
        PendingIntent pdIntent = PendingIntent.getActivity
                (this, requestID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //添加LargeIcon
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                resIntR);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(resIntR) /*空气质量图标*/
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

    /**
     * 设置6点，11点，18点
     */
    public void setAlarmManager(){
        int hour= Integer.parseInt(TimeUtils.getCurentTime("HH"));
        String curDate=TimeUtils.getCurentTime("yyyyMMdd");
        String date;

        if(hour<6){
            date=curDate+"0600";
        }else if(hour<11){
            date=curDate+"1100";
        }else if(hour<18){
            date=curDate+"1800";
        }else{
            Date date1= TimeUtils.getTomorrow();
            curDate=TimeUtils.getAssignFormatTime(date1,"yyyyMMdd");
            date=curDate+"0600";
        }
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmUpdateReceiver.class);
        PendingIntent pdIntent=PendingIntent.getBroadcast(this,Global.REQUEST_ALARM_RECEIVER,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,TimeUtils.getTimemills(date,Global.MATCH_DATE_MINUTE),pdIntent);
    }
}
