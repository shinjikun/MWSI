package com.indra.rover.mwsi.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.indra.rover.mwsi.R;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    String search_column, search_value;
    String[] arryColumIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        arryColumIds = getResources().getStringArray(R.array.search_filter_ids);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnSearch:
                search();
                break;
        }

    }


    private void search(){
        EditText txtSearch =  (EditText)findViewById(R.id.txtValue);
        search_value = txtSearch.getText().toString();
        Spinner  spn = (Spinner)findViewById(R.id.spn);
        int index = spn.getSelectedItemPosition();
        if(!search_value.isEmpty()){
            search_column = arryColumIds[index];
            sendResult();
        }
        else {
            finish();
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


    private void sendResult(){
        Intent intent = new Intent();
        intent.putExtra("key",search_column);
        intent.putExtra("value",search_value);

        if (getParent() == null) {
            setResult(Activity.RESULT_OK, intent);
        } else {
            getParent().setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

}
