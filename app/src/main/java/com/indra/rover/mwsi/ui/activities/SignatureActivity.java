package com.indra.rover.mwsi.ui.activities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.ui.widgets.SignatureArea;
import com.indra.rover.mwsi.utils.DialogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SignatureActivity extends AppCompatActivity implements
        View.OnClickListener,DialogUtils.DialogListener {

    SignatureArea signPanel;
    String crdocno;
    String deliv_code;
    boolean isEditMode=true;
    DialogUtils dialogUtils;
    int DLG_ID =12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        dialogUtils = new DialogUtils(this);
        dialogUtils.setListener(this);
        signPanel = (SignatureArea)findViewById(R.id.signPanel);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            crdocno = extras.getString("id");
            deliv_code = extras.getString("deliv_code");
            isEditMode = extras.getBoolean("editMode");

        }

        if(!isEditMode){
            Button button = (Button)findViewById(R.id.btnCancel);
            button.setText("Close");
            findViewById(R.id.btnClearSign).setVisibility(View.GONE);
            findViewById(R.id.btnDoneSign).setVisibility(View.GONE);
            signPanel.setEditMode(false);
        }


        final EditText edit_txt = (EditText) findViewById(R.id.editText);

        edit_txt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signPanel.setReceipient(edit_txt.getText().toString());
                    return true;
                }
                return false;
            }
        });
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
        switch (id){
            case R.id.btnClearSign:
                signPanel.clearSignature();
                File file = getImageFile();
                if(file.exists())
                    file.delete();
                break;
            case R.id.btnDoneSign:
                saveSignature();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    private void saveSignature(){
        Bitmap bitMap = signPanel.getBitmap();
        File contentDir=new File(android.os.Environment.getExternalStorageDirectory()
                ,getPackageName()+"/downloads/signatures");
        if(!contentDir.exists())
            contentDir.mkdir();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(crdocno);
        strBuilder.append('-');
        strBuilder.append("sign");
        strBuilder.append(".png");
        String fileName = strBuilder.toString();
        File myPath = new File(contentDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitMap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    bitMap, myPath.getPath(), fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialogUtils.showOKDialog(DLG_ID,"","Signature Successfully Saved!",new Bundle());
    }


    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {
        finish();
    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }



    private File getImageFile(){
        File    contentDir=new File(android.os.Environment.getExternalStorageDirectory()
                ,getPackageName()+"/downloads/signatures");
        if(!contentDir.exists())
            contentDir.mkdir();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(crdocno);
        strBuilder.append('-');
        strBuilder.append("sign");

        strBuilder.append(".png");
        String fileName = strBuilder.toString();

        return new File(contentDir, fileName);
    }
}
