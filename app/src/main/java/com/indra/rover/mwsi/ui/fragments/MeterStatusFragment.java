package com.indra.rover.mwsi.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MRUDao;
import com.indra.rover.mwsi.data.pojo.MRU;

import com.indra.rover.mwsi.ui.view.CustomItemView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeterStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MeterStatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param";
    private static final String ARG_IDPARAM = "id";
    /**
     * status screen type
     */
    private int mParam;

    private String mruID;
    private LinearLayout mLayout;
    MRUDao mruDao;
    MRU selectedMRU;



    public static MeterStatusFragment newInstance(int param, String mruID) {
        MeterStatusFragment fragment = new MeterStatusFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param);
        args.putString(ARG_IDPARAM,mruID);
        fragment.setArguments(args);

        return fragment;
    }
    public MeterStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mruDao = new MRUDao(getActivity());
        if (getArguments() != null) {
            mParam = getArguments().getInt(ARG_PARAM1);
            mruID = getArguments().getString(ARG_IDPARAM);
            selectedMRU = mruDao.getMRU(mruID);
        }
        MainApp.bus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meter_status, container, false);
        // Inflate the layout for this fragment
         mLayout= (LinearLayout) view.findViewById(R.id.pnl_status);

        setup();

        return  view;
    }


    private void setup(){
        switch(mParam){
           case 1 :
               meterScreen();
                break;
            case 2 :
               deliveryScreen();
                break;
        }

    }


    private void meterScreen(){
        //start with a clean slate( no views added to  this layout
        mLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        CustomItemView item = new CustomItemView(getActivity());
        item.setLabel("Total Accounts :");
        String totalAcount = String.valueOf(selectedMRU.getCustomer_count());
        item.setValue(totalAcount);
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("Found Connected :");
        String foundConnect = String.valueOf(selectedMRU.getActive_count());
        item.setValue(foundConnect);
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);


        item = new CustomItemView(getActivity());
        item.setLabel("Unread Meter :");
        String str = String.valueOf(selectedMRU.getUnread());
        item.setValue(str);
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("Read Meter :");
         str = String.valueOf(selectedMRU.getRead());
        item.setValue(str);
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("Zero Consumption :");
        item.setValue("0");
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("Out of Range :");
        item.setValue("0");
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("With OC :");
        item.setValue("0");
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);
    }

    private void deliveryScreen(){
        //start with a clean slate( no views added to  this layout
        mLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        CustomItemView item = new CustomItemView(getActivity());
        item.setLabel("Total Accounts :");
        String totalAcount = String.valueOf(selectedMRU.getCustomer_count());
        item.setValue(totalAcount);
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);


        item = new CustomItemView(getActivity());
        item.setLabel("Printable :");
        String totalPrintable = String.valueOf(selectedMRU.getCustomer_count());
        item.setValue(totalPrintable);
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);


        item = new CustomItemView(getActivity());
        item.setLabel("Billed :");
        item.setValue("0");
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("UnBilled :");
        item.setValue("0");
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("Delivered :");
        item.setValue("0");
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("UnDelivered :");
        item.setValue("0");
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);
    }



    @Subscribe
    public void getMessage(String mruID) {
            selectedMRU = mruDao.getMRUStats(mruID);

            setup();

    }


    @Override
    public void onDetach() {
        super.onDetach();
        MainApp.bus.unregister(this);

    }
}
