package com.example.administrator.managestu;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.infura.InfuraHttpService;

import java.math.BigInteger;

public class MainActivity_register extends AppCompatActivity {

    String useraddress ;//账户地址
    String privatekey ;//账户私钥
    String testUrl = "https://ropsten.infura.io/v3/06e4b5119d0240c6afb64bbb988e9421";//以太坊测试网络
    String contractAdd = "0x09463f7413fc287ee34510c8be94565a60463844";
    Web3j web3j;
    Credentials credentials;
    long minigaslimit = 210000*2L;//gaslimit min 210000
    long minigasprice = 20000000000L;
    BigInteger gasLimit = new BigInteger(String.valueOf(minigaslimit+10));
    BigInteger gasPrice = new BigInteger(String.valueOf(minigasprice+10));

    Bundle fromlogin;

    EditText add_name;
    String aname;
    EditText add_pwd;
    String apwd;
    EditText add_sex;
    String  asex;
    EditText add_age;
    String aage;
    EditText add_state;
    String astate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        fromlogin = this.getIntent().getExtras();
        useraddress = fromlogin.getString("useraddress");
        privatekey = fromlogin.getString("privatekey");

        add_name = (EditText) findViewById(R.id.editRName);
        add_pwd = (EditText) findViewById(R.id.editRPwd);
        add_sex = (EditText) findViewById(R.id.editRSex);
        add_age = (EditText) findViewById(R.id.editRAge);
        add_state = (EditText) findViewById(R.id.editRState);

        initWeb3j();
        initCredential(privatekey);
        Log.w("!!!","init");

    }

    //initWeb3j
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
            Toast.makeText(MainActivity_register.this, result, Toast.LENGTH_LONG).show();
        }
    }

    void initWeb3j(){
        InitWeb3jTask task = new InitWeb3jTask();
        task.execute(testUrl);
    }
    //addPerson
    private  class writeTask extends  AsyncTask<String, String, String> {
        @Override
        protected  String doInBackground(String... params) {
            String result;
            StudyManage studyManage = StudyManage.load(contractAdd,web3j,credentials,gasPrice,gasLimit);
            BigInteger age = new BigInteger(aage);
            BigInteger state = new BigInteger(astate);
            try {
                RemoteCall<TransactionReceipt> addPer = studyManage.addPerson(useraddress,aname,apwd,asex,age,state);
                result = addPer.send().getStatus().toString();
                Log.w("!!!!",result);
            } catch (Exception e) {
                result = e.getMessage();
                Log.w("!!!",e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(MainActivity_register.this, result, Toast.LENGTH_LONG).show();
        }
    }

    void writetoblock(){
        writeTask wtask = new writeTask();
        wtask.execute();
    }

    //注册按钮
    public void registerOK(View view) {

        aname = add_name.getText().toString();
        apwd = add_pwd.getText().toString();
        asex = add_sex.getText().toString();
        aage = add_age.getText().toString();
        astate = add_state.getText().toString();
        Log.w("!!!","!!!");
        writetoblock();
        //Intent toForlogin = new Intent(MainActivity_register.this,MainActivity_forlogin.class);
        //finish();
        //startActivity(toForlogin);
        //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
