package com.example.administrator.managestu;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.infura.InfuraHttpService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class selStudyResult extends AppCompatActivity {

    Bundle userInfo;
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

    BigInteger recordNum;
    String recordTime;
    String recordSchool;
    String recordConfirm;
    //Boolean retRecState = false;
    //Boolean retReadAllState = false;


    ResAdapter adapter;
    List<Result> resList = new ArrayList<Result>();
    ListView resListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_study_result);
        resList.clear();
        initWeb3j();

        userInfo = this.getIntent().getExtras();
        useraddress = userInfo.getString("useraddress");
        privatekey = userInfo.getString("privatekey");
        initCredential(privatekey);

        resListView = (ListView) findViewById(R.id.stuList);
        initResult();

    }

    private void initResult() {
        readRnumfromblock();
        Log.w("W","initList");

        /*Result res1 = new Result("1","2012-2015","qsxwqdxcd","yzedxdwexd");
        resList.add(res1);
        Result res2 = new Result("2","2015-2019","fjcwexdw","xdwxdewdx");
        resList.add(res2);
        adapter = new ResAdapter(selStudyResult.this,R.layout.res_item,resList);
        resListView.setAdapter(adapter);

        resListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent toAddCourse = new Intent(selStudyResult.this,addCourse.class);
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
            Toast.makeText(selStudyResult.this, result, Toast.LENGTH_LONG).show();
        }
    }

    void initWeb3j(){
        InitWeb3jTask task = new InitWeb3jTask();
        task.execute(testUrl);
    }

    //readRnumTask用来调用智能合约的函数获得记录数
    private  class readRnumTask extends  AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;
            StudyManage studyManage = StudyManage.load(contractAdd, web3j, credentials, gasPrice, gasLimit);
            try {
                recordNum = studyManage.getRnum(useraddress).send();
                result = recordNum.toString();
                Log.w("!!!","result:"+result);
            } catch (Exception e) {
                result = e.getMessage();
                Log.w("!!!","exception"+e.getMessage());
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(selStudyResult.this, result, Toast.LENGTH_LONG).show();
            readRecordfromblock();
        }
    }

    void readRnumfromblock(){
        readRnumTask rtask = new readRnumTask();
        rtask.execute();
    }

    //readRecordTask用来调用智能合约的函数获得每条记录
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
                    Log.w("!!!","read record:"+index.toString());
                }
                result = recordTime;
                Log.w("!!!",result);
                //retReadAllState = true;
            } catch (Exception e) {
                result = e.getMessage();
                Log.w("!!!",e.getMessage());
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(selStudyResult.this, result, Toast.LENGTH_LONG).show();
            adapter = new ResAdapter(selStudyResult.this,R.layout.res_item,resList);
            resListView.setAdapter(adapter);
            resListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String theuniversity = resList.get(i).theSchool;
                    String thetime = resList.get(i).theTime;
                    Bundle forCourse = new Bundle();
                    forCourse.putString("useraddress",useraddress);
                    forCourse.putString("privatekey",privatekey);
                    forCourse.putString("university",theuniversity);
                    forCourse.putString("time",thetime);
                    Intent toCourselist = new Intent(selStudyResult.this,addCourseforStu.class);
                    toCourselist.putExtras(forCourse);
                    startActivity(toCourselist);
                }
            });
        }
    }

    void readRecordfromblock(){
        readRecordTask rtask = new readRecordTask();
        rtask.execute("0");
    }
}
