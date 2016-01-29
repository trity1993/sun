package cc.trity.sun.fragments.base;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    protected static final String TAG="BaseFragment";
    protected Activity activity;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
//        setHasOptionsMenu(true);
        initVariables();

    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_base, menu);
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                    Intent intent=new Intent(activity, ChooseAreaActivity.class);
                    activity.startActivity(intent);
            break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public void init(){
        initView();
        loadData();
    }

    /**
     * 初始化变量，包括Intent带的数据和Activity内的变量
     * 注意这个在得到view布局之前执行
     */
    public abstract void initVariables();

    /**
     * 加载Layout布局文件，初始化控件，以及相应的事件方法
     */
    public abstract void initView();

    /**
     * 进行调用获取数据
     */
    public abstract void loadData();

}
