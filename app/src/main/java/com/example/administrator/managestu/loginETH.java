package com.example.administrator.managestu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class loginETH extends AppCompatActivity {

    EditText uadd;
    EditText upkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_eth);

        uadd = (EditText) findViewById(R.id.editeth_Add);
        upkey = (EditText) findViewById(R.id.editeth_Pkey);
    }

    public void loginETH(View view) {

        String useraddress;
        String privatekey;
        Bundle ethInfo = new Bundle();
        useraddress = uadd.getText().toString();
        privatekey = upkey.getText().toString();
        ethInfo.putString("useraddress",useraddress);
        ethInfo.putString("privatekey",privatekey);

        Intent tologin = new Intent(loginETH.this,MainActivity_forlogin.class);
        tologin.putExtras(ethInfo);
        finish();
        startActivity(tologin);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
