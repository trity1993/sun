package cc.trity.sun.location;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cc.trity.library.utils.LogUtils;

/**
 * 百度定位封装类
 * Created by TryIT on 2016/2/28.
 */
public class BadiLoactionHelper {
    private static final String TAG="BadiLoactionHelper";
    private LocationClient mLocationClient;
    private Context context;
    private BDLocationListener bdLocationListener;

    public BadiLoactionHelper(Context context, BDLocationListener bdLocationListener) {
        this.context = context;
        this.bdLocationListener = bdLocationListener;
    }

    /*初始化百度sdk的配置*/

    public void initBaiduLocation() {
        // 声明LocationClient类
        mLocationClient = new LocationClient(context);
        // 注册监听函数
        mLocationClient.registerLocationListener(bdLocationListener);
        // 设置定位参数
        setLocationOption();
    }

    /*
    * 设置定位参数。 定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等。
    * 具体参数的设置，祥看官方文档介绍
    */
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.SetIgnoreCacheException(true);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setScanSpan(24 * 60 * 60 * 1000);// 设置发起定位请求的间隔时间为5000ms
        mLocationClient.setLocOption(option);
    }

    /*递归请求位置信息，直至成功开启*/

    public void requestLocation() {
        if (mLocationClient != null) {
            if (!mLocationClient.isStarted()) {
                LogUtils.d(TAG, "start");

                mLocationClient.start();
            } else {
                mLocationClient.requestLocation();
            }
        } else {
            LogUtils.d(TAG, "locClient is null or not started");
        }
    }

    /*取消定位请求*/
    public void cancelRequestLocation(){
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }
}
