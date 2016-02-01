package cc.trity.sun.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.sun.R;
import cc.trity.sun.activities.base.BaseActivity;
import cc.trity.sun.adapters.EndlessLoopAdapter;
import cc.trity.sun.db.DataBaseManager;
import cc.trity.sun.fragments.WeatherFragment;
import cc.trity.sun.model.Global;
import cc.trity.sun.model.city.County;
import cc.trity.sun.view.CirclePageIndicator;
import cc.trity.sun.view.CubeOutTransformer;

public class MainActivity extends BaseActivity {
    public static final int ADD_FRAGMENT=0;
    private static final String TAG = "MainActivity";
    @InjectView(R.id.viewpager_main)
    ViewPager viewpagerMain;
    int[] resInt = new int[]{Color.parseColor("#ffc722")
            , Color.parseColor("#ff2259")
            , Color.parseColor("#00d8a2")
            , Color.parseColor("#545a4a")
            , Color.parseColor("#b13fd7")};
    List<Fragment> fragmentList=new ArrayList<>();
    List<County> countyList;


    int lenght = 1;
    @InjectView(R.id.indicator)
    CirclePageIndicator indicator;

    String countyCode = null;
    String countyName = null;
    EndlessLoopAdapter endlessLoopAdapter = null;
    DataBaseManager dataBaseManager=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        this.init();
    }

    @Override
    public void initVariables() {

        dataBaseManager=DataBaseManager.getInstance(MainActivity.this);
        countyList=dataBaseManager.loadCounties();

        lenght=countyList.size();

        County county=null;
        for (int i = 0; i < lenght; i++) {
            county=countyList.get(i);
            Fragment fragment = WeatherFragment.newInstance(resInt[i], i, county.getWeaterCode(), county.getPlaceName());
            fragmentList.add(fragment);
        }
        //读取sharePrf是否打开前台线程
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        Global.isStartService=sharedPreferences.getBoolean(Global.SHARE_PREF_SERVICE, true);
    }

    @Override
    public void initView() {
        if (lenght == 1) {
            endlessLoopAdapter = new EndlessLoopAdapter(this.getSupportFragmentManager(), fragmentList, lenght);
        } else if (lenght <= 3) {
            //循环多一次，倍数增加
            for (int i = 0; i < lenght; i++) {
                Fragment fragment = WeatherFragment.newInstance(resInt[i], i, countyCode, countyName);
                fragmentList.add(fragment);
            }
            endlessLoopAdapter = new EndlessLoopAdapter(this.getSupportFragmentManager(), fragmentList, Integer.MAX_VALUE);
        } else if (lenght >= 4) {
            endlessLoopAdapter = new EndlessLoopAdapter(this.getSupportFragmentManager(), fragmentList, Integer.MAX_VALUE);
        }
        viewpagerMain.setAdapter(endlessLoopAdapter);
        viewpagerMain.setCurrentItem(lenght * 100);//设置再中间可以左右滑动
        viewpagerMain.setPageTransformer(true, new CubeOutTransformer());
        indicator.setViewPagerFixedLength(viewpagerMain, lenght);
        indicator.setSnap(true);
        if(lenght==0){
            Intent intent = new Intent(MainActivity.this, ChooseAreaActivity.class);
            startActivityForResult(intent, MainActivity.ADD_FRAGMENT);
        }
    }

    @Override
    public void loadData() {
//        startService(intent);
//        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    public void updateView(){

        countyList.clear();
        countyList.addAll(dataBaseManager.loadCounties());
        lenght=countyList.size();

        fragmentList.clear();
        for (int i = 0; i < lenght; i++) {
            County county=countyList.get(i);
            Fragment fragment = WeatherFragment.newInstance(resInt[i], i, county.getWeaterCode(), county.getPlaceName());
            fragmentList.add(fragment);
        }

        if (lenght == 1) {
            endlessLoopAdapter = new EndlessLoopAdapter(this.getSupportFragmentManager(), fragmentList, lenght);
        } else if (lenght <= 3) {
            //循环多一次，倍数增加
            for (int i = 0; i < lenght; i++) {
                Fragment fragment = WeatherFragment.newInstance(resInt[i], i, countyCode, countyName);
                fragmentList.add(fragment);
            }
            endlessLoopAdapter = new EndlessLoopAdapter(this.getSupportFragmentManager(), fragmentList, Integer.MAX_VALUE);
        } else if (lenght >= 4) {
            endlessLoopAdapter = new EndlessLoopAdapter(this.getSupportFragmentManager(), fragmentList, Integer.MAX_VALUE);
        }
        viewpagerMain.setAdapter(endlessLoopAdapter);
        indicator.setViewPagerFixedLength(viewpagerMain, lenght);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case ADD_FRAGMENT:
                    updateView();
                    break;
            }
        }
    }
}
