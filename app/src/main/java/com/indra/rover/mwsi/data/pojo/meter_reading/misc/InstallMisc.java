package com.indra.rover.mwsi.data.pojo.meter_reading.misc;

/**
 * Created by Indra on 9/8/2016.
 */
public class InstallMisc {
    /**
     * Installment for Installation Charges Indicator
     */

    String install_wtr_ind;
    /**
     * Installment for Sewer Installation Charges Amount
     */
    float install_sewer_due;
    /**
     * Installment for Sewer Installation Charges Indicator
     */
    String install_sew_ind;
    /**
     * Installment for A/R Amount
     */
    float install_ar_due;
    /**
     * Installment for A/R Indicator
     */
    String install_ar_ind;
    /**
     * Installment for Advances Amount
     */
    float install_adv_due;
    /**
     * Installment for Advances Indicator
     */
    String install_adv_ind;

    public InstallMisc(String records[]){
        install_wtr_ind = records[50];
        install_sewer_due = Float.parseFloat(records[51]);
        install_sew_ind = records[52];
        install_ar_due = Float.parseFloat(records[53]);
        install_ar_ind = records[54];
        install_adv_due = Float.parseFloat(records[55]);
        install_adv_ind = records[56];
    }


    public void setInstall_adv_ind(String install_adv_ind) {
        this.install_adv_ind = install_adv_ind;
    }

    public void setInstall_ar_ind(String install_ar_ind) {
        this.install_ar_ind = install_ar_ind;
    }

    public void setInstall_sew_ind(String install_sew_ind) {
        this.install_sew_ind = install_sew_ind;
    }

    public void setInstall_wtr_ind(String install_wtr_ind) {
        this.install_wtr_ind = install_wtr_ind;
    }

}
