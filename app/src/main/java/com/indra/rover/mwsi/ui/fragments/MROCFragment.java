package com.indra.rover.mwsi.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.db.RefTableDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterOC;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.ObservationCode;
import com.indra.rover.mwsi.utils.MessageTransport;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MROCFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_ID = "id";

    View mView;
    private String crdocno;
    private RefTableDao refTableDao;
    Spinner spnOC1,spnOC2;
    MeterReadingDao mtrDao;
    MeterOC meterOC;
    public MROCFragment() {
    }

    public static MROCFragment newInstance(String mru_id ) {
        MROCFragment fragment = new MROCFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, mru_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refTableDao = new RefTableDao(getActivity());
        mtrDao = new MeterReadingDao(getActivity());
        if (getArguments() != null) {
            crdocno = getArguments().getString(ARG_ID);
            meterOC = mtrDao.getMeterOCs(crdocno);
        }

        MainApp.bus.register(this);
    }


    private void launchCamera(){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mroc, container, false);

        Button imgCapture = (Button)mView.findViewById(R.id.btnImageCapture);
        imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
        imgCapture = (Button) mView.findViewById(R.id.btnImageCapture1);
        imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        spnOC1 = (Spinner)mView.findViewById(R.id.spnOC1);
        spnOC2 = (Spinner)mView.findViewById(R.id.spnOC2);
        mView.findViewById(R.id.btnClrOc1).setOnClickListener(this);
        mView.findViewById(R.id.btnClrOc2).setOnClickListener(this);
        mView.findViewById(R.id.btnCancelOC).setOnClickListener(this);
        mView.findViewById(R.id.btnSaveOC).setOnClickListener(this);
        mView.findViewById(R.id.btnEditOC).setOnClickListener(this);
        initContent();
        setUp();
        return mView;
    }

    private void initContent(){
        List<ObservationCode> arrayList = refTableDao.getOCodes();
        // Spinner Drop down elements
        List<String> arryOC1 = new ArrayList<>();
        List<String> arryOC2 = new ArrayList<>();
        for(int i = 0;i<arrayList.size();i++){
            ObservationCode ocCode = arrayList.get(i);
            String value = ocCode.getFf_code() + " - "+ocCode.getFf_desc();
            if(ocCode.getBill_related() == 1){
                arryOC1.add(value);
            }
            else {
                arryOC2.add(value);
            }

        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arryOC1);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnOC1.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arryOC2);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnOC2.setAdapter(dataAdapter);
    }



    private void editMode(boolean isEditMode){
        int vis = View.VISIBLE;
        int vis2 = View.INVISIBLE;
        MainApp.isEditMode = isEditMode;
        if(!isEditMode){
            vis = View.INVISIBLE;
            vis2 = View.VISIBLE;
        }
        mView.findViewById(R.id.btnClrOc1).setVisibility(vis);
        mView.findViewById(R.id.btnClrOc2).setVisibility(vis);
        mView.findViewById(R.id.spnOC1).setVisibility(vis);
        mView.findViewById(R.id.spnOC2).setVisibility(vis);
        mView.findViewById(R.id.btnCancelOC).setVisibility(vis);
        mView.findViewById(R.id.btnSaveOC).setVisibility(vis);

        mView.findViewById(R.id.lblOC1).setVisibility(vis2);
        mView.findViewById(R.id.lblOC2).setVisibility(vis2);
        mView.findViewById(R.id.btnEditOC).setVisibility(vis2);
        String lbl = "Capture Image";
        if(!isEditMode){
            lbl= "View Image";
        }
        Button btn =  (Button)mView.findViewById(R.id.btnImageCapture);
        btn.setText(lbl);
        btn =  (Button)mView.findViewById(R.id.btnImageCapture1);
        btn.setText(lbl);
    }

    @Subscribe
    public void getMessage(MessageTransport msgTransport) {
        String action = msgTransport.getAction();
        if(action.equals("navigate")){
            String id = msgTransport.getMessage();
            meterOC = mtrDao.getMeterOCs(id);
            setUp();
        }
        else if(action.equals("readstat")){
            String readstat = msgTransport.getMessage();
            meterOC.setReadstat(readstat);
        }
    }

    private void setUp(){
        if(meterOC!=null){

        }

        editMode(false);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

            case R.id.btnCancelOC:
                editMode(false);
                break;
            case R.id.btnSaveOC:
                editMode(false);
                break;
            case R.id.btnEditOC:
                editMode(true);
                break;
            case R.id.btnClrOc1:
                break;
            case R.id.btnClrOc2:
                break;

        }
    }
}
