package cc.trity.sun.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import cc.trity.goldtrade.R;
import cc.trity.goldtrade.listener.OnDialogListener;

/**
 * 一些通用的方法
 * Created by TryIT on 2016/1/12.
 */
public final class CommonUtils {

    public static boolean isEmpty(Context context) {
        if (context == null)
            return true;
        return false;
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resInt) {
        Toast.makeText(context, resInt, Toast.LENGTH_SHORT).show();
    }

    public static AlertDialog showDialog(Context context,int resTitle,int resMsg,int rescancelBtn,int resPosBtn,final OnDialogListener onDialogListener) {
        AlertDialog dialog=new AlertDialog.Builder(context)
                .setTitle(resTitle)
                .setMessage(resMsg)
                .setPositiveButton(resPosBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.cancel();
                        onDialogListener.onClickPositiveButton();
                    }
                }).setNeutralButton(rescancelBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.cancel();
                        onDialogListener.onClickNeutralButton();
                    }
                }).create();
        return dialog;

    }
    public static void showDialog(Context context,String title,String msg,String posBtn,final OnDialogListener onDialogListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(posBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.cancel();
//                        BaseActivity.this.showNetworkSeting();//转到网络设置
                        onDialogListener.onClickPositiveButton();
                    }
                }).setNeutralButton(R.string.BUTTON_CANCLE, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.cancel();
                        onDialogListener.onClickNeutralButton();
                        // finish();
                    }
                }).create().show();

    }

}
