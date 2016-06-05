package cc.trity.sun.ui.fragments.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import cc.trity.library.activity.BaseAppCompatActivity;
import cc.trity.library.api.BaseInit;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements BaseInit {
    protected static final String TAG="BaseFragment";
    protected BaseAppCompatActivity activity;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=(BaseAppCompatActivity)getActivity();
//        setHasOptionsMenu(true);
        initVariables();

    }

    public void init(Bundle savedInstanceState){
        initView(savedInstanceState);
        loadData();
    }

}
