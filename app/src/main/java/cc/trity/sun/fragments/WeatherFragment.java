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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.library.utils.CommonUtils;
import cc.trity.library.utils.GsonUtils;
import cc.trity.library.utils.LogUtils;
import cc.trity.library.utils.TimeUtils;
import cc.trity.sun.R;
import cc.trity.sun.activities.ChooseAreaActivity;
import cc.trity.sun.activities.SettingActivity;
import cc.trity.sun.fragments.base.BaseFragment;
import cc.trity.sun.listener.AbstractRequestCallback;
import cc.trity.sun.listener.ZoomTouchListener;
import cc.trity.sun.model.WeatherMsg;
import cc.trity.sun.model.weathersponse.ReponseForcecastWeather;
import cc.trity.sun.model.weathersponse.WeatherContainer;
import cc.trity.sun.presenter.WeatherPresenter;
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

    private int resBgColor, resbg,pageSize;
    private String countyCode, countyName;
    private int hour;


    private WeatherContainer weatherContainer;

    private WeatherPresenter weatherPresenter;


    Handler handler = new Handler();

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance(int resBgColor, int resbg,
                                              String countyCode, String countyName,int pageSize) {
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("resBgColor", resBgColor);
        bundle.putInt("resBg", resbg);
        bundle.putInt("pageSize", pageSize);
        bundle.putString("countyCode", countyCode);
        bundle.putString("countyName", countyName);
        weatherFragment.setArguments(bundle);

        return weatherFragment;
    }

    @Override
    public void initVariables() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            resBgColor = bundle.getInt("resBgColor");
            resbg = bundle.getInt("resBg");
            countyCode = bundle.getString("countyCode");
            countyName = bundle.getString("countyName");
            pageSize = bundle.getInt("pageSize");
        }
        hour = Integer.valueOf(TimeUtils.getCurentTime("HH"));

        weatherPresenter=WeatherPresenter.getInstance(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.inject(this, view);
        this.initView(savedInstanceState);
        return view;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
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
                        if(pageSize<=8){
                            ChooseAreaActivity.toStartChooseAreaAct(activity,resBgColor);
                        }else{
                            CommonUtils.showToast(activity,R.string.error_max_page);
                        }

                        break;
                    case R.id.action_setting:
                        SettingActivity.actionStart(activity, resBgColor);
                        break;
                }
                return true;
            }
        });
        //地点
        if (countyName != null)
            locationWeatherText.setText(countyName);
        //天气图标
        if (hour > 18 || hour < 6) {
            imgWeatherFlag.setImageResource(R.mipmap.icon_sunny_night);
        } else {
            imgWeatherFlag.setImageResource(R.mipmap.icon_sunny);
        }
        //设置swipeRefresh
        swipeRefresh.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                WeatherFragment.this.loadData();
                if (swipeRefresh != null){
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
        refreshRlLayout.setOnTouchListener(new ZoomTouchListener(activity));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(true);
                    loadData();
                }
            }
        }, 1000);

    }

    @Override
    public void loadData() {
        if (TextUtils.isEmpty(countyCode)) {
            errorCountyCode();
            return;
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

                weatherPresenter.toCreateForGround(weatherMsg);
            }
        }
        if (swipeRefresh != null){
            swipeRefresh.setRefreshing(false);
        }
    }

    public void errorUpdateView(String errorMesg){
//        CommonUtils.showToast(activity, errorMesg);
        CommonUtils.showSnackbar(rlLayout,errorMesg);
        if (swipeRefresh != null)
            swipeRefresh.setRefreshing(false);
    }

    public void errorCountyCode(){
//        CommonUtils.showToast(activity, R.string.error_location_code);
        CommonUtils.showSnackbar(rlLayout,  R.string.error_location_code);

        if (swipeRefresh != null)
            swipeRefresh.setRefreshing(false);
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
