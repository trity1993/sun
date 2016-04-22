package cc.trity.sun.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.library.utils.CommonUtils;
import cc.trity.library.utils.LogUtils;
import cc.trity.library.utils.Utils;
import cc.trity.sun.R;
import cc.trity.sun.activities.base.AppBaseActivity;
import cc.trity.sun.db.DataBaseManager;
import cc.trity.sun.location.BadiLoactionHelper;
import cc.trity.sun.model.city.County;
import cc.trity.sun.utils.Utility;

public class SplashActivity extends AppBaseActivity implements BDLocationListener, Handler.Callback {
    private static final String TAG = "SplashActivity";

    @InjectView(R.id.txt_splash_num)
    TextView txtSplashNum;

    private final int DEGREE_MAX_NUM = 23;

    private ScheduledThreadPoolExecutor exec;


    private boolean isSucess = false;

    private BadiLoactionHelper badiLoactionHelper;

    Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        this.init(savedInstanceState);

    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void loadData() {
        //初始化定位
        badiLoactionHelper = new BadiLoactionHelper(getApplicationContext(), this);
        badiLoactionHelper.initBaiduLocation();
        badiLoactionHelper.requestLocation();
        CommonUtils.showToast(SplashActivity.this, R.string.locating);
        //数字体
        exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                String txtDegree = txtSplashNum.getText().toString();
                int degreeNum = Utils.convertToInt(txtDegree);
                if (degreeNum >= DEGREE_MAX_NUM) {
                    exec.shutdownNow();
                    MainActivity.toMainAct(SplashActivity.this, isSucess);
                    finish();
                } else {
                    handler.sendEmptyMessage(degreeNum);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

    }

    @Override
    public boolean handleMessage(Message msg) {
        long nextdegree = msg.what + Math.round(Math.random() * 10);
        if (nextdegree > DEGREE_MAX_NUM) {
            txtSplashNum.setText(String.valueOf(DEGREE_MAX_NUM));
        } else {
            txtSplashNum.setText(String.valueOf(nextdegree));

        }
        return true;
    }

    @Override
    protected void onDestroy() {
        if (exec != null)
            exec.shutdownNow();
        if (badiLoactionHelper != null)
            badiLoactionHelper.cancelRequestLocation();
        super.onDestroy();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            CommonUtils.showToast(this, R.string.locate_fail);
            return;
        }

        if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            //模仿地点进行选择
            String provinceName= Utils.safeSubString(location.getProvince(),0,location.getProvince().length()-1);
            String cityName=Utils.safeSubString(location.getCity(), 0, location.getCity().length() - 1);
            String districtName=Utils.safeSubString(location.getDistrict(), 0, location.getDistrict().length() - 1);

            LogUtils.d(TAG,provinceName+","+ cityName+","+districtName);
            String countryCode= Utility.getCountryCode
                    (provinceName, cityName, districtName, SplashActivity.this);
            if (countryCode!=null){
                County county = new County(districtName,countryCode);
                county.setId("1");
                if (saveCounties(county)) {
                    isSucess = true;// 定位成功，执行对应的操作
                    LogUtils.d(TAG, "更新成功");
                }
            }else{
                isSucess = false;// 定位失败
                CommonUtils.showToast(SplashActivity.this,R.string.locate_fail_select);
            }

        } else {
            // 定位失败
            CommonUtils.showToast(this, R.string.locate_fail);
        }
    }

    /**
     * 保存当前定位得到的县信息
     *
     * @param county
     */
    private boolean saveCounties(County county) {
        DataBaseManager dataBaseManager = new DataBaseManager(SplashActivity.this);
        return dataBaseManager.updateCounty(county);
    }

}
