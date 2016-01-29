package cc.trity.sun.networks;

import cc.trity.sun.model.Global;
import cc.trity.sun.model.WeatherRequest;
import cc.trity.sun.utils.TimeUtils;
import cc.trity.sun.utils.URLEncoderUtils;

/**
 * 网络操作类
 * Created by TryIT on 2016/1/22.
 */
public class HttpManager {
    /**
     * 生成请求链接url
     * 依靠isForcecast来判断是请求预报链接，还是相关指数等
     * @param isForecast
     * @param countyCode 对应城市代码
     * @return
     */
    public static String getReqAdress(boolean isForecast,String countyCode){
        //填充请求数据
        WeatherRequest weatherRequest=new WeatherRequest();
        weatherRequest.setDate(TimeUtils.getCurentTime("yyyyMMddHHmm"));
        weatherRequest.setAreaid(countyCode);
        //变成预报天气是应该执行下面这个方法
        if(isForecast)
            weatherRequest.changeForecastType();

        //生成密钥
        String encryptKey= URLEncoderUtils.standardURLEncoder(weatherRequest.generatePubliKey(), Global.PRIVATE_KEY);
        weatherRequest.setKey(encryptKey);
        return weatherRequest.generateUrl();
    }
}
