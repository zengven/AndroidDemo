
package pub.ven.emoji.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * author: zengven
 * date: 2016/10/10
 * Desc: 缓存工具类
 */
public class PreferencesUtil {
    private static final String TAG = "PreferencesUtil";

    private static String PREFERENCE_FILE_NAME = "leme_config";
    private SharedPreferences mPreferences = null;

    private PreferencesUtil(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    };

    private static PreferencesUtil mInstance = null;

    public static synchronized PreferencesUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PreferencesUtil(context);
        }
        return mInstance;
    }

    public boolean putBoolean(String key, boolean value) {
        return mPreferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    public boolean putString(String key, String value) {
        return mPreferences.edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    public boolean putLong(String key, long value) {
        return mPreferences.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue){
        return mPreferences.getLong(key, defValue);
    }

    public void retainAllObejct(Set<String> object, String[] target) {
        Set<String> result = new HashSet<String>();
        if (null == object || object.size() == 0) {
            return ;
        }
        if (null != target && target.length > 0) {
            for (int i=0; i<target.length; i++) {
                if (!TextUtils.isEmpty(target[i])) result.add(target[i]);
            }
        }
        object.retainAll(result);
    }

    public boolean putStringSet(String key, Set<String> values) {
        return mPreferences.edit().putStringSet(key, values).commit();
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        return mPreferences.getStringSet(key, defValues);
    }

    /**
     * 清楚指定key对应的数据
     *
     * @param key
     */
    public boolean removeData(String key) {
        return mPreferences.edit().remove(key).commit();
    }

}
