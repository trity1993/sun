package cc.trity.sun.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.library.utils.Utils;
import cc.trity.sun.R;
import cc.trity.sun.activities.base.AppBaseActivity;
import cc.trity.sun.adapters.ForceCastRAdapter;
import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.model.ChartItem;
import cc.trity.sun.model.ForcecastItem;
import cc.trity.sun.model.weathersponse.WeatherContainer;
import cc.trity.sun.presenter.WeatherPresenter;
import cc.trity.sun.utils.UIUtils;
import cc.trity.sun.utils.Utility;
import cc.trity.sun.view.LineChart;

public class ForecastActivity extends AppBaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.forecast_lineChart)
    LineChart forecastLineChart;
    @InjectView(R.id.forecast_recycler)
    RecyclerView recyclerView;

    private List<ForcecastItem> forcecastList;

    private WeatherContainer weatherContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        ButterKnife.inject(this);
        this.init(savedInstanceState);
    }

    @Override
    public void initVariables() {

        Intent intent=getIntent();
        String countyCode=intent.getStringExtra(AppConstants.COUNTRY_CODE);

        weatherContainer= Utility.getWeatherContainer(this, countyCode);
        if(weatherContainer!=null){

            WeatherPresenter weatherPresenter=new WeatherPresenter(this);

            forcecastList=weatherPresenter.getForcecastData(weatherContainer);
        }


    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //初始化toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (resToolBgColor != -1) {//说明是由前者进行传递过来
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setBackgroundColor(getResources().getColor(resToolBgColor));
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setBackgroundColor(Color.parseColor("#0000d8a2"));
        }
        //设置recyclerview,设置宽高
        ViewGroup.LayoutParams layoutParams=recyclerView.getLayoutParams();
        layoutParams.height= UIUtils.getDisplayMetrics(ForecastActivity.this).heightPixels/3;
        ForceCastRAdapter forceCastRAdapter=new ForceCastRAdapter(ForecastActivity.this,forcecastList);
        recyclerView.setAdapter(forceCastRAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void loadData() {
        List<ChartItem> list = new ArrayList<>();

        int leng=forcecastList.size();
        for(int i=0;i<leng;i++){
            ChartItem chartItem = new ChartItem();

            chartItem.setDate(i + "");
            chartItem.setTempDay(Utils.convertToInt(forcecastList.get(i).getDayTemp()));
            chartItem.setTempNight(Utils.convertToInt(forcecastList.get(i).getNightTemp()));
            list.add(chartItem);
        }

        forecastLineChart.paddingData(list);
    }

    /**
     * @param context
     * @param resbgcolor 背景颜色
     */
    public static void showWeatherForecastAct(Context context, int resbgcolor,String countyCode) {
        Intent intent = new Intent(context, ForecastActivity.class);
        intent.putExtra(AppConstants.INTENT_BG_COLOR, resbgcolor);
        intent.putExtra(AppConstants.COUNTRY_CODE, countyCode);
        context.startActivity(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_setting).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}
