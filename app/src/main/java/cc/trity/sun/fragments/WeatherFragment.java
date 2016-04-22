package cc.trity.sun.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.library.utils.CommonUtils;
import cc.trity.library.utils.GsonUtils;
import cc.trity.library.utils.LogUtils;
import cc.trity.library.utils.TimeUtils;
import cc.trity.library.utils.Utils;
import cc.trity.sun.R;
import cc.trity.sun.activities.ChooseAreaActivity;
import cc.trity.sun.activities.ForecastActivity;
import cc.trity.sun.activities.SettingActivity;
import cc.trity.sun.activities.h5.H5Activity;
import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.fragments.base.BaseFragment;
import cc.trity.sun.listener.AbstractRequestCallback;
import cc.trity.sun.listener.ZoomTouchImplListener;
import cc.trity.sun.model.WeatherMsg;
import cc.trity.sun.model.weathersponse.ReponseForcecastWeather;
import cc.trity.sun.model.weathersponse.WeatherContainer;
import cc.trity.sun.presenter.WeatherPresenter;
import cc.trity.sun.service.WeatherForegroundService;
import cc.trity.sun.utils.Utility;

/**
 * 显示天气信息
 * A simple {@link BaseFragment} subclass.
 */
public class WeatherFragment extends BaseFragment {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.rl_layout)
    RelativeLayout rlLayout;
    @InjectView(R.id.img_weather_flag)
    ImageView imgWeatherFlag;
    @InjectView(R.id.txt_weather_location)
    TextView locationWeatherText;
    @InjectView(R.id.swipeRefresh_weather_Layout)
    SwipeRefreshLayout swipeRefresh;
    @InjectView(R.id.srefresh_rl_layout)
    RelativeLayout refreshRlLayout;
    @InjectView(R.id.txt_location_temp)
    TextView txtLocationTemp;

    View transit;
    FrameLayout fLayoutRoot;

    private int resBgColor, resbg,pageSize,pageNum;
    private String countyCode, countyName;
    private int hour;
    private long delayMillis=1000;

    private WeatherContainer weatherContainer;

    private WeatherPresenter weatherPresenter;


    Handler handler = new Handler();

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance(int resBgColor, int resbg,
                                              String countyCode, String countyName,
                                              int pageSize,int pageNum) {
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.INTENT_BG_COLOR, resBgColor);
        bundle.putInt("resBg", resbg);
        bundle.putInt("pageSize", pageSize);
        bundle.putInt("pageNum", pageNum);
        bundle.putString(AppConstants.COUNTRY_CODE, countyCode);
        bundle.putString(AppConstants.COUNTRY_NAME, countyName);
        weatherFragment.setArguments(bundle);

        return weatherFragment;
    }

    @Override
    public void initVariables() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            resBgColor = bundle.getInt(AppConstants.INTENT_BG_COLOR);
            resbg = bundle.getInt("resBg");
            countyCode = bundle.getString(AppConstants.COUNTRY_CODE);
            countyName = bundle.getString(AppConstants.COUNTRY_NAME);
            pageSize = bundle.getInt("pageSize");
            pageNum=bundle.getInt("pageNum",-1);
        }
        hour = Utils.convertToInt(TimeUtils.getCurentTime("HH"));

        weatherPresenter=new WeatherPresenter(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.transit_layout, container, false);
        initTransitView(view);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, delayMillis);
        return view;
    }


    @Override
    public void initView(Bundle savedInstanceState) {

        View view=View.inflate(activity, R.layout.fragment_weather, fLayoutRoot);
        ButterKnife.inject(this, view);

        ViewGroup viewGroup=(ViewGroup)fLayoutRoot.getParent();
        viewGroup.removeView(fLayoutRoot);
        fLayoutRoot=null;
        viewGroup.addView(view);

        //设置背景
        rlLayout.setBackgroundResource(resbg);

        //设置toolbar
        AppCompatActivity appCompatActivity = activity;
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.inflateMenu(R.menu.menu_base);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        if (pageSize <= 8) {
                            ChooseAreaActivity.toStartChooseAreaAct(activity, resBgColor);
                        } else {
                            CommonUtils.showToast(activity, R.string.error_max_page);
                        }

                        break;
                    case R.id.action_setting:
                        SettingActivity.actionStart(activity, resBgColor);
                        break;
                }
                return true;
            }
        });

        inistReFreshLayout();

        setViewAppear();

    }

    /**
     * 设置swipeRefresh
     */
    private void inistReFreshLayout(){

        swipeRefresh.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                WeatherFragment.this.loadData();
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
        //设置缩放手势
        refreshRlLayout.setOnTouchListener(new ZoomTouchImplListener(activity) {

            @Override
            public void onZoomOut() {
                H5Activity.toH5Act(activity);
            }

            @Override
            public void onZoomIn() {
                ForecastActivity.showWeatherForecastAct(activity, resBgColor, countyCode);
            }
        });

    }

    @Override
    public void loadData() {
        if (TextUtils.isEmpty(countyCode)) {
            errorCountyCode();
            return;
        }
        if(fLayoutRoot!=null){
            this.initView(null);
        }

        if (weatherPresenter.isUpdate(weatherContainer,countyCode)) {
            weatherPresenter.loadWeather(activity,countyCode,requestCallback);
        } else {
            if(weatherContainer==null){
                if(countyCode!=null){
                    weatherContainer=Utility.getWeatherContainer(activity,countyCode);
                }
                if(weatherContainer==null){
                    weatherPresenter.loadWeather(activity,countyCode,requestCallback);
                }
            }

            updateView(weatherContainer);//无论是否为null都要进行更新操作
        }
    }

    public void setViewAppear(){
        //地点
        if (countyName != null)
            locationWeatherText.setText(countyName);
        //天气图标
        if (hour > 18 || hour < 6) {
            imgWeatherFlag.setImageResource(R.mipmap.icon_sunny_night);
        } else {
            imgWeatherFlag.setImageResource(R.mipmap.icon_sunny);
        }
    }

    public void updateView(WeatherContainer weatherContainer){

        if(weatherPresenter!=null){
            WeatherMsg weatherMsg=weatherPresenter.updateData(weatherContainer,countyName);
            if(weatherMsg!=null){
                //需要判断是否为null，否则会有空指针的异常
                if(imgWeatherFlag!=null&&txtLocationTemp!=null){
                    //设置温度
                    txtLocationTemp.setText(weatherMsg.getWeatherTemp());
                    txtLocationTemp.setVisibility(View.VISIBLE);
                    //更新图片
                    imgWeatherFlag.setImageResource(weatherMsg.getWeatherImage());

                }

                WeatherForegroundService.toCreateForGround(activity,weatherMsg);
            }
        }
        if (swipeRefresh != null){
            swipeRefresh.setRefreshing(false);
        }
    }

    public void errorUpdateView(String errorMesg){
//        CommonUtils.showToast(activity, errorMesg);
        CommonUtils.showSnackbar(rlLayout,errorMesg);//多次弹出的时候，会有空指针的情况
        if (swipeRefresh != null)
            swipeRefresh.setRefreshing(false);
    }

    public void errorCountyCode(){
//        CommonUtils.showToast(activity, R.string.error_location_code);
        CommonUtils.showSnackbar(rlLayout, R.string.error_location_code);

        if (swipeRefresh != null)
            swipeRefresh.setRefreshing(false);
    }

    /**
     * 设置显示过渡动画
     */
    public void initTransitView(View view){

        transit=view.findViewById(R.id.transit);
        fLayoutRoot=(FrameLayout)view.findViewById(R.id.flayout_root);

        if(resbg>0){
            fLayoutRoot.setBackgroundResource(resbg);
        }
        transit.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.anim_alpha_scale));

    }

    @Override
    public void onPause() {
        super.onPause();
        if(weatherPresenter!=null)
            weatherPresenter.saveWeatherMsg(weatherContainer, countyCode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private AbstractRequestCallback requestCallback=new AbstractRequestCallback(activity) {
        @Override
        public void onSuccess(String content) {
            LogUtils.d(TAG, content + "");
            ReponseForcecastWeather weatherData = GsonUtils.getClass(content, ReponseForcecastWeather.class);
            if(weatherData==null){
                onFail(R.string.error_denode);
                return ;
            }
            weatherContainer = weatherData.getWeatherContainer();

            //更新UI
            updateView(weatherContainer);
            //保存缓存数据
            if(weatherPresenter!=null)
                weatherPresenter.saveWeatherMsg(weatherContainer,countyCode);
        }

        @Override
        public void onFail(String errorMsg) {
            errorUpdateView(errorMsg);
        }

        @Override
        public void onFail(int resErrormsgInt) {
            String errorMessage=null;
            if(resErrormsgInt!=0){
                errorMessage=activity.getResources().getString(resErrormsgInt);
            }else{
                errorMessage=activity.getResources().getString(R.string.error_donnot_knowledge);

            }
//            LogUtils.e(TAG,errorMessage);
            errorUpdateView(errorMessage);
        }
    };
}
