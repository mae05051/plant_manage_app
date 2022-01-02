package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class NotiService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int day = intent.getIntExtra("day",0);
            int time = intent.getIntExtra("time",0);
            setAlert(day,time);
        }

        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setAlert(int day, int time) {
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder noti= null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String chID="plant"; //알림채널 식별자
            String chName="plant"; //알림채널의 이름(별명)
            NotificationChannel channel= new NotificationChannel(chID,chName,NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            noti=new Notification.Builder(this, chID);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            noti= new Notification.Builder(this);
        }
        noti.setSmallIcon(android.R.drawable.ic_menu_view);
        noti.setContentTitle("알림");//알림창 제목
        noti.setContentText(day+"(윌일)부터 주기에 따라 "+time+"시에 알람이 울립니다.");//알림창 내용
        notificationManager.notify(View.generateViewId(), noti.build());
    }

}
