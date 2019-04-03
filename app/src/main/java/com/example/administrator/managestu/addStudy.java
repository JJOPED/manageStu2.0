package com.example.administrator.managestu;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class addStudy extends AppCompatActivity {

    studyRecord record;
    EditText aAdd;
    EditText aTime;
    EditText aSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_study);

        //保存新增的学籍信息
        aAdd = (EditText)findViewById(R.id.newAdd);
        aTime = (EditText) findViewById(R.id.newTime);
        aSchool = (EditText) findViewById(R.id.newSchool);
       /* record.addofStudent = aAdd.getText().toString();
        record.timeofStudent = aTime.getText().toString();
        record.addofStudent = aSchool.getText().toString();*/
    }

    public void addStudy(View view) {
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder addnewStudy = new AlertDialog.Builder(this);
        addnewStudy.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //通过调用智能合约的函数写入区块链:pushStudy()
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
}
