package com.ab.yuri.aifuwu.util;

import com.ab.yuri.aifuwu.gson.Info;
import com.google.gson.Gson;

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













}
