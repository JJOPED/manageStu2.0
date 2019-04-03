package com.example.administrator.managestu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity_register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
    }

    public void registerOK(View view) {
        Intent toForlogin = new Intent(MainActivity_register.this,MainActivity_forlogin.class);
        finish();
        startActivity(toForlogin);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
