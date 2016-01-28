package cc.trity.sun.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by TryIT on 2016/1/18.
 */
public class CycleViewPagerAdapter extends PagerAdapter {
    private List<Fragment> fragmentList;
    public CycleViewPagerAdapter(List<Fragment> fragmentList){
        this.fragmentList=fragmentList;

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment)object).getView() == view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
