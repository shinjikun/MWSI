package com.indra.rover.mwsi.ui.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.ui.view.CustomItemView;


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

    /**
     * status screen type
     */
    private int mParam;

    private LinearLayout mLayout;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter 1.
     * @return A new instance of fragment MeterStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeterStatusFragment newInstance(int param) {
        MeterStatusFragment fragment = new MeterStatusFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }
    public MeterStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getInt(ARG_PARAM1);

        }
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
        int index = R.array.status_screen_1;
        switch(mParam){
            case 0:
                index = R.array.status_screen_1;

                break;
            case 1 :
                index = R.array.status_screen_2;

                break;
            case 2 :
                index = R.array.status_screen_3;
                break;
        }
        screen1(index);
    }

    private void screen1(int index){
        String[] arry1 = getResources().getStringArray(index);
        for(int i=0;i<arry1.length;i++){
            CustomItemView item = new CustomItemView(getActivity());
            item.setLabel(arry1[i]);
            item.setValue("XXXX");
            item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            mLayout.addView(item);
        }
    }



}
