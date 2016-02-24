package cc.trity.sun.activities.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.sun.R;
import cc.trity.sun.activities.MainActivity;
import cc.trity.sun.activities.base.AppBaseActivity;
import cc.trity.sun.engine.AppConstants;
import cc.trity.sun.engine.User;

/**
 * 登陆，注册，的转接act
 * Created by TryIT on 2016/2/23.
 */
public class LoginMainActivity extends AppBaseActivity {

    private static final int LOGIN_REDIRECT_OUTSIDE = 3000;	//登录后跳转到其它页面
    private static final int LOGIN_REDIRECT_INSIDE = 3001;	//登录后仍然在本页面

    @InjectView(R.id.btnLogin1)
    Button btnLogin1;
    @InjectView(R.id.btnLogin2)
    Button btnLogin2;
    @InjectView(R.id.textView1)
    TextView textView1;
    @InjectView(R.id.textView2)
    TextView textView2;
    @InjectView(R.id.btnLogin3)
    Button btnLogin3;
    @InjectView(R.id.textView3)
    TextView textView3;
    @InjectView(R.id.btnLogout)
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        ButterKnife.inject(this);
        this.init(savedInstanceState);
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        btnLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        btnLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.getInstance(LoginMainActivity.this).isLoginStatus()) {
                    gotoNewsActivity();
                } else {
                    Intent intent = new Intent(LoginMainActivity.this,
                            LoginActivity.class);
                    intent.putExtra(AppConstants.NeedCallback, true);
                    startActivityForResult(intent, LOGIN_REDIRECT_OUTSIDE);
                }
            }
        });

        btnLogin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.getInstance(LoginMainActivity.this).isLoginStatus()) {
                    changeText();
                } else {
                    Intent intent = new Intent(LoginMainActivity.this,
                            LoginActivity.class);
                    intent.putExtra(AppConstants.NeedCallback, true);
                    startActivityForResult(intent, LOGIN_REDIRECT_INSIDE);
                }
            }
        });

        Button btnLogout = (Button)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance(LoginMainActivity.this).reset();
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=RESULT_OK)
            return;
        switch (requestCode){
            case LOGIN_REDIRECT_INSIDE:
                changeText();
                break;
            case LOGIN_REDIRECT_OUTSIDE:
                gotoNewsActivity();
                break;
        }
    }

    private void gotoNewsActivity() {
        Intent intent = new Intent(LoginMainActivity.this,
                MainActivity.class);
        startActivity(intent);
    }

    private void changeText() {
        textView1.setText("1");
    }
}
