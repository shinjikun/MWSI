package com.indra.rover.mwsi.data.pojo.meter_reading.misc;

import java.io.Serializable;

/**
 * Created by Indra on 9/8/2016.
 */
public class ProRate implements Serializable{

    short tarif_prorate;
    short fcda_prorate;
    short cera_prorate;
    short env_prorate;
    short sew_proate;

    public ProRate(){

    }

    public ProRate(String records[]){
        tarif_prorate = Short.parseShort(records[32]);
        fcda_prorate = Short.parseShort(records[33]);
        cera_prorate = Short.parseShort(records[34]);
        env_prorate = Short.parseShort(records[35]);
        sew_proate = Short.parseShort(records[36]);
    }

    public void setCera_prorate(short cera_prorate) {
        this.cera_prorate = cera_prorate;
    }

    public void setEnv_prorate(short env_prorate) {
        this.env_prorate = env_prorate;
    }

    public void setFcda_prorate(short fcda_prorate) {
        this.fcda_prorate = fcda_prorate;
    }

    public void setSew_proate(short sew_proate) {
        this.sew_proate = sew_proate;
    }

    public void setTarif_prorate(short tarif_prorate) {
        this.tarif_prorate = tarif_prorate;
    }

    public short getCera_prorate() {
        return cera_prorate;
    }

    public short getEnv_prorate() {
        return env_prorate;
    }

    public short getFcda_prorate() {
        return fcda_prorate;
    }

    public short getSew_proate() {
        return sew_proate;
    }

    public short getTarif_prorate() {
        return tarif_prorate;
    }
}
