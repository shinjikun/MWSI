package com.indra.rover.mwsi.data.pojo;

import android.database.Cursor;

/**
 * Created by Indra on 8/26/2016.
 */
public class DeliveryCode {
    private String del_code;
    private String del_desc;
    private int del_signreq_flag;


    public DeliveryCode(){

    }

    public DeliveryCode(Cursor cursor){
        this.del_code =cursor.getString(cursor.getColumnIndexOrThrow("DEL_CODE"));
        this.del_desc = cursor.getString(cursor.getColumnIndexOrThrow("DEL_DESC"));
        this.del_signreq_flag = cursor.getInt(cursor.getColumnIndexOrThrow("SIGNREQ_FLAG"));
    }
    public String getDel_code() {
        return del_code;
    }

    public void setDel_code(String del_code) {
        this.del_code = del_code;
    }

    public String getDel_desc() {
        return del_desc;
    }

    public void setDel_desc(String del_desc) {
        this.del_desc = del_desc;
    }

    public void setDel_signreq_flag(int del_signreq_flag) {
        this.del_signreq_flag = del_signreq_flag;
    }

    public int getDel_signreq_flag() {
        return del_signreq_flag;
    }
}
