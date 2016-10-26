package com.indra.rover.mwsi.data.pojo.meter_reading.references;

import android.database.Cursor;

import com.indra.rover.mwsi.utils.Utils;

/**
 * Created by Indra on 10/10/2016.
 */
public class GLCharge {

 String gl_code;
 double gl_rate;
 String effectivity_date;
 String gl_desc;
 double gl_rate_old=0;

    public GLCharge(Cursor cursor){
        this.gl_code =cursor.getString(cursor.getColumnIndexOrThrow("GL_CHARGE_CODE"));
        String rate = cursor.getString(cursor.getColumnIndexOrThrow("GL_RATE"));
        if(Utils.isNotEmpty(rate)){
            this.gl_rate = Double.parseDouble(rate);
        }
        String oldrate = cursor.getString(cursor.getColumnIndexOrThrow("GL_RATE_OLD"));
        if(Utils.isNotEmpty(oldrate)){
            this.gl_rate_old = Double.parseDouble(oldrate);
        }

        this.gl_desc = cursor.getString(cursor.getColumnIndexOrThrow("GL_CHARGE_DESC"));
        this.effectivity_date = cursor.getString(cursor.getColumnIndexOrThrow("EFFECTIVITY_DATE"));
    }

    public double getGl_rate() {
        return gl_rate;
    }

    public String getEffectivity_date() {
        return effectivity_date;
    }

    public String getGl_code() {
        return gl_code;
    }

    public String getGl_desc() {
        return gl_desc;
    }

    public double getGl_rate_old() {
        return gl_rate_old;
    }

    public void setGl_code(String gl_code) {
        this.gl_code = gl_code;
    }

    public void setGl_rate_old(double gl_rate_old) {
        this.gl_rate_old = gl_rate_old;
    }

    public void setGl_rate(double gl_rate) {
        this.gl_rate = gl_rate;
    }
}
