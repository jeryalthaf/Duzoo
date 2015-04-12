package com.duzoo.android.application;

import android.preference.PreferenceManager;

/**
 * Created by RRaju on 4/7/2015.
 */
public class DuzooPreferenceManager {

    public DuzooPreferenceManager() {
    }

    public static void putKey(String key,String value) {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit().putString(key,value).commit();
    }

    public static void putKey(String key,int value) {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit().putInt(key,value).commit();
    }

    public static void putKey(String key,Boolean value) {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit().putBoolean(key,value).commit();
    }

    public static String getKey(String key) {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).getString(key,"duzoo");
    }

    public static boolean getBooleanKey(String key) {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).getBoolean(key,false);
    }

    public static int getIntKey(String key) {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).getInt(key,0);
    }
}
