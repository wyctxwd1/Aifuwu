package com.ab.yuri.aifuwu;

import android.app.Dialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SchooldaysActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schooldays);

        Toolbar toolbar= (Toolbar) findViewById(R.id.schooldays_toolbar);
        CollapsingToolbarLayout collapsingToolbar= (CollapsingToolbarLayout) findViewById(R.id.schooldays_collapsing_toolbar);
        ImageView schooldaysTitleView= (ImageView) findViewById(R.id.schooldays_image_view);
        ImageView schooldaysContentView= (ImageView) findViewById(R.id.schooldays_content_img);


        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle("校历");
        Glide.with(this).load(R.drawable.schooldays_title_view).into(schooldaysTitleView);
        Glide.with(this).load(R.drawable.schooldays_content_view).into(schooldaysContentView);













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
