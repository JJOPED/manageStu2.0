package com.example.administrator.managestu;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.infura.InfuraHttpService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class selForAdmin extends AppCompatActivity {

    //来自上一个activity的信息
    Bundle adminInfo;
    String adminaddress;
    String privatekey;
    String useraddress;
    EditText inAdd;

    //连接到以太坊测试网络
    String testUrl = "https://ropsten.infura.io/v3/06e4b5119d0240c6afb64bbb988e9421";//以太坊测试网络
    String contractAdd = "0x074f662cccc086bb12c8dc0efa38e02f53e2c378";
    Web3j web3j;
    Credentials credentials;
    long minigaslimit = 210000*2L;//gaslimit min 210000
    long minigasprice = 20000000000L;
    BigInteger gasLimit = new BigInteger(String.valueOf(minigaslimit+10));
    BigInteger gasPrice = new BigInteger(String.valueOf(minigasprice+10));

    TextView stuName;
    TextView stuSex;
    TextView stuAge;
    TextView stuRec;

    String pname, psex, page;
    BigInteger recordNum;
    String recordTime;
    String recordSchool;
    String recordConfirm;

    ResAdapter adapter ;
    List<Result> resList = new ArrayList<Result>();
    ListView resListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_for_admin);
        adminInfo = this.getIntent().getExtras();
        adminaddress = adminInfo.getString("useraddress");
        privatekey = adminInfo.getString("privatekey");
        initCredential(privatekey);

        initWeb3j();

        inAdd = (EditText) findViewById(R.id.inputAdd);
        stuName = (TextView) findViewById(R.id.stu_name);
        stuSex = (TextView) findViewById(R.id.stu_sex);
        stuAge = (TextView) findViewById(R.id.stu_age);
        stuRec = (TextView) findViewById(R.id.stu_rnum);
        resListView = (ListView) findViewById(R.id.selResforAdmin);


        Button buttonSelforAdmin = (Button) findViewById(R.id.selOK);
        //Log.w("W","!!!!");//ok
        buttonSelforAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resList.clear();
                //adapter.notifyDataSetChanged();

                useraddress = inAdd.getText().toString();
                Log.w("!!!","stuaddress:"+useraddress);

                initResult();

                //adapter = new ResAdapter(selForAdmin.this,R.layout.res_item,resList);
                //resListView.setAdapter(adapter);
            }
        });
    }

    private void initResult() {

        readInfofromblock();
        /*Result restemp = new Result("1","2015","Xiamen","abc");
        resList.add(restemp);
        adapter = new ResAdapter(selForAdmin.this,R.layout.res_item,resList);
        resListView.setAdapter(adapter);
        resListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent toAddCourse = new Intent(selForAdmin.this,addCourse.class);
                startActivity(toAddCourse);
            }
        });*/

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
            Toast.makeText(selForAdmin.this, result, Toast.LENGTH_LONG).show();
        }
    }

    void initWeb3j(){
        InitWeb3jTask task = new InitWeb3jTask();
        task.execute(testUrl);
    }

    //readInfoTask用来调用智能合约的函数获得查询学生的信息
    private  class readInfoTask extends  AsyncTask<String, String, String> {
        @Override
        protected  String doInBackground(String... params) {
            String result;
            StudyManage studyManage = StudyManage.load(contractAdd,web3j,credentials,gasPrice,gasLimit);
            try {
                pname = studyManage.readPersonInfoAfterLogin(useraddress).send().getValue1();
                psex = studyManage.readPersonInfoAfterLogin(useraddress).send().getValue2();
                page = studyManage.readPersonInfoAfterLogin(useraddress).send().getValue3().toString();
                result = "readname:"+pname;
                Log.w("!!!","info result:"+result);
            } catch (Exception e) {
                result = e.getMessage();
                Log.w("!!!","info exception"+e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(selForAdmin.this, result, Toast.LENGTH_LONG).show();
            stuName.setText(pname);
            stuSex.setText(psex);
            stuAge.setText(page);
            readRnumfromblock();
        }
    }

    void readInfofromblock(){
        readInfoTask rtask = new readInfoTask();
        rtask.execute();
    }

    //readRnumTask用来调用智能合约的函数获得记录数目
    private  class readRnumTask extends  AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;
            StudyManage studyManage = StudyManage.load(contractAdd, web3j, credentials, gasPrice, gasLimit);
            try {
                recordNum = studyManage.getRnum(useraddress).send();
                result = recordNum.toString();
                Log.w("!!!","rnum result:"+result);
            } catch (Exception e) {
                result = e.getMessage();
                Log.w("!!!","rnum dexception"+e.toString());
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(selForAdmin.this, result, Toast.LENGTH_LONG).show();
            stuRec.setText(recordNum.toString());
            readRecordfromblock();
        }
    }

    void readRnumfromblock(){
        readRnumTask rtask = new readRnumTask();
        rtask.execute();
    }

    //readRecordTask用来调用智能合约的函数获得记录
    private  class readRecordTask extends  AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;
            BigInteger index = new BigInteger(params[0]);
            StudyManage studyManage = StudyManage.load(contractAdd, web3j, credentials, gasPrice, gasLimit);
            try {
                for(;index.compareTo(recordNum)==-1;index=index.add(BigInteger.ONE)){
                    recordTime = studyManage.readRecordforStu(useraddress,index).send().getValue1();
                    recordSchool = studyManage.readRecordforStu(useraddress,index).send().getValue2();
                    recordConfirm = studyManage.readRecordforStu(useraddress,index).send().getValue3();
                    Result restemp = new Result(index.add(BigInteger.ONE).toString(),recordTime,recordSchool,recordConfirm);
                    resList.add(restemp);
                    //Log.w("!!!","read record:"+index.toString());
                }
                result = recordTime;
                Log.w("!!!","recordresult:"+result);
            } catch (Exception e) {
                result = e.getMessage();
                Log.w("!!!","exception:"+e.toString());
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(selForAdmin.this, result, Toast.LENGTH_LONG).show();
            adapter = new ResAdapter(selForAdmin.this,R.layout.res_item,resList);
            resListView.setAdapter(adapter);
            resListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String theuniversity = resList.get(i).theSchool;
                    String  thetime = resList.get(i).theTime;
                    Bundle foraddCourse = new Bundle();
                    foraddCourse.putString("useraddress",useraddress);
                    foraddCourse.putString("privatekey",privatekey);
                    foraddCourse.putString("university",theuniversity);
                    foraddCourse.putString("time",thetime);

                    Intent toAddCourse = new Intent(selForAdmin.this,addCourse.class);
                    toAddCourse.putExtras(foraddCourse);
                    startActivity(toAddCourse);
                }
            });
        }
    }

    void readRecordfromblock(){
        readRecordTask rtask = new readRecordTask();
        rtask.execute("0");
    }

}
