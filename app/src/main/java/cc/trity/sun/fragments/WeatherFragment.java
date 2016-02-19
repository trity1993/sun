package cc.trity.sun.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.library.net.RequestCallback;
import cc.trity.library.utils.CommonUtils;
import cc.trity.library.utils.GsonUtils;
import cc.trity.library.utils.LogUtils;
import cc.trity.library.utils.TimeUtils;
import cc.trity.sun.R;
import cc.trity.sun.activities.ChooseAreaActivityApp;
import cc.trity.sun.activities.MainActivity;
import cc.trity.sun.activities.SettingActivityApp;
import cc.trity.sun.fragments.base.BaseFragment;
import cc.trity.sun.model.ReponseForcecastWeather;
import cc.trity.sun.model.WeatherContainer;
import cc.trity.sun.model.WeatherMsg;
import cc.trity.sun.presenter.WeatherPresenter;
import cc.trity.sun.utils.Utility;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class WeatherFragment extends BaseFragment implements RequestCallback {
    private final int REFRESH_UPDATE_VIEW = 0;
    private final int ERROR_UPDATE_VIEW = 1;
    private final int ERROR_CITY_CODE = 2;
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

    private int resBgColor, resbg;
    private String countyCode, countyName;
    private int hour;

    private WeatherContainer weatherContainer;

    private WeatherPresenter weatherPresenter;

    Handler handler = new Handler(this);

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance(int resBgColor, int resbg,
                                              String countyCode, String countyName) {
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("resBgColor", resBgColor);
        bundle.putInt("resBg", resbg);
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
        this.initView();
        return view;
    }


    @Override
    public void initView() {
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
                        Intent intent = new Intent(activity, ChooseAreaActivityApp.class);
                        intent.putExtra("resBgColor", resBgColor);
                        activity.startActivityForResult(intent, MainActivity.ADD_FRAGMENT);
                        break;
                    case R.id.action_setting:
                        Intent intentSet = new Intent(activity, SettingActivityApp.class);
                        intentSet.putExtra("resBgColor", resBgColor);
                        startActivity(intentSet);
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
            }
        });
        refreshRlLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

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
            handler.sendEmptyMessage(ERROR_CITY_CODE);
            return;
        }
        if (weatherPresenter.isUpdate(weatherContainer,countyCode)) {
            weatherPresenter.loadWeather(activity,countyCode,this);
        } else {
            if(weatherContainer==null){
                if(countyCode!=null){
                    weatherContainer=Utility.getWeatherContainer(activity,countyCode);

                }
                if(weatherContainer==null){
                    weatherPresenter.loadWeather(activity,countyCode,this);
                }else{
                    Message msg = new Message();
                    msg.what=REFRESH_UPDATE_VIEW;
                    msg.obj = weatherContainer;
                    handler.sendMessage(msg);
                }
            }


        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case REFRESH_UPDATE_VIEW:
                WeatherContainer weatherContainer = (WeatherContainer) msg.obj;
                if(weatherPresenter!=null){
                    WeatherMsg weatherMsg=weatherPresenter.updateData(weatherContainer,countyName);
                    if(weatherMsg!=null){
                        updateView(weatherMsg);
                        weatherPresenter.toCreateForGround(weatherMsg);
                    }
                }

                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
                break;
            case ERROR_UPDATE_VIEW:
                CommonUtils.showToast(activity, R.string.error_server_response);
                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
                break;
            case ERROR_CITY_CODE:
                CommonUtils.showToast(activity, R.string.error_location_code);
                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
                break;
        }
        return true;
    }

    public void updateView(WeatherMsg weatherMsg){
        //需要判断是否为null，否则会有空指针的异常
        if(imgWeatherFlag!=null&&txtLocationTemp!=null){
            //设置温度
            txtLocationTemp.setText(weatherMsg.getWeatherTemp());
            txtLocationTemp.setVisibility(View.VISIBLE);
            //更新图片
            imgWeatherFlag.setImageResource(weatherMsg.getWeatherImage());

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(weatherPresenter!=null)
            weatherPresenter.saveWeatherMsg(weatherContainer,countyCode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @Override
    public void onSuccess(String content) {
        LogUtils.d(TAG, content + "");
        ReponseForcecastWeather weatherData = GsonUtils.getClass(content, ReponseForcecastWeather.class);
        if(weatherData==null){
            onFail(null);
            return ;
        }
        weatherContainer = weatherData.getWeatherContainer();

        Message msg = new Message();
        msg.obj = weatherContainer;
        msg.what=REFRESH_UPDATE_VIEW;
        handler.sendMessage(msg);
    }

    @Override
    public void onFail(String errorMessage) {
        LogUtils.e(TAG, "error");
        handler.sendEmptyMessage(ERROR_UPDATE_VIEW);
    }
}
