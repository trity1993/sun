package cc.trity.sun.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.sun.R;
import cc.trity.sun.activities.base.AppBaseActivity;

public class SplashActivity extends AppBaseActivity implements Handler.Callback {

    @InjectView(R.id.txt_splash_num)
    TextView txtSplashNum;

    private final int DEGREE_MAX_NUM=23;

    private ScheduledThreadPoolExecutor exec;

    Handler handler=new Handler(this);
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
        exec=new ScheduledThreadPoolExecutor(1);
        exec.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                String txtDegree=txtSplashNum.getText().toString();
                int degreeNum=Integer.valueOf(txtDegree);
                if(degreeNum>=DEGREE_MAX_NUM){
                    exec.shutdownNow();
//                    MainActivity.toMainAct(SplashActivity.this);
//                    finish();
                }else{
                    handler.sendEmptyMessage(degreeNum);
                }
            }
        },1,1, TimeUnit.SECONDS);

    }

    @Override
    public boolean handleMessage(Message msg) {
        long nextdegree=msg.what+Math.round(Math.random()*10);
        if(nextdegree>DEGREE_MAX_NUM){
            txtSplashNum.setText(String.valueOf(DEGREE_MAX_NUM));
        }else{
            txtSplashNum.setText(String.valueOf(nextdegree));

        }
        return true;
    }

    @Override
    protected void onDestroy() {
        if(exec!=null)
            exec.shutdownNow();
        super.onDestroy();
    }
}
