package com.ab.yuri.aifuwu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.yuri.aifuwu.RecyclerView.Uses;
import com.ab.yuri.aifuwu.RecyclerView.UsesAdapter;
import com.ab.yuri.aifuwu.gson.WeatherNow;
import com.ab.yuri.aifuwu.util.HttpUtil;
import com.ab.yuri.aifuwu.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String STUDENT_NAME="student_name";
    public static final String STUDENT_DEPARTMENT="student_department";
    public static final String STUDENT_MAJOR="student_major";
    public static String stuName;
    private List<Uses> usesList=new ArrayList<>();
    private UsesAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private Toolbar mainToolbar;
    private ImageView bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        final String studentName=intent.getStringExtra(STUDENT_NAME);
        final String studentDepartment=intent.getStringExtra(STUDENT_DEPARTMENT);
        final String studentMajor=intent.getStringExtra(STUDENT_MAJOR);
        stuName=intent.getStringExtra(STUDENT_NAME);

        mainToolbar= (Toolbar) findViewById(R.id.main_toolbar);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navView= (NavigationView) findViewById(R.id.nav_view);
        bg= (ImageView) findViewById(R.id.bg);
        final RecyclerView recyclerView= (RecyclerView) findViewById(R.id.main_recycler_view);


        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }





        setSupportActionBar(mainToolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_main_menu_left);
        }



        /*
        添加侧边栏head数据和图片
         */
        final View headerLayout = navView.inflateHeaderView(R.layout.nav_header);
        TextView stuName= (TextView) headerLayout.findViewById(R.id.stu_name);
        TextView stuDepartment= (TextView) headerLayout.findViewById(R.id.stu_department);
        TextView stuMajor= (TextView) headerLayout.findViewById(R.id.stu_major);
        stuName.setText(studentName);
        stuDepartment.setText(studentDepartment);
        stuMajor.setText(studentMajor);
        Glide.with(this).load(R.drawable.bg_head).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    headerLayout.setBackground(resource);
                }
            }
        });

        /*
        加载背景
         */
        Glide.with(this).load(R.drawable.bg).into(bg);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.run:
                        Intent run=new Intent(MainActivity.this,RunActivity.class);
                        startActivity(run);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.score:
                        Intent score=new Intent(MainActivity.this,ScoreActivity.class);
                        startActivity(score);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.schooldays:
                        Intent schooldays=new Intent(MainActivity.this,SchooldaysActivity.class);
                        startActivity(schooldays);
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.oneday:
                        Intent oneday=new Intent(MainActivity.this, OnedayActivity.class);
                        startActivity(oneday);
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.about_us:
                        Intent aboutUs=new Intent(MainActivity.this,AboutUsActivity.class);
                        startActivity(aboutUs);
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.quit:
                        Intent quit=new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(quit);
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });


        initUses();
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new UsesAdapter(usesList);
        recyclerView.setAdapter(adapter);


        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if (weatherString!=null){
            //有缓存时直接解析天气数据
            WeatherNow weatherNow=Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weatherNow);
        }else {
            //没有缓存时直接解析天气数据
            requestWeather();
        }

    }

    private void initUses() {
        Uses run=new Uses("跑操查询",R.drawable.img_run_big);
        usesList.add(run);
        Uses score=new Uses("成绩查询",R.drawable.img_score_big);
        usesList.add(score);
        Uses schooldays=new Uses("校历",R.drawable.img_schooldays_big);
        usesList.add(schooldays);
        Uses library=new Uses("一言",R.drawable.img_oneday_big);
        usesList.add(library);
    }

    /*
   加载天气数据
    */
    public void requestWeather(){
        final String weatherUrl="http://guolin.tech/api/weather?cityid=CN101190101&key=56ad57d30e4e43888f020ed6a592dd40";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final WeatherNow weatherNow= Utility.handleWeatherResponse(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherNow!=null&&"ok".equals(weatherNow.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weatherNow);
                        }
                    }
                });

            }
        });
    }

     /*
    展示天气数据
     */
    private void showWeatherInfo(WeatherNow weatherNow){
        String updateTime =weatherNow.basic.update.loc.split(" ")[0];
        String weatherInfo = weatherNow.now.cond.txt;
        String degree = weatherNow.now.tmp + "℃";
        TextView update= (TextView) findViewById(R.id.title_update_time);
        TextView weatherInfoText= (TextView) findViewById(R.id.weather_info_text);
        TextView degreeText= (TextView) findViewById(R.id.degree_text);
        update.setText(updateTime);
        weatherInfoText.setText(weatherInfo);
        degreeText.setText(degree);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }
}
