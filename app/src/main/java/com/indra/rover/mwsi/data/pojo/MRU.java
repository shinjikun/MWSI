package com.indra.rover.mwsi.data.pojo;

import android.database.Cursor;
import android.util.Log;

import java.util.Date;

/**
 * Created by Indra on 8/22/2016.
 */
public class MRU  extends Item{



    public MRU(){

    }



    public MRU(String id,int unread,int unprinted,int undelivered,int total){
        this.id = id;
        this.unread = unread;
        this.unprinted =unprinted;
        this.undelivered = undelivered;
        this.total = total;
    }




    public MRU(Cursor cursor){
        this.id =cursor.getString(cursor.getColumnIndexOrThrow("MRU"));
        Log.i("Test","im mru"+this.id);
        this.bc_code = cursor.getString(cursor.getColumnIndexOrThrow("BC_CODE"));
        this.reader_code = cursor.getString(cursor.getColumnIndexOrThrow("READER_CODE"));

        this.reader_name = cursor.getString(cursor.getColumnIndexOrThrow("READER_NAME"));
        Log.i("Test",this.reader_name);
        this.reading_date = cursor.getString(cursor.getColumnIndexOrThrow("SCHED_RDG_DATE"));
        this.due_date = cursor.getString(cursor.getColumnIndexOrThrow("DUE_DATE"));
        this.kam_mru = cursor.getShort(cursor.getColumnIndexOrThrow("KAM_MRU_FLAG"));
        this.max_seq_no = cursor.getString(cursor.getColumnIndexOrThrow("MAX_SEQNO"));
        this.customer_count = cursor.getInt(cursor.getColumnIndexOrThrow("CUST_COUNT"));
        this.active_count = cursor.getInt(cursor.getColumnIndexOrThrow("ACTIVE_COUNT"));
        this.blocked_count =cursor.getInt(cursor.getColumnIndexOrThrow("BLOCKED_COUNT"));
        this.read = cursor.getInt(cursor.getColumnIndexOrThrow("READ_METERS"));
        Log.i("Test","read"+this.read);
        this.unread = cursor.getInt(cursor.getColumnIndexOrThrow("UNREAD_METERS"));
        this.undelivered =cursor.getInt(cursor.getColumnIndexOrThrow("UNDELIV_BILLS"));
    }



    /**
     * MRU ID
     */
   private String id;


    /**
     * Business Center Code
     */
    private String bc_code;


    /**
     * Meter Reader's code or ID
     */
    private String reader_code;

    /**
     * Meter Reader's Name
     */
    private String reader_name;

    /**
     * scheduled start reading date
     */
    private String reading_date;
    /**
     * scheduled end reading date
     */
    private String due_date;

    /**
     * Key Account Management Flag
     */
    private short  kam_mru;

    /**
     * Max Sequence Number
     */
    private String max_seq_no;

    /**
     * total number of customer in MRU
     */
    private int customer_count;

    /**
     * total number of active customer
     */
    private int active_count;

    /**
     * total number of blocked account
     */
    private int blocked_count;

    /**
     * total number of read meters
     */
    private int read;

    /**
     * total unread meter reading
     */
    private  int unread;
    /**
     * total count of all Meter Reading
     */
   private int total;
    /**
     * total number of unprinted meter reader
     */
   private int unprinted;
    /**
     * total number of undelivered meter reader
     */
   private int undelivered;

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


    public void setActive_count(int active_count) {
        this.active_count = active_count;
    }

    public int getActive_count() {
        return active_count;
    }

    public void setBc_code(String bc_code) {
        this.bc_code = bc_code;
    }

    public String getBc_code() {
        return bc_code;
    }

    public void setBlocked_count(int blocked_count) {
        this.blocked_count = blocked_count;
    }

    public int getBlocked_count() {
        return blocked_count;
    }

    public void setCustomer_count(int customer_count) {
        this.customer_count = customer_count;
    }

    public int getCustomer_count() {
        return customer_count;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public short getKam_mru() {
        return kam_mru;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public void setKam_mru(short kam_mru) {
        this.kam_mru = kam_mru;
    }

    public void setMax_seq_no(String max_seq_no) {
        this.max_seq_no = max_seq_no;
    }

    public String getMax_seq_no() {
        return max_seq_no;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setReader_code(String reader_code) {
        this.reader_code = reader_code;
    }

    public String getReader_code() {
        return reader_code;
    }

    public void setReader_name(String reader_name) {
        this.reader_name = reader_name;
    }

    public String getReader_name() {
        return reader_name;
    }

    public String getReading_date() {
        return reading_date;
    }

    public void setReading_date(String reading_date) {
        this.reading_date = reading_date;
    }
}
