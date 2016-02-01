package cc.trity.sun.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;

import cc.trity.sun.R;
import cc.trity.sun.activities.base.BasePrefActivity;
import cc.trity.sun.model.Global;
import cc.trity.sun.service.WeatherForegroundService;
import cc.trity.sun.utils.CommonUtils;

/**
 * 设置页
 */
public class SettingActivity extends BasePrefActivity implements Preference.OnPreferenceClickListener {
    private SharedPreferences mSharedPrefs;
    private CheckBoxPreference checkboxPrefs;

    @Override
    protected void onResume() {
        super.onResume();
        initPreference();//注意这里必须在onResume里初始化，否则空指针的异常
    }

    public void initPreference() {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
        PreferenceManager mPreferenceManager = bpfragment.getPreferenceManager();
        checkboxPrefs=(CheckBoxPreference)mPreferenceManager.findPreference("check_update_preference");
        checkboxPrefs.setOnPreferenceChangeListener(this);
    }

    @Override
    public void loadData() {

    }

    @Override
    public int getPreferencesResId() {
        return R.xml.prefence_settings;
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == checkboxPrefs) {
            boolean isStartService=(Boolean)newValue;
            if(isStartService){
                this.sendBroadcast(new Intent(Global.BROADCAST_START_SERVICE));
            }else{
                CommonUtils.showToast(this, "正在关闭...");
                this.stopService(new Intent(this, WeatherForegroundService.class));
            }
            SharedPreferences.Editor editor=mSharedPrefs.edit();
            editor.putBoolean(Global.SHARE_PREF_SERVICE,isStartService);
            editor.commit();
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        return false;
    }
}
