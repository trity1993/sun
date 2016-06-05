package cc.trity.sun.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.library.utils.CommonUtils;
import cc.trity.sun.R;
import cc.trity.sun.adapters.WeatherRecyclerAdapter;
import cc.trity.sun.db.DataBaseManager;
import cc.trity.sun.ui.fragments.base.BaseFragment;
import cc.trity.sun.listener.OnRecyclerItemClickListener;
import cc.trity.sun.model.WeatherMsg;
import cc.trity.sun.model.city.County;
import cc.trity.sun.model.weathersponse.WeatherContainer;
import cc.trity.sun.presenter.WeatherPresenter;
import cc.trity.sun.utils.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class FourShowFragment extends BaseFragment {
    @InjectView(R.id.fragment_rv_show)
    RecyclerView fragRvShow;

    private int[] resInt = new int[]{
            R.drawable.corner_yellow_bg
            , R.drawable.corner_pink_bg
            , R.drawable.corner_green_bg
            , R.drawable.corner_blue_bg};
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<County> countyList;
    private List<WeatherMsg> weatherMsgList;

    private int lenght = 1;

    private DataBaseManager dataBaseManager = null;

    private WeatherRecyclerAdapter weatherRecyclerAdapter;

    private WeatherPresenter weatherPresenter;


    public FourShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void initVariables() {
        dataBaseManager = new DataBaseManager(activity);
        countyList = dataBaseManager.loadCounties();

        lenght = countyList.size();

        County county = null;
        WeatherContainer container=null;

        weatherPresenter=new WeatherPresenter(activity);
        weatherMsgList=new ArrayList<>();

        for (int i = 0; i < lenght; i++) {
            county = countyList.get(i);
            container= Utility.getWeatherContainer(activity,county.getWeaterCode());
            if(container!=null){
                WeatherMsg weatherMsg=weatherPresenter.updateData(container, county.getPlaceName());
                if(weatherMsg!=null){
                    weatherMsg.setWeatherBackground(resInt[i % 4]);
                    weatherMsgList.add(weatherMsg);
                }
            }
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        weatherRecyclerAdapter=new WeatherRecyclerAdapter(activity,weatherMsgList);
        weatherRecyclerAdapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CommonUtils.showSnackbar(view, "click=" + position);
            }
        });
        fragRvShow.setHasFixedSize(true);
        fragRvShow.setAdapter(weatherRecyclerAdapter);
        fragRvShow.setLayoutManager(new GridLayoutManager(activity,2));
    }

    @Override
    public void loadData() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_four_show, container, false);
        ButterKnife.inject(this, view);
        this.init(savedInstanceState);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
