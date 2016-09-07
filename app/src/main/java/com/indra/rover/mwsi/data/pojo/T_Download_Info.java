package com.indra.rover.mwsi.data.pojo;

/**
 * Created by Indra on 9/7/2016.
 */
public class T_Download_Info {

    /**
     * MRU ID
     */
    String mru_id;

    /**
     *  Sequence Number
     */
    String seq_number;


    Customer customer;
    /**
     * Bill class
     */
    String bill_class;
    String rate_type;
    short bulk_flag;
    int acct_status;
    String meter_number;
    String mterial_number;
    int meter_size;
    int num_dials;
    short grp_flag;
    String block_tag;
    String disc_tag;
    String prevRDGdate;
    int actprevRDG;
    int billprevRDG;
    int billprevRG2;
    short pracflag;
    short pconsavgflag;
    int ave_consumpstions;
    int dpreplmtr_code;
    int nmintRDG;
    int nmConsumptionFactor;
    int nmConsfactor;
    int prevFF1;
    int prevFF2;
    int gt34flag;
    int gt34factor;
    short tarif_prorate;
    short fcda_prorate;
    short cera_prorate;
    short env_prorate;
    short sew_proate;
    String previous_Remarks;
    String previous_invoice_num;
    String wbpaydtls1;
    String wbpaydtls2;
    String miscpaydtls;
    String gdpaydtls;
    String install_wtr_ind;
    String install_sew_ind;
    String install_ar_ind;
    String install_adv_ind;
    String tin;
    short vat_exempt;
    short djscheck_flg;
    int numuser;
    String prevconsline1;
    String prevconsline2;
    String soa_number;
    int spbill_rule;
    String spbill_eff_date;


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
