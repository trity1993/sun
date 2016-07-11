package cc.trity.library.adapter;

import java.util.List;

/**
 * 通用适配器接口
 * 不建议使用List<T>接口，显得太臃肿，
 * 需要特殊的方法个人建议是使用通过获取arraylist来执行
 * <p/>
 * Created by Tony on 4/8/15.
 */
public interface IListAdapter<T> {

    /**
     * 设置数据列表
     *
     * @param list
     */
    void setList(List<T> list);

    /**
     * 获取数据列表
     *
     * @return
     */
    List<T> getList();

    /**
     * 添加数据列表
     *
     * @param list
     */
    boolean addAll(List<T> list);

    /**
     * 添加Item
     *
     * @param item
     */
    boolean add(T item);

    /**
     * 获取Item
     *
     * @param position
     * @return
     */
    T getItem(int position);

    /**
     * 判断是否存在该Item
     *
     * @param item
     * @return
     */
    boolean contains(T item);

    /**
     * 删除Item
     *
     * @param item
     */
    boolean remove(T item);

    /**
     * 删除Item
     *
     * @param position
     */
    T remove(int position);

    /**
     * 清空数据items
     */
    void clear();

    /**
     * 通知数据刷新
     */
    void notifyDataSetChanged();
}
