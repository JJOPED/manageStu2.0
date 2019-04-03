package com.example.administrator.managestu;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity_forlogin extends AppCompatActivity {

    EditText editAdd;
    EditText editPwd;
    EditText editState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_forlogin);

        editAdd = (EditText) findViewById(R.id.editAdd);
        editPwd = (EditText) findViewById(R.id.editPwd);
        editState = (EditText) findViewById(R.id.editState);
    }

    public void buttonofReg(View view) {
        Intent toReg = new Intent(MainActivity_forlogin.this,MainActivity_register.class);
        MainActivity_forlogin.this.finish();
        startActivity(toReg);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void buttonofLog(View view) {
        String address = editAdd.getText().toString();
        String password = editPwd.getText().toString();
        String state = editState.getText().toString();

        /*loginInfo0.putString("address",address);
        loginInfo0.putString("pwd",password);
        loginInfo0.putString("state",state);*/
        //通过地址和密码获得用户信息:findPerson(address _add)
        String namefromContract = "AAA";
        String pwdfromContract = "222";
        String sexyfromContract = "female";
        String agefromContract = "20";

        Bundle loginInfo0 = new Bundle();//存储登录的信息
        loginInfo0.putString("name",namefromContract);
        loginInfo0.putString("sexy",sexyfromContract);;
        loginInfo0.putString("age",agefromContract);

        //Log.w("w",state);
        if(state.equals("0")){
            Intent tostuLogin = new Intent(MainActivity_forlogin.this,stuLogin.class);
            if(password.equals(pwdfromContract)){ //判断登录信息是否准确:login()
                tostuLogin.putExtras(loginInfo0);
                MainActivity_forlogin.this.finish();
                startActivity(tostuLogin);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
            else {
                wrrongPwdDialog();
            }
            return;
        }
        else if(state.equals("1")){
            Intent toadminLogin = new Intent(MainActivity_forlogin.this,adminLogin.class);
            if(password.equals(pwdfromContract)) {
                toadminLogin.putExtras(loginInfo0);
                MainActivity_forlogin.this.finish();
                startActivity(toadminLogin);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
            else {
                wrrongPwdDialog();
            }
            return;
        }
    }

    private void wrrongPwdDialog() {
        AlertDialog.Builder wrrongPwd = new AlertDialog.Builder(this);
        wrrongPwd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return ;
            }
        });
        //设置提示内容
        wrrongPwd.setTitle("提示");
        wrrongPwd.setMessage("密码错误");
        wrrongPwd.show();
    }
}
