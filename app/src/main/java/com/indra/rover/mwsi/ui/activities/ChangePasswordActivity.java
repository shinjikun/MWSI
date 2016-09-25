package com.indra.rover.mwsi.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.PreferenceKeys;

public class ChangePasswordActivity extends AppCompatActivity  implements
        View.OnClickListener, DialogUtils.DialogListener,Constants{

    DialogUtils dialogUtils;
    PreferenceKeys preferenceKeys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        dialogUtils = new DialogUtils(this);
        dialogUtils.setListener(this);
        preferenceKeys = new PreferenceKeys(this);
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
            case R.id.btnSubmit:
                validate();
                break;
        }
    }

    private void validate(){
        EditText txt = (EditText)findViewById(R.id.txtOldPass);
        String oldpass = txt.getText().toString();
        txt = (EditText)findViewById(R.id.txtNewPass);
        String newpass = txt.getText().toString();
        txt = (EditText)findViewById(R.id.txtRetypePass);
        String repass = txt.getText().toString();
        if(oldpass.isEmpty()){
            dialogUtils.showOKDialog("Fill-in the necessary field");
            return;
        }
        if(newpass.isEmpty()){
            dialogUtils.showOKDialog("Fill-in the necessary field");
            findViewById(R.id.txtNewPass);
            return;
        }
        if(repass.isEmpty()){
            dialogUtils.showOKDialog("Fill-in the necessary field");
            return;
        }
        String oldpass2 = preferenceKeys.getData(ADMIN_PASSWORD,ADMIN_DEFAULT_PASS);
        if(!oldpass.equals(oldpass2)){
            dialogUtils.showOKDialog("Old password doesn't match");
            return;
        }
        if(!newpass.equals(repass)){
            dialogUtils.showOKDialog("New password doesn't match to Re-type password");
            return;
        }
        preferenceKeys.setData(ADMIN_PASSWORD,newpass);
        dialogUtils.showOKDialog(777,null,"Password Changed Successfully",new Bundle());

    }


    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {

        if(dialog_id ==777)
            finish();
    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }
}
