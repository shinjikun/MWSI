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
public class DialogUtils  {
   private DialogListener listener =null;
   private Context context;




    public DialogUtils(Context context){
        this.context  = context;
    }


    public void showYesNoDialog(int dialog_id,String message,Bundle params){
        showYesNoDialog(dialog_id,null,message,params);
    }


    public void showYesNoDialog(final  int dialog_id,String title,String message,final Bundle params){
        final Dialog  dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.setCancelable(false);
        ImageButton dlgBtnClose = (ImageButton)dialog.findViewById(R.id.dlg_btn_close);
        dlgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btn =  (Button)dialog.findViewById(R.id.dlg_btn_no);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.dialog_cancel(dialog_id,params);
                }
                dialog.dismiss();
            }
        });
        btn =  (Button)dialog.findViewById(R.id.dlg_btn_yes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.dialog_confirm(dialog_id,params);
                }
                dialog.dismiss();
            }
        });
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

    public void showOKDialog(String message){
        showOKDialog(0,null,message, new Bundle());
    }

    public void showOKDialog(String title,String message){
        showOKDialog(0,title,message, new Bundle());
    }


    public void showOKDialog(final int dialog_id,String title,String message,final Bundle params){
        final Dialog  dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ok);
        dialog.setCancelable(false);
        ImageButton dlgBtnClose = (ImageButton)dialog.findViewById(R.id.dlg_btn_close);
        dlgBtnClose.setVisibility(View.INVISIBLE);
        Button btn =  (Button)dialog.findViewById(R.id.dlg_btn_yes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.dialog_confirm(dialog_id,params);
                }
                dialog.dismiss();
            }
        });
        TextView txt = (TextView)dialog.findViewById(R.id.dlg_title);
        if(!Utils.isNotEmpty(title)){
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

    public interface DialogListener{

         void dialog_confirm(int dialog_id,Bundle params);
         void dialog_cancel(int dialog_id, Bundle params);
    }
}
