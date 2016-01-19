package cc.trity.sun;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by TryIT on 2016/1/12.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityTest {
    @Ignore
    @Test
    public void testMainActivity() {
//        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
//        mainActivity.findViewById(R.id.login).performClick();
//
//        Intent expectedIntent = new Intent(mainActivity, LoginActivity.class);
//        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
//
//        Intent actualIntent = shadowActivity.getNextStartedActivity();
//        Assert.assertEquals(expectedIntent, actualIntent);
    }
}
