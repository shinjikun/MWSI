package com.indra.rover.mwsi.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.RefTableDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.DeliveryCode;

import java.util.ArrayList;
import java.util.List;

public class MRDeliveryRFragment extends Fragment {
    private static final String ARG_ID = "id";


    private RefTableDao refTableDao;
    View mView;
    public MRDeliveryRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mru_id Parameter 1.
     * @return A new instance of fragment MRDeliveryRFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MRDeliveryRFragment newInstance(String mru_id) {
        MRDeliveryRFragment fragment = new MRDeliveryRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, mru_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
         String   mParamID = getArguments().getString(ARG_ID);
        }
        refTableDao = new RefTableDao(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mView = inflater.inflate(R.layout.fragment_mrdelivery, container, false);


        initContent();
        return mView;
    }



    private void initContent(){
        Spinner spinDelivery =  (Spinner)mView.findViewById(R.id.spnDevCode);
        List<DeliveryCode> arrayList = refTableDao.getDeliveryCodes();
        // Spinner Drop down elements
        List<String> arryDevCodes = new ArrayList<>();
        for(int i = 0;i<arrayList.size();i++){
            DeliveryCode deliveryCode = arrayList.get(i);
            arryDevCodes.add(deliveryCode.getDel_code()+" - "+deliveryCode.getDel_desc());

        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arryDevCodes);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinDelivery.setAdapter(dataAdapter);
    }



}
