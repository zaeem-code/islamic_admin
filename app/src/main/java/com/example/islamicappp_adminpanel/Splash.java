package com.example.islamicappp_adminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class Splash extends AppCompatActivity {
    private static final int REQUEST_WRITE_STORAGE = 112;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        String str = "android.permission.WRITE_EXTERNAL_STORAGE";
        if (!(ContextCompat.checkSelfPermission(this, str) == 0))
        {
            ActivityCompat.requestPermissions(this, new String[]{str}, 112);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    gotoNEXT() ;
                    finish();
                }
            }, 2500);
        }
    }



    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 112) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                Toast.makeText(this, "The application can't start without this permission", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    gotoNEXT();
                    finish();
                }
            }, 2500);
        }
    }
    private void gotoNEXT(){

        FirebaseMessaging.getInstance().subscribeToTopic("APP");
        boolean chk=getSharedPreferences("Login",MODE_PRIVATE).getBoolean("chk",false);
        Toast.makeText(this, ""+chk, Toast.LENGTH_SHORT).show();
        if (chk){
            Splash.this.startActivity(new Intent(Splash.this.getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK
            ));
        }else {
            Splash.this.startActivity(new Intent(Splash.this.getApplicationContext(), SigninActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK
            ));
        }


    }
}
