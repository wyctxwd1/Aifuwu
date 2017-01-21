package com.ab.yuri.aifuwu.util;

import android.util.Log;

import com.ab.yuri.aifuwu.gson.Exercise;
import com.ab.yuri.aifuwu.gson.Info;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Yuri on 2017/1/19.
 */

public class Utility {
    /*
    解析和处理返回的login信息
     */
    public static Info handleInfoResponse(String response){
        try {
            Gson gson=new Gson();
            Info info=gson.fromJson(response,Info.class);
            return info;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Exercise handleExerciseResponse(String response){
        try {
            Gson gson=new Gson();
            Exercise exercise=gson.fromJson(response,Exercise.class);
            return exercise;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
















}
