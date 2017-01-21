package com.ab.yuri.aifuwu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ab.yuri.aifuwu.gson.Info;
import com.ab.yuri.aifuwu.util.HttpUtil;
import com.ab.yuri.aifuwu.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    public SharedPreferences pref;
    private EditText accountExit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;
    public ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountExit= (EditText) findViewById(R.id.account);
        passwordEdit= (EditText) findViewById(R.id.password);
        login= (Button) findViewById(R.id.login);
        rememberPass= (CheckBox) findViewById(R.id.remember_pass);

        pref= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember=pref.getBoolean("remember_password",false);
        if (isRemember){
            //将账号密码设置到文本框中
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            accountExit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account=accountExit.getText().toString();
                String password=passwordEdit.getText().toString();
                requestInfo(account,password);
            }
        });


    }
    /*
    显示进度框
     */
    public void showProgressDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /*
    关闭进度框
     */
    public void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }



    public void requestInfo(final String account,final String password){
        showProgressDialog();
        String infoUrl="http://115.28.223.204/zf.php?method=info&app_secret=neoix&student_id="+account+"&student_password="+password;
        HttpUtil.sendOkHttpRequest(infoUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(LoginActivity.this,"网络错误，请重新登录",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Info info= Utility.handleInfoResponse(responseText);
                Log.d("A", "onResponse: "+info.studentName+info.studentDepatment+info.studentMajor);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if (info.studentName!=null){
                            SharedPreferences.Editor editor=pref.edit();
                            if (rememberPass.isChecked()){
                                editor.putBoolean("remember_password",true);
                                editor.putString("account",account);
                                editor.putString("password",password);
                            }else {
                                editor.clear();
                            }
                            editor.apply();
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra(MainActivity.STUDENT_NAME,info.studentName);
                            intent.putExtra(MainActivity.STUDENT_DEPARTMENT,info.studentDepatment);
                            intent.putExtra(MainActivity.STUDENT_MAJOR,info.studentMajor);
                            startActivity(intent);
                        }else {
                            Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
