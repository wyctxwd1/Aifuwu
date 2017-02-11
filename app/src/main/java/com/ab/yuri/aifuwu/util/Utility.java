package com.ab.yuri.aifuwu.util;



import com.ab.yuri.aifuwu.gson.Exercise;
import com.ab.yuri.aifuwu.gson.GPA;
import com.ab.yuri.aifuwu.gson.Info;
import com.ab.yuri.aifuwu.gson.Oneday;
import com.ab.yuri.aifuwu.gson.OnedayImg;
import com.ab.yuri.aifuwu.gson.WeatherNow;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;



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
        /*
        解析和处理返回的Exercise信息
        */
        try {
            Gson gson=new Gson();
            Exercise exercise=gson.fromJson(response,Exercise.class);
            return exercise;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static GPA handleGPAResponse(String response){
         /*
        解析和处理返回的GPA信息
        */
        try {
            Gson gson=new Gson();
            GPA gpa=gson.fromJson(response,GPA.class);
            return gpa;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static WeatherNow handleWeatherResponse(String response){
         /*
        解析和处理返回的WeatherNow信息
        */
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            Gson gson=new Gson();
            WeatherNow weatherNow=gson.fromJson(weatherContent,WeatherNow.class);
            return weatherNow;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static Oneday handleOnedayResponse(String response){
         /*
        解析和处理返回的Oneday信息
        */
        try {
            Gson gson=new Gson();
            Oneday oneday=gson.fromJson(response,Oneday.class);
            return oneday;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static OnedayImg handleOnedayImgResponse(String response){
         /*
        解析和处理返回的Oneday信息
        */
        try {
            Gson gson=new Gson();
            OnedayImg onedayImg=gson.fromJson(response,OnedayImg.class);
            return onedayImg;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
