package com.indra.rover.mwsi.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indra.rover.mwsi.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MRCustomerInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MRCustomerInfoFragment extends Fragment {
    private static final String ARG_ID = "id";

    private String mParamID;


    public MRCustomerInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MRCustomerInfoFragment.
     */
    public static MRCustomerInfoFragment newInstance(String param1) {
        MRCustomerInfoFragment fragment = new MRCustomerInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamID = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mrcustomer_info, container, false);
    }

}
