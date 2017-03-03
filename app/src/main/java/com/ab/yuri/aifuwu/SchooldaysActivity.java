package com.ab.yuri.aifuwu;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ab.yuri.aifuwu.gson.WeatherNow;
import com.ab.yuri.aifuwu.service.AutoUpdateService;
import com.ab.yuri.aifuwu.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class SchooldaysActivity extends AppCompatActivity {



    private ImageView schooldaysContentView;
    private ImageView schooldaysTitleView;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schooldays);

        toolbar = (Toolbar) findViewById(R.id.schooldays_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.schooldays_collapsing_toolbar);
        schooldaysTitleView = (ImageView) findViewById(R.id.schooldays_image_view);
        schooldaysContentView = (ImageView) findViewById(R.id.schooldays_content_img);


        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        //设置Title

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle("校历");
        Glide.with(this).load(R.drawable.schooldays_title_view).into(schooldaysTitleView);


        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String imgUrl=prefs.getString("schooldays_pic",null);
        if (imgUrl!=null){
            //有缓存时直接解析URL
            showSchoolDays(imgUrl);
        }else {
            //没有缓存时开启子线程从网络爬取校历图片
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Document doc;
                    try {
                        doc = Jsoup.connect("http://jwc.njupt.edu.cn/s/24/t/923/5a/df/info88799.htm").get();
                        Elements jpgs = doc.select("img[src$=.jpg]");
                        final String schooldaysImgUrl= jpgs.attr("src");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(SchooldaysActivity.this).edit();
                                editor.putString("schooldays_pic",schooldaysImgUrl);
                                editor.apply();
                                showSchoolDays(schooldaysImgUrl);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }).start();
        }


    }

    //展示图片
    private void showSchoolDays(String imgUrl){
    final String schoolDaysUrl="http://jwc.njupt.edu.cn"+imgUrl;
    Glide.with(this).load(schoolDaysUrl).into(schooldaysContentView);

    //点击弹开大图
    schooldaysContentView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LayoutInflater inflater=LayoutInflater.from(SchooldaysActivity.this);
            View dialogView=inflater.inflate(R.layout.schooldays_dialog,null);
            final AlertDialog dialog=new AlertDialog.Builder(SchooldaysActivity.this).create();
            final PhotoView dialogImg= (PhotoView) dialogView.findViewById(R.id.schooldays_dialog_imageView);
            Glide.with(SchooldaysActivity.this).load(schoolDaysUrl).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    dialogImg.setImageDrawable(resource);
                }
            });

            dialog.setView(dialogView);
            dialog.show();
            dialogImg.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    dialog.cancel();
                }

                @Override
                public void onOutsidePhotoTap() {
                    dialog.cancel();
                }
            });


        }
    });

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
