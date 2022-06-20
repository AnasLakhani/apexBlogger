package com.coderlytics.apexblogger.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SpHelper {

    private final static String PREF_FILE = "kiryana";
    private final static String IS_LOGIN = "is_login";
    private final static String ACCESS_TOKEN = "access_token";
    private final static String IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH";
    public final static String AGE = "AGE";
    public static final String EMAIL = "EMAIL";
    public static final String GENDER = "GENDER";
    public static final String IMAGEURL = "IMAGEURL";
    public static final String ID = "ID";
    public static final String PHONE = "PHONE";
    public static final String ROLE = "ROLE";
    public static final String USERNAME = "USERNAME";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SpHelper(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_FILE, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static boolean isLogin(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getBoolean(IS_LOGIN, false) && settings.contains(ACCESS_TOKEN);
    }

    public static String getValue(Context context,String key) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getString(key, "");
    }

    public static void setKey(Context context, String key, String value) {
        context.getSharedPreferences(PREF_FILE, 0).edit().putString(key, value).apply();
    }


    public static String getLocalToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getString(ACCESS_TOKEN, "");
    }

    public static void makeLogin(Context context, String v_AGE,
                                 String v_EMAIL,
                                 String v_GENDER,
                                 String v_IMAGEURL,
                                 String v_ID,
                                 String v_PHONE,
                                 String v_ROLE,
                                 String v_USERNAME) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(AGE, v_AGE);
        editor.putString(EMAIL, v_EMAIL);
        editor.putString(GENDER, v_GENDER);
        editor.putString(IMAGEURL, v_IMAGEURL);
        editor.putString(ID, v_ID);
        editor.putString(PHONE, v_PHONE);
        editor.putString(ROLE, v_ROLE);
        editor.putString(USERNAME, v_USERNAME);
        editor.apply();
    }

    public static void destroyLogin(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(IS_LOGIN, false);
        editor.apply();
        editor.remove(ACCESS_TOKEN);
        editor.apply();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}