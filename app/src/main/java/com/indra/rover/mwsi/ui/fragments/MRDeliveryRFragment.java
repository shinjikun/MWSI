package com.indra.rover.mwsi.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indra.rover.mwsi.R;

public class MRDeliveryRFragment extends Fragment {
    private static final String ARG_ID = "id";

    private String mParamID;


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
            mParamID = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mrdelivery, container, false);
    }


}
