package com.indra.rover.mwsi.data.pojo;

/**
 * Created by Indra on 9/7/2016.
 */
public class CurrentReading {

    String acctnum;
    String meterno;
    String readstat;
    String rdg_date;
    String recmd_seqno;
    String new_meterni;
    String ffcode1;
    String ffcode2;
    String remarks;
    /**
     * current or present reading
     */
    int presentRDG;
    String range_code;
    String mr_type_code;
    int billed_cons;
    int csmb_orign_cons;
    int constype_code;
    String del_code;
    String delivery_date;
    String delivery_time;
    String delivery_remarks;
    int csmb_type_code;
    String csmb_parent;
    short mb_pref_flag;
    String bill_print_date;
    short mr_rep_flag;
    int rdg_tries;
    int sp_comp;
    double gps_latitude;
    double gps_longitude;
}
