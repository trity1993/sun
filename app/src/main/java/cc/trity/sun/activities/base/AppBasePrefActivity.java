package cc.trity.sun.activities.base;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.sun.R;
import cc.trity.sun.listener.OnPreferenceListener;

/**
 * Created by TryIT on 2015/4/7.
 * 设置的基类，抽象类，则不需要进行实现相关的方法
 */
public abstract class AppBasePrefActivity extends AppBaseActivity implements OnPreferenceListener,Preference.OnPreferenceChangeListener {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private int resToolBg;
    protected BasePreferenceFragment bpfragment;
    @Override
    public void initVariables() {
        resToolBg=getIntent().getIntExtra("resBgColor",-1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_settings);
        ButterKnife.inject(this);
        initView(savedInstanceState);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        trySetupToolbar(toolbar);
        if(resToolBg!=-1){
            toolbar.setBackgroundColor(resToolBg);
        }
        //用fragment来进行代替布局，使用一种嵌套的方式，使得用上PreferenceFragment进行显示
        bpfragment=BasePreferenceFragment.newInstance(this);
        getFragmentManager().beginTransaction().replace(R.id.include_setting_container, bpfragment).commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_setting).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    public static class BasePreferenceFragment extends PreferenceFragment{
        public static BasePreferenceFragment newInstance(OnPreferenceListener onPreferenceListener){
            Bundle bundle=new Bundle();
            bundle.putInt("resId", onPreferenceListener.getPreferencesResId());
            BasePreferenceFragment basePreferenceFragment=new BasePreferenceFragment();
            basePreferenceFragment.setArguments(bundle);
            return basePreferenceFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle=getArguments();
            int resId=bundle.getInt("resId");
            addPreferencesFromResource(resId);//加载preference的xml
        }
    }
}
