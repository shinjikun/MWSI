package com.indra.rover.mwsi.data.pojo.meter_reading.references;

import android.database.Cursor;

public class RangeTolerance {
  int minrange;
  int maxrange;
  int ceilconsump;
  int type;

    public RangeTolerance(Cursor cursor){
        this.minrange =cursor.getInt(cursor.getColumnIndexOrThrow("MIRANGE"));
        this.maxrange = cursor.getInt(cursor.getColumnIndexOrThrow("MAXRANGE"));
        this.ceilconsump = cursor.getInt(cursor.getColumnIndexOrThrow("CEILCONSUMP"));
        this.type =  cursor.getInt(cursor.getColumnIndexOrThrow("DELI_TYPE"));
    }


}
