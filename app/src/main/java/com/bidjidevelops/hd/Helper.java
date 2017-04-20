package com.bidjidevelops.hd;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by You on 27/03/2017.
 */

public class Helper extends BaseApp {

    /*alamat utama, silahkan disesuaikan dgn ip sobat dan nama folder sobat*/
//    public static String BASE_URL = "http://192.168.123.51/hd/";
    public static String BASE_URL = "http://169.254.125.7/hd/";
//    public static String BASE_URL = "http://192.168.50.11/hd/";
    public static String BASE_IMGUS = BASE_URL+"uploads/";

    /*fungsi cek kesamaan text*/
    public static boolean isCompare(EditText et1, EditText et2){
        String a = et1.getText().toString();
        String b = et2.getText().toString();
        /*jika a sama dengan b*/
        if (a.equals(b)){
            return false;
        }else{
            return true;
        }
    }

    /*fungsi untuk menampilkan pesan*/
    public static void pesan (Context c, String msg){
        Toast.makeText(c,msg, Toast.LENGTH_LONG).show();
    }

    /*fungsi untuk mengecek apakah isian kosong*/
    public static boolean isEmpty(EditText editText){
        /*jika banyak huruf lebih dari 0*/
        if (editText.getText().toString().trim().length() > 0){
            /*tidak dikembalikan*/
            return false;
        }else {
            /*kembalikan*/
            return true;
        }
    }

    // validasi untuk inputan email
    public static boolean isEmailValid(EditText email) {
        boolean isValid = false;
        String expression = "\"/^\\\\w+[\\\\+\\\\.\\\\w-]*@([\\\\w-]+\\\\.)*\\\\w+[\\\\w-]*\\\\.([a-z]{2,4}|\\\\d+)$/i\";";
        CharSequence inputStr = email.getText().toString();

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
