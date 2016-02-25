package cc.trity.library.net;

import android.os.Handler;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import cc.trity.library.BaseApplication;
import cc.trity.library.cache.CacheManager;
import cc.trity.library.utils.FileUtils;
import cc.trity.library.utils.LogUtils;
import cc.trity.library.utils.TimeUtils;
import cc.trity.tritylibrary.R;

/**
 * 执行http请求操作
 * Created by TryIT on 2016/2/4.
 */
public class HttpRequest implements Runnable {
    private static final String TAG = "HttpRequest";

    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String USER_AGENT = "User-Agent";

    private final static String COOKIE_PATH = FileUtils.getDiskCacheDirPath(BaseApplication.getContext()) + File.separator + "cookie";

    public static final String REQUEST_GET = "GET";
    public static final String REQUEST_POST = "POST";

    private URLData urlData = null;
    private RequestCallback requestCallback = null;
    private List<RequestParameter> parameter = null;
    private String urlStr = null;
    private String newUrl = null; // 拼接key-value后的url
    private String cacheKey;//使用url，会导致一些发时间作为参数到服务器中，导致无法再次获取缓存

    //cookie的相关信息
    private CookieManager cmrCookieMan;
    private AutoCookieStore autoCookieStore;

    //头信息
    HashMap<String, String> httpHeaders;

    public static long deltaBetweenServerAndClientTime; // 服务器时间和客户端时间的差值

    protected Handler handler;

    public HttpRequest(final URLData data, final List<RequestParameter> parameter,
                       final RequestCallback callback) {
        this.urlData = data;
        this.urlStr = urlData.getUrl();
        this.parameter = parameter;
        this.requestCallback = callback;

        handler = new Handler();
    }

    public HttpRequest(final URLData data, final List<RequestParameter> parameter, final String cacheKey,
                       final RequestCallback callback) {
        this(data, parameter, callback);
        this.cacheKey = cacheKey;
    }

    @Override
    public void run() {
        HttpURLConnection httpURLConnection = null;
        try {
            String netType = urlData.getNetType();

            if (netType.equals(REQUEST_GET)) {

                //设置参数得到url
                final StringBuffer paramBuffer = new StringBuffer();

                if ((parameter != null) && (parameter.size() > 0)) {

                    sortKeys();//进行排序，使得url不会因为顺序的原因而导致不唯一

                    for (RequestParameter p : parameter) {
                        if (paramBuffer.length() == 0) {
                            paramBuffer.append(p.getName() + "=" + p.getValue());
                        } else {
                            paramBuffer.append("&" + p.getName() + "=" + p.getValue());
                        }
                    }
                }
                newUrl = urlStr + "?" + paramBuffer.toString();
                LogUtils.d(TAG, newUrl);

                //判断是否超过缓存时间，是否需要更新
                if (urlData.getExpires() > 0) {
                    final String content;
                    if (cacheKey != null) {
                        content = CacheManager.getInstance(BaseApplication.getContext()).getFileCache(cacheKey);
                    } else {
                        //默认使用url作为key
                        content = CacheManager.getInstance(BaseApplication.getContext()).getFileCache(newUrl);
                    }
                    if (content != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                requestCallback.onSuccess(content);

                            }
                        });
                        return;
                    }
                }
                addCookie();// 添加Cookie到请求头中

                URL url = new URL(newUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod(REQUEST_GET);
                httpURLConnection.setDoInput(true);
                //添加http头部信息
                setHttpHeaders(httpURLConnection);

                //额外设置
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setReadTimeout(8000);
                httpURLConnection.connect();//连接

            } else if (netType.equals(REQUEST_POST)) {
                addCookie();// 添加Cookie到请求头中

                URL url = new URL(urlStr);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod(REQUEST_POST);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                //额外设置
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setReadTimeout(8000);
                httpURLConnection.connect();//连接
                //发送post数据
                DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
                //添加参数
                StringBuffer sBuffer = new StringBuffer();
                if (parameter != null && parameter.size() > 0) {
                    for (RequestParameter pTmp : parameter) {
                        if (sBuffer.length() == 0) {
                            sBuffer.append(pTmp.getName() + "=" + pTmp.getValue());
                        } else {
                            sBuffer.append("&" + pTmp.getName() + "=" + pTmp.getValue());
                        }
                    }
                    out.writeBytes(sBuffer.toString());
                }
            } else {
                return;
            }

            //获取返回的数据
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 更新服务器时间和本地时间的差值
                updateDeltaBetweenServerAndClientTime(httpURLConnection);

                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream != null) {
                    //判断是否使用gzip进行压缩
                    if(httpURLConnection.getContentEncoding()!=null
                            &&httpURLConnection.getContentEncoding().contains("gzip")){
                        inputStream=new GZIPInputStream(inputStream);
                    }
                    BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    final StringBuffer sbuf = new StringBuffer();
                    while ((line = bufReader.readLine()) != null) {
                        sbuf.append(line);
                    }
                    final String content = sbuf.toString();
                    if (content.isEmpty()) {
                        handleNetworkError(R.string.error_server_response);
                    }
                    String strResponse = "{\"isError\":false,\"errorType\":0,\"errorMessage\":\"\",\"result\":" + content + "}";
//                    LogUtils.d(TAG,strResponse);
                    JSONObject jsonObject = new JSONObject(strResponse);//不可以用gson进行解析，会认为result的值，而进而解析导致错误

                    Response responseInGson = new Response(jsonObject.getBoolean("isError"), jsonObject.getInt("errorType"),
                            jsonObject.getString("errorMessage"), jsonObject.getString("result"));
                    if (responseInGson.isError()) {
                        if (responseInGson.getErrorType() == 1) {//为1的时候，说明cookie失效，重新登录
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    requestCallback.onCookieExpired();
                                }
                            });
                        } else {
                            handleNetworkError(R.string.error_server_response);

                        }
                    } else {
                        //缓存到文件中
                        CacheManager cacheManager = CacheManager.getInstance(BaseApplication.getContext());
                        if (urlData.getNetType().equals(REQUEST_GET)
                                && urlData.getExpires() > 0) {
                            if (cacheKey != null) {
                                cacheManager.putFileCache(cacheKey, content, urlData.getExpires());
                            } else {
                                cacheManager.putFileCache(newUrl, content, urlData.getExpires());
                            }
                        }
                        //成功进行回调
                        if (requestCallback != null) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    requestCallback.onSuccess(content);

                                }
                            });
                        }
                        // 保存Cookie
                        saveCookie();
                    }

                } else {
                    handleNetworkError(R.string.error_server_response);
                }
            } else {
                handleNetworkError(R.string.error_network_die);
            }

        } catch (Exception e) {
            LogUtils.e(TAG, Log.getStackTraceString(e));
            handleNetworkError(R.string.error_network_die);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                httpURLConnection = null;
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
     * 从本地获取cookie列表
     */
    public void addCookie() {
        autoCookieStore = new AutoCookieStore(BaseApplication.getContext());
        cmrCookieMan = new CookieManager(
                autoCookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cmrCookieMan);
    }

    /**
     * cookie列表保存到本地
     */
    public void saveCookie() {
        List<HttpCookie> httpCookieList = cmrCookieMan.getCookieStore().getCookies();
        try {
            URI url = new URI(newUrl);
            for (HttpCookie httpCookie : httpCookieList)
                autoCookieStore.add(url, httpCookie);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * http header可以存储很多信息，如：appid,clientType(android、iOS)
     * 自定义添加HttpURLConnection的头部信息
     * 不明白不直接设置，而是通过map集合进行转化设置
     *
     * @param httpURLConnection
     */
    private void setHttpHeaders(HttpURLConnection httpURLConnection) {
        if(httpURLConnection!=null){
            httpURLConnection.setRequestProperty(ACCEPT_CHARSET, "UTF-8,*");
            httpURLConnection.setRequestProperty(USER_AGENT, "Young Heart Android App ");
            httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
        }

        /*httpHeaders.clear();
        httpHeaders.put(ACCEPT_CHARSET, "UTF-8,*");
        httpHeaders.put(USER_AGENT, "Young Heart Android App ");

        if ((httpURLConnection != null) && (httpHeaders != null)) {
            for (final Map.Entry<String, String> entry : httpHeaders.entrySet()) {
                if (entry.getKey() != null) {
                    httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
        }*/
    }

    /**
     * 通过得到http header的date信息，
     * 来更新服务器与本地手机端的差值，使得时间总是相同。
     * @param connection
     */
    private void updateDeltaBetweenServerAndClientTime(HttpURLConnection connection){
        if(connection!=null){
            String key;
            String serverDate=null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if(key.equals("Date")){
                    serverDate=connection.getHeaderField(i);
                    break;
                }
            }
            if(serverDate!=null&&!serverDate.isEmpty()){

                Date serverDateUAT = TimeUtils.getGMT8Time(serverDate);
                if(serverDate!=null){
                    deltaBetweenServerAndClientTime = serverDateUAT.getTime()
                            + 8 * 60 * 60 * 1000
                            - System.currentTimeMillis();
                }
            }
        }
    }

    /**
     * 对URL的键值进行排序，产生唯一值
     * 因为不同参数的不同位置下，产生的数据不同。
     */
    private void sortKeys() {

    }
}
