package com.indra.rover.mwsi.compute;

/**
 * MWSI Bill Computation Module
 * Created by Indra on 9/22/2016.
 */
public interface BillComputation {

    /**
     * compute basic charge amount based on tariff schedule
     * @param consumption
     * @param rate_type
     * @return
     */
     double getBasicCharge(long consumption,char rate_type);

    /**
     *  Special computation  for basic charge amount for BP> 34 days
     * @param l1
     * @param l2
     * @param rate_type rate type
     * @return computed basic charge
     */
     double getBulkBasicCharge(long l1,long l2 ,char rate_type);

    /**
     *  Total Basic Charge for Bulk Account
     * @param l
     * @param c1
     * @param c2
     * @return
     */
     double getGT3BasicCharge(long l,char c1, char c2);

    /**
     *  Total Basic Charge for HR/LT Accounts
     * @param l
     * @return
     */
     double getHRLBasicCharge(long l);

    /**
     *   Get Basic Charge based on OC only entry
     * @param l
     * @param oc oc code
     * @return
     */
     double getOCBasicCharge(long l,char oc);









}
