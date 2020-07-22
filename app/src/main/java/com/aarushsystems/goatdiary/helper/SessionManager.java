package com.aarushsystems.goatdiary.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "goat_diary";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_LANGUAGE = "pref_language";
    private static final String KEY_EMAIL = "email";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setKeyEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public String getKeyEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public void setPrefLanguage(int val) {
        editor.putInt(KEY_LANGUAGE, val);
        editor.commit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public int getPrefLanguage() {
        return pref.getInt(KEY_LANGUAGE, 0);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
