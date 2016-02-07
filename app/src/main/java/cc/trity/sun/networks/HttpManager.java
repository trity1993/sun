package cc.trity.sun.networks;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cc.trity.library.net.RequestParameter;
import cc.trity.library.utils.TimeUtils;
import cc.trity.sun.model.Global;
import cc.trity.sun.model.WeatherRequest;
import cc.trity.sun.utils.URLEncoderUtils;

/**
 * 网络操作类
 * Created by TryIT on 2016/1/22.
 */
public class HttpManager {
    private static final String TAG="HttpManager";
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
    /**
     * 生成请求链接url
     * 依靠isForcecast来判断是请求预报链接，还是相关指数等
     * @param isForecast 是否为预报天气
     * @param countyCode 对应城市代码
     * @param urlStr 固定url
     * @return 返回最终的参数容器
     */
    public static List<RequestParameter> getReqParameters(boolean isForecast,String countyCode
            ,String urlStr){

        List<RequestParameter> requestParameters=new ArrayList<>();
        //填充请求数据
        RequestParameter parameter=new RequestParameter("areaid",countyCode);
        RequestParameter parameter1=null;
        if(isForecast){
            parameter1=new RequestParameter("type","forecast_v");
        }else{
            parameter1=new RequestParameter("type","index_v");
        }
        RequestParameter parameter2=new RequestParameter("date",
                TimeUtils.getCurentTime("yyyyMMddHHmm"));
        //只需要获取前面6个
        RequestParameter parameter3=new RequestParameter("appid",Global.APP_ID);

        requestParameters.add(parameter);
        requestParameters.add(parameter1);
        requestParameters.add(parameter2);
        requestParameters.add(parameter3);

        String newUrl=generateUrl(requestParameters, urlStr);
        if(!TextUtils.isEmpty(newUrl)){
            requestParameters.set(requestParameters.size()-1,new RequestParameter("appid"
                    ,Global.APP_ID.substring(0,6)));
            String encryptKey= URLEncoderUtils.standardURLEncoder(newUrl, Global.PRIVATE_KEY);

            RequestParameter parameter4=new RequestParameter("key",encryptKey);
            requestParameters.add(parameter4);
        }

       return requestParameters;
    }

    /**
     * 通过parameters的键值对和国定的urlStr
     * 来生成请求的链接
     * @param parameters 参数的键值对
     * @param urlStr 固定不变的url
     * @return
     */
    public static String generateUrl(List<RequestParameter> parameters,String urlStr){
        StringBuffer paramBuffer=new StringBuffer();
        if(parameters!=null&parameters.size()>0){
            for(RequestParameter p:parameters){
                if(paramBuffer.length()==0){
                    paramBuffer.append(p.getName()+"="+p.getValue());
                }else{
                    paramBuffer.append("&"+p.getName()+"="+p.getValue());
                }
            }
            return urlStr+"?"+paramBuffer.toString();
        }
        return Global.DEFAULT_STRING_VALUE;
    }
}
