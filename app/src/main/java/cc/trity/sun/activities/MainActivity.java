package cc.trity.sun.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.sun.R;
import cc.trity.sun.activities.base.BaseActivity;
import cc.trity.sun.fragments.WeatherFragment;
import cc.trity.sun.listener.HttpCallbackListener;
import cc.trity.sun.model.Global;
import cc.trity.sun.model.WeatherRequest;
import cc.trity.sun.networks.HttpNetWorkTools;
import cc.trity.sun.utils.LogUtils;
import cc.trity.sun.utils.TimeUtils;
import cc.trity.sun.utils.URLEncoderUtils;
import cc.trity.sun.view.CirclePageIndicator;
import cc.trity.sun.view.CubeOutTransformer;

public class MainActivity extends BaseActivity {


    @InjectView(R.id.viewpager_main)
    ViewPager viewpagerMain;
    String[] sampleTitle;
    int[] resInt = new int[]{Color.parseColor("#ffc722")
            , Color.parseColor("#ff2259")
            , Color.parseColor("#00d8a2")
            , Color.parseColor("#545a4a")
            , Color.parseColor("#b13fd7")};
    List<Fragment> fragmentList;

    int lenght = 5;
    @InjectView(R.id.indicator)
    CirclePageIndicator indicator;
    boolean isCircle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        this.init();
    }

    @Override
    public void initVariables() {
        sampleTitle = new String[lenght];
        fragmentList = new ArrayList<>(lenght);

        for (int i = 0; i < lenght; i++) {
            Fragment fragment = WeatherFragment.newInstance(resInt[i], i);
            fragmentList.add(fragment);
        }
    }

    @Override
    public void initView() {
        viewpagerMain.setAdapter(new FragmentStatePagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position%fragmentList.size());
            }

            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }
        });

        viewpagerMain.setPageTransformer(true, new CubeOutTransformer());
        indicator.setViewPager(viewpagerMain);
        indicator.setSnap(true);
    }

    @Override
    public void loadData() {
        //填充请求数据
        WeatherRequest weatherRequest=new WeatherRequest();
        weatherRequest.setDate(TimeUtils.getCurentTime("yyyyMMddHHmm"));
        weatherRequest.setAreaid(Global.CITY_GD_CODE);
        //变成预报天气是应该执行下面这个方法
        weatherRequest.changeForecastType();


        //生成密钥
        String encryptKey=URLEncoderUtils.standardURLEncoder(weatherRequest.generatePubliKey(),Global.PRIVATE_KEY);
        weatherRequest.setKey(encryptKey);
        //生成url
        String url=weatherRequest.generateUrl();
        HttpNetWorkTools.sendRequestWithHttpURLConnection(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                LogUtils.d(MainActivity.this.getLocalClassName(),response+"");
            }

            @Override
            public void onError(Exception e) {
                LogUtils.d(MainActivity.this.getLocalClassName(),"error");

            }
        });
    }


}
