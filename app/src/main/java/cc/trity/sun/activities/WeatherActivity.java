package cc.trity.sun.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.sun.R;
import cc.trity.sun.activities.base.BaseActivity;
import cc.trity.sun.listener.HttpCallbackListener;
import cc.trity.sun.networks.HttpNetWorkTools;
import cc.trity.sun.utils.Utility;

public class WeatherActivity extends BaseActivity {


    @InjectView(R.id.publish_text)
    TextView publishText;
    @InjectView(R.id.current_date)
    TextView currentDate;
    @InjectView(R.id.weather_desp)
    TextView weatherDesp;
    @InjectView(R.id.temp1)
    TextView temp1;
    @InjectView(R.id.temp2)
    TextView temp2;
    @InjectView(R.id.weather_info_layout)
    LinearLayout weatherInfoLayout;
    @InjectView(R.id.adLayout)
    LinearLayout adLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.toolbar_title_custom)
    TextView cityName;
    private String countyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.inject(this);

        this.init(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ActionBar.DISPLAY_SHOW_HOME:
            case ActionBar.DISPLAY_USE_LOGO:
            case ActionBar.DISPLAY_HOME_AS_UP:
            case android.R.id.home:
            case android.R.id.icon:
                Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.action_refresh:
                publishText.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherCode = prefs.getString("weather_code", "");
                if (!TextUtils.isEmpty(weatherCode)) {
                    queryWeatherInfo(weatherCode);
                }
                break;
        }
        return true;
    }

    @Override
    public void initVariables() {
        countyCode = getIntent().getStringExtra("county_code");

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        trySetupToolbar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void loadData() {
        if (!TextUtils.isEmpty(countyCode)) {
            // 有县级代号时就去查询天气
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityName.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
            // 没有县级代号时就直接显示本地天气
            showWeather();
        }
    }

    /**
     * 查询县级代号所对应的天气代号。
     */
    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    /**
     * 查询天气代号所对应的天气。
     */
    private void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(address, "weatherCode");
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息。
     */
    private void queryFromServer(final String address, final String type) {
        HttpNetWorkTools.sendRequestWithHttpURLConnection(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        // 从服务器返回的数据中解析出天气代号
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    // 处理服务器返回的天气信息
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
     */
    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityName.setText(prefs.getString("city_name", ""));
        temp1.setText(prefs.getString("temp1", ""));
        temp2.setText(prefs.getString("temp2", ""));
        weatherDesp.setText(prefs.getString("weather_desp", ""));
        publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
        currentDate.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityName.setVisibility(View.VISIBLE);
//		Intent intent = new Intent(this, AutoUpdateService.class);
//		startService(intent);
    }

}