package cc.trity.library.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cc.trity.library.api.BaseInit;

/**
 * Created by TryIT on 2016/2/4.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements BaseInit {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
    }

    protected void init(Bundle savedInstanceState){
        initView(savedInstanceState);
        loadData();
    }

    @Override
    protected void onPause() {
//        RequestManager.getInstance().cancelRequest();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        RequestManager.getInstance().cancelRequest();

        super.onDestroy();
    }
}
