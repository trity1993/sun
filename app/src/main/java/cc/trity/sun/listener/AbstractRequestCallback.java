package cc.trity.sun.listener;

import cc.trity.library.net.RequestCallback;

/**
 * Created by TryIT on 2016/2/5.
 */
public abstract class AbstractRequestCallback implements RequestCallback {

    public abstract void onSuccess(String content);

    public void onFail(String errorMessage) {
        //统一输出错误的信息
    }
}