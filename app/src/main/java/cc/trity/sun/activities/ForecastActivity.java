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
import cc.trity.sun.R;
import cc.trity.sun.activities.base.AppBaseActivity;
import cc.trity.sun.adapters.ForceCastRAdapter;
import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.model.ChartItem;
import cc.trity.sun.model.ForcecastItem;
import cc.trity.sun.utils.UIUtils;
import cc.trity.sun.view.LineChart;

public class ForecastActivity extends AppBaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.forecast_lineChart)
    LineChart forecastLineChart;
    @InjectView(R.id.forecast_recycler)
    RecyclerView recyclerView;

    private List<ForcecastItem> forcecastList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        ButterKnife.inject(this);
        this.init(savedInstanceState);
    }

    @Override
    public void initVariables() {
        forcecastList=new ArrayList<>();
        for(int i=0;i<5;i++){
            ForcecastItem forcecastItem=new ForcecastItem(R.mipmap.little_icon_sunny,
                    "21","saturday 9/11","23","10");
            forcecastList.add(forcecastItem);
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
            toolbar.setBackgroundColor(Color.parseColor("#0000d8a2"));
        }
        //设置recyclerview
        ViewGroup.LayoutParams layoutParams=recyclerView.getLayoutParams();
        layoutParams.height= UIUtils.getDisplayMetrics(ForecastActivity.this).heightPixels/3;
        ForceCastRAdapter forceCastRAdapter=new ForceCastRAdapter(ForecastActivity.this,forcecastList);
        recyclerView.setAdapter(forceCastRAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void loadData() {
        List<ChartItem> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            ChartItem chartItem = new ChartItem();
            chartItem.setDate(i + "");
            chartItem.setTempDay(10 + (int) (Math.random() * 10));
            chartItem.setTempNight((int) (Math.random() * 10));
            list.add(chartItem);
        }
        forecastLineChart.paddingData(list);
    }

    public static void showWeatherForecastAct(Context context, int resbgcolor) {
        Intent intent = new Intent(context, ForecastActivity.class);
        intent.putExtra(AppConstants.INTENT_BG_COLOR, resbgcolor);
        context.startActivity(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_setting).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}
