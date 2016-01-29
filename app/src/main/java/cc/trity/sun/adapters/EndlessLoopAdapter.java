package cc.trity.sun.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 为了解决通用性，设定count来设定是否为无线循环
 * 另一个当<=3的时候进行倍数增长就好了
 * Created by TryIT on 2016/1/23.
 */
public class EndlessLoopAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private int count;
    private int length;
    public EndlessLoopAdapter(FragmentManager fm,List<Fragment> fragmentList,int count) {
        super(fm);
        this.fragmentList=fragmentList;
        this.length=fragmentList.size();
        this.count=count;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position%length);
    }

    @Override
    public int getCount() {
        return count;
    }
}
