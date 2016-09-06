package com.indra.rover.mwsi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {



    public static boolean isInternetOn(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                boolean connected = networkInfo != null
                        && networkInfo.isConnected();
                if (!connected) {

                }
                return connected;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }


    public static String getCurrentDate(){
        //"yyyy-MM-dd'T'HH:mm:ssZ"
        //"dd-MM-yyyy hh:mm:ss"
        String date = (DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
        return date;
    }


    public static String formatDate(String strDate){
        if(strDate ==null ||strDate.isEmpty()){
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        try {
            Date date1 = formatter.parse(strDate);

            return DateFormat.format("MMM dd, yyyy", date1).toString();


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }

    //compare two dates
    public static boolean compareDate(String strDate1, String strDate2) {


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        try {
            Date date1 = formatter.parse(strDate1);
            Date date2 = formatter.parse(strDate2);

            if (date1.getTime() >= date2.getTime()) {
                return true;
            }
           return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }




    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }







}
