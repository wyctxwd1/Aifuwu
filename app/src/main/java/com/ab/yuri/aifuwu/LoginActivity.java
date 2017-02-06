package com.ab.yuri.aifuwu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ab.yuri.aifuwu.gson.Info;
import com.ab.yuri.aifuwu.util.HttpUtil;
import com.ab.yuri.aifuwu.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;


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
    public static String idNumeber;
    public static String idPassword;
    private ImageView bgLogin;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountExit= (EditText) findViewById(R.id.account);
        passwordEdit= (EditText) findViewById(R.id.password);
        login= (Button) findViewById(R.id.login);
        rememberPass= (CheckBox) findViewById(R.id.remember_pass);
        bgLogin= (ImageView) findViewById(R.id.bg_login);

        pref= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember=pref.getBoolean("remember_password",false);
        String account=pref.getString("account","");
        accountExit.setText(account);
        if (isRemember){
            //将账号密码设置到文本框中
            String password=pref.getString("password","");
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

        idNumeber=accountExit.getText().toString();
        idPassword=passwordEdit.getText().toString();



        /*
        加载必应每日一图作为背景
         */
        String bingPic=pref.getString("bing_pic",null);
        if (bingPic!=null){
            Glide.with(this).load(bingPic).into(bgLogin);
        }else {
            loadBingPic();
        }
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if (info.studentName!=null){
                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("account",account);
                            if (rememberPass.isChecked()){
                                editor.putBoolean("remember_password",true);
                                editor.putString("password",password);
                            }else {
                                editor.remove("password");
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


    /*
    加载必应每日一图
     */

    private void loadBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(LoginActivity.this).load(bingPic).into(bgLogin);
                    }
                });

            }
        });
    }
}
