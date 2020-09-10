package com.example.islamicappp_adminpanel;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class AdsService extends Service {

    Context context;
    private Integer counter;
    int second=30;


    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            createNotificationChannel();
//            Intent notificationIntent = new Intent(this, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                    0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Islamic app")
                    .setContentText("app is waiting for response")
                    .setSmallIcon(R.drawable.logo_main)
//                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
        }


        context = this.getApplicationContext();


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {


        callbgTask();





        return START_STICKY;
    }


//
//    Timer tmr = new Timer();
//    TimerTask tt;

    public void callbgTask()
    {
        Timer();
//        final Handler handler = new Handler();
//
//        tt = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable()
//                {
//                    public void run()
//                    {
//                        try {
//
//
//
//
//                        if (second>0)
//                        {
//                            second--;
//
//                            store_second(second);
//
//                        }
//                        else
//                        {
//                               tmr.cancel();
//                                tmr.purge();
//                                stopSelf();
//
//                        }
//
//
//
//
//
//
//
//
//
//
//                        } catch (Exception e)
//                        {
//                         //   Toast.makeText(context, "io error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        };
//        tmr.schedule(tt, 0,1000); //execute in every 1000 ms
    }






    private void createNotificationChannel()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);



    }





    public void store_second(int second)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("Admin_DATA");

        HashMap hashMap=new HashMap();
        hashMap.put("second",second);

        databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });

    }


    CountDownTimer yourCountDownTimer;
private void Timer(){
    yourCountDownTimer= new CountDownTimer(second*1000, 1000) {

        public void onTick(long millisUntilFinished) {
            if ((int) (millisUntilFinished/1000)>0)
            {

                store_second((int) (millisUntilFinished/1000));

            }
            else
            {
                if (yourCountDownTimer!=null){

                    yourCountDownTimer.cancel();
                }
                stopSelf();


            }
            Log.v("dell","seconde left :  "+(int) (millisUntilFinished/1000));

        }

        public void onFinish() {
//            tmr.cancel();
//            tmr.purge();


            if (yourCountDownTimer!=null){

                yourCountDownTimer.cancel();
            }
            stopSelf();
        }

    }.start();
}

}