package com.indra.rover.mwsi.print.layout;

import android.content.Context;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;

/**
 * Created by Indra on 10/5/2016.
 */
public class HTMLayout  extends PrintLayout {

    public HTMLayout(Context context) {
        super(context);
    }

    @Override
    String headerConfig() {
        return "\n";
    }

    @Override
    String breadCrumbsHeader() {
        return "\n";
    }

    @Override
    String billHeader(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    String serviceInfo(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    String meterInfo(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    String paymentHistory(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    String billSummary(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    String billAdvisory(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    String billFooter(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    String billValidity(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    String breadCrumbsFooter() {
        return "\n";
    }

    @Override
    String testfont() {
        return null;
    }

    @Override
    String billDiscon(MeterPrint mtrPrint) {
        return "\n";
    }
}
