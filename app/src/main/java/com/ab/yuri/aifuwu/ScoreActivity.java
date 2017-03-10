package com.ab.yuri.aifuwu;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.yuri.aifuwu.RecyclerView.ScoreAdapter;
import com.ab.yuri.aifuwu.gson.GPA;
import com.ab.yuri.aifuwu.util.HttpUtil;
import com.ab.yuri.aifuwu.util.Utility;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ScoreActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView scoreTitleView;
    private List<GPA.DetailBean> scoreList=new ArrayList<>();
    private ImageView bgScore;
    private SwipeRefreshLayout refreshLayout;
    private AppBarLayout mAppBarLayout;
    private LinearLayout scoreLayout;
    private NestedScrollView scoreScroll;
    public ProgressDialog progressDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        mToolbar= (Toolbar) findViewById(R.id.score_toolbar);
        mCollapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.score_collapsing_toolbar);
        scoreTitleView= (ImageView) findViewById(R.id.score_image_view);
        bgScore= (ImageView) findViewById(R.id.bg_score);
        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.refresh_score);
        mAppBarLayout= (AppBarLayout) findViewById(R.id.score_appBar);
        scoreLayout= (LinearLayout) findViewById(R.id.score_content);
        scoreScroll= (NestedScrollView) findViewById(R.id.score_scroll);


        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        final String account=prefs.getString("account","");
        final String password=prefs.getString("password_save","");

        /*
        设置Title
         */
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle("成绩查询");

        Glide.with(this).load(R.drawable.score_title).into(scoreTitleView);
        Glide.with(this).load(R.drawable.bg_score).into(bgScore);

        showProgressDialog();
        requestScore(account,password);

        scoreLayout.setVisibility(View.INVISIBLE);


        /*
        刷新并使得刷新位置在最上方。
         */
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestScore(account,password);
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset>=0){
                    refreshLayout.setEnabled(true);
                }else {
                    refreshLayout.setEnabled(false);
                }
            }
        });
    }




    /*
    通过网络请求成绩数据
     */
    private void requestScore(final String id,final String password){
        String scoreUrl=""+id+"&student_password="+password;
        HttpUtil.sendOkHttpRequest(scoreUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ScoreActivity.this,"网络错误，请重试",Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final GPA gpa= Utility.handleGPAResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if (gpa != null) {
                            if (gpa.gpa!=null){
                                showScore(gpa);
                            }else {
                                Toast.makeText(ScoreActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(ScoreActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();
                        }
                        refreshLayout.setRefreshing(false);
                    }
                });


            }
        });
    }

    /*
    展示数据
     */
    private void showScore(GPA gpa){
        scoreLayout.removeAllViews();
        String GPA=gpa.gpa;

        for (int i=gpa.detail.size()-1;i>=0;i--){
            scoreList.add(gpa.detail.get(i));
        }

        View GPAView= LayoutInflater.from(this).inflate(R.layout.score_gpa,scoreLayout,false);
        TextView gpaView= (TextView) GPAView.findViewById(R.id.gpa);
        gpaView.setText(GPA);
        scoreLayout.addView(GPAView);

        View scoreView=LayoutInflater.from(this).inflate(R.layout.score_detail,scoreLayout,false);
        RecyclerView recyclerView= (RecyclerView) scoreView.findViewById(R.id.score_detail);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        ScoreAdapter adapter=new ScoreAdapter(scoreList);
        recyclerView.setAdapter(adapter);
        scoreScroll.smoothScrollTo(0,0);
        scoreLayout.addView(scoreView);


        scoreLayout.setVisibility(View.VISIBLE);


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

    /*
   显示进度框
    */
    public void showProgressDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在为您查询考试成绩...\n（由于期末用户高峰，查询速度可能较慢）");
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
}