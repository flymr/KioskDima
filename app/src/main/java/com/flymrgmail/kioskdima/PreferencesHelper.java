package com.flymrgmail.kioskdima;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private final String PREF_NAME = "kioskPreferences";
    private final String HOST_URL = "url";
    private final String APP_PASS = "password";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public PreferencesHelper(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public String getUrl(){
        return preferences.getString(HOST_URL, "http://www.google.com");
    }

    public void saveUrl(String url){
        editor.putString(HOST_URL, url);
        editor.apply();
        editor.commit();
    }

    public String getPass(){
        return preferences.getString(APP_PASS, "");
    }

    public void savePassword(String password){
        editor.putString(APP_PASS, password);
        editor.apply();
        editor.commit();
    }
}
