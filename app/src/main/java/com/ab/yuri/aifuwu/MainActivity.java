package com.ab.yuri.aifuwu;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Uses> usesList=new ArrayList<>();
    private UsesAdapter adapter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar main_toolbar= (Toolbar) findViewById(R.id.main_toolbar);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.main_drawer_layout);
        NavigationView navView= (NavigationView) findViewById(R.id.nav_view);
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.main_recycler_view);


        setSupportActionBar(main_toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_main_menu_left);
        }


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.run:
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.schooldays:
                        Intent intent=new Intent(MainActivity.this,SchooldaysActivity.class);
                        startActivity(intent);
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


    }

    private void initUses() {
        Uses run=new Uses("跑操查询",R.drawable.img_run_big);
        usesList.add(run);
        Uses score=new Uses("成绩查询",R.drawable.img_score_big);
        usesList.add(score);
        Uses schooldays=new Uses("校历",R.drawable.img_schooldays_big);
        usesList.add(schooldays);
        Uses notice=new Uses("校新闻通知",R.drawable.img_notice_big);
        usesList.add(notice);
        Uses library=new Uses("图书馆",R.drawable.img_library_big);
        usesList.add(library);
        Uses about_us=new Uses("关于我们",R.drawable.img_about_us_big);
        usesList.add(about_us);
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
