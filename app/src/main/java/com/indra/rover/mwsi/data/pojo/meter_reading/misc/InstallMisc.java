package com.indra.rover.mwsi.data.pojo.meter_reading.misc;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Indra on 9/8/2016.
 */
public class InstallMisc implements Serializable {
    /**
     * Installment for Installation Charges Indicator
     */

    private String install_wtr_ind;
    /**
     * Installment for Sewer Installation Charges Amount
     */
   private float install_sewer_due;
    /**
     * Installment for Sewer Installation Charges Indicator
     */
    private String install_sew_ind;
    /**
     * Installment for A/R Amount
     */
    private float install_ar_due;
    /**
     * Installment for A/R Indicator
     */
    private String install_ar_ind;
    /**
     * Installment for Advances Amount
     */
    private float install_adv_due;
    /**
     * Installment for Advances Indicator
     */
    private String install_adv_ind;

    public InstallMisc(String records[]){
        install_wtr_ind = records[50];
        if(!records[51].isEmpty())
            install_sewer_due = Float.parseFloat(records[51]);
        install_sew_ind = records[52];
        if(!records[53].isEmpty())
            install_ar_due = Float.parseFloat(records[53]);
        install_ar_ind = records[54];
        if(!records[55].isEmpty())
            install_adv_due = Float.parseFloat(records[55]);
        install_adv_ind = records[56];
    }

    public float getInstall_adv_due() {
        return install_adv_due;
    }

    public float getInstall_ar_due() {
        return install_ar_due;
    }

    public float getInstall_sewer_due() {
        return install_sewer_due;
    }

    public String getInstall_adv_ind() {
        return install_adv_ind;
    }

    public String getInstall_ar_ind() {
        return install_ar_ind;
    }

    public String getInstall_sew_ind() {
        return install_sew_ind;
    }

    public String getInstall_wtr_ind() {
        return install_wtr_ind;
    }
}
