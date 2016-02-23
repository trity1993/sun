package cc.trity.sun.listener;

import android.content.Context;

import cc.trity.library.net.RequestCallback;
import cc.trity.library.utils.CommonUtils;
import cc.trity.sun.R;

/**
 * 统一错误输出的接口
 * Created by TryIT on 2016/2/5.
 */
public abstract class AbstractRequestCallback implements RequestCallback {
    private Context context;
    public AbstractRequestCallback(Context context) {
        this.context=context;
    }

    public abstract void onSuccess(String content);

    @Override
    public void onFail(int resErrorMessage) {
        //统一输出错误的信息
        CommonUtils.showToast(context, R.string.error_network_refresh);
    }
}