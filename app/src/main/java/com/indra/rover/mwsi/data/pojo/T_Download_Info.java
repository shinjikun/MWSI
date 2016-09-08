package com.indra.rover.mwsi.data.pojo;

import com.indra.rover.mwsi.data.pojo.meter_reading.misc.InstallMisc;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.PreviousData;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.ProRate;

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


    CustomerInfo customer;
    /**
     * Bill class
     */
    String bill_class;
    String rate_type;
    String bulk_flag;
    int acct_status;
    String meter_number;
    String mterial_number;
    int meter_size;
    int num_dials;
    String grp_flag;
    String block_tag;
    String disc_tag;
    String prevRDGdate;
    int actprevRDG;
    short pracflag;
    short pconsavgflag;
    int ave_consumpstions;
    int dpreplmtr_code;

    ProRate prorates;
    PreviousData prevReading;
    String wbpaydtls1;
    String wbpaydtls2;
    String miscpaydtls;
    String gdpaydtls;
    InstallMisc installMisc;
    String tin;
    short vat_exempt;
    short djscheck_flg;
    int numuser;


    int nmintRDG;

    int nmConsfactor;
    int gt34flag;
    int gt34factor;



    String soa_number;
    int spbill_rule;
    String spbill_eff_date;
    String dldocno;

    public CustomerInfo getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerInfo customer) {
        this.customer = customer;
    }

    public void setAcct_status(int acct_status) {
        this.acct_status = acct_status;
    }


    public void setMeter_number(String meter_number) {
        this.meter_number = meter_number;
    }

    public void setMru_id(String mru_id) {
        this.mru_id = mru_id;
    }

    public void setSeq_number(String seq_number) {
        this.seq_number = seq_number;
    }

    public void setBill_class(String bill_class) {
        this.bill_class = bill_class;
    }

    public void setRate_type(String rate_type) {
        this.rate_type = rate_type;
    }

    public void setBulk_flag(String bulk_flag) {
        this.bulk_flag = bulk_flag;
    }

    public void setMeter_size(int meter_size) {
        this.meter_size = meter_size;
    }

    public void setDldocno(String dldocno) {
        this.dldocno = dldocno;
    }

    public void setMterial_number(String mterial_number) {
        this.mterial_number = mterial_number;
    }

    public void setNum_dials(int num_dials) {
        this.num_dials = num_dials;
    }




    public void setGrp_flag(String grp_flag) {
        this.grp_flag = grp_flag;
    }

    public void setBlock_tag(String block_tag) {
        this.block_tag = block_tag;
    }

    public void setDisc_tag(String disc_tag) {
        this.disc_tag = disc_tag;
    }

    public void setPrevRDGdate(String prevRDGdate) {
        this.prevRDGdate = prevRDGdate;
    }
    public void setActprevRDG(int actprevRDG) {
        this.actprevRDG = actprevRDG;
    }



    public void setPracflag(short pracflag) {
        this.pracflag = pracflag;
    }

    public void setPconsavgflag(short pconsavgflag) {
        this.pconsavgflag = pconsavgflag;
    }

    public void setAve_consumpstions(int ave_consumpstions) {
        this.ave_consumpstions = ave_consumpstions;
    }

    public void setDpreplmtr_code(int dpreplmtr_code) {
        this.dpreplmtr_code = dpreplmtr_code;
    }

    public void setProrates(ProRate prorates) {
        this.prorates = prorates;
    }

    public ProRate getProrates() {
        return prorates;
    }

    public void setInstallMisc(InstallMisc installMisc) {
        this.installMisc = installMisc;
    }

    public InstallMisc getInstallMisc() {
        return installMisc;
    }

    public void setPrevReading(PreviousData prevReading) {
        this.prevReading = prevReading;
    }

    public PreviousData getPrevReading() {
        return prevReading;
    }


    public void setNmintRDG(int nmintRDG) {
        this.nmintRDG = nmintRDG;
    }



    public void setNmConsfactor(int nmConsfactor) {
        this.nmConsfactor = nmConsfactor;
    }

    public void setGt34factor(int gt34factor) {
        this.gt34factor = gt34factor;
    }

    public void setGt34flag(int gt34flag) {
        this.gt34flag = gt34flag;
    }
}
