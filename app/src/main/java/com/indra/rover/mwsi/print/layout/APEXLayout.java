package com.indra.rover.mwsi.print.layout;

import android.content.Context;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;

import java.util.ArrayList;

/**
 * Created by Indra on 10/5/2016.
 */
public class APEXLayout  extends   PrintLayout {

    public APEXLayout(Context context) {
        super(context);
    }

    @Override
    String eodReport(ArrayList<MeterPrint> mtrPrints) {
        return null;
    }

    @Override
    String mrStub(MeterPrint meterPrint) {
        return null;
    }

    @Override
    String headerConfig() {
        return "";
    }

    @Override
    String breadCrumbsHeader() {
        return "";
    }

    @Override
    String billHeader(MeterPrint mtrPrint) {
        return "";
    }

    @Override
    String serviceInfo(MeterPrint mtrPrint) {
        return "";
    }

    @Override
    String meterInfo(MeterPrint mtrPrint) {
        return "";
    }

    @Override
    String paymentHistory(MeterPrint mtrPrint) {
        return "";
    }

    @Override
    String billSummary(MeterPrint mtrPrint) {
        return "";
    }

    @Override
    String billAdvisory(MeterPrint mtrPrint,String range) {
        return "";
    }

    @Override
    String billFooter(MeterPrint mtrPrint) {
        return "";
    }

    @Override
    String billValidity(MeterPrint mtrPrint) {
        return null;
    }

    @Override
    String breadCrumbsFooter() {
        return "";
    }

    @Override
    String testfont() {
        return null;
    }

    @Override
    String billDiscon(MeterPrint mtrPrint) {
        return "";
    }


}
