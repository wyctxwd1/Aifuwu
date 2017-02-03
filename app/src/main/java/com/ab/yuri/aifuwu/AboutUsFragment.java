package com.ab.yuri.aifuwu;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ab.yuri.aifuwu.RecyclerView.AboutUsAdapter;
import com.ab.yuri.aifuwu.RecyclerView.Uses;
import com.ab.yuri.aifuwu.RecyclerView.UsesAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuri on 2017/2/3.
 */

public class AboutUsFragment extends Fragment {

    private RelativeLayout title;
    private RecyclerView mRecyclerView;
    private List<Uses> mUsesList=new ArrayList<>();
    private AboutUsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.about_us_area,container,false);
        title= (RelativeLayout) view.findViewById(R.id.about_us_title);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.about_us_recycler);

        initUses();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter= new AboutUsAdapter(mUsesList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));


        Glide.with(this).load(R.drawable.about_us_title).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    title.setBackground(resource);
                }
            }
        });


        return view;
    }

    private void initUses(){
        Uses app=new Uses("开发：Echan",R.drawable.icon_app);
        mUsesList.add(app);
        Uses draw=new Uses("图片：Shirley",R.drawable.icon_draw);
        mUsesList.add(draw);
        Uses weibo=new Uses("联系我们：@Echan_Wu",R.drawable.icon_weibo);
        mUsesList.add(weibo);

    }


}
