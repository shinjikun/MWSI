package com.indra.rover.mwsi.data.pojo.meter_reading;

import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.meter_reading.misc.CustomerInfo;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.PreviousData;


public class MeterRHistory {

   private PreviousData previousData;
   private CustomerInfo customerInfo;

    public MeterRHistory(Cursor cursor){
        this.previousData = new PreviousData(cursor);
        this.customerInfo = new CustomerInfo(cursor);
    }


    public PreviousData getPreviousData() {
        return previousData;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }
}
