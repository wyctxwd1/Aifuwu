package com.ab.yuri.aifuwu;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.yuri.aifuwu.gson.Exercise;
import com.ab.yuri.aifuwu.util.HttpUtil;
import com.ab.yuri.aifuwu.util.Utility;
import com.ab.yuri.aifuwu.widget.NoScrollListView;
import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RunActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView runTitleView;
    private LinearLayout runLayout;
    private ArrayAdapter<String> adapter;
    private NestedScrollView scrollView;
    private ImageView bgRun;
    private SwipeRefreshLayout refreshLayout;
    private AppBarLayout mAppBarLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        mToolbar= (Toolbar) findViewById(R.id.run_toolbar);
        mCollapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.run_collapsing_toolbar);
        runTitleView= (ImageView) findViewById(R.id.run_image_view);
        runLayout= (LinearLayout) findViewById(R.id.run_content);
        scrollView= (NestedScrollView) findViewById(R.id.run_scroll);
        bgRun= (ImageView) findViewById(R.id.bg_run);
        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.refresh_run);
        mAppBarLayout= (AppBarLayout) findViewById(R.id.run_appBar);


        //设置Title
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle("跑操查询");

        Glide.with(this).load(R.drawable.run_title).into(runTitleView);
        Glide.with(this).load(R.drawable.bg_run).into(bgRun);

        requestRun(LoginActivity.idNumeber,MainActivity.stuName);

        runLayout.setVisibility(View.INVISIBLE);


        /*
        刷新并使得刷新位置在最上方。
         */
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestRun(LoginActivity.idNumeber,MainActivity.stuName);

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
    通过网络请求跑操数据
     */
    private void requestRun(final String id,final String name){
        String runUrl="http://115.28.223.204/exercise?method=fetch&type=DETAIL&student_id="+id+"&student_name="+name;
        HttpUtil.sendOkHttpRequest(runUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RunActivity.this,"网络错误，请重试",Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Exercise exercise= Utility.handleExerciseResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (exercise!=null&&"success".equals(exercise.query)){
                            showRun(exercise);
                        }else {
                            showZero();
                            Toast.makeText(RunActivity.this,"您本学期并未跑步",Toast.LENGTH_SHORT).show();
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
    private void showRun(Exercise exercise){
        String times=exercise.times;
        runLayout.removeAllViews();

        View timesView= LayoutInflater.from(this).inflate(R.layout.run_times,runLayout,false);
        TextView runTimes= (TextView)timesView.findViewById(R.id.run_times);
        runTimes.setText(times);
        runLayout.addView(timesView);

        View detailView=LayoutInflater.from(this).inflate(R.layout.run_detail,runLayout,false);
        NoScrollListView listView= (NoScrollListView) detailView.findViewById(R.id.run_detail);
        adapter=new ArrayAdapter<String>(this,R.layout.run_list_item_1,exercise.list);
        listView.setAdapter(adapter);
        scrollView.smoothScrollTo(0,0);
        runLayout.addView(detailView);
        runLayout.setVisibility(View.VISIBLE);


    }

    private void showZero(){
        runLayout.removeAllViews();
        View timesView= LayoutInflater.from(this).inflate(R.layout.run_times,runLayout,false);
        TextView runTimes= (TextView)timesView.findViewById(R.id.run_times);
        runTimes.setText("0");
        runLayout.addView(timesView);
        runLayout.setVisibility(View.VISIBLE);

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
