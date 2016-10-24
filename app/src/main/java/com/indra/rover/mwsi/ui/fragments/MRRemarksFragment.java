package com.indra.rover.mwsi.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterRemarks;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.MessageTransport;
import com.squareup.otto.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class MRRemarksFragment extends Fragment  implements View.OnClickListener{


    private static final String IDPARAM = "id";
    View mView;
    MeterReadingDao meterReadingDao;
    MeterRemarks meterRemarks;
    DialogUtils  dialogUtils;
    public MRRemarksFragment() {
    }


    public static MRRemarksFragment newInstance(String crdocno){
        MRRemarksFragment fragment = new MRRemarksFragment();
        Bundle args = new Bundle();
        args.putString(IDPARAM, crdocno);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meterReadingDao = new MeterReadingDao(getActivity());
        dialogUtils = new DialogUtils(getActivity());
        if (getArguments() != null) {
            String   mParamID = getArguments().getString(IDPARAM);
             meterRemarks = meterReadingDao.getRemarks(mParamID);
        }


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
        MainApp.bus.register(this);
        setUp();
        return mView;
    }


    private void setUp(){
        if(meterRemarks!=null){
            EditText editText=  (EditText) mView.findViewById(R.id.txtMRCDesc);
            editText.setText(meterRemarks.getRemarks());
            TextView txt =   (TextView) mView.findViewById(R.id.lblMRCDesc);
            txt.setText(meterRemarks.getRemarks());
        }
        setEditMode(false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
       switch (id){
           case R.id.btnDelMRDesc:
               clearRemarks();
               break;
           case R.id.btnOKMRDesc:
               editRemarks();
               break;
           case R.id.btnCancelMRDesc:
               cancelRemarks();
               break;
           case R.id.btnEditMRRemarks:
               String readstat = meterRemarks.getReadstat();
               if(readstat.equals("P")||readstat.equals("Q")){
                   setEditMode(true);
               }
               else {
                   dialogUtils.showOKDialog(2,"No Remarks Entry","Cannot " +
                           "Enter a Remark for an Unprinted Bill!",new Bundle());
               }

               break;
       }

    }

    private void cancelRemarks(){
        TextView txt =   (TextView) mView.findViewById(R.id.lblMRCDesc);
        String remarks = String.valueOf(txt.getText());
        EditText editText=  (EditText) mView.findViewById(R.id.txtMRCDesc);
        editText.setText(remarks);
        setEditMode(false);
    }

    private void editRemarks(){
        EditText editText=  (EditText) mView.findViewById(R.id.txtMRCDesc);
        String remarks = String.valueOf(editText.getText());
        editText.setText(remarks);
        TextView txt =   (TextView) mView.findViewById(R.id.lblMRCDesc);
        txt.setText(remarks);
        if(meterRemarks!=null){
            meterReadingDao.addRemarks(remarks,meterRemarks.getCrdodcno());
            meterRemarks.setRemarks(remarks);
        }
        setEditMode(false);
    }

    private void clearRemarks(){
        if(meterRemarks!=null){
            meterReadingDao.addRemarks("",meterRemarks.getCrdodcno());
        }
        EditText editText=  (EditText) mView.findViewById(R.id.txtMRCDesc);
        editText.setText("");
        TextView txt =   (TextView) mView.findViewById(R.id.lblMRCDesc);
        txt.setText("");
    }




    private void setEditMode(boolean isEditable){
        MainApp.isEditMode = isEditable;
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


    @Subscribe
    public void getMessage(MessageTransport msgTransport) {
        String action = msgTransport.getAction();
        if(action.equals("navigate")){
            String id = msgTransport.getMessage();
            meterRemarks = meterReadingDao.getRemarks(id);
            setUp();
        }
        else if(action.equals("readstat")){
            String readstat = msgTransport.getMessage();
            meterRemarks.setReadstat(readstat);
        }
    }

}
