package com.example.administrator.managestu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class selStudyResult extends AppCompatActivity {

    //Bundle resBundle;
    List<Result> resList = new ArrayList<Result>();
    ListView resListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_study_result);

        //resBundle = this.getIntent().getExtras();

        initResult();
        ResAdapter adapter = new ResAdapter(selStudyResult.this,R.layout.res_item,resList);
        resListView = (ListView) findViewById(R.id.stuList);
        resListView.setAdapter(adapter);
        //Log.w("w","!!!");//
    }

    private void initResult() {
        Result res1 = new Result("1","2012-2015","qs","yz");
        resList.add(res1);
        Result res2 = new Result("2","2015-2019","fj","xd");
        resList.add(res2);
    }
}
