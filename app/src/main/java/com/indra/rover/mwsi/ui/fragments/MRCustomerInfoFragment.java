package com.indra.rover.mwsi.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.pojo.T_Download_Info;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.PreviousData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MRCustomerInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MRCustomerInfoFragment extends Fragment {
    private static final String DLDCONO = "id";
    T_Download_Info download_info;
    private View mLayout;

    public MRCustomerInfoFragment() {
        // Required empty public constructor
    }


    public static MRCustomerInfoFragment newInstance(T_Download_Info download_info) {
        MRCustomerInfoFragment fragment = new MRCustomerInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(DLDCONO,download_info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            download_info = (T_Download_Info)getArguments().getSerializable(DLDCONO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_mrcustomer_info, container, false);
        setUp();
        return mLayout;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setUp(){
        PreviousData previousData = download_info.getPreviousData();
        TextView txt = (TextView)mLayout.findViewById(R.id.txtPReadingDate);
        txt.setText(previousData.getPrevRDGDate());
        txt = (TextView)mLayout.findViewById(R.id.txtPrevRemarks);
        txt.setText(previousData.getPrevRemarks());
        txt = (TextView)mLayout.findViewById(R.id.txtPReading);
        txt.setText(String.valueOf(previousData.getActPrevReading()));
        txt = (TextView)mLayout.findViewById(R.id.txtMRCOC1);
        txt.setText(String.valueOf(previousData.getPrevFF1()));
        txt = (TextView)mLayout.findViewById(R.id.txtMRCOC2);
        txt.setText(String.valueOf(previousData.getPrevFF2()));
    }
}
