package com.example.administrator.managestu;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.infura.InfuraHttpService;
import org.web3j.crypto.Credentials;

import java.math.BigInteger;

public class MainActivity_forlogin extends AppCompatActivity {

    Bundle ethInfo;
    String useraddress;// ="0xC60D8DE6625B9DDbC579e502dEF8c3E8933b8A3b" ;//账户地址
    String privatekey;//= "C16E811A0F025ED8165699745D5CC927CC7FBAE8AA29CF036A99F0E75F55B950";//账户私钥
    String testUrl = "https://ropsten.infura.io/v3/06e4b5119d0240c6afb64bbb988e9421";//以太坊测试网络
    String contractAdd = "0x074f662cccc086bb12c8dc0efa38e02f53e2c378";
    Web3j web3j;
    Credentials credentials;
    long minigaslimit = 210000*2L;//gaslimit min 210000
    long minigasprice = 20000000000L;
    BigInteger gasLimit = new BigInteger(String.valueOf(minigaslimit+10));
    BigInteger gasPrice = new BigInteger(String.valueOf(minigasprice+10));

    EditText editPwd;
    String password;
    EditText editState;
    BigInteger state;

    boolean isregistered = false;
    boolean resofLogin;
    String pname, psex, page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_forlogin);

        ethInfo = this.getIntent().getExtras();
        useraddress = ethInfo.getString("useraddress");
        privatekey = ethInfo.getString("privatekey");
        Log.w("!!!",privatekey);

        initWeb3j();
        initCredential(privatekey);

        editPwd = (EditText) findViewById(R.id.editPwd);
        editState = (EditText) findViewById(R.id.editState);
    }

    public void buttonofReg(View view) {
        Intent toReg = new Intent(MainActivity_forlogin.this,MainActivity_register.class);
        Bundle add_key = new Bundle();
        add_key.putString("useraddress",useraddress);
        add_key.putString("privatekey",privatekey);
        toReg.putExtras(add_key);
        //MainActivity_forlogin.this.finish();
        startActivity(toReg);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void buttonofLog(View view) {

        password = editPwd.getText().toString();
        state = new BigInteger(editState.getText().toString());

        //调用智能合约的login函数，返回一个bool值，判断是否正确登录
        //调用智能合约的readPersonInfoAfterLogin，返回useraddress对应的个人信息
        //readfromblock();
        checkRegblock();
    }

    private void wrrongPwdDialog() {
        AlertDialog.Builder wrrongPwd = new AlertDialog.Builder(MainActivity_forlogin.this);
        wrrongPwd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return ;
            }
        });
        //设置提示内容
        wrrongPwd.setTitle("提示");
        wrrongPwd.setMessage("密码或身份错误");
        wrrongPwd.show();
    }

    private void initCredential(String privatekey){
        credentials = Credentials.create(privatekey);
    }

    protected class InitWeb3jTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params){
            String url = params[0];
            String result;
            try {
                InfuraHttpService initHttpService = new InfuraHttpService(url);
                web3j = Web3jFactory.build(initHttpService);
                result = "InitWeb3jTask is ok!";
            }catch (Exception e){
                result = "InitWeb3jTask is not ok!";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(MainActivity_forlogin.this, result, Toast.LENGTH_LONG).show();
        }
    }

    void initWeb3j(){
        InitWeb3jTask task = new InitWeb3jTask();
        task.execute(testUrl);
    }

    //checkregister
    private  class checkTask extends  AsyncTask<String, String, String> {
        @Override
        protected  String doInBackground(String... params) {
            String result;
            StudyManage studyManage = StudyManage.load(contractAdd,web3j,credentials,gasPrice,gasLimit);
            try {
                isregistered = studyManage.checkRegist(useraddress).send();
                result = String.valueOf(isregistered);
                Log.w("!!!","check:"+result);
            } catch (Exception e) {
                result = e.getMessage();
                Log.w("!!!",e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(MainActivity_forlogin.this, result, Toast.LENGTH_LONG).show();

            if(isregistered){
                readfromblock();
            }
            else{
                AlertDialog.Builder hasRegisted = new AlertDialog.Builder(MainActivity_forlogin.this);
                hasRegisted.setTitle("提示");
                hasRegisted.setMessage("此账号未注册，请注册后登录。");
                hasRegisted.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                hasRegisted.show();
            }
        }
    }

    void checkRegblock(){
        checkTask wtask = new checkTask();
        wtask.execute();
    }

    //readTask用来调用智能合约的函数获取个人基本信息
    private  class readTask extends  AsyncTask<String, String, String> {
        @Override
        protected  String doInBackground(String... params) {
            String result;
            StudyManage studyManage = StudyManage.load(contractAdd,web3j,credentials,gasPrice,gasLimit);
            try {
                pname = studyManage.readPersonInfoAfterLogin(useraddress).send().getValue1();
                psex = studyManage.readPersonInfoAfterLogin(useraddress).send().getValue2();
                page = studyManage.readPersonInfoAfterLogin(useraddress).send().getValue3().toString();
                resofLogin = studyManage.login(useraddress,password,state).send();
                result = "ReadTask is ok!";
                //Log.w("!!!","read");
            } catch (Exception e) {
                result = e.getMessage();
                //Log.w("!!!",e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(MainActivity_forlogin.this, result, Toast.LENGTH_LONG).show();
            Bundle loginInfo = new Bundle();//存储登录的信息以及地址和私钥
            loginInfo.putString("useraddress",useraddress);
            loginInfo.putString("privatekey",privatekey);
            loginInfo.putString("name",pname);
            loginInfo.putString("sex",psex);
            loginInfo.putString("age",page);
            if(resofLogin){
                if(state.toString().equals("0")){
                    Intent tostuLogin = new Intent(MainActivity_forlogin.this,stuLogin.class);
                    tostuLogin.putExtras(loginInfo);
                    MainActivity_forlogin.this.finish();
                    startActivity(tostuLogin);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
                else {
                    Intent toadminLogin = new Intent(MainActivity_forlogin.this,adminLogin.class);
                    toadminLogin.putExtras(loginInfo);
                    MainActivity_forlogin.this.finish();
                    startActivity(toadminLogin);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
            }
            else {
                wrrongPwdDialog();
            }
        }
    }

    void readfromblock(){
        readTask rtask = new readTask();
        rtask.execute();
    }
}
