package cc.trity.library.net;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cc.trity.library.BaseApplication;
import cc.trity.library.cache.CacheManager;
import cc.trity.library.utils.GsonUtils;
import cc.trity.library.utils.LogUtils;
import cc.trity.tritylibrary.R;

/**
 * 执行http请求操作
 * Created by TryIT on 2016/2/4.
 */
public class HttpRequest implements Runnable {
    private static final String TAG="HttpRequest";

    public static final String REQUEST_GET = "get";
    public static final String REQUEST_POST = "post";

    private URLData urlData = null;
    private RequestCallback requestCallback = null;
    private List<RequestParameter> parameter = null;
    private String urlStr = null;
    private String newUrl = null; // 拼接key-value后的url
    private String cacheKey;//使用url，会导致一些发时间作为参数到服务器中，导致无法再次获取缓存

    protected Handler handler;

    public HttpRequest(final URLData data,final List<RequestParameter> parameter,
                      final RequestCallback callback){
        this.urlData=data;
        this.urlStr=urlData.getUrl();
        this.parameter=parameter;
        this.requestCallback=callback;

        handler=new Handler();
    }
    public HttpRequest(final URLData data,final List<RequestParameter> parameter,final String cacheKey,
                      final RequestCallback callback){
        this(data,parameter,callback);
        this.cacheKey=cacheKey;
    }

    @Override
    public void run() {
        HttpURLConnection httpURLConnection=null;
        try {
            String netType=urlData.getNetType();
            if(netType.equals(REQUEST_GET)){

                //设置参数得到url
                final StringBuffer paramBuffer = new StringBuffer();

                if ((parameter != null) && (parameter.size() > 0)) {

                    sortKeys();//进行排序，使得url不会因为顺序的原因而导致不唯一

                    for(RequestParameter p:parameter){
                        if(paramBuffer.length()==0){
                            paramBuffer.append(p.getName()+"="+p.getValue());
                        }else{
                            paramBuffer.append("&"+p.getName()+"="+p.getValue());
                        }
                    }
                }
                newUrl=urlStr+"?"+paramBuffer.toString();
                LogUtils.d(TAG,newUrl);

                //判断是否超过缓存时间，是否需要更新
                if(urlData.getExpires()>0){
                    final String content;
                    if(cacheKey!=null){
                        content= CacheManager.getInstance(BaseApplication.getContext()).getFileCache(cacheKey);
                    }else{
                        //默认使用url作为key
                        content=CacheManager.getInstance(BaseApplication.getContext()).getFileCache(newUrl);
                    }
                    if(content!=null){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                requestCallback.onSuccess(content);

                            }
                        });
                        return ;
                    }
                }

                URL url=new URL(newUrl);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod(REQUEST_GET);
                httpURLConnection.setDoInput(true);
                //额外设置
                httpURLConnection.setConnectTimeout(8000);
                httpURLConnection.setReadTimeout(8000);
                httpURLConnection.connect();//连接

            }else if(netType.equals(REQUEST_POST)){
                URL url=new URL(urlStr);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod(REQUEST_POST);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoInput(true);
                //额外设置
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setReadTimeout(8000);
                httpURLConnection.connect();//连接
                //发送post数据
                DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
                //添加参数
                StringBuffer sBuffer=new StringBuffer();
                if(parameter!=null&&parameter.size()>0){
                    for(RequestParameter pTmp:parameter){
                        if(sBuffer.length()==0){
                            sBuffer.append(pTmp.getName()+"="+pTmp.getValue());
                        }else{
                            sBuffer.append("&"+pTmp.getName()+"="+pTmp.getValue());
                        }
                    }
                    out.writeBytes(sBuffer.toString());
                }
            }else{
                return ;
            }

            //获取返回的数据
            if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream inputStream=httpURLConnection.getInputStream();
                if(inputStream!=null){
                    BufferedReader bufReader=new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    final StringBuffer sbuf = new StringBuffer();
                    while ((line = bufReader.readLine()) != null) {
                        sbuf.append(line);
                    }
                    final String content =sbuf.toString();
                    if(content.isEmpty()){
                        handleNetworkError(R.string.error_server_response);
                    }
                    String strResponse="{'isError':false,'errorType':0,'errorMessage':'','result':"+content+"}";
                    Response responseInGson= GsonUtils.getClass(strResponse, Response.class);
                    if(responseInGson.isError()){
                        handleNetworkError(R.string.error_server_response);
                    }else{
                        //缓存到文件中
                        CacheManager cacheManager=CacheManager.getInstance(BaseApplication.getContext());
                        if (urlData.getNetType().equals(REQUEST_GET)
                                && urlData.getExpires() > 0) {
                            if(cacheKey!=null){
                                cacheManager.putFileCache(cacheKey, content, urlData.getExpires());
                            }else{
                                cacheManager.putFileCache(newUrl,content,urlData.getExpires());
                            }
                        }
                        //成功进行回调
                        if(requestCallback!=null){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    requestCallback.onSuccess(content);

                                }
                            });
                        }
                    }

                }else{
                    handleNetworkError(R.string.error_server_response);
                }
            }else{
                handleNetworkError(R.string.error_network_die);
            }

        }catch (Exception e){
            LogUtils.e(TAG, Log.getStackTraceString(e));
            handleNetworkError(R.string.error_donnot_knowledge);
        }finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
                httpURLConnection=null;
            }
        }
    }

    public void handleNetworkError(final int resErrorInt) {
        if ((requestCallback != null)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    HttpRequest.this.requestCallback.onFail(resErrorInt);
                }
            });
        }
    }

    /**
     * 对URL的键值进行排序，产生唯一值
     * 因为不同参数的不同位置下，产生的数据不同。
     */
    private void sortKeys(){

    }
}
