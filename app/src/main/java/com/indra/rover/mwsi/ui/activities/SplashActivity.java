package com.indra.rover.mwsi.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.indra.rover.mwsi.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread startThread =  new Thread(){
            @Override
            public void run() {
                try{
                    //Display for 3 seconds
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    loadMainScreen();
                }
            }
        };
        startThread.start();
    }

    private void loadMainScreen(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }





}
