package com.example.administrator.managestu;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.infura.InfuraHttpService;

import java.math.BigInteger;

public class adminLogin extends AppCompatActivity {

    Bundle loginInfo;
    String useraddress;
    String privatekey;

    String testUrl = "https://ropsten.infura.io/v3/06e4b5119d0240c6afb64bbb988e9421";//以太坊测试网络
    String contractAdd = "0x074f662cccc086bb12c8dc0efa38e02f53e2c378";
    Web3j web3j;
    Credentials credentials;
    long minigaslimit = 210000*2L;//gaslimit min 210000
    long minigasprice = 20000000000L;
    BigInteger gasLimit = new BigInteger(String.valueOf(minigaslimit+10));
    BigInteger gasPrice = new BigInteger(String.valueOf(minigasprice+10));

    String name;
    String sex;
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
        sex = loginInfo.getString("sex");
        age = loginInfo.getString("age");
        useraddress = loginInfo.getString("useraddress");
        privatekey = loginInfo.getString("privatekey");
        Log.w("!!!","schlogin:"+useraddress);

        nameText = (TextView) findViewById(R.id.getInfoName);
        sexyText = (TextView) findViewById(R.id.getInfoSexy);
        ageText = (TextView) findViewById(R.id.getInfoAge);

        initCredential(privatekey);
        initWeb3j();

        Toast mToast;
        mToast = Toast.makeText(adminLogin.this, null, Toast.LENGTH_LONG);
        mToast.setText("请稍等···");
        mToast.show();

        readInfofromblock();

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
            //Toast.makeText(adminLogin.this, result, Toast.LENGTH_LONG).show();
        }
    }

    void initWeb3j(){
        InitWeb3jTask task = new InitWeb3jTask();
        task.execute(testUrl);
    }

    //readInfoTask用来调用智能合约的函数获得查询学生的信息
    private  class readInfoTask extends AsyncTask<String, String, String> {
        @Override
        protected  String doInBackground(String... params) {
            String result;
            StudyManage studyManage = StudyManage.load(contractAdd,web3j,credentials,gasPrice,gasLimit);
            try {
                name = studyManage.readPersonInfoAfterLogin(useraddress).send().getValue1();
                sex = studyManage.readPersonInfoAfterLogin(useraddress).send().getValue2();
                age = studyManage.readPersonInfoAfterLogin(useraddress).send().getValue3().toString();
                result = "readname:"+name;
                Log.w("!!!","info result:"+result);
            } catch (Exception e) {
                result = e.getMessage();
                Log.w("!!!","info exception:"+e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            //Toast.makeText(adminLogin.this, result, Toast.LENGTH_LONG).show();
            nameText.setText(name);
            sexyText.setText(sex);
            ageText.setText(age);
        }
    }
    void readInfofromblock(){
        readInfoTask rtask = new readInfoTask();
        rtask.execute();
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
