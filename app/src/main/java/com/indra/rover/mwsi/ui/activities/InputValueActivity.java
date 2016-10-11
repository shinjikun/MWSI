package com.indra.rover.mwsi.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;
import com.indra.rover.mwsi.utils.GPSTracker;
import com.indra.rover.mwsi.utils.Utils;

public class InputValueActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtValues;
    MeterReadingDao mtrDao;
    int type;
    int MR_TYPE1=1;
    int MR_TYPE2=2;
    String docid;
    MeterInfo meterInfo;
    String oldValue;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_value);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gpsTracker =  new GPSTracker(this);
        txtValues =  (EditText) findViewById(R.id.txtValue);
        mtrDao = new MeterReadingDao(this);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
           type =  extras.getInt("type");
            if(type == MR_TYPE1){
                docid =  extras.getString("id");
                meterInfo = mtrDao.fetchInfo(docid);
                if(meterInfo !=null){
                    oldValue = meterInfo.getPresRdg();
                    txtValues.setText(meterInfo.getPresRdg());
                }
            }

        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
          case  R.id.btnBackSpace:
            int length = txtValues.getText().length();
            if (length > 0) {
                txtValues.getText().delete(length - 1, length);
            }
            break;

            case R.id.btnClear:
                txtValues.setText("");
                break;

            case R.id.btnEnter:
                sendResult();
                break;
            case R.id.btnCancel:
                finish();
                break;

        }

    }


     void numKeys(View view){
        String tag = view.getTag().toString();
        txtValues.append(tag);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


     void setValue(){


        int tries =0 ;
        if(Utils.isNotEmpty(meterInfo.getRdg_tries())){
            tries = Integer.parseInt(meterInfo.getRdg_tries());
        }
        tries = tries+1;
        String value = txtValues.getText().toString();
        if(value.isEmpty()){
            meterInfo.setReadStat("U");
        }else {
            String readStat = meterInfo.getReadStat();
            if(readStat.equals("R")){
                meterInfo.setReadStat("E");
            }
            else {
                meterInfo.setReadStat("R");
            }
        }


        String latitude = null;
        String longtitude = null;
        if(gpsTracker.canGetLocation()){
            latitude = String.valueOf(gpsTracker.getLatitude());
            longtitude = String.valueOf(gpsTracker.getLongitude());

        }
        mtrDao.updateReading(Utils.getFormattedDate(),
                Utils.getFormattedTime(),
                value,latitude,longtitude,tries, meterInfo.getDldocno(),meterInfo.getReadStat()
        );
    }



     void sendResult(){
        String value = txtValues.getText().toString();
        Intent intent = new Intent();


        if(!value.equals(oldValue)){
            setValue();
            intent.putExtra("value",value);
            if (getParent() == null) {
                setResult(Activity.RESULT_OK, intent);
            } else {
                getParent().setResult(Activity.RESULT_OK, intent);
            }
        }

        finish();
    }
}
