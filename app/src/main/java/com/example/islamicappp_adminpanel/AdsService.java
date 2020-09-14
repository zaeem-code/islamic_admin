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
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class AdsService extends Service {

    Context context;
    private Integer counter;
    int second = 120;
    String chk = "s";

    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Notification notification;
            createNotificationChannel();
//            Intent notificationIntent = new Intent(this, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                    0, notificationIntent, 0);
            if (chk.equals("start")) {
                notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Islamic app Question of the Day")
                        .setContentText("Session is currently in progress")
                        .setSmallIcon(R.drawable.logo_main)
//                    .setContentIntent(pendingIntent)
                        .build();
                Log.v("bg", "->>>>>>> started ");

                startForeground(1, notification);
            }  else {
                notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Islamic app closed")
                        .setSmallIcon(R.drawable.logo_main)
//                    .setContentIntent(pendingIntent)
                        .build();
                Log.v("bg", "->>>>>>> other ");

                startForeground(1, notification);
            }
        }




            context = this.getApplicationContext();


        }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    int startId = 0;

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;


            chk = intent.getAction();
        if (!TextUtils.isEmpty(chk)){
                if (chk.equals("exit")) {

                    Log.v("bg","->>>>>>> exit detect ");
                    store_second(1);
                    if (yourCountDownTimer != null) {
                        Toast.makeText(context, "Session expired", Toast.LENGTH_SHORT).show();
                        yourCountDownTimer.cancel();
                        stopSelf();

                        stopForeground(true);
                        stopSelfResult(startId);

                        onDestroy();


                    }
                }
                else if (chk.equals("start")) {

                    Log.v("bg","->>>>>>> start detect ");
                    callbgTask();
                }}
            else {
                Log.v("bg","->>>>>>> no value got detect ");
                }





        return START_STICKY;
    }


    public void callbgTask() {
        Timer();
//
    }


    NotificationManager manager = null;
    NotificationChannel serviceChannel = null;

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (serviceChannel == null) {
                serviceChannel = new NotificationChannel(
                        CHANNEL_ID,
                        "Foreground Service Channel",
                        NotificationManager.IMPORTANCE_HIGH
                );

                serviceChannel.setSound(null, null);
            }
            if (manager == null) {
                manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);

            }
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


    public void store_second(int second) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Admin_DATA");

        HashMap hashMap = new HashMap();
        hashMap.put("second", second);

        databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

                notification(String.valueOf(second));
            }
        });

    }


    CountDownTimer yourCountDownTimer;

    private void Timer() {
        yourCountDownTimer = new CountDownTimer(second * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                if ((int) (millisUntilFinished / 1000) > 0) {

                    store_second((int) (millisUntilFinished / 1000));

                } else {
                    if (yourCountDownTimer != null) {

                        yourCountDownTimer.cancel();
                        yourCountDownTimer = null;
                    }
                    stopSelf();
                    if (startId != 0) {

                        stopSelfResult(startId);
                    }
                    stopForeground(true);
                }
                Log.v("dell", "seconde left :  " + (int) (millisUntilFinished / 1000));

            }

            public void onFinish() {
//            tmr.cancel();
//            tmr.purge();


                if (yourCountDownTimer != null) {

                    yourCountDownTimer.cancel();
                    yourCountDownTimer = null;
                }
                stopSelf();
                if (startId != 0) {

                    stopSelfResult(startId);
                }
                stopForeground(true);
            }

        }.start();
    }

    @Override
    public void onDestroy() {
        try {
            stopSelf();

            stopForeground(true);
            if (yourCountDownTimer != null) {

                yourCountDownTimer.cancel();
                yourCountDownTimer = null;
            }
        } catch (Exception e) {

        }

        super.onDestroy();
    }

    private void notification(String time) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotificationChannel();
            if (!TextUtils.isEmpty(chk)) {
                if (chk.equals("exit") && time.equals("1")) {


                    Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setContentTitle("Islamic app closed")
                            .setContentText("Current Session  Expired")
                            .setSmallIcon(R.drawable.logo_main)
                            .setAutoCancel(false)
                            .setVibrate(null)
                            .setVisibility((NotificationCompat.VISIBILITY_PUBLIC))
                            .setOngoing(true)
                            .setSound(null)
                            .build();
                    if (manager != null) {
                        manager.notify(1, notification);
                    }
                } else if (chk.equals("start")) {


                    Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setContentTitle("Islamic app Question of the Day")
                            .setContentText("Session is currently in progress, Remaining time: " + time)
                            .setSmallIcon(R.drawable.logo_main)
                            .setAutoCancel(false)
                            .setVibrate(null)
                            .setVisibility((NotificationCompat.VISIBILITY_PUBLIC))
                            .setOngoing(true)
                            .setSound(null)

//                    .setContentIntent(pendingIntent)
                            .build();

                    notification.flags = Notification.FLAG_ONGOING_EVENT;

                    if (manager != null) {
                        manager.notify(1, notification);
                    }

                }

            }
            }
        }

    }