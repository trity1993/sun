package cc.trity.sun.engine;

import android.content.Context;
import android.util.Log;

import java.util.List;

import cc.trity.library.net.DefaultThreadPool;
import cc.trity.library.net.HttpRequest;
import cc.trity.library.net.RequestCallback;
import cc.trity.library.net.RequestManager;
import cc.trity.library.net.RequestParameter;
import cc.trity.library.net.Response;
import cc.trity.library.net.URLData;
import cc.trity.library.net.UrlConfigManager;
import cc.trity.library.utils.GsonUtils;
import cc.trity.library.utils.LogUtils;
import cc.trity.sun.mockdata.MockService;

/**
 * 网络异步请求的操作类
 * 获取请求队列，再加入到线程池中，最后进行执行
 * Created by TryIT on 2016/2/5.
 */
public class RemoteService {
    private static final String TAG="RemoteService";
    private static RemoteService service = null;

    private RemoteService() {

    }

    public static RemoteService getInstance() {
        if (RemoteService.service == null) {
            synchronized (RemoteService.class){
                RemoteService.service = new RemoteService();
            }
        }
        return RemoteService.service;
    }

    public void invoke(final Context context,
                       final String apiKey,
                       final List<RequestParameter> params,
                       final String cacheKey,
                       final RequestCallback callBack) {
        invoke(context,apiKey,params,cacheKey,callBack,false);
    }
    public void invoke(final Context context,
                       final String apiKey,
                       final List<RequestParameter> params,
                       final String cacheKey,
                       final RequestCallback callBack,boolean isForceUpdate) {

        final URLData urlData = UrlConfigManager.findURL(context, apiKey);

        if(urlData.getMockClass()!=null){//说明有模拟数据，则直接返回数据
            try{
                MockService mockService=(MockService)Class.forName(urlData.getMockClass()).newInstance();
                String strResponse=mockService.getJsonData();
                final Response response= GsonUtils.getClass(strResponse,Response.class);
                if(callBack!=null){
                    if(response.isError()){
                        callBack.onFail(response.getErrorMessage());
                    }else{
                        callBack.onSuccess(response.getResult());
                    }
                }
            }catch (Exception e){
                LogUtils.e(TAG, Log.getStackTraceString(e));
            }
        }else{
            if(isForceUpdate){
                urlData.setExpires(0);
            }
            HttpRequest request = RequestManager.getInstance().createRequest(
                    urlData, params, cacheKey, callBack);
            DefaultThreadPool.getInstance().execute(request);
        }
    }
}
