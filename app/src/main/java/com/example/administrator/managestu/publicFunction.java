package com.example.administrator.managestu;

import android.os.AsyncTask;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.infura.InfuraHttpService;

import java.math.BigInteger;

public class publicFunction {

    String useraddress ="0xC60D8DE6625B9DDbC579e502dEF8c3E8933b8A3b" ;//账户地址
    String privatekey = "C16E811A0F025ED8165699745D5CC927CC7FBAE8AA29CF036A99F0E75F55B950";//账户私钥
    String testUrl = "https://ropsten.infura.io/v3/06e4b5119d0240c6afb64bbb988e9421";//以太坊测试网络
    String contractAdd = "0x09463f7413fc287ee34510c8be94565a60463844";
    Web3j web3j;
    Credentials credentials;
    long minigaslimit = 210000*2L;//gaslimit min 210000
    long minigasprice = 20000000000L;
    BigInteger gasLimit = new BigInteger(String.valueOf(minigaslimit+10));
    BigInteger gasPrice = new BigInteger(String.valueOf(minigasprice+10));

    public void setUseraddress(String add){
        useraddress = add;
    }
    public void setPrivatekey(String pkey){
        privatekey = pkey;
    }

    public Web3j getWeb3j() {
        return web3j;
    }

    void initWeb3j(){
        InitWeb3jTask task = new InitWeb3jTask();
        task.execute(testUrl);
    }

    public Credentials initCredential(String privatekey){
        Credentials credentials = Credentials.create(privatekey);
        return credentials;
    }

    public class InitWeb3jTask extends AsyncTask<String, String, String> {
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

        /*@Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(MainActivity_forlogin.this, result, Toast.LENGTH_LONG).show();
        }*/
    }
}
