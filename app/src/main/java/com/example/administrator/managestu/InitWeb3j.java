package com.example.administrator.managestu;

import android.os.AsyncTask;
import android.widget.Toast;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.infura.InfuraHttpService;

public class InitWeb3j extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params){
        String url = params[0];
        String result;
        Web3j web3j;
        try {
            InfuraHttpService initHttpService = new InfuraHttpService(url);
            //HttpService initHttpService = new HttpService(url);
            web3j = Web3jFactory.build(initHttpService);
            result = "InitWeb3jTask is ok!";
        }catch (Exception e){
            result = "InitWeb3jTask is not ok!";
        }
        return result;
    }
    /*@Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
    }*/
}
