package com.example.administrator.managestu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class selForAdmin extends AppCompatActivity {

    List<Result> resList = new ArrayList<Result>();
    ListView resListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_for_admin);

        Button buttonSelforAdmin = (Button) findViewById(R.id.selOK);
        //Log.w("W","!!!!");//ok
        buttonSelforAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initResult();
                ResAdapter adapter = new ResAdapter(selForAdmin.this,R.layout.res_item,resList);
                resListView = (ListView) findViewById(R.id.selResforAdmin);
                resListView.setAdapter(adapter);
            }
        });
        //Log.w("W","!!!!");

    }

    /*public void selStuforAdmin(View view) {
        //Log.w("W","!!!!");
        initResult();
        ResAdapter adapter = new ResAdapter(selForAdmin.this,R.layout.item,resList);
        resListView = (ListView) findViewById(R.id.stuList);
        resListView.setAdapter(adapter);
    }*/

    private void initResult() {
        //Log.w("W","!!!!");
        Result res1 = new Result("1","2012-2015","qsxisxbh","yzdwc");
        resList.add(res1);
        Result res2 = new Result("2","2015-2019","fjqxdxcd","xdceqdcx");
        resList.add(res2);
    }
}
