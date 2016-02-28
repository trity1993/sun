package cc.trity.sun.baidlocatesdk;

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

    /**
     * 初始化百度sdk的配置
     */
    public void initBaiduLocation() {
        // 声明LocationClient类
        mLocationClient = new LocationClient(context);
        // 注册监听函数
        mLocationClient.registerLocationListener(bdLocationListener);
        // 设置定位参数
        setLocationOption();
    }

    /**
     * 设置定位参数。 定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等。
     */
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(24 * 60 * 60 * 1000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
    }

    /**
     * 递归请求位置信息，直至成功开启
     */
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
}
