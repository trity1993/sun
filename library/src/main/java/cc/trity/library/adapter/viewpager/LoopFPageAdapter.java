package cc.trity.library.adapter.viewpager;

import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cc.trity.library.fragment.BaseFragment;


/**
 * fragment方式的无限循环
 * Created by trity on 20/6/16.
 */
public class LoopFPageAdapter extends CommonFPageAdapter {

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
            List<BaseFragment> list = new ArrayList<>();
            Collections.copy(getList(), list);//使用此copy函数才能为深拷贝
            for (BaseFragment bfTmp : list) {//循环添加多一次
                add(bfTmp);
            }
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
     * 设置开始位置，使得能够一开就左滑动
     *
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

}
