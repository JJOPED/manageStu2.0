package com.example.administrator.managestu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class adminLogin extends AppCompatActivity {
    Bundle loginInfo;
    String useraddress;
    String privatekey;
    String name;
    String sexy;
    String age;
    TextView nameText;
    TextView sexyText;
    TextView ageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        loginInfo = this.getIntent().getExtras();
        name = loginInfo.getString("name");
        sexy = loginInfo.getString("sex");
        age = loginInfo.getString("age");
        useraddress = loginInfo.getString("useraddress");
        privatekey = loginInfo.getString("privatekey");

        nameText = (TextView) findViewById(R.id.getInfoName);
        sexyText = (TextView) findViewById(R.id.getInfoSexy);
        ageText = (TextView) findViewById(R.id.getInfoAge);
        nameText.setText(name);
        sexyText.setText(sexy);
        ageText.setText(age);
    }

    public void selForSch(View view) {
        Intent toSelResult = new Intent(adminLogin.this,selForAdmin.class);
        //adminLogin.this.finish();
        Bundle adminInfo = new Bundle();
        adminInfo.putString("useraddress",useraddress);
        adminInfo.putString("privatekey",privatekey);
        toSelResult.putExtras(adminInfo);
        startActivity(toSelResult);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void insForSch(View view) {
        Intent toAddStu = new Intent(adminLogin.this,addStudy.class);
        Bundle adminInfo = new Bundle();
        adminInfo.putString("useraddress",useraddress);
        adminInfo.putString("privatekey",privatekey);
        toAddStu.putExtras(adminInfo);
        startActivity(toAddStu);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
