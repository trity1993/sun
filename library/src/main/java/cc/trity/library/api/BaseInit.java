package cc.trity.library.api;

import android.os.Bundle;

/**
 * 基类需要实现初始化的接口
 * Created by TryIT on 2016/2/27.
 */
public interface BaseInit {
    /**
     * 初始化变量，包括Intent带的数据和Activity内的变量
     * 注意这个在得到view布局之前执行
     */
    void initVariables();

    /**
     * 加载Layout布局文件，初始化控件，以及相应的事件方法
     */
    void initView(Bundle savedInstanceState);

    /**
     * 进行调用获取数据
     */
    void loadData();
}
