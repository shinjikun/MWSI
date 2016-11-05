package com.indra.rover.mwsi.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.PreferenceKeys;


public class ChangeStatusActivity extends AppCompatActivity implements View.OnClickListener,
        DialogUtils.DialogListener {

    PreferenceKeys prefs;
    DialogUtils dialogUtils;
    String[] arryStatus;
    Spinner spn;
    int DLG_CHANGE_STAT=22;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        prefs = PreferenceKeys.getInstance(this);
        dialogUtils = new DialogUtils(this);
        dialogUtils.setListener(this);
        String appStatus =   prefs.getData(Constants.APP_STATUS,"DOWNLOADED");
        arryStatus =  getResources().getStringArray(R.array.rover_statuses);
         spn =  (Spinner)findViewById(R.id.spn);
         for(int i=0;i<arryStatus.length;i++){
             String spnStatus = arryStatus[i];
             if(spnStatus.equals(appStatus)){
                spn.setSelection(i);
                 break;
             }
         }


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
        int id = view.getId();
        switch(id){
            case R.id.btn:
                String selectedIndex =  String.valueOf(spn.getSelectedItem());
                Bundle bundle = new Bundle();
                bundle.putString("selected",selectedIndex);
                dialogUtils.showYesNoDialog(DLG_CHANGE_STAT,"Are you sure you want change the " +
                        "rover's status?\nThis will affect the current reading.",bundle);
                break;
        }
    }

    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {
        if(DLG_CHANGE_STAT == dialog_id){
            String appStatus =   params.getString("selected");
            prefs.setData(Constants.APP_STATUS,appStatus);
            dialogUtils.showOKDialog("Rover Status was Changed");

        }
    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }
}
