package cc.trity.nativewithh5.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import cc.trity.nativewithh5.engine.Dispatcher;

/**
 * Created by TryIT on 2016/2/25.
 */
public class H5BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @JavascriptInterface
    public void gotoAnyWhere(String url) {
        if (url != null) {
            Toast.makeText(H5BaseActivity.this, "execute gotoAnyWhere",
                    Toast.LENGTH_SHORT).show();

        }
        Dispatcher.gotoAnyWhere(url,H5BaseActivity.this);
    }
}
