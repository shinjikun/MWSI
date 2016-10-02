package com.indra.rover.mwsi.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.db.RefTableDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterDelivery;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.DeliveryCode;
import com.indra.rover.mwsi.ui.activities.SignatureActivity;
import com.indra.rover.mwsi.ui.widgets.CustomSpinView;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.MessageTransport;
import com.indra.rover.mwsi.utils.Utils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MRDeliveryRFragment extends Fragment  implements View.OnClickListener,DialogUtils.DialogListener, AdapterView.OnItemSelectedListener{
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
    CustomSpinView deliv_opt;
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
         deliv_opt =  (CustomSpinView)mView.findViewById(R.id.deliv_opt);
         mView.findViewById(R.id.btnEditMRDelivery).setOnClickListener(this);
         mView.findViewById(R.id.btnSaveDeliv).setOnClickListener(this);
         mView.findViewById(R.id.btnCancelMRDelivery).setOnClickListener(this);
         mView.findViewById(R.id.btnSignature).setOnClickListener(this);
         deliv_opt.getBtnClr().setOnClickListener(this);
         deliv_opt.getSpn().setOnItemSelectedListener(this);
        initContent();
        setUp();
        return mView;
    }




    private void initContent(){
        arrayList = refTableDao.getDeliveryCodes();
        // Spinner Drop down elements
        List<String> arryDevCodes = new ArrayList<>();
        arryDevCodes.add(" ");
        for(int i = 0;i<arrayList.size();i++){
            DeliveryCode deliveryCode = arrayList.get(i);
            arryDevCodes.add(deliveryCode.getDel_code()+" - "+deliveryCode.getDel_desc());

        }
       deliv_opt.setOptValues(getActivity(),arryDevCodes);
    }


    private void editMode(boolean isEditMode){
        int vis = View.VISIBLE;
        int vis2 = View.INVISIBLE;
        MainApp.isEditMode = isEditMode;
        Button btn =  (Button)mView.findViewById(R.id.btnSignature);

        if(!isEditMode){
            vis = View.INVISIBLE;
            vis2 = View.VISIBLE;
            btn.setText("View Signature");
        }
        else {
            btn.setText("Ask For Signature");
        }

            mView.findViewById(R.id.btnSaveDeliv).setVisibility(vis);
            mView.findViewById(R.id.btnCancelMRDelivery).setVisibility(vis);
            mView.findViewById(R.id.txtMRDRemarks).setEnabled(isEditMode);
            mView.findViewById(R.id.btnEditMRDelivery).setVisibility(vis2);
            deliv_opt.editMode(isEditMode);


    }

    private void setUp(){
        if(meterDelivery!=null){
            String remarks = meterDelivery.getDev_remarks();
            EditText txt = (EditText) mView.findViewById(R.id.txtMRDRemarks);
            txt.setText(remarks);
            String deliv_code = meterDelivery.getDev_code();
            int index = getPosition(deliv_code);
            deliv_opt.setSelection(index);
            String str =   deliv_opt.getSelectedItem();
            deliv_opt.setValues(str);

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
            case R.id.btnClrOpt:
                deliv_opt.setSelection(0);
                break;
            case R.id.btnSignature:
                loadSignature();
                break;
        }

    }


    private void savetoDB(){
        EditText txt = (EditText) mView.findViewById(R.id.txtMRDRemarks);
        String remarks = String.valueOf(txt.getText());
        if(!Utils.isNotEmpty(remarks)){
            remarks="";
        }

        int index = deliv_opt.getSelectedItemPosition();
        String deliv_code ="";

        if(index !=0){
         deliv_code =   arrayList.get(index-1).getDel_code();
        }
        String spncode = deliv_opt.getSelectedItem();
        deliv_opt.setValues(spncode);
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
                pos = i+1;
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


    private void loadSignature(){
        Intent intent = new Intent(getActivity(), SignatureActivity.class);
        intent.putExtra("id",crdocno);
        intent.putExtra("editMode",true);
        getActivity().startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        if(position !=0){
         int isSignRequired    =   arrayList.get(position-1).getDel_signreq_flag();
            if(isSignRequired ==1 ){
                mView.findViewById(R.id.btnSignature).setVisibility(View.VISIBLE);
            }
            else  {
                mView.findViewById(R.id.btnSignature).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
