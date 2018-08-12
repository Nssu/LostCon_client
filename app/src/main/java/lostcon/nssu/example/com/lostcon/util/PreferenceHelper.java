package lostcon.nssu.example.com.lostcon.util;

import android.content.SharedPreferences;

import lostcon.nssu.example.com.lostcon.common.BaseApplication;
import lostcon.nssu.example.com.lostcon.common.Constants;

public class PreferenceHelper {
    public static void saveUser(int key)
    {
        SharedPreferences.Editor editor = BaseApplication.preferences.edit();
        editor.putInt(Constants.PREF_USER, key).apply();
    }

    public static int loadUser()
    {
        return BaseApplication.preferences.getInt(Constants.PREF_USER, 0);
    }

    


}
