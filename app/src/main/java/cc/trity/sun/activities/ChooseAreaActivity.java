package cc.trity.sun.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.sun.R;
import cc.trity.sun.activities.base.BaseActivity;
import cc.trity.sun.db.DataBaseManager;
import cc.trity.sun.listener.HttpCallbackListener;
import cc.trity.sun.model.City;
import cc.trity.sun.model.County;
import cc.trity.sun.model.Global;
import cc.trity.sun.model.Province;
import cc.trity.sun.networks.HttpNetWorkTools;
import cc.trity.sun.utils.LogUtils;
import cc.trity.sun.utils.Utility;

public class ChooseAreaActivity extends BaseActivity {
    private static final String TAG="ChooseAreaActivity";
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    @InjectView(R.id.toolbar_title_custom)
    TextView txtAreaTitle;
    @InjectView(R.id.list_area)
    ListView listArea;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayAdapter<String> adapter;
    private DataBaseManager dataBaseManager;
    private List<String> dataList = new ArrayList<String>();
    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 县列表
     */
    private List<County> countyList;
    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLevel;
    /**
     * 是否从WeatherActivity中跳转过来。
     */
    private boolean isFromWeatherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        ButterKnife.inject(this);
        this.init();
    }

    @Override
    public void initVariables() {
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        if (prefs.getBoolean("city_selected", false) && !isFromWeatherActivity) {
//            Intent intent = new Intent(this, WeatherActivity.class);
//            startActivity(intent);
//            finish();
//            return;
//        }
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listArea.setAdapter(adapter);
        listArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long arg3) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(index);
                    queryCities();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    LogUtils.d(TAG,"selectedProvince="+selectedProvince);

                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(index);
                    queryCounties();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    LogUtils.d(TAG,"selectedCity="+selectedCity);

                } else if (currentLevel == LEVEL_COUNTY) {
                    String countyCode = countyList.get(index).getCountyCode();
                    LogUtils.d(TAG,"countyCode="+countyCode);
//                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
//                    intent.putExtra("county_code", countyCode);
//                    startActivity(intent);
//                    finish();
                }
            }
        });
    }

    @Override
    public void loadData() {
        dataBaseManager = DataBaseManager.getInstance(this);
        queryProvinces();  // 加载省级数据
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryProvinces() {
        provinceList = dataBaseManager.loadProvinces();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listArea.setSelection(0);
            txtAreaTitle.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, "province");
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCities() {
        cityList = dataBaseManager.loadCities(selectedProvince.getId());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listArea.setSelection(0);
            txtAreaTitle.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCounties() {
        countyList = dataBaseManager.loadCounties(selectedCity.getId());
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listArea.setSelection(0);
            txtAreaTitle.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }

    /**
     * 根据传入的代号和类型从服务器上查询省市县数据。
     */
    private void queryFromServer(final String code, final String type) {

        String address;
        if (!TextUtils.isEmpty(code)) {
            address = String.format(Global.URL_AREA_FORMAT, Integer.valueOf(code));
        } else {
            address = Global.URL_PRIVENIC;
        }
        showProgressDialog();
        LogUtils.d(TAG, address);
        HttpNetWorkTools.sendRequestWithHttpURLConnection(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvincesResponse(dataBaseManager,
                            response);
                } else if ("city".equals(type)) {
                    result = Utility.handleCitiesResponse(dataBaseManager,
                            response, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountiesResponse(dataBaseManager,
                            response, selectedCity.getId());
                }
                if (result) {
                    // 通过runOnUiThread()方法回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                            } else if ("city".equals(type)) {
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                                queryCities();
                            } else if ("county".equals(type)) {
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,
                                "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        } else {
            if (isFromWeatherActivity) {
                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_add).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}