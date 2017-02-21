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
import com.ab.yuri.aifuwu.gson.GPA;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Yuri on 2017/1/26.
 */

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder>{
    private List<GPA.DetailBean> mScoreList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView lessonYear;
        TextView lessonTerm;
        TextView lessonCode;
        TextView lessonName;
        TextView lessonCredit;
        TextView lessonGPA;
        TextView lessonScore;

        public ViewHolder(View view) {
            super(view);
            lessonYear= (TextView) view.findViewById(R.id.lesson_year);
            lessonTerm= (TextView) view.findViewById(R.id.lesson_term);
            lessonCode= (TextView) view.findViewById(R.id.lesson_code);
            lessonName= (TextView) view.findViewById(R.id.lesson_name);
            lessonCredit= (TextView) view.findViewById(R.id.lesson_credit);
            lessonGPA= (TextView) view.findViewById(R.id.lesson_GPA);
            lessonScore= (TextView) view.findViewById(R.id.lesson_score);

        }
    }

    public ScoreAdapter(List<GPA.DetailBean> scoreList){
        mScoreList=scoreList;
    }

    @Override
    public ScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ScoreAdapter.ViewHolder holder, int position) {
        GPA.DetailBean score=mScoreList.get(position);
        holder.lessonYear.setText(score.year);
        holder.lessonTerm.setText("第"+score.term+"学期");
        holder.lessonCode.setText(score.lessonCode);
        holder.lessonName.setText(score.lessonName);
        holder.lessonCredit.setText("学分："+score.lessonCredit);
        holder.lessonGPA.setText("绩点："+score.lessonGPA);
        holder.lessonScore.setText("成绩:"+score.lessonScore);
    }

    @Override
    public int getItemCount() {
        return mScoreList.size();
    }
}


