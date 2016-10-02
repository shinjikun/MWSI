package com.indra.rover.mwsi.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.indra.rover.mwsi.R;

import java.util.List;


public class CustomSpinView extends RelativeLayout {

    TextView txtOpt, lblOpt;
    Spinner spn;
    ImageButton btnClr;
    public CustomSpinView(Context context) {
        super(context);
        init();
    }


    public CustomSpinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(){
        inflate(getContext(), R.layout.view_spin_label, this);
        this.txtOpt = (TextView)findViewById(R.id.txtOpt);
        this.lblOpt = (TextView)findViewById(R.id.lblOpt);
        this.spn =  (Spinner)findViewById(R.id.spnOpt);
        this.btnClr = (ImageButton)findViewById(R.id.btnClrOpt);
    }

    private void init(Context context,AttributeSet attrs) {
        init();

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomSpinOpt,
                0, 0);

        try {
            String label = a.getString(R.styleable.CustomSpinOpt_label);
            setLabel(label);
            boolean isEditable = a.getBoolean(R.styleable.CustomSpinOpt_editable,true);
            editMode(isEditable);
        } finally {
            a.recycle();
        }

    }

    public void setLabel(String text){
        this.txtOpt.setText(text);
        invalidate();
        requestLayout();
    }


    public void setValues(String value){
        this.lblOpt.setText(value);
        invalidate();
        requestLayout();
    }


    public void setOptValues(Context context, List<String> arryDevCodes){
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arryDevCodes);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        this.spn.setAdapter(dataAdapter);
        invalidate();
        requestLayout();
    }

    public void setSelection(int index){
        this.spn.setSelection(index);
        invalidate();
        requestLayout();
    }


    public void editMode(boolean editMode){
        if(editMode){
            this.spn.setVisibility(View.VISIBLE);
            this.btnClr.setVisibility(View.VISIBLE);
            this.lblOpt.setVisibility(View.GONE);
        }
        else {
            this.spn.setVisibility(View.GONE);
            this.btnClr.setVisibility(View.GONE);
            this.lblOpt.setVisibility(View.VISIBLE);
        }
    }

    public Spinner getSpn() {
        return spn;
    }

    public String getSelectedItem(){
        return spn.getSelectedItem().toString();
    }

    public ImageButton getBtnClr() {
        return btnClr;
    }

    public int getSelectedItemPosition(){
        return spn.getSelectedItemPosition();
    }

}
