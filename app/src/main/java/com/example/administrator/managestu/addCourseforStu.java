package com.example.administrator.managestu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.infura.InfuraHttpService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class addCourseforStu extends AppCompatActivity {

    Bundle forCourse;
    String useraddress;
    String privatekey;
    String theuniversity;
    String thetime;
    String addanduniandtime;

    TextView universityView;
    TextView timeView;

    String testUrl = "https://ropsten.infura.io/v3/06e4b5119d0240c6afb64bbb988e9421";//以太坊测试网络
    String contractAdd = "0x074f662cccc086bb12c8dc0efa38e02f53e2c378";
    Web3j web3j;
    Credentials credentials;
    long minigaslimit = 210000*2L;//gaslimit min 210000
    long minigasprice = 20000000000L;
    BigInteger gasLimit = new BigInteger(String.valueOf(minigaslimit+10));
    BigInteger gasPrice = new BigInteger(String.valueOf(minigasprice+10));

    BigInteger courseNum;
    String courseName;
    String courseGrade;

    CourAdapter adapter;
    ListView courseListView;
    List<Course> courseList = new ArrayList<Course>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coursestu);

        forCourse = this.getIntent().getExtras();
        useraddress = forCourse.getString("useraddress");
        privatekey = forCourse.getString("privatekey");
        theuniversity = forCourse.getString("university");
        thetime = forCourse.getString("time");
        addanduniandtime = useraddress + theuniversity + thetime;

        universityView = (TextView) findViewById(R.id.set_university);
        timeView = (TextView) findViewById(R.id.set_time);
        courseListView = (ListView) findViewById(R.id.course_listofstu);

        universityView.setText(theuniversity);
        timeView.setText(thetime);
        initWeb3j();
        initCredential(privatekey);

        Toast mToast;
        mToast = Toast.makeText(addCourseforStu.this, null, Toast.LENGTH_LONG);
        mToast.setText("请稍后···");
        mToast.show();

        initCourse();
    }

    void initCourse(){
        /*Course ctemp1 = new Course("数据库","86");
        Course ctemp2 = new Course("database","90");
        courseList.add(ctemp1);
        courseList.add(ctemp2);*/


        readCnumfromblock();

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
            //Toast.makeText(addCourseforStu.this, result, Toast.LENGTH_LONG).show();
        }
    }

    void initWeb3j(){
        InitWeb3jTask task = new InitWeb3jTask();
        task.execute(testUrl);
    }

    //readTask用来调用智能合约的函数获得记录数
    private  class readCnumTask extends  AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;
            StudyManage studyManage = StudyManage.load(contractAdd, web3j, credentials, gasPrice, gasLimit);
            try {
                courseNum = studyManage.getCnum(addanduniandtime).send();
                result = courseNum.toString();
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
            //Toast.makeText(addCourseforStu.this, result, Toast.LENGTH_LONG).show();
            if(result.equals("0")){
                Toast mToast;
                mToast = Toast.makeText(addCourseforStu.this, null, Toast.LENGTH_LONG);
                mToast.setText("课程信息为空");
                mToast.show();
                return;
            }
            else{
                readCoursefromblock();
            }
        }
    }

    void readCnumfromblock(){
        readCnumTask rtask = new readCnumTask();
        rtask.execute();
    }

    //readRecordTask用来调用智能合约的函数获得每条记录
    private  class readCourseTask extends  AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;
            BigInteger index = new BigInteger(params[0]);
            StudyManage studyManage = StudyManage.load(contractAdd, web3j, credentials, gasPrice, gasLimit);
            try {
                for(;index.compareTo(courseNum)==-1;index=index.add(BigInteger.ONE)){
                    courseName = studyManage.readCourse(addanduniandtime,index).send().getValue1();
                    courseGrade = studyManage.readCourse(addanduniandtime,index).send().getValue2();
                    Course temp = new Course(courseName,courseGrade);
                    courseList.add(temp);
                    Log.w("!!!","read course:"+index.toString());
                }
                result = courseName;
                Log.w("!!!",result);
            } catch (Exception e) {
                result = e.getMessage();
                Log.w("!!!",e.toString());
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Toast.makeText(addCourseforStu.this, result, Toast.LENGTH_LONG).show();
            Toast mToast;
            mToast = Toast.makeText(addCourseforStu.this, null, Toast.LENGTH_LONG);
            mToast.setText("课程信息读取完成");
            mToast.show();

            adapter = new CourAdapter(addCourseforStu.this,R.layout.course_item,courseList);
            courseListView.setAdapter(adapter);
        }
    }

    void readCoursefromblock(){
        readCourseTask rtask = new readCourseTask();
        rtask.execute("0");
    }
}
