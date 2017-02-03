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
 * Created by Yuri on 2017/1/2.
 */

public class UsesAdapter extends RecyclerView.Adapter<UsesAdapter.ViewHolder> {
    private Context mContext;
    private List<Uses> mUsesList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View useView;
        ImageView useImg;
        TextView useName;

        public ViewHolder(View view) {
            super(view);
            useView=view;
            useImg= (ImageView) view.findViewById(R.id.use_img);
            useName= (TextView) view.findViewById(R.id.use_name);
        }
    }

    public UsesAdapter(List<Uses> usesList){
        mUsesList=usesList;
    }

    @Override
    public UsesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.use_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.useView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                switch (position) {
                    case 0:
                        Intent run=new Intent(mContext,RunActivity.class);
                        mContext.startActivity(run);
                        break;
                    case 1:
                        Intent score=new Intent(mContext,ScoreActivity.class);
                        mContext.startActivity(score);
                        break;
                    case 2:
                    Intent schooldays = new Intent(mContext, SchooldaysActivity.class);
                    mContext.startActivity(schooldays);
                }


                }

        });

        return holder;
    }

    @Override
    public void onBindViewHolder(UsesAdapter.ViewHolder holder, int position) {
        Uses uses=mUsesList.get(position);
        holder.useName.setText(uses.getName());
        Glide.with(mContext).load(uses.getImgId()).into(holder.useImg);


    }

    @Override
    public int getItemCount() {
        return mUsesList.size();
    }
}
