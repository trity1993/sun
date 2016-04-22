package cc.trity.sun.activities.h5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.nativewithh5.activities.H5BaseActivity;
import cc.trity.nativewithh5.engine.Dispatcher;
import cc.trity.sun.R;

public class H5Activity extends H5BaseActivity {

    @InjectView(R.id.h5_webview)
    WebView h5Webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        ButterKnife.inject(this);

        h5Webview.getSettings().setJavaScriptEnabled(true);
        h5Webview.loadUrl("file:///android_asset/index.html");

        h5Webview.addJavascriptInterface(new JSInteface1(), "baobao");

    }

    public static void toH5Act(Context context){
        Intent intent=new Intent(context,H5Activity.class);
        context.startActivity(intent);
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void loadData() {

    }

    class JSInteface1 {
        @JavascriptInterface
        public void callAndroidMethod(int a, float b, String c, boolean d) {
            if (d) {
                String strMessage = "-" + (a + 1) + "-" + (b + 1) + "-" + c
                        + "-" + d;
                Toast.makeText(H5Activity.this, "execute callAndroidMethod",
                        Toast.LENGTH_SHORT).show();
            }
        }
        @JavascriptInterface
        public void gotoAnyWhere(String url) {
            if (url != null) {
                Toast.makeText(H5Activity.this, "execute gotoAnyWhere",
                        Toast.LENGTH_SHORT).show();
                Dispatcher.gotoAnyWhere(url, H5Activity.this);

            }
        }
    }
    @JavascriptInterface
    public void callAndroidMethod(int a, float b, String c, boolean d) {
        Toast.makeText(H5Activity.this, "execute callAndroidMethod",
                Toast.LENGTH_SHORT).show();
    }
}
