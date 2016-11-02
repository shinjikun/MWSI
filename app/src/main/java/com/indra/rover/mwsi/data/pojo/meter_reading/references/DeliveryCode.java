package com.indra.rover.mwsi.data.pojo.meter_reading.references;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by Indra on 8/26/2016.
 */
public class DeliveryCode implements Serializable {
    private String del_code;
    private String del_desc;
    private int del_signreq_flag;
    private int del_remreq_flag;

    public DeliveryCode(){

    }

    public DeliveryCode(Cursor cursor){
        this.del_code =cursor.getString(cursor.getColumnIndexOrThrow("DEL_CODE"));
        this.del_desc = cursor.getString(cursor.getColumnIndexOrThrow("DEL_DESC"));
        this.del_signreq_flag = cursor.getInt(cursor.getColumnIndexOrThrow("SIGNREQ_FLAG"));
        this.del_remreq_flag = cursor.getInt(cursor.getColumnIndexOrThrow("REMREQ_FLAG"));
    }
    public String getDel_code() {
        return del_code;
    }


    public String getDel_desc() {
        return del_desc;
    }

    public int getRemarksFlag() {
        return del_remreq_flag;
    }

    public int getDel_signreq_flag() {
        return del_signreq_flag;
    }
}
