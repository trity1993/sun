package cc.trity.sun.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.sun.R;
import cc.trity.sun.fragments.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends BaseFragment {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.rl_layout)
    RelativeLayout rlLayout;

    private int resBgColor,selectPage;

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance(int resBgColor,int selectPage){
        WeatherFragment weatherFragment=new WeatherFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("resBgColor",resBgColor);
        bundle.putInt("selectPage",selectPage);
        weatherFragment.setArguments(bundle);

        return weatherFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.inject(this, view);
        this.init();
        return view;
    }

    @Override
    public void initVariables() {
        Bundle bundle = getArguments();
        if (bundle != null){
            resBgColor=bundle.getInt("resBgColor");
            selectPage=bundle.getInt("selectPage");
        }
    }

    @Override
    public void initView() {
        rlLayout.setBackgroundColor(resBgColor);
        AppCompatActivity appCompatActivity=(AppCompatActivity)activity;
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setBackgroundColor(resBgColor);

    }

    @Override
    public void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
