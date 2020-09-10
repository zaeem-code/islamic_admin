package com.example.islamicappp_adminpanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.Hd).setOnClickListener(this);

        findViewById(R.id.ITM).setOnClickListener(this);
        findViewById(R.id.WOTHD).setOnClickListener(this);
        findViewById(R.id.EOTD).setOnClickListener(this);
        findViewById(R.id.NTOE).setOnClickListener(this);
        findViewById(R.id.AOTD).setOnClickListener(this);
        findViewById(R.id.HOTD).setOnClickListener(this);
        findViewById(R.id.QOTD).setOnClickListener(this);
        findViewById(R.id.UB).setOnClickListener(this);
        findViewById(R.id.Vl).setOnClickListener(this);
        findViewById(R.id.K_0).setOnClickListener(this);
        findViewById(R.id.K_1).setOnClickListener(this);
        findViewById(R.id.K_2).setOnClickListener(this);
        findViewById(R.id.K_3).setOnClickListener(this);
        findViewById(R.id.K_4).setOnClickListener(this);
        findViewById(R.id.K_5).setOnClickListener(this);
        findViewById(R.id.UI).setOnClickListener(this);
        findViewById(R.id.answer).setOnClickListener(this);
        findViewById(R.id.date_handle).setOnClickListener(this);


        findViewById(R.id.UYL).setOnClickListener(this);
        findViewById(R.id.FBL).setOnClickListener(this);
        findViewById(R.id.video).setOnClickListener(this);
        findViewById(R.id.video_urdu).setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.ITM:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","ITM"));


            break;
            case R.id.Hd:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","hd"));

                break;
            case R.id.UYL:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","UYL"));
                break;
            case R.id.FBL:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","FBL"));
                break;
            case R.id.video:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","video"));
                break;
            case R.id.video_urdu:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","video_urdu"));
                break;
                case R.id.WOTHD:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","WOTHD"));

                break;
            case R.id.EOTD:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","EOTD"));


                break;
            case R.id.NTOE:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","NTOE"));


                break;
            case R.id.AOTD:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","AOTD"));


                break;
            case R.id.HOTD:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","HOTD"));


                break;
            case R.id.QOTD:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","QOTD"));


                break;
            case R.id.UB:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","UB"));


                break;


            case R.id.Vl:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","VL"));


                break;

            case R.id.K_0:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","K_0"));


                break;
            case R.id.K_1:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","K_1"));


                break;

            case R.id.K_2:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","K_2"));


                break;



            case R.id.K_3:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","K_3"));


                break;


            case R.id.K_4:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","K_4"));

                break;

            case R.id.K_5:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","K_5"));
                break;


            case R.id.answer:
                startActivity(new Intent(this,AnswerActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;


            case R.id.date_handle:
                startActivity(new Intent(this,UploadingDataActivity.class).putExtra("chk","K_6"));
                break;


        }



    }


}