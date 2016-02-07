package cc.trity.sun.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.List;

import cc.trity.library.activity.BaseActivity;
import cc.trity.library.net.RequestCallback;
import cc.trity.library.net.RequestParameter;
import cc.trity.library.utils.FileUtils;
import cc.trity.library.utils.GsonUtils;
import cc.trity.library.utils.NetWorkUtils;
import cc.trity.library.utils.TimeUtils;
import cc.trity.sun.R;
import cc.trity.sun.engine.RemoteService;
import cc.trity.sun.listener.HttpCallbackListener;
import cc.trity.sun.model.Global;
import cc.trity.sun.model.WeatherContainer;
import cc.trity.sun.model.WeatherDetail;
import cc.trity.sun.model.WeatherMsg;
import cc.trity.sun.networks.HttpManager;
import cc.trity.sun.networks.HttpNetWorkTools;
import cc.trity.sun.service.WeatherForegroundService;
import cc.trity.sun.utils.Utility;

/**
 * 处理更新weather的逻辑
 * Created by TryIT on 2016/1/29.
 */
public class WeatherPresenter {
    private static final String TAG="WeatherPresenter";

    private Context context;
    private static WeatherPresenter weatherPresenter;
    private WeatherPresenter(Context context){
        this.context=context;
    }
    public static WeatherPresenter getInstance(Context context){
        if(weatherPresenter==null){
            synchronized (WeatherPresenter.class){
                return weatherPresenter=new WeatherPresenter(context);
            }
        }
        return weatherPresenter;
    }

    /**
     * 加载天气信息
     * @param countyCode
     * @param httpCallbackListener
     */
    public void loadWeather(String countyCode,HttpCallbackListener httpCallbackListener){
        //生成url
        if(NetWorkUtils.isNetworkAvailable(context)){
            String url = HttpManager.getReqAdress(true, countyCode);
            HttpNetWorkTools.sendRequestWithHttpURLConnection(url, httpCallbackListener);
        }else{
            httpCallbackListener.onError(null);
        }
    }
    public void loadWeather(BaseActivity baseActivity,String countyCode,RequestCallback requestCallback){
        //生成url
        if(NetWorkUtils.isNetworkAvailable(context)){
            //执行网络请求的操作
            List<RequestParameter> parameterList=HttpManager.getReqParameters(true,countyCode,Global.URL_WEATHER);
            RemoteService.getInstance().invoke(baseActivity,"getWeatherForecast",parameterList,requestCallback);
        }else{
            requestCallback.onFail(null);
        }
    }

    /**
     * 检测是否需要更新天气的信息
     * @param weatherContainer
     * @param countyCode
     * @return
     */
    public boolean isUpdate(WeatherContainer weatherContainer,String countyCode) {
        if(context==null)
            return false;
        weatherContainer = Utility.getWeatherContainer(context, countyCode);
        if (weatherContainer == null)
            return false;
        long dateHM = Long.valueOf(weatherContainer.getReleaseTime().trim());
        long curDate = Long.valueOf(TimeUtils.getCurentTime("yyyyMMddHHmm"));

        StringBuilder sbDay=new StringBuilder(TimeUtils.getCurentTime("yyyyMMdd"));
        sbDay.append("0600");
        long curTime=Long.valueOf(sbDay.toString());
        if (dateHM < curTime && curDate >= curTime) {
            return true;
        }

        sbDay.replace(8,12,"1100");
        curTime=Long.valueOf(sbDay.toString());
        if (dateHM < curTime && curDate >= curTime) {
            return true;
        }

        sbDay.replace(8, 12, "1800");
        curTime=Long.valueOf(sbDay.toString());
        if (dateHM < curTime && curDate >= curTime) {
            return true;
        }
        return false;
    }

    /**
     * 通过weatherContainer更新数据
     * @param weatherContainer
     * @return
     */
    public WeatherMsg updateData(WeatherContainer weatherContainer) {
        if (weatherContainer == null) {
            return null;
        }
        //加载天气编码的名称
        String[] weatherName=context.getResources().getStringArray(R.array.weather_names);
        //加载图片资源
        int[] resImage = null;
        int[] reslittleImage = null;
        String imgIconNum;
        StringBuilder temp = new StringBuilder();
        WeatherDetail weatherDetail = weatherContainer.getWeatherDetailList().get(0);

        int hour = Integer.valueOf(TimeUtils.getCurentTime("HH"));
        if (hour > 18 || hour < 6) {
            temp.append(weatherDetail.getNightTemp());
            imgIconNum = weatherDetail.getNightNum();

            resImage = FileUtils.getResourseArray(context, R.array.weather_night_icon);
            reslittleImage = FileUtils.getResourseArray(context, R.array.weather_little_night_icon);

        } else {
            temp.append(weatherDetail.getDayTemp());
            imgIconNum = weatherDetail.getDayNum();

            resImage = FileUtils.getResourseArray(context, R.array.weather_icon);
            reslittleImage = FileUtils.getResourseArray(context, R.array.weather_little_icon);
        }
        if (!TextUtils.isEmpty(temp.toString())) {
            String degreeStr = context.getResources().getString(R.string.degree);
            temp.append(degreeStr);
        }

        //设置图标
        int num = Integer.valueOf(imgIconNum);
        //资源id
        int resId=0;
        int resLittleId=0;
        if (num == 53) {
            resId=resImage[resImage.length - 1];
            resLittleId=reslittleImage[resImage.length - 1];
        } else if (num == 99) {
            resId=R.mipmap.ic_launcher;
            resLittleId=R.mipmap.ic_launcher;
        } else {
            resId=resImage[num];
            resLittleId=reslittleImage[num];
        }

        WeatherMsg weatherMsg = new WeatherMsg();
        weatherMsg.setWeatherTemp(temp.toString());
        weatherMsg.setWeatherDetail(weatherName[num]);
        weatherMsg.setWeatherImage(resId);
        weatherMsg.setWeatherLittleImage(resLittleId);

        return weatherMsg;
    }

    /**
     * 保存天气信息，城市countyCode存储数据库，
     * weatherContainer天气信息存储到sharePre
     * @param weatherContainer
     * @param countyCode
     */
    public void saveWeatherMsg(WeatherContainer weatherContainer,String countyCode){
        if (weatherContainer != null && countyCode != null) {
            if (TimeUtils.getHM(weatherContainer.getReleaseTime()).compareTo("18") >= 0) {//晚上的情况不能直接替换
                WeatherContainer weatherContainer1 = Utility.getWeatherContainer(context, countyCode);
                if (weatherContainer1 == null) {
                    String jsonWeatherInfo = GsonUtils.createGsonString(weatherContainer);
                    Utility.saveCountyWeatherInfo(context, countyCode, jsonWeatherInfo);
                    return;
                }
                weatherContainer.getWeatherDetailList().get(0)
                        .setDayNum(weatherContainer1.getWeatherDetailList().get(0).getDayNum());
                weatherContainer.getWeatherDetailList().get(0)
                        .setDayTemp(weatherContainer1.getWeatherDetailList().get(0).getDayTemp());
                weatherContainer.getWeatherDetailList().get(0)
                        .setNightWindDirectNum(weatherContainer1.getWeatherDetailList().get(0).getDayWindDirectNum());
                weatherContainer.getWeatherDetailList().get(0)
                        .setNightWindPowerNum(weatherContainer1.getWeatherDetailList().get(0).getDayWindPowerNum());
            }
            String jsonWeatherInfo = GsonUtils.createGsonString(weatherContainer);
            Utility.saveCountyWeatherInfo(context, countyCode, jsonWeatherInfo);
        }
    }

    /**
     * 创建前台线程
     * @param weatherMsg
     */
    public void toCreateForGround(WeatherMsg weatherMsg){
        if(Global.isStartService){
            Global.isStartService=false;
            Intent intent=new Intent(context, WeatherForegroundService.class);
            intent.putExtra(Global.INTENT_WEATHER_MSG, weatherMsg);
            context.startService(intent);
        }

    }
}
