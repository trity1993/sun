package cc.trity.library.adapter.viewpager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cc.trity.library.adapter.IListAdapter;
import cc.trity.library.fragment.BaseFragment;


/**
 * Created by trity on 14/6/16.
 */
public class CommonFPageAdapter extends FragmentStatePagerAdapter implements IListAdapter<BaseFragment> {

    protected List<BaseFragment> mFragmentList;

    public CommonFPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void setList(List<BaseFragment> list) {
        mFragmentList = list;
    }

    @Override
    public List<BaseFragment> getList() {
        return mFragmentList;
    }

    @Override
    public boolean addAll(List<BaseFragment> list) {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>(list.size());
        }
        return mFragmentList.addAll(list);
    }

    @Override
    public boolean add(BaseFragment item) {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
        }
        return mFragmentList.add(item);
    }

    @Override
    public BaseFragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public boolean contains(BaseFragment item) {
        return mFragmentList.contains(item);
    }

    @Override
    public boolean remove(BaseFragment item) {
        if (mFragmentList != null) {
            return mFragmentList.remove(item);
        }
        return false;
    }

    @Override
    public BaseFragment remove(int position) {
        if (mFragmentList != null) {
            return mFragmentList.remove(position);
        }
        return null;
    }

    @Override
    public void clear() {
        if (mFragmentList != null) {
            mFragmentList.clear();
        }
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getFragmentTitle();
    }
}
