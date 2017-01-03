package com.ab.yuri.aifuwu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
                    case 2:
                    Intent intent = new Intent(mContext, SchooldaysActivity.class);
                    mContext.startActivity(intent);
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
