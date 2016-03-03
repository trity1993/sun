package cc.trity.library.net;

import java.util.ArrayList;
import java.util.List;

/**
 * 维护请求队列，添加和删除请求
 * Created by TryIT on 2016/2/5.
 */
public class RequestManager {
    private List<HttpRequest> requestList = null;

    private static RequestManager requestManager=null;

    private RequestManager(){
        // 异步请求列表
        requestList=new ArrayList<>();
    }

    public static RequestManager getInstance(){
        if(requestManager==null){
            synchronized (RequestManager.class){
                requestManager=new RequestManager();
            }
        }
        return requestManager;
    }

    /**
     * 无参数调用
     */
    public HttpRequest createRequest(final URLData urlData,
                                     final RequestCallback requestCallback) {
        return createRequest(urlData, null, requestCallback);
    }

    /**
     * 有参数调用
     */
    public HttpRequest createRequest(final URLData urlData,
                                     final List<RequestParameter> params,
                                     final RequestCallback requestCallback) {
        final HttpRequest request = new HttpRequest(urlData, params,
                requestCallback);

        addRequest(request);
        return request;
    }
    public HttpRequest createRequest(final URLData urlData,
                                     final List<RequestParameter> params,final String cacheKey,
                                     final RequestCallback requestCallback) {
        final HttpRequest request = new HttpRequest(urlData, params,cacheKey,
                requestCallback);

        addRequest(request);
        return request;
    }

    /**
     * 添加Request到列表
     */
    public void addRequest(final HttpRequest request) {
        requestList.add(request);
    }

    /**
     * 取消网络请求,面对httpUrlConnection如何取消网络请求
     */
    public void cancelRequest() {
        /*if ((requestList != null) && (requestList.size() > 0)) {
            for (final HttpRequest request : requestList) {
                if (request != null) {
//                    try {
//                        request.getRequest().abort();
                        requestList.remove(request);
//                    } catch (final UnsupportedOperationException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }*/
    }
}
