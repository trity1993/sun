package cc.trity.sun.activities.base;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cc.trity.library.activity.BaseAppCompatActivity;
import cc.trity.library.ActivityCollector;
import cc.trity.sun.R;
import cc.trity.sun.activities.SettingActivity;

/**
 * Created by TryIT on 2016/1/13.
 */
public abstract class AppBaseActivity extends BaseAppCompatActivity {

    protected boolean needCallback;//是否需要回调

    private ProgressDialog progressDialog;

    protected int resToolBgColor=-1;

    protected boolean isFirstOpen=true;

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
            case R.id.action_setting:
                SettingActivity.actionStart(AppBaseActivity.this, resToolBgColor);
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

}
