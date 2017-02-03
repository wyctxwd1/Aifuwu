package com.ab.yuri.aifuwu.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.yuri.aifuwu.R;
import com.ab.yuri.aifuwu.RunActivity;
import com.ab.yuri.aifuwu.SchooldaysActivity;
import com.ab.yuri.aifuwu.ScoreActivity;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Yuri on 2017/2/3.
 */

public class AboutUsAdapter extends RecyclerView.Adapter<AboutUsAdapter.ViewHolder> {
    private Context mContext;
    private List<Uses> mUsesList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView useImg;
        TextView useName;

        public ViewHolder(View view) {
            super(view);
            useImg= (ImageView) view.findViewById(R.id.about_us_item_img);
            useName= (TextView) view.findViewById(R.id.about_us_item_txt);
        }
    }

    public AboutUsAdapter(List<Uses> usesList){
        mUsesList=usesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.about_us_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uses uses=mUsesList.get(position);
        holder.useName.setText(uses.getName());
        Glide.with(mContext).load(uses.getImgId()).into(holder.useImg);
    }

    @Override
    public int getItemCount() {
        return mUsesList.size();
    }
}
