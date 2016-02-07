package cc.trity.sun.activities.base;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cc.trity.library.activity.BaseActivity;
import cc.trity.library.net.RequestCallback;
import cc.trity.library.utils.CommonUtils;
import cc.trity.sun.ActivityCollector;
import cc.trity.sun.R;

/**
 * Created by TryIT on 2016/1/13.
 */
public abstract class AppBaseActivity extends BaseActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }
    /**
     * 初始化toolbar
     * @param mToolbar
     */
    protected void trySetupToolbar(Toolbar mToolbar) {
        try {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(getClass().getSimpleName(), "toolbar is null!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 实现后退按钮
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home://相应 actionbar的后退按钮
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {//后退
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    /**
     * 显示进度对话框
     */
    protected void showProgressDialog(String showMesg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(showMesg);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    protected void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 统一错误时候的输出
     */
    public abstract class AbstractRequestCallback
            implements RequestCallback {

        public abstract void onSuccess(String content);

        public void onFail(String errorMessage) {
            CommonUtils.showToast(AppBaseActivity.this, R.string.error_network);
        }
    }
}
