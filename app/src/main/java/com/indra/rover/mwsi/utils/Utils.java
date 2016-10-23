package com.indra.rover.mwsi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.text.TextUtils;
import android.text.format.DateFormat;

import java.text.DecimalFormat;
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

    /**
     * format a current date with this format MM/dd/yyyy
     * @return formatted date
     */
    public static String getFormattedDate(){
        String date = (DateFormat.format("MM/dd/yyyy", new java.util.Date()).toString());
        return date;
    }


    /**
     * returns a formatted time with this format hh:mm:ss
     * @return
     */
    public static String getFormattedTime(){
        String time = (DateFormat.format("kk:mm:ss", new java.util.Date()).toString());
        return time;
    }

    public static String getCurrentDate1(){
        String date = (DateFormat.format("yyyy-MM-dd_hhmm", new java.util.Date()).toString());
        return date;
    }


    public static String getCurrentDate(String format){
        String date = (DateFormat.format(format, new java.util.Date()).toString());
        return date;
    }

    public static String getCurrentDate(){
        //"yyyy-MM-dd'T'HH:mm:ssZ"
        //"dd-MM-yyyy hh:mm:ss"
        String date = (DateFormat.format("yyyy-MM-dd kk:mm:ss", new java.util.Date()).toString());
        return date;
    }


    public static String formatDate(String strDate){
        if(strDate ==null ||strDate.isEmpty()){
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US);
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


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US);
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


    /**
     *
     * vibrate utils
     * @param context
     */
   public static void vibrate(Context context){
       Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
       // Vibrate for 700 milliseconds
       v.vibrate(700);
   }


    public static boolean isNotEmpty(String str){
        if(str!=null && !str.isEmpty()){
            return true;
        }
        return false;
    }


    /**
     * format a given string by removing the newline and replacing it to space
     * @param str  string to be formatted
     * @return formmatted string
     */
     public static String formatString(String str){
        return  str.replaceAll("\n"," ");
     }


    /**
     * round double data type to 2 decimal places
     * e.g  200.3456 = 200.35
     * @param val value to be rounded
     * @return rounded value to 2 decimal places
     */
    public static double roundDouble(double val){
        DecimalFormat df = new DecimalFormat("#.##");
        return  Double.valueOf(df.format(val));

    }



    /**
     * round double data type to 6 decimal places
     * e.g  200.3456377 = 200.3456378
     * @param val value to be rounded
     * @return rounded value to 6 decimal places
     */
    public static double roundDouble6(double val){
        DecimalFormat df = new DecimalFormat("#.######");
        return  Double.valueOf(df.format(val));

    }





}
