package com.indra.rover.mwsi.data.pojo;

/**
 * Class used to REFER the PERSON who uses this app ..albeit MWSI personnel
 * Created by Indra on 8/26/2016.
 */
public class Meter_Reader  {

    private String id;
    private String name;
    private String assignCode;


    public Meter_Reader(String id, String name, String assignCode){
        this.id = id;
        this.name = name;
        this.assignCode =  assignCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAssignCode(String assignCode) {
        this.assignCode = assignCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignCode() {
        return assignCode;
    }

    public String getName() {
        return name;
    }
}
