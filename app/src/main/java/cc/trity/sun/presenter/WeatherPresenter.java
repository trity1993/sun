package cc.trity.sun.presenter;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cc.trity.library.activity.BaseActivity;
import cc.trity.library.net.RequestCallback;
import cc.trity.library.net.RequestParameter;
import cc.trity.library.utils.FileUtils;
import cc.trity.library.utils.GsonUtils;
import cc.trity.library.utils.NetWorkUtils;
import cc.trity.library.utils.TimeUtils;
import cc.trity.library.utils.Utils;
import cc.trity.sun.R;
import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.engine.RemoteService;
import cc.trity.sun.listener.HttpCallbackListener;
import cc.trity.sun.model.ForcecastItem;
import cc.trity.sun.model.WeatherMsg;
import cc.trity.sun.model.weathersponse.WeatherContainer;
import cc.trity.sun.model.weathersponse.WeatherDetail;
import cc.trity.sun.networks.HttpManager;
import cc.trity.sun.networks.HttpNetWorkTools;
import cc.trity.sun.utils.Utility;

/**
 * 处理更新weather的逻辑
 * Created by TryIT on 2016/1/29.
 */
public class WeatherPresenter {
    private static final String TAG="WeatherPresenter";

    private Context context;
    public WeatherPresenter(Context context){
        this.context=context;
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

    /**
     * 异步加载weather天气
     * @param baseActivity
     * @param countyCode
     * @param requestCallback
     */
    public void loadWeather(BaseActivity baseActivity,String countyCode,RequestCallback requestCallback){
        //生成url
        if(NetWorkUtils.isNetworkAvailable(context)){
            //执行网络请求的操作
            List<RequestParameter> parameterList=HttpManager.getReqParameters(true, countyCode, AppConstants.URL_WEATHER);
            RemoteService.getInstance().invoke(baseActivity, "getWeatherForecast", parameterList, countyCode, requestCallback);
        }else{
            requestCallback.onFail(R.string.error_network_die);
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
        long dateHM = Utils.convertToLong(weatherContainer.getReleaseTime().trim());
        long curDate = Utils.convertToLong(TimeUtils.getCurentTime("yyyyMMddHHmm"));

        StringBuilder sbDay=new StringBuilder(TimeUtils.getCurentTime("yyyyMMdd"));
        sbDay.append("0600");
        long updateTime=Utils.convertToLong(sbDay.toString());
        if (dateHM < updateTime && curDate >= updateTime) {
            return true;
        }

        sbDay.replace(8,12,"1100");
        updateTime=Utils.convertToLong(sbDay.toString());
        if (dateHM < updateTime && curDate >= updateTime) {
            return true;
        }

        sbDay.replace(8, 12, "1800");
        updateTime=Utils.convertToLong(sbDay.toString());
        if (dateHM < updateTime && curDate >= updateTime) {
            return true;
        }
        return false;
    }

    /**
     * 通过weatherContainer更新数据
     * @param weatherContainer
     * @return
     */
    public WeatherMsg updateData(WeatherContainer weatherContainer,String countName) {
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

        int hour = Utils.convertToInt(TimeUtils.getCurentTime("HH"));
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
        if(TextUtils.isEmpty(imgIconNum))
            return null;
        int num = Utils.convertToInt(imgIconNum);
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
        weatherMsg.setWeatherLocation(countName);
        weatherMsg.setWeatherImage(resId);
        weatherMsg.setWeatherLittleImage(resLittleId);

        return weatherMsg;
    }

    /**
     * 得到预报所需要的数据
     * @param weatherContainer
     * @return
     */
    public List<ForcecastItem> getForcecastData(WeatherContainer weatherContainer) {
        if (weatherContainer == null) {
            return null;
        }
        List<ForcecastItem> forcecastItems=new ArrayList<>();

        //加载天气编码的名称
        String[] weatherName=context.getResources().getStringArray(R.array.weather_names);
        //加载图片资源
        int[] resDayImages= FileUtils.getResourseArray(context, R.array.weather_little_icon);
        int[] resNightImages=FileUtils.getResourseArray(context, R.array.weather_little_night_icon);
        int[] resImages;
        String imgIconNum,imgDayIconNum,imgNightIconNum;
        List<WeatherDetail> weatherDetails = weatherContainer.getWeatherDetailList();

        if(weatherDetails!=null&&weatherDetails.size()!=0){
            for(WeatherDetail weatherDetail:weatherDetails){
                String temp=null;
                int hour = Utils.convertToInt(TimeUtils.getCurentTime("HH"));
                if (hour > 18 || hour < 6) {
                    temp=weatherDetail.getNightTemp();
                    imgIconNum = weatherDetail.getNightNum();
                    resImages=FileUtils.getResourseArray(context, R.array.weather_little_night_icon);
                } else {
                    temp=weatherDetail.getDayTemp();
                    imgIconNum = weatherDetail.getDayNum();
                    resImages=FileUtils.getResourseArray(context, R.array.weather_little_icon);
                }
                //得到早上和下午Image编号
                imgDayIconNum=weatherDetail.getDayNum();
                imgNightIconNum=weatherDetail.getNightNum();

                if (TextUtils.isEmpty(temp)) {
                    return null;
                }

                //设置图标
                int num = Utils.convertToInt(imgIconNum);
                int dayNum = Utils.convertToInt(imgDayIconNum);
                int nightNum = Utils.convertToInt(imgNightIconNum);

                String curDateStr= weatherContainer.getReleaseTime();

                ForcecastItem forcecastItem = new ForcecastItem();
                forcecastItem.setReleaseTime(TimeUtils.getAssignFormatTime(curDateStr,AppConstants.MATCH_DATE_MINUTE,"dd/MM"));//重新设置时间格式
                forcecastItem.setWeatherTemp(temp);
                forcecastItem.setWeatherDetail(weatherName[num]);

                forcecastItem.setDayTemp(weatherDetail.getDayTemp());
                forcecastItem.setNightTemp(weatherDetail.getNightTemp());

                forcecastItem.setWeatherLittleImage(checkResImg(resImages, num));
                forcecastItem.setResDayImage(checkResImg(resDayImages, dayNum));
                forcecastItem.setResNightImage(checkResImg(resNightImages, nightNum));

                forcecastItems.add(forcecastItem);
            }

        }

        return forcecastItems;
    }

    /**
     * 检测返回的数据图片所代表的编码是否符合要求
     * @param resImgs
     * @param index
     * @return
     */
    public int checkResImg(int[] resImgs,int index){
        if (index == 53) {
            return resImgs[resImgs.length - 1];
        } else if (index == 99) {
            return R.mipmap.ic_launcher;
        } else {
            return resImgs[index];
        }
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
}
