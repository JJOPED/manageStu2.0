package com.example.administrator.managestu;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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

public class addStudy extends AppCompatActivity {

    Bundle adminInfo;
    String adminAdress;
    String privatekey;

    //studyRecord record;
    String studentAdd;
    String studentTime;
    String studentSchool;
    EditText aAdd;
    EditText aTime;
    EditText aSchool;

    //连接到以太坊测试网络
    String testUrl = "https://ropsten.infura.io/v3/06e4b5119d0240c6afb64bbb988e9421";//以太坊测试网络
    String contractAdd = "0x074f662cccc086bb12c8dc0efa38e02f53e2c378";
    Web3j web3j;
    Credentials credentials;
    long minigaslimit = 210000*2L;//gaslimit min 210000
    long minigasprice = 20000000000L;
    BigInteger gasLimit = new BigInteger(String.valueOf(minigaslimit+10));
    BigInteger gasPrice = new BigInteger(String.valueOf(minigasprice+10));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_study);

        adminInfo = this.getIntent().getExtras();
        adminAdress = adminInfo.getString("useraddress");
        privatekey = adminInfo.getString("privatekey");

        //保存新增的学籍信息
        aAdd = (EditText)findViewById(R.id.newAdd);
        aTime = (EditText) findViewById(R.id.newTime);
        aSchool = (EditText) findViewById(R.id.newSchool);
        /*record.timeofStudent = aTime.getText().toString();
        record.schoolofStudent = aSchool.getText().toString();*/

        //初始化网络连接以及凭证
        initCredential(privatekey);
        initWeb3j();

    }

    //加入学籍信息的按钮
    public void addStudy(View view) {
        studentAdd = aAdd.getText().toString();
        studentTime = aTime.getText().toString();
        studentSchool = aSchool.getText().toString();
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder addnewStudy = new AlertDialog.Builder(this);
        addnewStudy.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //通过调用智能合约的函数写入区块链
                readfromblock();
                return ;
            }
        });
        addnewStudy.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                return;
            }
        });
        addnewStudy.setTitle("提示");
        addnewStudy.setMessage("确认添加吗？");
        addnewStudy.show();
    }

    //初始化凭证
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
            Toast.makeText(addStudy.this, result, Toast.LENGTH_LONG).show();
        }
    }

    void initWeb3j(){
        InitWeb3jTask task = new InitWeb3jTask();
        task.execute(testUrl);
    }

    private  class readTask extends  AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;
            StudyManage studyManage = StudyManage.load(contractAdd, web3j, credentials, gasPrice, gasLimit);
            try {
                RemoteCall<TransactionReceipt> addR = studyManage.addRecord(adminAdress,studentAdd,studentTime,studentSchool);
                result = addR.send().getStatus();
                Log.w("!!!",result);
            } catch (Exception e) {
                result = e.getMessage();
                //Log.w("!!!",e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(addStudy.this, result, Toast.LENGTH_LONG).show();
        }
    }

    void readfromblock(){
        readTask rtask = new readTask();
        rtask.execute();
    }
}
