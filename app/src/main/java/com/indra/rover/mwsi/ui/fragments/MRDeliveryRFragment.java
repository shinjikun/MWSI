package com.indra.rover.mwsi.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.db.RefTableDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterDelivery;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.DeliveryCode;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.MessageTransport;
import com.indra.rover.mwsi.utils.Utils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MRDeliveryRFragment extends Fragment  implements View.OnClickListener,DialogUtils.DialogListener{
    /**
     * id paramater
     */
    private static final String ARG_ID = "id";

    /**
     * helper class to query delivery description on the database
     */
    private RefTableDao refTableDao;
    private MeterReadingDao mtrReadingDao;
    View mView;
    MeterDelivery meterDelivery;
    List<DeliveryCode> arrayList;
    Spinner spinDelivery;
    /**
     * id of selected record
     */
    String crdocno;
    DialogUtils dialogUtils;
    public MRDeliveryRFragment() {
        // Required empty public constructor
    }

    public static MRDeliveryRFragment newInstance(String crdocno) {
        MRDeliveryRFragment fragment = new MRDeliveryRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, crdocno);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refTableDao = new RefTableDao(getActivity());
        mtrReadingDao = new MeterReadingDao(getActivity());
        if (getArguments() != null) {
            crdocno= getArguments().getString(ARG_ID);
            meterDelivery = mtrReadingDao.getDeliveryStat(crdocno);
        }
        dialogUtils = new DialogUtils(getActivity());
        dialogUtils.setListener(this);
        MainApp.bus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mView = inflater.inflate(R.layout.fragment_mrdelivery, container, false);
         mView.findViewById(R.id.btnEditMRDelivery).setOnClickListener(this);
         mView.findViewById(R.id.btnSaveDeliv).setOnClickListener(this);
         mView.findViewById(R.id.btnCancelMRDelivery).setOnClickListener(this);
        mView.findViewById(R.id.btnClrDeliv).setOnClickListener(this);
        initContent();
        setUp();
        return mView;
    }




    private void initContent(){
        spinDelivery =  (Spinner)mView.findViewById(R.id.spnDevCode);
        arrayList = refTableDao.getDeliveryCodes();
        // Spinner Drop down elements
        List<String> arryDevCodes = new ArrayList<>();
        arryDevCodes.add(" ");
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


    private void editMode(boolean isEditMode){
        int vis = View.VISIBLE;
        int vis2 = View.INVISIBLE;
        MainApp.isEditMode = isEditMode;
        if(!isEditMode){
            vis = View.INVISIBLE;
            vis2 = View.VISIBLE;
        }

            mView.findViewById(R.id.btnSaveDeliv).setVisibility(vis);
            mView.findViewById(R.id.btnCancelMRDelivery).setVisibility(vis);
            mView.findViewById(R.id.spnDevCode).setVisibility(vis);
            mView.findViewById(R.id.btnClrDeliv).setVisibility(vis);
            mView.findViewById(R.id.txtMRDRemarks).setVisibility(vis);

            mView.findViewById(R.id.btnEditMRDelivery).setVisibility(vis2);
            mView.findViewById(R.id.lblDevCode).setVisibility(vis2);
            mView.findViewById(R.id.lblMRDRemarks).setVisibility(vis2);

    }

    private void setUp(){
        if(meterDelivery!=null){
            String remarks = meterDelivery.getDev_remarks();
            EditText txt = (EditText) mView.findViewById(R.id.txtMRDRemarks);
            txt.setText(remarks);
            String deliv_code = meterDelivery.getDev_code();
            int index = getPosition(deliv_code);
            spinDelivery.setSelection(index);
            String str =   spinDelivery.getSelectedItem().toString();
            TextView txtView =  (TextView)mView.findViewById(R.id.lblDevCode);
            txtView.setText(str);
            txtView =  (TextView)mView.findViewById(R.id.lblMRDRemarks);
            txtView.setText(remarks);
        }
        editMode(false);
    }


    @Override
    public void onClick(View view) {
         int id = view.getId();
        switch (id){
            case R.id.btnEditMRDelivery:
                String readstat = meterDelivery.getReadstat();
                if(readstat.equals("P")||readstat.equals("Q")){
                    editMode(true);
                }
                else {
                    dialogUtils.showOKDialog(2,"No Delivery Remarks Entry","Cannot Enter a Delivery Remark " +
                            "for an Unprinted Bill!",new Bundle());
                }
                break;
            case R.id.btnCancelMRDelivery:
                editMode(false);
                break;
            case R.id.btnSaveDeliv:
                savetoDB();
                break;
            case R.id.btnClrDeliv:
                spinDelivery.setSelection(0);
                break;
        }

    }


    private void savetoDB(){
        EditText txt = (EditText) mView.findViewById(R.id.txtMRDRemarks);
        String remarks = String.valueOf(txt.getText());
        if(!Utils.isNotEmpty(remarks)){
            remarks="";
        }

        int index = spinDelivery.getSelectedItemPosition();
        String deliv_code ="";

        if(index !=0){
         deliv_code =   arrayList.get(index).getDel_code();
        }
        TextView lbl = (TextView)mView.findViewById(R.id.lblMRDRemarks);
        lbl.setText(remarks);
        String spncode = spinDelivery.getSelectedItem().toString();
        lbl = (TextView)mView.findViewById(R.id.lblDevCode);
        lbl.setText(spncode);
        boolean isNewDelivery = true;
        if(Utils.isNotEmpty(meterDelivery.getDeliv_date())&&Utils.isNotEmpty(meterDelivery.getDeliv_time())){
            isNewDelivery = false;
        }
        mtrReadingDao.addDelivRemarks(deliv_code,remarks,crdocno, isNewDelivery);
        editMode(false);
    }

    @Subscribe
    public void getMessage(MessageTransport msgTransport) {
        String action = msgTransport.getAction();
        if(action.equals("navigate")){
           crdocno = msgTransport.getMessage();
            meterDelivery = mtrReadingDao.getDeliveryStat(crdocno);
            setUp();
        }
        else if(action.equals("readstat")){
            String readstat = msgTransport.getMessage();
            meterDelivery.setReadstat(readstat);
        }
    }

    private int getPosition(String deliv_code) {
        int size = arrayList.size();
        int pos =0;
        for(int i=0;i<size;i++){
            DeliveryCode dl = arrayList.get(i);
            if(dl.getDel_code().equals(deliv_code)){
                pos = i;
            }
        }
        return pos;
    }

    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {

    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }
}
