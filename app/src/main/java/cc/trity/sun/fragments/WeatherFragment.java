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
import cc.trity.sun.R;
import cc.trity.sun.activities.ChooseAreaActivity;
import cc.trity.sun.activities.MainActivity;
import cc.trity.sun.fragments.base.BaseFragment;
import cc.trity.sun.listener.HttpCallbackListener;
import cc.trity.sun.model.ReponseForcecastWeather;
import cc.trity.sun.model.WeatherContainer;
import cc.trity.sun.model.WeatherDetail;
import cc.trity.sun.networks.HttpManager;
import cc.trity.sun.networks.HttpNetWorkTools;
import cc.trity.sun.utils.CommonUtils;
import cc.trity.sun.utils.FileUtils;
import cc.trity.sun.utils.GsonUtils;
import cc.trity.sun.utils.LogUtils;
import cc.trity.sun.utils.TimeUtils;
import cc.trity.sun.utils.Utility;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class WeatherFragment extends BaseFragment {
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

    private int resBgColor, selectPage;
    private String countyCode, countyName;
    private int hour;

    private WeatherContainer weatherContainer;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_UPDATE_VIEW:
                    WeatherContainer weatherContainer = (WeatherContainer) msg.obj;
                    updateWeatherView(weatherContainer);
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


        }
    };

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance(int resBgColor, int selectPage,
                                              String countyCode, String countyName) {
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("resBgColor", resBgColor);
        bundle.putInt("selectPage", selectPage);
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
            selectPage = bundle.getInt("selectPage");
            countyCode = bundle.getString("countyCode");
            countyName = bundle.getString("countyName");
        }
        hour = Integer.valueOf(TimeUtils.getCurentTime("HH"));

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
        //设置toolbar
        rlLayout.setBackgroundColor(resBgColor);
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(resBgColor);
        toolbar.inflateMenu(R.menu.menu_base);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        Intent intent = new Intent(activity, ChooseAreaActivity.class);
                        intent.putExtra("resBgColor", resBgColor);
                        activity.startActivityForResult(intent, MainActivity.ADD_FRAGMENT);
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
        if (isUpdate()) {
            loadWeather();
        } else {
            if(weatherContainer==null){
                if(countyCode!=null)
                    weatherContainer=Utility.getWeatherContainer(activity,countyCode);
            }
            if(weatherContainer==null){
                loadWeather();
            }else{
                Message msg = new Message();
                msg.what=REFRESH_UPDATE_VIEW;
                msg.obj = weatherContainer;
                handler.sendMessage(msg);
            }

        }

    }
    public void loadWeather(){
        //生成url
        String url = HttpManager.getReqAdress(true, countyCode);
        HttpNetWorkTools.sendRequestWithHttpURLConnection(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                LogUtils.d(TAG, response + "");
                ReponseForcecastWeather weatherData = GsonUtils.getClass(response, ReponseForcecastWeather.class);
                weatherContainer = weatherData.getWeatherContainer();

                Message msg = new Message();
                msg.obj = weatherContainer;
                msg.what=REFRESH_UPDATE_VIEW;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                LogUtils.e(TAG, "error");
                handler.sendEmptyMessage(ERROR_UPDATE_VIEW);
            }
        });
    }

    public boolean isUpdate() {
        weatherContainer = Utility.getWeatherContainer(activity, countyCode);
        if (weatherContainer == null)
            return false;
        long dateHM = Long.valueOf(weatherContainer.getReleaseTime().trim());
        long curDate = Long.valueOf(TimeUtils.getCurentTime("yyyyMMddHHmm"));

        StringBuilder sbDay=new StringBuilder(TimeUtils.getCurentTime("yyyyMMdd"));
        sbDay.append("0600");
        long curTime=Long.valueOf(sbDay.toString());
        if (dateHM < curTime && curDate >= curTime) {
            return true;
        }

        sbDay.replace(8,12,"1100");
        curTime=Long.valueOf(sbDay.toString());
        if (dateHM < curTime && curDate >= curTime) {
            return true;
        }

        sbDay.replace(8,12,"1800");
        curTime=Long.valueOf(sbDay.toString());
        if (dateHM < curTime && curDate >= curTime) {
            return true;
        }
        return false;
    }

    public void updateWeatherView(WeatherContainer weatherContainer) {
        if (weatherContainer == null) {
            return;
        }
        int[] resImage = FileUtils.getResourseArray(activity, R.array.weather_num);
        String imgIconNum;
        StringBuilder temp = new StringBuilder();
        WeatherDetail weatherDetail = weatherContainer.getWeatherDetailList().get(0);

        if (hour > 18 || hour < 6) {
            temp.append(weatherDetail.getNightTemp());
            imgIconNum = weatherDetail.getNightNum();
        } else {
            temp.append(weatherDetail.getDayTemp());
            imgIconNum = weatherDetail.getDayNum();
        }
        LogUtils.d(TAG, "imgIconNum=" + imgIconNum);

        int num = Integer.valueOf(imgIconNum);
        if(imgWeatherFlag==null){
            return ;
        }
        if (num == 53) {
            imgWeatherFlag.setImageResource(resImage[resImage.length - 1]);
        } else if (num == 99) {
            imgWeatherFlag.setImageResource(R.mipmap.ic_launcher);
        } else {
            imgWeatherFlag.setImageResource(resImage[num]);
        }
        LogUtils.d(TAG, "temp=" + temp.toString());
        if (!TextUtils.isEmpty(temp.toString())) {
            String degreeStr = getResources().getString(R.string.degree);
            temp.append(degreeStr);
            txtLocationTemp.setText(temp.toString());
            txtLocationTemp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (weatherContainer != null && countyCode != null) {
            if (TimeUtils.getHM(weatherContainer.getReleaseTime()).compareTo("18") >= 0) {//晚上的情况不能直接替换
                WeatherContainer weatherContainer1 = Utility.getWeatherContainer(activity, countyCode);
                if (weatherContainer1 == null) {
                    String jsonWeatherInfo = GsonUtils.createGsonString(weatherContainer);
                    Utility.saveCountyWeatherInfo(activity, countyCode, jsonWeatherInfo);
                    return;
                }
                weatherContainer.getWeatherDetailList().get(0)
                        .setDayNum(weatherContainer1.getWeatherDetailList().get(0).getDayNum());
                weatherContainer.getWeatherDetailList().get(0)
                        .setDayTemp(weatherContainer1.getWeatherDetailList().get(0).getDayTemp());
                weatherContainer.getWeatherDetailList().get(0)
                        .setNightWindDirectNum(weatherContainer1.getWeatherDetailList().get(0).getDayWindDirectNum());
                weatherContainer.getWeatherDetailList().get(0)
                        .setNightWindPowerNum(weatherContainer1.getWeatherDetailList().get(0).getDayWindPowerNum());


            }
            String jsonWeatherInfo = GsonUtils.createGsonString(weatherContainer);
            Utility.saveCountyWeatherInfo(activity, countyCode, jsonWeatherInfo);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
