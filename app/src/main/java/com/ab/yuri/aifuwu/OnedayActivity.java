package com.ab.yuri.aifuwu;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.yuri.aifuwu.gson.Exercise;
import com.ab.yuri.aifuwu.gson.Oneday;
import com.ab.yuri.aifuwu.gson.OnedayImg;
import com.ab.yuri.aifuwu.util.HttpUtil;
import com.ab.yuri.aifuwu.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OnedayActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private LinearLayout onedayLayout;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneday);


        mToolbar= (Toolbar) findViewById(R.id.oneday_toolbar);
        onedayLayout= (LinearLayout) findViewById(R.id.oneday_layout);
        img= (ImageView) findViewById(R.id.oneday_img);



        //设置Title
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Glide.with(OnedayActivity.this)
                .load("https://blog.mayuko.cn/api/one-api/img.php")
                .placeholder(R.drawable.loading)
                .crossFade()
                .into(img);

        requestOnedayText();

    }



    private void requestOnedayText(){
        String onedayTextUrl="https://sslapi.hitokoto.cn/";
        HttpUtil.sendOkHttpRequest(onedayTextUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Oneday oneday= Utility.handleOnedayResponse(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOnedayText(oneday);
                    }
                });

            }
        });

    }




    private void showOnedayText(Oneday oneday){
        String onedayText=oneday.text;
        String onedayTextFrom="——"+oneday.from;


        View onedayView= LayoutInflater.from(this).inflate(R.layout.oneday_detail,onedayLayout,false);
        TextView text= (TextView) onedayView.findViewById(R.id.oneday_text);
        TextView from= (TextView) onedayView.findViewById(R.id.oneday_text_from);
        text.setText(onedayText);
        from.setText(onedayTextFrom);

        onedayLayout.addView(onedayView);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
