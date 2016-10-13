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

    public GLCharge(Cursor cursor){
        this.gl_code =cursor.getString(cursor.getColumnIndexOrThrow("GL_CHARGE_CODE"));
        String rate = cursor.getString(cursor.getColumnIndexOrThrow("GL_RATE"));
        if(Utils.isNotEmpty(rate)){
            this.gl_rate = Double.parseDouble(rate);
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
}
