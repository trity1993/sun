package cc.trity.library.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cc.trity.library.api.BaseInit;
import cc.trity.library.net.RequestManager;

/**
 * Created by TryIT on 2016/2/4.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseInit {

    /**
     * 请求列表管理器
     */
    protected RequestManager requestManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestManager=new RequestManager();
        super.onCreate(savedInstanceState);
        initVariables();
    }

    protected void init(Bundle savedInstanceState){
        initView(savedInstanceState);
        loadData();
    }

    @Override
    protected void onPause() {
        if (requestManager != null) {
            requestManager.cancelRequest();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (requestManager != null) {
            requestManager.cancelRequest();
        }
        super.onDestroy();
    }



    public RequestManager getRequestManager() {
        return requestManager;
    }
}
