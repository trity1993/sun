package cc.trity.sun.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import cc.trity.library.net.RequestCallback;
import cc.trity.library.utils.CommonUtils;
import cc.trity.sun.R;
import cc.trity.sun.activities.sign.LoginActivity;
import cc.trity.sun.engine.AppConstants;

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

    @Override
    public void onCookieExpired() {
        //统一弹出提示框进行提示重新登录的操作
        new AlertDialog.Builder(context)
                .setTitle("出错啦")
                .setMessage("Cookie过期，请重新登录")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(
                                        context,
                                        LoginActivity.class);
                                intent.putExtra(AppConstants.NeedCallback,
                                        true);
                                context.startActivity(intent);
                            }
                        }).show();
    }
}