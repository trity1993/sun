package cc.trity.library.adapter.viewpager;

import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import cc.trity.library.fragment.BaseFragment;


/**
 * fragment方式的无限循环
 * bug1：Can't change tag of fragment BlankFragment
 * 原来基类继承PagerFragmentAdapter，而不是FragmentStatePagerAdapter，改成FragmentStatePagerAdapter即可
 * bug2:需要使用到Collections.copy的时候，无法显示对应的view+fragment already active的问题
 * Created by trity on 20/6/16.
 */
public abstract class LoopFPageAdapter extends CommonFPageAdapter {

    private int realIndex;//少于4个的情况，需要标记其真正的索引

    public LoopFPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        if (getRealCount() <= 1) {
            return getRealCount();
        } else if (getRealCount() < 4) {
            realIndex = getRealCount();
            List<BaseFragment> list = copyList(getList());
            addAll(list);
        }
        return Integer.MAX_VALUE;
    }

    public int getRealCount() {
        return getList() == null ? 0 : getList().size();
    }

    public int toRealPosition(int position) {
        return position % getRealCount();
    }

    @Override
    public BaseFragment getItem(int position) {
        return super.getItem(toRealPosition(position));
    }

    /**
     * 设置开始位置，使得能够一开就可以左滑动
     * viewPage.setCurrentItem(loopFAdapter.getStartPosition())
     * @return
     */
    public int getStartPosition() {
        return getRealCount() * 1000;
    }

    /**
     * 少于4个的情况，需要对其索引进行add
     *
     * @param item
     */
    @Override
    public boolean add(BaseFragment item) {
        if (realIndex <= 0)
            return super.add(item);
        else {
            if (getList() == null)
                setList(new ArrayList<BaseFragment>());
            getList().add(realIndex++, item);
            return true;
        }
    }

    /**
     * 在<4的情况使用
     * 需要自行定义深拷贝，用系统提供的深拷贝无法完成要求
     * 注意此BaseFragment的子类不可以使用setArguments进行传值,而是直接使用构造函数传值，
     * 原因是viewpager在加载fragment的使用才会在oncreate的使用getArguments才得到对应的值
     * @param sourceList
     * @return
     */
    public abstract List<BaseFragment> copyList(List<BaseFragment> sourceList);
}
