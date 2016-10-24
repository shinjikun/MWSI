package com.indra.rover.mwsi.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.db.RefTableDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterRHistory;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.ObservationCode;
import com.indra.rover.mwsi.utils.MessageTransport;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MRCustomerInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MRCustomerInfoFragment extends Fragment {
    private static final String IDPARAM = "id";
    String dldocno;
    private View mLayout;
    MeterRHistory meterRHistory;
    MeterReadingDao meterReadingDao;

    List<ObservationCode> arryOCs;
    public MRCustomerInfoFragment() {
        // Required empty public constructor
    }


    public static MRCustomerInfoFragment newInstance(String  dldcono) {
        MRCustomerInfoFragment fragment = new MRCustomerInfoFragment();
        Bundle args = new Bundle();
        args.putString(IDPARAM,dldcono);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dldocno = getArguments().getString(IDPARAM);
             meterReadingDao = new MeterReadingDao(getActivity());
            meterRHistory = meterReadingDao.fetchConHistory(dldocno);
            RefTableDao refDao = new RefTableDao(getActivity());
            arryOCs = refDao.getOCodes();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_mrcustomer_info, container, false);
        MainApp.bus.register(this);
        setUp();
        return mLayout;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainApp.bus.unregister(this);
    }

    private void setUp(){

        TextView txt = (TextView)mLayout.findViewById(R.id.txtPReadingDate);
        txt.setText(meterRHistory.getPrevRDGDate());
        txt = (TextView)mLayout.findViewById(R.id.txtPrevRemarks);
        txt.setText(meterRHistory.getPrevRemarks());
        txt = (TextView)mLayout.findViewById(R.id.txtPReading);
        txt.setText(String.valueOf(meterRHistory.getActPrevReading()));
        txt = (TextView)mLayout.findViewById(R.id.txtMRCOC1);
        String ocCode = meterRHistory.getPrevFF1();

        txt.setText(meterRHistory.getPrevFF1()+" - "+findOCdesc(ocCode));
        txt = (TextView)mLayout.findViewById(R.id.txtMRCOC2);
        ocCode = meterRHistory.getPrevFF2();
        txt.setText(meterRHistory.getPrevFF2()+" - "+findOCdesc(ocCode));

    }


    @Subscribe
    public void getMessage(MessageTransport msgTransport) {

        String action = msgTransport.getAction();
        if(action.equals("navigate")){
            String id = msgTransport.getMessage();
             meterRHistory =meterReadingDao.fetchConHistory(id);
             setUp();
        }


    }


    private String findOCdesc(String ocCode){
        int size =  arryOCs.size();
        String str="";
        for(int i=0;i<size;i++){
            String str2 = arryOCs.get(i).getFf_code();
            if(ocCode.equals(str2)){
                return  arryOCs.get(i).getFf_desc();

            }

        }
    return str;
    }


}
