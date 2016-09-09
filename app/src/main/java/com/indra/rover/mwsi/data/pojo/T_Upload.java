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
    String ULDOCNO;
    InstallMisc installMisc;


    public String getULDOCNO() {
        return ULDOCNO;
    }

    public String getMru_id() {
        return mru_id;
    }

    public InstallMisc getInstallMisc() {
        return installMisc;
    }

    public String getAcct_num() {
        return acct_num;
    }

    public float getPrevunpaid() {
        return prevunpaid;
    }

    public float getSeptic_charge() {
        return septic_charge;
    }


    public float getChangesize_charge() {
        return changesize_charge;
    }

    public float getRestoration_charge() {
        return restoration_charge;
    }

    public float getMisc_charge() {
        return misc_charge;
    }

    public float getInstall_sewer_charge() {
        return install_sewer_charge;
    }

    public float getAdvance() {
        return advance;
    }

    public float getReopening_fee() {
        return reopening_fee;
    }

    public float getMeter_charges() {
        return meter_charges;
    }

    public float getGd_charge() {
        return gd_charge;
    }

    public float getOther_charges() {
        return other_charges;
    }
}
