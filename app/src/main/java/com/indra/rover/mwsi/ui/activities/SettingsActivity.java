package com.indra.rover.mwsi.ui.activities;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.PreferenceKeys;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener,Constants {

    PreferenceKeys prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        prefs = PreferenceKeys.getInstance(this);
        init();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        int id =  view.getId();
        switch (id){
            case R.id.btnLogin:
                showLogin();
                break;
        }
    }

    Dialog dialog;
    public void showLogin(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        dialog.setCancelable(false);
        final EditText txtDlg = (EditText)dialog.findViewById(R.id.dlg_body);
        ImageButton dlgBtnClose = (ImageButton)dialog.findViewById(R.id.dlg_btn_close);
        dlgBtnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btn =  (Button)dialog.findViewById(R.id.dlg_btn_yes);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String value =   txtDlg.getText().toString();
                findViewById(R.id.pnl_admin_settings).setVisibility(View.VISIBLE);
                findViewById(R.id.pnl_admin_login).setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        btn =  (Button)dialog.findViewById(R.id.dlg_btn_no);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    private void init(){

    }

}
