package com.indra.rover.mwsi.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indra.rover.mwsi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MRRemarksFragment extends Fragment  implements View.OnClickListener{


    View mView;
    public MRRemarksFragment() {
    }

    private enum visibility{

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mrremarks, container, false);
        // Inflate the layout for this fragment
        mView.findViewById(R.id.btnDelMRDesc).setOnClickListener(this);
        mView.findViewById(R.id.btnOKMRDesc).setOnClickListener(this);
        mView.findViewById(R.id.btnCancelMRDesc).setOnClickListener(this);
        mView.findViewById(R.id.btnEditMRRemarks).setOnClickListener(this);
        setEditMode(false);
        return mView;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
       switch (id){
           case R.id.btnDelMRDesc:
               break;

           case R.id.btnOKMRDesc:
               setEditMode(false);
               break;
           case R.id.btnCancelMRDesc:
               setEditMode(false);
               break;

           case R.id.btnEditMRRemarks:
               setEditMode(true);
               break;
       }

    }


    private void setEditMode(boolean isEditable){
       if(isEditable){
           mView.findViewById(R.id.btnDelMRDesc).setVisibility(View.VISIBLE);
           mView.findViewById(R.id.btnOKMRDesc).setVisibility(View.VISIBLE);
           mView.findViewById(R.id.btnCancelMRDesc).setVisibility(View.VISIBLE);
           mView.findViewById(R.id.txtMRCDesc).setVisibility(View.VISIBLE);
           mView.findViewById(R.id.btnEditMRRemarks).setVisibility(View.GONE);
           mView.findViewById(R.id.lblMRCDesc).setVisibility(View.GONE);

       }
        else {
           mView.findViewById(R.id.btnDelMRDesc).setVisibility(View.GONE);
           mView.findViewById(R.id.btnOKMRDesc).setVisibility(View.GONE);
           mView.findViewById(R.id.btnCancelMRDesc).setVisibility(View.GONE);
           mView.findViewById(R.id.txtMRCDesc).setVisibility(View.GONE);
           mView.findViewById(R.id.btnEditMRRemarks).setVisibility(View.VISIBLE);
           mView.findViewById(R.id.lblMRCDesc).setVisibility(View.VISIBLE);
       }

    }
}
