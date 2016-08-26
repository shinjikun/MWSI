package com.indra.rover.mwsi.data.pojo;

/**
 * Created by Indra on 8/22/2016.
 */
public class MRU  extends Item{


    public MRU(String id,int unread,int unprinted,int undelivered,int total){
        this.id = id;
        this.unread = unread;
        this.unprinted =unprinted;
        this.undelivered = undelivered;
        this.total = total;
    }

    /**
     * MRU ID
     */
    String id;

    /**
     * total unread meter reading
     */
    int unread;
    /**
     * total count of all Meter Reading
     */
   int total;
    /**
     * total number of unprinted meter reader
     */
   int unprinted;
    /**
     * total number of undelivered meter reader
     */
    int undelivered;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getUnread() {
        return unread;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setUnprinted(int unprinted) {
        this.unprinted = unprinted;
    }

    public int getUnprinted() {
        return unprinted;
    }

    public void setUndelivered(int undelivered) {
        this.undelivered = undelivered;
    }

    public int getUndelivered() {
        return undelivered;
    }
}
