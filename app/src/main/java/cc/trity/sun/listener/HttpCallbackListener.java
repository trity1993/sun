package cc.trity.sun.listener;

/**
 * 因为http请求在线程中执行，不可return来检测是否执行成功，并作想用的的处理
 * 使用回调接口即可完成
 * Created by TryIT on 2016/1/7.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
