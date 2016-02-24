package cc.trity.library.net;

/**
 * 请求后的回调接口
 * Created by TryIT on 2016/2/4.
 */
public interface RequestCallback {

    void onSuccess(String content);

    void onFail(String errorMsg);

    void onFail(int resErrorMsgInt);

    void onCookieExpired();//针对cookie失效的时候处理
}
