package cc.trity.sun.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.library.adapter.viewpager.LoopFPageAdapter;
import cc.trity.library.fragment.BaseFragment;
import cc.trity.library.utils.CommonUtils;
import cc.trity.sun.R;
import cc.trity.sun.db.DataBaseManager;
import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.model.city.County;
import cc.trity.sun.ui.activities.base.AppBaseActivity;
import cc.trity.sun.ui.fragments.WeatherFragment;
import cc.trity.sun.ui.view.CubeOutTransformer;
import cc.trity.sun.ui.view.TipViewController;

public class MainActivity extends AppBaseActivity {
    public static final int ADD_FRAGMENT = 0;
    private static final String TAG = "MainActivity";

    @InjectView(R.id.viewpager_main)
    ViewPager viewpagerMain;

    @InjectView(R.id.view_pager_dots)
    LinearLayout llDot;

    int[] resInt = new int[]{R.color.yellow_bg
            , R.color.pink_bg
            , R.color.green_bg
            , R.color.blue_bg};

    private int[] resDrawableInt = new int[]{
            R.drawable.corner_yellow_bg
            , R.drawable.corner_pink_bg
            , R.drawable.corner_green_bg
            , R.drawable.corner_blue_bg};
    List<BaseFragment> fragmentList = new ArrayList<>();
    List<County> countyList;

    int lenght = 1;

    County county = null;
    DataBaseManager dataBaseManager = null;
    LoopFPageAdapter loopAdapter;

    private TipViewController tipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        this.init(savedInstanceState);
        if (isFirstOpen) {
            tipView=new TipViewController(this);
            tipView.showView();
        }

    }

    @Override
    public void initVariables() {

        dataBaseManager = new DataBaseManager(MainActivity.this);
        countyList = dataBaseManager.loadCounties();

        lenght = countyList.size();

        if (lenght == 0) {
            CommonUtils.showToast(MainActivity.this, R.string.locate_fail_select);
            ChooseAreaActivity.toStartChooseAreaAct(this, -1);
            return;//此return并不能导致oncreate的return
        }

        for (int i = 0; i < lenght; i++) {
            county = countyList.get(i);
            BaseFragment fragment = WeatherFragment.newInstance(resInt[i % 4], resDrawableInt[i % 4], county.getWeaterCode(), county.getPlaceName(), lenght, i);
            fragmentList.add(fragment);
        }
        //读取sharePrf是否打开前台线程
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        AppConstants.isStartService = sharedPreferences.getBoolean(AppConstants.SHARE_PREF_SERVICE, true);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        loopAdapter=new LoopFPageAdapter(getSupportFragmentManager());
        viewpagerMain.setAdapter(loopAdapter);
        viewpagerMain.setCurrentItem(lenght * 100);//设置再中间可以左右滑动
        viewpagerMain.setPageTransformer(true, new CubeOutTransformer());
        //进行监听viewPager的滑动操作
        viewpagerMain.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < llDot.getChildCount(); i++) {
                    llDot.getChildAt(i).setSelected(false);
                }
                llDot.getChildAt(position).setSelected(true);
            }
        });
    }

    @Override
    public void loadData() {

        //初始化下方圆圈操作
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < lenght; i++) {
            View view = inflater.inflate(R.layout.item_main_dot, null);
            if (i == 0) {
                view.setSelected(true);
            }
            llDot.addView(view);
        }
        loopAdapter.addAll(fragmentList);
        loopAdapter.notifyDataSetChanged();
    }

    public void updateView() {

        //点击城市后的回调：需要生成多一个Fragment并加入ViewPager中

    }

    public static void toMainAct(Context context, boolean isSuccessLoacte) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AppConstants.IS_LOACTE_SUCCESS, isSuccessLoacte);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_FRAGMENT:
                if (resultCode == RESULT_OK) {
                    updateView();
                } else if (resultCode == RESULT_CANCELED) {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(tipView!=null)
            tipView.removePoppedViewAndClear();
        super.onDestroy();
    }

}
