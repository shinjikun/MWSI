package com.indra.rover.mwsi.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.indra.rover.mwsi.R;

public class MROCFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";

    View mView;
    // TODO: Rename and change types of parameters
    private String mParamID;


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
        return mView;
    }

}
