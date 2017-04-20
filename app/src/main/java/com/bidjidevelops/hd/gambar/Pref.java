package com.bidjidevelops.hd.gambar;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    //fungsi untuk menyimpan data sementara
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE =0;
    private static final String PREF_NAME = "Upload";
    private static final String IS_FIRST_LOGIN = "isFirstTimeLogin";

    public Pref(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstLaunched(boolean isFirstLaunched){
        editor.putBoolean(IS_FIRST_LOGIN, isFirstLaunched );
        editor.commit();
    }

    public boolean isFirstTimeLaunched(){
        return pref.getBoolean(IS_FIRST_LOGIN, true);
    }
}
