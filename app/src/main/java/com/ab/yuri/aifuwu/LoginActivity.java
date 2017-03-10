package com.ab.yuri.aifuwu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
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
    private RelativeLayout bg;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountExit= (EditText) findViewById(R.id.account);
        passwordEdit= (EditText) findViewById(R.id.password);
        login= (Button) findViewById(R.id.login);
        rememberPass= (CheckBox) findViewById(R.id.remember_pass);
        bg= (RelativeLayout) findViewById(R.id.login_bg);


        //沉浸式状态栏
        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }



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





        /*
        设置背景
         */

        Glide.with(this).load(R.drawable.bg_login).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bg.setBackground(resource);
                }
            }
        });
    }


    /*
    显示进度框
     */
    public void showProgressDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在登录...");
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
        String infoUrl="http://115.28.223.204/zf.php?method=info&app_secret=xxxxx&student_id="+account+"&student_password="+password;
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
                        if (info!=null&&info.studentName!=null){
                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("account",account);
                            editor.putString("password_save",password);
                            editor.putString("student_name",info.studentName);
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

}
