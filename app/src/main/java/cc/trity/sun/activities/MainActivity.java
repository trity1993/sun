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
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewpagerMain.setPageTransformer(true, new CubeOutTransformer());
        indicator.setViewPager(viewpagerMain);
    }

    @Override
    public void loadData() {

    }


}
