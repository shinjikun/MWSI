package com.indra.rover.mwsi.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MRUDao;
import com.indra.rover.mwsi.data.pojo.MRU;

import com.indra.rover.mwsi.ui.widgets.CustomItemView;
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
    private static final String ARG_TABINDEX = "tabindex";
    private static final String ARG_IDPARAM = "mru_id";
    /**
     * status screen type
     */
    private int mTabIndex;


    private LinearLayout mLayout;
    MRUDao mruDao;
    MRU selectedMRU;
    String mSelectedMRU;


    public static MeterStatusFragment newInstance(int param, String mruID) {
        MeterStatusFragment fragment = new MeterStatusFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TABINDEX, param);
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
            mTabIndex = getArguments().getInt(ARG_TABINDEX);
         String   mruID = getArguments().getString(ARG_IDPARAM);
            selectedMRU = mruDao.getMRU(mruID);
            this.mSelectedMRU = mruID;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meter_status, container, false);
        // Inflate the layout for this fragment
         mLayout= (LinearLayout) view.findViewById(R.id.pnl_status);
        MainApp.bus.register(this);
        setup();

        return  view;
    }


    private void setup(){
        switch(mTabIndex){
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
        String str = String.valueOf(mruDao.countUnRead(mSelectedMRU,"U"));
        item.setValue(str);
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("Read Meter :");
        item.setValue(String.valueOf(mruDao.countUnRead(mSelectedMRU,"R")));
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("Zero Consumption :");
        item.setValue(String.valueOf(mruDao.countZeroCons(mSelectedMRU)));
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("Out of Range :");
        item.setValue(String.valueOf(mruDao.countOutofRange(mSelectedMRU)));
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("With OC :");
        item.setValue(String.valueOf(mruDao.countOC(mSelectedMRU)));
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
        item.setValue(String.valueOf(mruDao.countPrintable(mSelectedMRU)));
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("Printed :");
        item.setValue(String.valueOf(mruDao.countPrinted(mSelectedMRU)));
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
        item.setValue(String.valueOf(mruDao.countDelivered(mSelectedMRU)));
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);

        item = new CustomItemView(getActivity());
        item.setLabel("UnDelivered :");
        item.setValue(String.valueOf(mruDao.countUnDelivered(mSelectedMRU)));
        item.setLayoutParams(layoutParams);
        mLayout.addView(item);
    }



    @Subscribe
    public void getMessage(String mruID) {
            selectedMRU = mruDao.getMRUStats(mruID);
            this.mSelectedMRU = mruID;
            setup();

    }


    @Override
    public void onDetach() {
        super.onDetach();
        MainApp.bus.unregister(this);

    }
}
