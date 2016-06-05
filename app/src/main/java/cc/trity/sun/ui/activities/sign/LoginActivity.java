package cc.trity.sun.ui.activities.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.library.net.RequestParameter;
import cc.trity.library.utils.CommonUtils;
import cc.trity.library.utils.GsonUtils;
import cc.trity.sun.R;
import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.engine.RemoteService;
import cc.trity.sun.engine.User;
import cc.trity.sun.listener.AbstractRequestCallback;
import cc.trity.sun.model.UserInfo;
import cc.trity.sun.ui.activities.MainActivity;
import cc.trity.sun.ui.activities.base.AppBaseActivity;

public class LoginActivity extends AppBaseActivity {

    @InjectView(R.id.login_status_message)
    TextView loginStatusMessage;
    @InjectView(R.id.login_status)
    LinearLayout loginStatus;
    @InjectView(R.id.email)
    EditText email;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.sign_in_button)
    Button signInButton;
    @InjectView(R.id.login_form)
    ScrollView loginForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        this.init(savedInstanceState);
    }

    @Override
    public void initVariables() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;
        needCallback = bundle.getBoolean(AppConstants.NeedCallback);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public void loadData() {

    }

    /**
     * 模拟登陆操作
     */
    private void login(){
        AbstractRequestCallback loginCallback=new AbstractRequestCallback(LoginActivity.this) {
            @Override
            public void onSuccess(String content) {
                UserInfo userInfo= GsonUtils.getClass(content,UserInfo.class);
                if(userInfo!=null){
                    User user=User.getInstance(LoginActivity.this);
                    user.reset();
                    user.setLoginName(userInfo.getLoginName());
                    user.setUserName(userInfo.getUserName());
                    user.setScore(userInfo.getScore());
                    user.setLoginStatus(true);
                    user.save();
                }
                if(needCallback){
                    setResult(RESULT_OK);
                    finish();
                }else{
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFail(String errorMsg) {
                CommonUtils.showToast(LoginActivity.this,errorMsg);
            }
        };

        ArrayList<RequestParameter> params = new ArrayList<>();
        RequestParameter rp1 = new RequestParameter("loginName", "jianqiang.bao");
        RequestParameter rp2 = new RequestParameter("password", "1111");//这里需要使用不对称加密，当然注册的时候也为不对称的加密的结果，这样进行匹配
        params.add(rp1);
        params.add(rp2);

        RemoteService.getInstance().invoke(this,"login",params,null,loginCallback);
    }
}
