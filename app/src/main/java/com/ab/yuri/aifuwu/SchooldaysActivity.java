package com.ab.yuri.aifuwu;

import android.app.Dialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

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
        schooldaysContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater=LayoutInflater.from(SchooldaysActivity.this);
                View dialogView=inflater.inflate(R.layout.schooldays_dialog,null);
                final AlertDialog dialog=new AlertDialog.Builder(SchooldaysActivity.this).create();
                PhotoView dialogImg= (PhotoView) dialogView.findViewById(R.id.schooldays_dialog_imageView);
                dialogImg.setImageResource(R.drawable.schooldays_content_view);
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
