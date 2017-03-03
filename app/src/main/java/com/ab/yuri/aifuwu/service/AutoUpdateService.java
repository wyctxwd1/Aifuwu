package com.ab.yuri.aifuwu.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.ab.yuri.aifuwu.LoginActivity;
import com.ab.yuri.aifuwu.MainActivity;
import com.ab.yuri.aifuwu.OnedayActivity;
import com.ab.yuri.aifuwu.R;
import com.ab.yuri.aifuwu.RecyclerView.AboutUsAdapter;
import com.ab.yuri.aifuwu.gson.WeatherNow;
import com.ab.yuri.aifuwu.util.HttpUtil;
import com.ab.yuri.aifuwu.util.Utility;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        updateWeather();
        updateOnedayPic();
        updateSchooldaysPic();

        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour=8*60*60*1000;//8小时毫秒数
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi); //时间从系统开机算起且唤醒cpu
        return super.onStartCommand(intent, flags, startId);
    }
    /*
     更新天气信息
     */

    private void updateWeather(){
            String weatherUrl="http://guolin.tech/api/weather?cityid=CN101190101&key=56ad57d30e4e43888f020ed6a592dd40";

            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText=response.body().string();
                    WeatherNow weatherNow=Utility.handleWeatherResponse(responseText);
                    if (weatherNow!=null&&"ok".equals(weatherNow.status)){
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }

                }
            });
    }

      /*
        更新一言的图片
         */
      private void updateOnedayPic(){
          //开启线程爬取一个·one的图片
          new Thread(new Runnable() {
              @Override
              public void run() {
                  try {
                      Document doc= Jsoup.connect("http://wufazhuce.com/").get();
                      Elements pic=doc.select("meta[property=og:image]");
                      final String picUrl=pic.get(1).attr("content");
                      SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                      editor.putString("oneday_pic",picUrl);
                      editor.apply();
                  }catch (Exception e){
                      e.printStackTrace();
                  }

              }
          }).start();
      }

      /*
      更新校历图片
       */
      private void updateSchooldaysPic(){
          new Thread(new Runnable() {
              @Override
              public void run() {
                  Document doc;
                  try {
                      doc = Jsoup.connect("http://jwc.njupt.edu.cn/s/24/t/923/5a/df/info88799.htm").get();
                      Elements jpgs = doc.select("img[src$=.jpg]");
                      final String schooldaysImgUrl= jpgs.attr("src");
                      SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                      editor.putString("schooldays_pic",schooldaysImgUrl);
                      editor.apply();

                  } catch (Exception e) {
                      e.printStackTrace();

                  }

              }
          }).start();
      }


}
