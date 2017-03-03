package com.ab.yuri.aifuwu;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.yuri.aifuwu.gson.Oneday;
import com.ab.yuri.aifuwu.gson.WeatherNow;
import com.ab.yuri.aifuwu.service.AutoUpdateService;
import com.ab.yuri.aifuwu.util.HttpUtil;
import com.ab.yuri.aifuwu.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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


        loadOnedayImg();
        requestOnedayText();




    }

    private void loadOnedayImg() {

    }


    private void requestOnedayText(){
        String onedayTextUrl="https://sslapi.hitokoto.cn/";
        HttpUtil.sendOkHttpRequest(onedayTextUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OnedayActivity.this,"网络错误，请重试",Toast.LENGTH_SHORT).show();
                    }
                });
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

        onedayLayout.removeAllViews();

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String picUrl=prefs.getString("oneday_pic",null);
        if (picUrl!=null){
            //有缓存时直接解析URL
            Glide.with(OnedayActivity.this)
                .load(picUrl)
                    .placeholder(R.drawable.loading)
                    .crossFade()
                    .into(img);
        }else {
            //没有缓存时直接开启线程爬取一个·one的图片
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Document doc = Jsoup.connect("http://wufazhuce.com/").get();
                        Elements pic = doc.select("meta[property=og:image]");
                        final String picUrl = pic.get(1).attr("content");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(OnedayActivity.this).edit();
                                editor.putString("oneday_pic", picUrl);
                                editor.apply();
                                Glide.with(OnedayActivity.this)
                                        .load(picUrl)
                                        .placeholder(R.drawable.loading)
                                        .crossFade()
                                        .into(img);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }





        View onedayView= LayoutInflater.from(this).inflate(R.layout.oneday_detail,onedayLayout,false);
        TextView text= (TextView) onedayView.findViewById(R.id.oneday_text);
        TextView from= (TextView) onedayView.findViewById(R.id.oneday_text_from);
        text.setText(onedayText);
        from.setText(onedayTextFrom);

        onedayLayout.addView(img);
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
