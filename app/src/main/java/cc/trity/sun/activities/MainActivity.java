package cc.trity.sun.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.sun.R;
import cc.trity.sun.activities.base.AppBaseActivity;
import cc.trity.sun.adapters.EndlessLoopAdapter;
import cc.trity.sun.db.DataBaseManager;
import cc.trity.sun.fragments.WeatherFragment;
import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.model.city.County;
import cc.trity.sun.view.CirclePageIndicator;

public class MainActivity extends AppBaseActivity {
    public static final int ADD_FRAGMENT=0;
    private static final String TAG = "MainActivity";

    @InjectView(R.id.viewpager_main)
    ViewPager viewpagerMain;
    @InjectView(R.id.indicator)
    CirclePageIndicator indicator;

    int[] resInt = new int[]{R.color.yellow_bg
            , R.color.pink_bg
            , R.color.green_bg
            , R.color.blue_bg};

    private int[] resDrawableInt = new int[]{
            R.drawable.corner_yellow_bg
            , R.drawable.corner_pink_bg
            , R.drawable.corner_green_bg
            , R.drawable.corner_blue_bg};
    List<Fragment> fragmentList=new ArrayList<>();
    List<County> countyList;

    int lenght = 1;

    String countyCode = null;
    String countyName = null;
    EndlessLoopAdapter endlessLoopAdapter = null;
    DataBaseManager dataBaseManager=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        this.init(savedInstanceState);
    }

    @Override
    public void initVariables() {

        dataBaseManager=DataBaseManager.getInstance(MainActivity.this);
        countyList=dataBaseManager.loadCounties();

        lenght=countyList.size();

        if(lenght==0){
            Intent intent = new Intent(MainActivity.this, ChooseAreaActivityApp.class);
            startActivityForResult(intent, MainActivity.ADD_FRAGMENT);
            return;
        }

        County county=null;

        for (int i = 0; i < lenght; i++) {
            county=countyList.get(i);
            Fragment fragment = WeatherFragment.newInstance(resInt[i%4], resDrawableInt[i%4], county.getWeaterCode(), county.getPlaceName());
//            Fragment fragment=new FourShowFragment();
            fragmentList.add(fragment);
        }
        //读取sharePrf是否打开前台线程
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        AppConstants.isStartService=sharedPreferences.getBoolean(AppConstants.SHARE_PREF_SERVICE, true);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (lenght == 1) {
            endlessLoopAdapter = new EndlessLoopAdapter(this.getSupportFragmentManager(), fragmentList, lenght);
        } else if (lenght <= 3) {
            //循环多一次，倍数增加
            for (int i = 0; i < lenght; i++) {
                Fragment fragment = WeatherFragment.newInstance(resInt[i], resDrawableInt[i], countyCode, countyName);
                fragmentList.add(fragment);
            }
            endlessLoopAdapter = new EndlessLoopAdapter(this.getSupportFragmentManager(), fragmentList, Integer.MAX_VALUE);
        } else if (lenght >= 4) {
            endlessLoopAdapter = new EndlessLoopAdapter(this.getSupportFragmentManager(), fragmentList, Integer.MAX_VALUE);
        }
        viewpagerMain.setAdapter(endlessLoopAdapter);
        viewpagerMain.setCurrentItem(lenght * 100);//设置再中间可以左右滑动
//        viewpagerMain.setPageTransformer(true, new CubeOutTransformer());
        indicator.setViewPagerFixedLength(viewpagerMain, lenght);
        indicator.setSnap(true);

    }

    @Override
    public void loadData() {
    }

    public void updateView(){

        countyList.clear();
        countyList.addAll(dataBaseManager.loadCounties());
        lenght=countyList.size();

        fragmentList.clear();
        for (int i = 0; i < lenght; i++) {
            County county=countyList.get(i);
            Fragment fragment = WeatherFragment.newInstance(resInt[i%4], resDrawableInt[i%4], county.getWeaterCode(), county.getPlaceName());
            fragmentList.add(fragment);
        }

        if (lenght == 1) {
            endlessLoopAdapter = new EndlessLoopAdapter(this.getSupportFragmentManager(), fragmentList, lenght);
        } else if (lenght <= 3) {
            //循环多一次，倍数增加
            for (int i = 0; i < lenght; i++) {
                Fragment fragment = WeatherFragment.newInstance(resInt[i%4], resDrawableInt[i%4], countyCode, countyName);
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
