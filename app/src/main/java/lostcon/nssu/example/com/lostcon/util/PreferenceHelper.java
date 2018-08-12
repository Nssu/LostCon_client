package lostcon.nssu.example.com.lostcon.util;

import android.content.SharedPreferences;


import lostcon.nssu.example.com.lostcon.common.BaseApplication;
import lostcon.nssu.example.com.lostcon.common.Constants;

import com.google.gson.Gson;

import lostcon.nssu.example.com.lostcon.common.BaseApplication;
import lostcon.nssu.example.com.lostcon.common.Constants;
import lostcon.nssu.example.com.lostcon.model.Item;


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


    public static void saveItem(Item item)
    {
        Gson gson = new Gson();
        String json = gson.toJson(item,Item.class);
        SharedPreferences.Editor editor = BaseApplication.preferences.edit();

        editor.putString(Constants.PREF_ITEM,json).apply();
    }

    public static Item loadItem()
    {
        Gson gson = new Gson();
        String json = BaseApplication.preferences.getString(Constants.PREF_ITEM,null);
        return gson.fromJson(json,Item.class);
    }




}
