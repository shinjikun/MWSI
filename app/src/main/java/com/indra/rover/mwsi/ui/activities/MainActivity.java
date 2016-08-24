package com.indra.rover.mwsi.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.utils.DialogUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , DialogUtils.DialogListener{

    DialogUtils mDialogUtils;
    private final int DLG_CLOSE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDialogUtils = new DialogUtils(this);
        mDialogUtils.setListener(this);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        switch(id){
            case R.id.btnExit:
             mDialogUtils.showYesNoDialog(DLG_CLOSE,"Close the app?", new Bundle());
                break;

            case R.id.btnStatus:
                intent = new Intent(this, StatusActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMeterReading:
                intent = new Intent(this, MRUListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSettings:

                break;
        }
    }

    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {
        switch (dialog_id){
            case DLG_CLOSE:
                finish();
                break;
        }
    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }
}
