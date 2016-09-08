package com.indra.rover.mwsi.data.pojo;

import com.indra.rover.mwsi.data.pojo.meter_reading.misc.InstallMisc;

/**
 * Created by Indra on 9/8/2016.
 */
public class T_Upload {

    /**
     * mru id
     */
    String mru_id;
    /**
     * user account number
     */
    String acct_num;
    /**
     * basic charge
     */
    float basic_charge;
    /**
     * discount
      */
    float discount;
    int cera;
    int fcda;
    /**
     * environmental charge
     */
    float env_charge;
    /**
     *  sewer charge
     */
    float sewer_charge;
    /**
     * msc_ammount
     */
    float msc_amount;
    float sc_discount;
    float totchrg_wo_tax;
    float vat_charge;
    float tot_curr_charge;
    float prevunpaid;
    float septic_charge;
    float changesize_charge;
    float restoration_charge;
    float misc_charge;
    float install_sewer_charge;
    float advance;
    float reopening_fee;
    float meter_charges;
    float gd_charge;
    float other_charges;
    float subtotal_amt;
    float total_amt_due;
    int print_count;
    int print_tag;
    InstallMisc installMisc;

    public T_Upload(String[] record){

        this.mru_id = record[0];
        this.septic_charge = Float.parseFloat(record[44]);
        this.changesize_charge = Float.parseFloat(record[45]);
        this.restoration_charge = Float.parseFloat(record[46]);
        this.misc_charge = Float.parseFloat(record[47]);
        this.install_sewer_charge = Float.parseFloat(record[48]);
        this.advance = Float.parseFloat(record[49]);
        installMisc = new InstallMisc(record);
        this.reopening_fee = Float.parseFloat(record[59]);
        this.meter_charges = Float.parseFloat(record[60]);
        this.gd_charge = Float.parseFloat(record[61]);
        this.other_charges = Float.parseFloat(record[62]);
    }



}
