package com.indra.rover.mwsi.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.DeliveryDao;
import com.indra.rover.mwsi.data.pojo.DeliveryCode;
import com.indra.rover.mwsi.data.pojo.ObservationCode;

import java.util.ArrayList;
import java.util.List;

public class MROCFragment extends Fragment {
    private static final String ARG_ID = "id";

    View mView;
    private String mParamID;
    private DeliveryDao deliveryDao;
    Spinner spnOC1,spnOC2;
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
        if (getArguments() != null) {
            mParamID = getArguments().getString(ARG_ID);
        }
        deliveryDao = new DeliveryDao(getActivity());
    }


    private void launchCamera(){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mroc, container, false);

        ImageButton imgCapture = (ImageButton)mView.findViewById(R.id.btnImageCapture);
        imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
        imgCapture = (ImageButton)mView.findViewById(R.id.btnImageCapture1);
        imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        spnOC1 = (Spinner)mView.findViewById(R.id.spnOC1);
        spnOC2 = (Spinner)mView.findViewById(R.id.spnOC2);
        initContent();
        return mView;
    }

    private void initContent(){
        List<ObservationCode> arrayList = deliveryDao.getOCodes();
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



}
