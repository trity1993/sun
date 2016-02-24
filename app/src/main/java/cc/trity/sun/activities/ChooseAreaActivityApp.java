package cc.trity.sun.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.library.utils.LogUtils;
import cc.trity.sun.R;
import cc.trity.sun.activities.base.AppBaseActivity;
import cc.trity.sun.db.DataBaseManager;
import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.model.city.City;
import cc.trity.sun.model.city.County;
import cc.trity.sun.model.city.Province;
import cc.trity.sun.sdk.PlaceSaxParseHandler;

public class ChooseAreaActivityApp extends AppBaseActivity {
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
//    private DataBaseManager dataBaseManager;
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

    private int resToolBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        ButterKnife.inject(this);
        this.init(savedInstanceState);
    }

    @Override
    public void initVariables() {
        Intent intent=getIntent();
        resToolBg=intent.getIntExtra("resBgColor",-1);
        if(resToolBg!=-1)
            resToolBg=getResources().getColor(resToolBg);
    }

    @Override
    public void initView(Bundle save) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(resToolBg!=-1){
            toolbar.setBackgroundColor(resToolBg);
        }

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
                    LogUtils.d(TAG, "selectedProvince=" + selectedProvince);

                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(index);
                    queryCounties();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    LogUtils.d(TAG, "selectedCity=" + selectedCity);

                } else if (currentLevel == LEVEL_COUNTY) {
                    County county=countyList.get(index);
                    saveCounties(county);
                    String countyCode = county.getWeaterCode();
                    String countyName=county.getPlaceName();

                    LogUtils.d(TAG, "countyCode=" + countyCode);

                    toMainAct(ChooseAreaActivityApp.this,countyCode,countyName);
                }
            }
        });
    }

    @Override
    public void loadData() {
        //加载城市city.xml的数据
        try {
            provinceList= PlaceSaxParseHandler.getProvicneModel(getAssets().open(AppConstants.ASSETS_CITY));
            if(provinceList!=null){
                queryProvinces();  // 加载省级数据
            }
        } catch (Exception e) {
            LogUtils.e(TAG, Log.getStackTraceString(e));
        }
    }


    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryProvinces() {
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getPlaceName());
            }
            adapter.notifyDataSetChanged();
            listArea.setSelection(0);
            txtAreaTitle.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCities() {
        cityList = selectedProvince.getCityList();
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getPlaceName());
            }
            adapter.notifyDataSetChanged();
            listArea.setSelection(0);
            txtAreaTitle.setText(selectedProvince.getPlaceName());
            currentLevel = LEVEL_CITY;
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCounties() {
        countyList = selectedCity.getCountyList();
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getPlaceName());
            }
            adapter.notifyDataSetChanged();
            listArea.setSelection(0);
            txtAreaTitle.setText(selectedCity.getPlaceName());
            currentLevel = LEVEL_COUNTY;
        }
    }

    private void saveCounties(County county){
        DataBaseManager dataBaseManager=DataBaseManager.getInstance(ChooseAreaActivityApp.this);
        dataBaseManager.saveCounty(county);
    }

    /**
     * 回调函数进行传值跳转
     * @param activity
     * @param paramsCode
     * @param paramsName
     */
    public void toMainAct(Activity activity, String paramsCode, String paramsName) {
        Intent intent = new Intent();
        intent.putExtra("county_code", paramsCode);
        intent.putExtra("county_name", paramsName);
        AppConstants.pageLength++;
        activity.setResult(RESULT_OK,intent);
        finish();
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
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_add).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}