package cc.trity.sun.activities.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cc.trity.sun.R;

/**
 * Created by TryIT on 2016/1/13.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
    }
    public void init(){
        initView();
        loadData();
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
        getMenuInflater().inflate(R.menu.menu_base,menu);
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

    /**
     * 显示进度对话框
     */
    protected void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
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
     * 初始化变量，包括Intent带的数据和Activity内的变量
     * 注意这个在得到view布局之前执行
     */
    public abstract void initVariables();

    /**
     * 加载Layout布局文件，初始化控件，以及相应的事件方法
     */
    public abstract void initView();

    /**
     * 进行调用获取数据
     */
    public abstract void loadData();
}
