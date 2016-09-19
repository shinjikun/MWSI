package com.indra.rover.mwsi.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indra.rover.mwsi.R;

/**
 * Created by Indra on 8/22/2016.
 */
public class CustomItemView extends LinearLayout {
    TextView txtName,txtSubName;
    public CustomItemView(Context context){
        super(context);
        init();
    }




    public CustomItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }




    private void init(Context context,AttributeSet attrs) {
        init();

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomDetailView,
                0, 0);

        try {
            String titleText = a.getString(R.styleable.CustomDetailView_textlabel);
            String subTitleText = a.getString(R.styleable.CustomDetailView_textcontent);
            setLabel(titleText);
            setValue(subTitleText);
        } finally {
            a.recycle();
        }

    }


    private void init() {
        inflate(getContext(), R.layout.view_status_info, this);
        this.txtName = (TextView)findViewById(R.id.txtLabelTitle);
        this.txtSubName = (TextView)findViewById(R.id.txtValue);
    }



    public void setLabel(String text){
        this.txtName.setText(text);
        invalidate();
        requestLayout();
    }


    public void setValue(String text){
        this.txtSubName.setText(text);
        invalidate();
        requestLayout();
    }




}

