package com.indra.rover.mwsi.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.NewMeterInfo;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.Utils;

import java.util.ArrayList;

public class NewFoundMeterActivity extends AppCompatActivity  implements View.OnClickListener,
        DialogUtils.DialogListener{

    String mru_id;
    MeterReadingDao meterDao;
    int current =0;
    ArrayList<NewMeterInfo> arry;
    NewMeterInfo meterInfo;
    boolean isEditMode=false;
    Button btnSave,btnCancel;
    final int INPUT_REQ =68;
    DialogUtils dlgUtils;

    final int EDIT=1;
    final int ADD=0;
    int stateADDEDIT  = ADD;
    final int DLG_DEL=342;
    final int DLG_EDITMODE = 341;
    /**
     * Button for navigating previous or next records
     */
    ImageButton btnPrev, btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_found_meter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        findViewById(R.id.btnMREdit).setOnClickListener(this);
        btnSave =  (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnNext = (ImageButton)findViewById(R.id.fabRight);
        btnNext.setOnClickListener(this);
        btnPrev = (ImageButton) findViewById(R.id.fabLeft);
        btnPrev.setOnClickListener(this);

        dlgUtils = new DialogUtils(this);
        dlgUtils.setListener(this);
        Bundle extras = getIntent().getExtras();
        meterDao = new MeterReadingDao(this);
        arry = new ArrayList<>();
        if (extras != null) {
            this.mru_id = extras.getString("mru_id");
            this.arry = meterDao.fetchNewMeters(this.mru_id);
            prepareData(current);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.newmeter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // handle arrow click here
            case android.R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)
                break;
            case R.id.mnuAddNew:
                findViewById(R.id.content).setVisibility(View.VISIBLE);
                stateADDEDIT = ADD;
                resetForm();
                setEditMode(true);
                break;
            case R.id.mnuDelMeter:
                if(!arry.isEmpty()){
                    dlgUtils.showYesNoDialog(DLG_DEL,"Are you sure you want to " +
                            "delete this record?",new Bundle());

                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void prepareData(int index){
        if(!arry.isEmpty()){
            meterInfo = arry.get(index);
            EditText txt = (EditText)findViewById(R.id.txtMeterNumber);
            txt.setText(meterInfo.getMeterNo());
            txt = (EditText)findViewById(R.id.txtName);
            txt.setText(meterInfo.getCustName());
            txt = (EditText)findViewById(R.id.txtAddress);
            txt.setText(meterInfo.getCustAdd());
            txt = (EditText)findViewById(R.id.txtSeqNum);
            txt.setText(meterInfo.getSeqno());
            btnSave.setText("Edit");
            btnCancel.setVisibility(View.GONE);
            setEditMode(false);

        }
        else {
            findViewById(R.id.content).setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnSave.setText("Add New");
            btnNext.setEnabled(false);
            btnPrev.setEnabled(false);
        }
    }







    private void setReadingValue(String value){
        TextView txt = (TextView)findViewById(R.id.txtReading);
        txt.setText(value);
    }


    private void setEditMode(boolean isEditMode){

        this.isEditMode =isEditMode;

        EditText txt = (EditText)findViewById(R.id.txtMeterNumber);
        txt.setEnabled(isEditMode);
        txt = (EditText)findViewById(R.id.txtName);
        txt.setEnabled(isEditMode);
        txt = (EditText)findViewById(R.id.txtAddress);
        txt.setEnabled(isEditMode);
        txt = (EditText)findViewById(R.id.txtSeqNum);
        txt.setEnabled(isEditMode);
    }

    private void resetForm(){
        EditText txt = (EditText)findViewById(R.id.txtMeterNumber);
        txt.setText("");
        txt = (EditText)findViewById(R.id.txtName);
        txt.setText("");
        txt = (EditText)findViewById(R.id.txtAddress);
        txt.setText("");
        txt = (EditText)findViewById(R.id.txtSeqNum);
        txt.setText("");
        btnSave.setText("Save");
        btnCancel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Bundle bundle ;
        switch(id){

            case R.id.btnMREdit:
              loadMeterInput();
                break;

            case R.id.btnSave:
                String lable = ((Button)view).getText().toString();
                if(lable.equals("Add New")){
                    stateADDEDIT =ADD;
                    findViewById(R.id.content).setVisibility(View.VISIBLE);
                    setEditMode(true);
                    resetForm();
                }
                else if(lable.equals("Save")){
                    btnSave.setText("Edit");
                    btnCancel.setVisibility(View.GONE);
                    saveEdit();
                }
                else if(lable.equals("Edit")){
                    btnCancel.setVisibility(View.VISIBLE);
                    btnSave.setText("Save");
                    setEditMode(true);
                    stateADDEDIT = EDIT;

                }
                break;
            case R.id.fabLeft:
                bundle = new Bundle();
                bundle.putString("action","next");
                if(isEditMode){
                    dlgUtils.showYesNoDialog(DLG_EDITMODE,"There are still unsave record.\n" +
                            "Proceed to the Previous Record?",bundle);
                }
                else {
                    movePrevious();
                }
                break;
            case R.id.fabRight:
                bundle = new Bundle();
                bundle.putString("action","previous");
                if(isEditMode){
                    dlgUtils.showYesNoDialog(DLG_EDITMODE,"There are still unsave record.\n" +
                            "Proceed to the Next Record?",bundle);
                }else {
                    moveNext();
                }
                break;
        }
    }

    private void saveEdit(){
        NewMeterInfo mMeterInfo =  new NewMeterInfo();
        EditText txt = (EditText)findViewById(R.id.txtMeterNumber);
        mMeterInfo.setMeterNo(txt.getText().toString());
        txt = (EditText)findViewById(R.id.txtName);
        mMeterInfo.setCustName(txt.getText().toString());
        txt = (EditText)findViewById(R.id.txtAddress);
        mMeterInfo.setCustAdd(txt.getText().toString());
        txt = (EditText)findViewById(R.id.txtSeqNum);
        mMeterInfo.setSeqno(txt.getText().toString());
        TextView txtValue = (TextView)findViewById(R.id.txtReading);
        mMeterInfo.setPresRdg(txtValue.getText().toString());
        mMeterInfo.setRdg_date(Utils.getFormattedDate());
        mMeterInfo.setRdg_time(Utils.getFormattedTime());
        mMeterInfo.setMru_id(mru_id);
        long result ;
        switch (stateADDEDIT){

              case ADD:
                result =   meterDao.addFConn(mMeterInfo, true);
                  if(result ==-1){
                      dlgUtils.showOKDialog("There is already existing meter number in the list");
                  }
                  else if(result ==-2){
                      dlgUtils.showOKDialog("You've  already added this meter number on this list");
                  }
                  else {
                      dlgUtils.showOKDialog("Added Successfully");
                      arry.add(mMeterInfo);
                      current= arry.size()-1;
                      prepareData(current);
                      setEditMode(false);
                  }
                  break;
              case EDIT:
                   result =   meterDao.addFConn(meterInfo, false);
                  break;
          }
    }


    void loadMeterInput(){
        Intent    intent = new Intent(this, InputValueActivity.class);
        TextView txt = (TextView)findViewById(R.id.txtReading);
        intent.putExtra("type",InputValueActivity.MR_TYPE2);
        intent.putExtra("value",txt.getText());
        startActivityForResult(intent, INPUT_REQ);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == INPUT_REQ){
            if(resultCode ==  Activity.RESULT_OK){
                Bundle  bundle = data.getExtras();

                String value = bundle.getString("value");
                setReadingValue(value);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {
        switch (dialog_id){
            case DLG_DEL:
                 meterDao.deleteFMeter(meterInfo);
                arry.remove(current);
                dlgUtils.showOKDialog("Deleted Successfully");
                if(!arry.isEmpty())
                    current = arry.size()-1;
                else {
                    current =0;
                }
                prepareData(current);
                break;

            case DLG_EDITMODE:
                String action = params.getString("action");
                if(action.equals("next")){
                    moveNext();
                }
                else{
                    movePrevious();
                }
                break;
        }
    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }


    void movePrevious(){
        if(current!=0){
            current--;
            prepareData(current);
        }
        else{
            btnPrev.setEnabled(false);
        }
        btnNext.setEnabled(true);

    }
    void moveNext(){
        if(current< arry.size()-1){
            current++;
            prepareData(current);
            btnPrev.setEnabled(true);
        }
        else {
            btnNext.setEnabled(false);

        }
    }
}
