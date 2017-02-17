package app.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigHelp {
    private static final int MODE_PRIVATE = Context.MODE_PRIVATE;
    private static final String CONFIG_STORE = "XiaoHer.conf";

    private static final String KEY_ACCOUNT_NAME = "XiaoHer.account_name";

    /** 已显示的首次启动引导版本 */
    private static final String KEY_SHOWED_GUIDE_VERSION = "guide_version";
    private static final String KEY_USER_INFO = "user_info";

    private static final String KEY_XGPUSH_REGISTERED = "xgpush_registered";
    private static final String KEY_UID = "uid";
    private static final String KEY_DEVICE_ID = "device_id";

    /** 第一次进入主页 */
    private static final String KEY_FIRST_HOME = "first_home";

    private Context mContext = null;
    
    private static ConfigHelp mConfigHelp;
    
    public static ConfigHelp getInstance(Context context) {
    	if (mConfigHelp == null) {
    		mConfigHelp = new ConfigHelp(context);
    	}
    	
    	return mConfigHelp;
    }

    private ConfigHelp(Context context) {
        mContext = context;
    }

    public void setFirstHome(boolean first) {
        setBoolean(KEY_FIRST_HOME, first);
    }

    public boolean isFirstHome() {
        return getBoolean(KEY_FIRST_HOME, true);
    }

    public void setXGPushRegistered(boolean registered) {
        setBoolean(KEY_XGPUSH_REGISTERED, registered);
    }

    public boolean isXGPushRegistered() {
        return getBoolean(KEY_XGPUSH_REGISTERED, false);
    }

    public void saveUid(String uid) {
        setString(KEY_UID, uid);
    }

    public String getUid() {
        String uid = getString(KEY_UID, null);
        if (uid == null)
            uid = Installation.id(mContext);
        return uid;
    }




    /**
     * 保存当前登陆用户的用户名
     * @param name 用户名
     */
    public void saveAccountName(String name) {
        setString(KEY_ACCOUNT_NAME, name);
    }

    /**
     * 获取保存的登陆用户的用户名
     * @return
     */
    public String getAccountName() {
        return getString(KEY_ACCOUNT_NAME, "");
    }

    /**
     * 保存已显示的首次启动引导版本
     * @param version
     */
    public void saveShowedGuideVersion(int version) {
        setInteger(KEY_SHOWED_GUIDE_VERSION, version);
    }

    /**
     * 获取已显示的首次启动引导版本
     * @return
     */
    public int getShowedGuideVersion() {
        return getInteger(KEY_SHOWED_GUIDE_VERSION, 0);
    }

    public void saveDeviceId(String deviceId) {
        setString(KEY_DEVICE_ID, deviceId);
    }

    public String getDeviceId() {
        return getString(KEY_DEVICE_ID, "");
    }

    /**
     * Remove all settings
     */
    public void removeSettings() {

    }
    /**
     * Remove a setting
     *
     * @param key the setting to remove
     */
    public void removeSetting(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(CONFIG_STORE, MODE_PRIVATE);
        preferences.edit().remove(key).apply();
    }

    /**
     * Sets a new string setting
     *
     * @param key the key, null not allowed
     * @param value the new value, null not allowed
     */
    public void setString(String key, String value) {
        SharedPreferences preferences = mContext.getSharedPreferences(CONFIG_STORE, MODE_PRIVATE);

        preferences.edit().putString(key, value).apply();
    }

    /**
     * Get a stored string setting
     *
     * @param key the key for the setting
     * @param defaultValue the default value if key not found
     * @return the setting
     */
    public String getString(String key, String defaultValue) {
        SharedPreferences preferences = mContext.getSharedPreferences(CONFIG_STORE, MODE_PRIVATE);

        return preferences.getString(key, defaultValue);
    }

    /**
     * Store a long setting
     *
     * @param key the key for the setting
     * @param value the value for the setting
     */
    public void setLong(String key, Long value) {
        SharedPreferences preferences = mContext.getSharedPreferences(CONFIG_STORE, MODE_PRIVATE);

        preferences.edit().putLong(key, value).apply();
    }

    /**
     * Get a stored long setting
     *
     * @param key the key for the setting
     * @param defaultValue the default value if key not found
     * @return the setting
     */
    public Long getLong(String key, long defaultValue) {
        return mContext.getSharedPreferences(CONFIG_STORE, MODE_PRIVATE).getLong(key, defaultValue);
    }


    /**
     * Get a stored integer setting
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Integer getInteger(String key, Integer defaultValue) {
        return mContext.getSharedPreferences(CONFIG_STORE, MODE_PRIVATE).getInt(key, defaultValue);
    }

    /**
     * Set an integer
     * @param key
     * @param value
     */
    public void setInteger(String key, int value) {
        SharedPreferences preferences = mContext.getSharedPreferences(CONFIG_STORE, MODE_PRIVATE);

        preferences.edit().putInt(key, value).apply();
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
    	return mContext.getSharedPreferences(CONFIG_STORE, MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    public void setBoolean(String key, Boolean value) {
        SharedPreferences preferences = mContext.getSharedPreferences(CONFIG_STORE, MODE_PRIVATE);
        preferences.edit().putBoolean(key, value).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return mContext.getSharedPreferences(CONFIG_STORE, MODE_PRIVATE).getFloat(key, defaultValue);
    }

    public void setFloat(String key, float value) {
        SharedPreferences preferences = mContext.getSharedPreferences(CONFIG_STORE, MODE_PRIVATE);
        preferences.edit().putFloat(key, value).apply();
    }

}
