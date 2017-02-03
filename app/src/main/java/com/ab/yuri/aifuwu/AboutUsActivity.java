package com.ab.yuri.aifuwu;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity{

    private ViewPager vPager;
    private ImageView tabLine;
    private TextView feedback;
    private TextView aboutUs;
    private List<Fragment> fragmentList=new ArrayList<Fragment>();
    private FragmentAdapter mAdapter;
    private int currIndex;
    private int bmpW;
    private int offset;
    private int screenWidth;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mToolbar= (Toolbar) findViewById(R.id.about_us_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initTextView();
        initCursor();
        initViewPager();

    }

    private void initTextView(){
        aboutUs= (TextView) findViewById(R.id.about_us_page);
        feedback= (TextView) findViewById(R.id.feedback);

        aboutUs.setOnClickListener(new txListener(0));
        feedback.setOnClickListener(new txListener(1));
    }

    private class txListener implements View.OnClickListener{
        private int index=0;

        public txListener(int i) {
            index =i;
        }
        @Override
        public void onClick(View v) {
            vPager.setCurrentItem(index);
        }
    }


    private void initCursor(){
        tabLine= (ImageView) findViewById(R.id.cursor);
        bmpW=BitmapFactory.decodeResource(getResources(),R.drawable.cursor).getWidth();
        screenWidth=getResources().getDisplayMetrics().widthPixels;

        offset=(screenWidth/2-bmpW)/2;





        //imageview设置平移，使下划线平移到初始位置（平移一个offset）
        Matrix matrix=new Matrix();
        matrix.postTranslate(offset,0);
        tabLine.setImageMatrix(matrix);
    }

    private void initViewPager(){

        vPager= (ViewPager) findViewById(R.id.vPager);

        AboutUsFragment aboutUsFragment=new AboutUsFragment();
        FeedbackFragment feedbackFragment=new FeedbackFragment();
        fragmentList.add(aboutUsFragment);
        fragmentList.add(feedbackFragment);

        mAdapter=new FragmentAdapter(getSupportFragmentManager(),fragmentList);
        vPager.setAdapter(mAdapter);
        vPager.setCurrentItem(0);
        vPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int one=screenWidth/2;//两个相邻页面的偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation=new TranslateAnimation(currIndex*one,position*one,0,0);//平移动画
            currIndex=position;
            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
            animation.setDuration(200);//动画持续时间0.2秒
            tabLine.startAnimation(animation);//是用tabline来显示动画的
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
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
