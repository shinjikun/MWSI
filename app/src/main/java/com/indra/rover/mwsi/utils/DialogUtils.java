package com.indra.rover.mwsi.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.indra.rover.mwsi.R;


/**
 * Created by leonardoilagan on 3/27/16.
 * *
 */
public class DialogUtils  implements View.OnClickListener{
    DialogListener listener =null;
    int dialog_id;
    Dialog dialog;
    Button btndlgYes,btndlgNo;
    ImageButton btndlgClose;
    TextView txtdlg_title,txtdlg_body;
    Bundle params;
    Context context;
    public DialogUtils(int dialog_id,Bundle params){

    }



    public DialogUtils(Context context){
        this.context  = context;
    }


    public void showYesNoDialog(int dialog_id,String message,Bundle params){
        showYesNoDialog(dialog_id,null,message,params);
    }


    public void showYesNoDialog(int dialog_id,String title,String message,Bundle params){
        this.dialog_id = dialog_id;
        this.params = params;
         dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.setCancelable(false);
        ImageButton dlgBtnClose = (ImageButton)dialog.findViewById(R.id.dlg_btn_close);
        dlgBtnClose.setOnClickListener(this);
        Button btn =  (Button)dialog.findViewById(R.id.dlg_btn_no);
        btn.setOnClickListener(this);
        btn =  (Button)dialog.findViewById(R.id.dlg_btn_yes);
        btn.setOnClickListener(this);
        TextView txt = (TextView)dialog.findViewById(R.id.dlg_title);
        if(title == null){
            txt.setVisibility(View.GONE);
        }
        else {
            txt.setText(title);
        }
        txt = (TextView)dialog.findViewById(R.id.dlg_body);
        txt.setText(message);
        dialog.show();
    }


    public void showOKDialog(int dialog_id,String title,String message,Bundle params){
        this.dialog_id = dialog_id;
        this.params = params;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ok);
        dialog.setCancelable(false);
        ImageButton dlgBtnClose = (ImageButton)dialog.findViewById(R.id.dlg_btn_close);
        //dlgBtnClose.setOnClickListener(this);
        dlgBtnClose.setVisibility(View.INVISIBLE);
        Button btn =  (Button)dialog.findViewById(R.id.dlg_btn_yes);
        btn.setOnClickListener(this);
        TextView txt = (TextView)dialog.findViewById(R.id.dlg_title);
        if(title == null){
            txt.setVisibility(View.GONE);
        }
        else {
            txt.setText(title);
        }
        txt = (TextView)dialog.findViewById(R.id.dlg_body);
        txt.setText(message);
        dialog.show();
    }


    public void setListener(DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.dlg_btn_close:
                dialog.dismiss();
                break;
            case R.id.dlg_btn_yes:
                if(listener!=null){
                    listener.dialog_confirm(dialog_id,this.params);
                }
                dialog.dismiss();
                break;
            case R.id.dlg_btn_no:
                if(listener!=null){
                    listener.dialog_cancel(dialog_id,this.params);
                }
                dialog.dismiss();
                break;


        }
    }

    public interface DialogListener{

         void dialog_confirm(int dialog_id,Bundle params);
         void dialog_cancel(int dialog_id, Bundle params);
    }
}
