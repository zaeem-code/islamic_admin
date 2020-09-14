package com.example.islamicappp_adminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.MbmsStreamingSession;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

public class UploadingDataActivity extends AppCompatActivity implements View.OnClickListener , AdapterView.OnItemSelectedListener {
    Spinner spin;
    TextView counter;
    String item_to_be_uploaded_name =" ";
    Button submit;
    LinearLayout videolyt;
    String To;
    VideoView videoView;
    String BookURl;
    String Selected_item_from_dropdown_menu;
    String my_val="";

    String curr_Time;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String curr_date;
    Uri Videouri;
    private int second=1800;
    File BookFile;
    ProgressDialog pd;
    String[] Books = { "Select Targeted Book","Book 1", "Book 2", "Book 3", "Book 4", "Book 5"
            , "Book 6", "Book 7", "Book 8", "Book 9", "Book 10", "Book 11", "Book 12"};
    ArrayList<String> videos=new ArrayList<>();

    ProgressDialog progressDialog;
    StorageReference path;
    Uri pdf_uri;
    FirebaseDatabase database;
    private StorageReference mStorageRef;
    DatabaseReference myDataBaseRef;
    EditText Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading_data);

        spin =  findViewById(R.id.spinner);
        submit =  findViewById(R.id.Submit);
        Data =  findViewById(R.id.ED);
        videolyt =  findViewById(R.id.vidyt);
        videoView =  findViewById(R.id.videoView);
counter=findViewById(R.id.count);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading  .....");
        pd.setIcon(R.drawable.kalma_logo);

        spin.setOnItemSelectedListener(this);
        submit.setOnClickListener(this);
        videolyt.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.loadvid).setOnClickListener(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();


        detectProcess(getIntent().getStringExtra("chk"));



        Database_admin();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back:

                Data.setVisibility(View.VISIBLE);
                spin.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                videolyt.setVisibility(View.GONE);
                finish();
                break;

            case R.id.Submit:
                String data = Data.getText().toString().trim();
                if (TextUtils.isEmpty(data)) {

                    Data.setError("Can't send Empty Data");
                    data = "";

                } else
                {
                    if (!TextUtils.isEmpty(To))
                    {

                        if (To.equals("Question of the day"))
                        {
                            if (TextUtils.isEmpty(isamic_count)) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startForegroundService(new Intent(this, AdsService.class).setAction("start"));
                                } else {
                                    startService(new Intent(this, AdsService.class).setAction("start")  );
                                }

                                Dataupload(To);

                            }else {
                                Toast.makeText(this, "A session is already in progress\n Sorry cant start a new one Please wait", Toast.LENGTH_SHORT).show();
                            }

                        }else {

                            Dataupload(To);
                        }





                    }

                    else {
                        finish();
                        Toast.makeText(this, "Something went", Toast.LENGTH_SHORT).show();
                    }





                }

                break;


            case R.id.loadvid:


                if (!TextUtils.isEmpty(To))
                {

                    LoadVideo_fromDEVICE();
                }
                else {
                    finish();
                    Toast.makeText(this, "Something went", Toast.LENGTH_SHORT).show();
                }


        }
    }



    private void detectProcess(String chk){


        switch (chk)
        {


            case "UYL":
                Data.setHint("Place paste Recent Mehfil Youtube Link here.\n  eg: hfKogJ1HFgo");

                submit.setText("Upload video");

                To = "UYL";
                break;
            case "FBL":
                Data.setHint("Place Facebook page ID here...\n eg:211680932500811\n Kalam channel ID");


                submit.setText("Submit Pager ID");
                To = "FBL";
                break;
            case "ITM":
                Data.setHint("Itlaa e Mahfil");


                To = "Itlaa e Mahfil";

                submit.setText("Submit Itlaa e Mahfil");
                break;


            case "WOTHD":


                Data.setHint("Wazifa of the day");
                To = "Wazifa of the day";

                submit.setText("Submit Wazifa of the day");
                break;
            case "K_0":


                Data.setHint("Islamic calander");
                To = "IslamicCalander";

                submit.setText("Submit calander");
                break;

            case "hd":


                Data.setHint("Hadees of the day");
                To = "hadees of the day";

                submit.setText("Submit Hadeed of the day");
                break;

            case "K_1":


                Data.setHint("Kalma Darood");
                To = "K_1";

                break;


            case "K_2":


                Data.setHint("Kalma Darood");
                To = "K_2";

                break;


            case "K_3":


                Data.setHint("Kalma Darood");
                To = "K_3";

                break;


            case "K_4":


                Data.setHint("Kalma Darood");
                To = "K_4";

                break;

            case "K_5":


                Data.setHint("Kalma Darood");
                To = "K_5";

                break;


            case "K_6":


                Data.setHint("islamic date handle");
                To = "K_6";


                break;


            case "EOTD":

                Data.setHint("Event of the day");
                To = "Event of the day";

                break;
            case "NTOE":


                Data.setHint("Namaze Time of the Year");

                To = "Namaze Time of the Year";

                break;
            case "AOTD":


                Data.setHint("Ayat of the Day");
                To = "Ayat of the Day";

                break;
            case "HOTD":


                Data.setHint("Hadees of the day");
                To = "Hadees of the day";
                break;




            case "QOTD":
                Data.setHint("Question of the day");
                To = "Question of the day";


                break;
            case "UB":
                To="UB";
                Data.setVisibility(View.VISIBLE);
                Data.setHint("Enter Book name");
                spin.setVisibility(View.VISIBLE);
                submit.setText("Upload Book");
                ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Books);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spin.setAdapter(aa);
                break;

            case "video":
                To="video";
                my_val="true";
                Data.setVisibility(View.VISIBLE);
                Data.setHint("Enter youtube video link here.\n eg: hfKogJ1HFgo");
                spin.setVisibility(View.VISIBLE);
                submit.setText("Upload video");
                ArrayAdapter aaa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, video());
                aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aaa);
                break;

            case "video_urdu":
                To="video_urdu";
                my_val="true";
                Data.setVisibility(View.VISIBLE);
                Data.setHint("Enter youtube video urdu link here.\n eg: hfKogJ1HFgo");
                spin.setVisibility(View.VISIBLE);
                submit.setText("Upload video urdu");
                ArrayAdapter aaaa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, video());
                aaaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aaaa);
                break;


            case "VL":
                To="VL";

                Data.setVisibility(View.GONE);
                videolyt.setVisibility(View.VISIBLE);
                submit.setVisibility(View.INVISIBLE);
                Data.setText("fill it so onclck submit dont get error on book upload");

                break;








        }





    }

    //////////////////////spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position !=0)
        {


             Selected_item_from_dropdown_menu =parent.getItemAtPosition(position).toString();




        }else {
            Selected_item_from_dropdown_menu ="";

        }

        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#ffecc73a"));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {


    }
    private void Dataupload(String To){
// Read from the database

        switch (To){
            case "UB":
                item_to_be_uploaded_name =Data.getText().toString().trim();
                if (!TextUtils.isEmpty(Selected_item_from_dropdown_menu))
                {
                    //  bookUpload(BookURl,Booknumber);
//    Toast.makeText(this, " Book"+Booknumber, Toast.LENGTH_SHORT).show();

                    if (Selected_item_from_dropdown_menu.equals("select book"))
                    {

                        Toast.makeText(this, "select book", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (!TextUtils.isEmpty(item_to_be_uploaded_name))
                        {

                                select_book();


                        }
                        else{
                            item_to_be_uploaded_name ="";
                        }

                    }



                }
                else {
                    Toast.makeText(this, "No, Targeted Book selected", Toast.LENGTH_SHORT).show();
                }
                break;


            case "video":
                item_to_be_uploaded_name =Data.getText().toString().trim();
                if (!TextUtils.isEmpty(Selected_item_from_dropdown_menu))
                {
                        if (!TextUtils.isEmpty(item_to_be_uploaded_name))
                        {

                            if (my_val.equals("true"))
                            {
                                Toast.makeText(this, ""+ item_to_be_uploaded_name, Toast.LENGTH_SHORT).show();


                                if (!TextUtils.isEmpty(Selected_item_from_dropdown_menu))
                                {
                                    HashMap hashMap=new HashMap();
                                    hashMap.put("url", item_to_be_uploaded_name);
                                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
                                    databaseReference.child("video_data").child(Selected_item_from_dropdown_menu).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isComplete())
                                            {
                                                Toast.makeText(UploadingDataActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                            else
                            {

                            }
                        }
                        else{

                            Toast.makeText(this, "No, Targeted Video selected", Toast.LENGTH_SHORT).show();
                            item_to_be_uploaded_name ="";


                    }



                }
                else {
                    Toast.makeText(this, "Hey!\nNo Targeted Video selected", Toast.LENGTH_SHORT).show();
                }
                break;



            case "video_urdu":
                item_to_be_uploaded_name =Data.getText().toString().trim();
                if (!TextUtils.isEmpty(Selected_item_from_dropdown_menu))
                {


                        if (!TextUtils.isEmpty(item_to_be_uploaded_name))
                        {

                            if (my_val.equals("true"))
                            {
                                Toast.makeText(this, ""+ item_to_be_uploaded_name, Toast.LENGTH_SHORT).show();


                                if (!TextUtils.isEmpty(Selected_item_from_dropdown_menu))
                                {
                                    HashMap hashMap=new HashMap();
                                    hashMap.put("url", item_to_be_uploaded_name);
                                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
                                    databaseReference.child("urdu_data").child(Selected_item_from_dropdown_menu).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isComplete())
                                            {
                                                Toast.makeText(UploadingDataActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            }
                            else
                            {

                                Toast.makeText(this, "No, Targeted Video selected", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else{
                            item_to_be_uploaded_name ="";
                        }





                }
                else {
                    Toast.makeText(this, "Hey!\nNo Targeted Video selected", Toast.LENGTH_SHORT).show();
                }
                break;

            case "UYL":

                UploadmainApp_simpleData("Youtube");
////


                break;
            case "FBL":

                UploadmainApp_simpleData("Facebook");
////
                break;


            default:
                UploadmainApp_simpleData(To);
                break;
        }



    }



    private void UploadmainApp_simpleData(final String item){

        pd.show();
        myDataBaseRef = database.getReference("Admin_DATA");





        myDataBaseRef.child(item).setValue(Data.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete()){
                    Toast.makeText(UploadingDataActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                }else
                {
String msg=item;
switch (item){
    case "k_1":
        msg="Kalma_o_Darood";
        break;
    case "K_2":
    case "K_3":
    case "K_4":
    case "K_5":

        msg="Dua_e_Astaggfar";
        break;

}
                    new volleyfcm(getApplicationContext(),"New Item Uploaded","In "+msg+"\n"+"check it out now");
                    Toast.makeText(UploadingDataActivity.this, "successfully to upload", Toast.LENGTH_SHORT).show();




                    if (To.equals("Itlaa e Mahfil"))
                    {
                        sendSMSMessage();

                    }
                    else if (To.equals("Question of the day"))
                    {
                        sendSMSMessage();

                    }









                }
                if (pd!=null){
                    pd.dismiss();

                }

            }
        });
    }






    public void select_book()
    {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");


        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 1);
    }






    //video loading module
    private  void LoadVideo_fromDEVICE()
    {
        Videouri=null;
        videoView.setVisibility(View.GONE);

        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"),007);

    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == 007)
        {
            Uri uri = data.getData();

            Upload_img(uri);
        }




        if (requestCode == 1 && resultCode == -1 && data != null)
        {
            pdf_uri = data.getData();
            String imageName = new File(pdf_uri.getPath()).getName();

            Uri uri = pdf_uri;
            if (uri != null)
            {
                if (!TextUtils.isEmpty(Selected_item_from_dropdown_menu))
                {
                    Upload_file(uri, imageName, uri.getPath(), Selected_item_from_dropdown_menu);

                }
            }
        }



    }


    public void Upload_file(Uri pdf_uri2, final String imageName, final String pdf_path, final String book_pos)
    {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(1);
        progressDialog.setTitle("uploading file.......");
        progressDialog.setProgress(0);
        progressDialog.show();

        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append("");
        String file_name = sb.toString();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference =firebaseStorage.getReference();

        path = storageReference.child("upload").child(file_name);
        path.putFile(pdf_uri2).addOnSuccessListener((OnSuccessListener)
                new OnSuccessListener<UploadTask.TaskSnapshot>()

                {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            public void onSuccess(Uri uri) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM,dd,YYYY");
                                curr_date = simpleDateFormat.format(calendar.getTime());
                                Calendar calendar1 = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a");
                                curr_Time = simpleDateFormat1.format(calendar1.getTime());

                                new volleyfcm(getApplicationContext(),"New Item Uploaded","In Books \ncheck it out now");

                                HashMap<String, Object> hashMap = new HashMap<>();

                                hashMap.put("isseen", Boolean.valueOf(false));
                                hashMap.put("date",curr_date);
                                hashMap.put("time",curr_Time);
                                hashMap.put("file_type", "pdf");
                                hashMap.put("uri", uri.toString());
                                hashMap.put("file_name", imageName);
                                hashMap.put("file_path", pdf_path);
                                hashMap.put("download_status", "fail");
                                hashMap.put("Book_name", item_to_be_uploaded_name);

                                databaseReference.child("Book_data").child(book_pos).updateChildren(hashMap)
                                        .addOnCompleteListener(new OnCompleteListener() {
                                            public void onComplete(Task task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(UploadingDataActivity.this, "upoaded", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                    return;
                                                }
                                            }
                                        });
                            }
                        });
                    }
                }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
            public void onFailure(Exception e) {
            }
        }).addOnProgressListener((OnProgressListener) new OnProgressListener<UploadTask.TaskSnapshot>() {
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
            {
                progressDialog.setProgress((int) ((taskSnapshot.getBytesTransferred() * 100) / taskSnapshot.getTotalByteCount()));
            }
        });
    }



    public void Upload_img(Uri pdf_uri2) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setProgressStyle(1);
        this.progressDialog.setTitle("uploading file.......");
        this.progressDialog.setProgress(0);
        this.progressDialog.show();
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append("");
        String file_name = sb.toString();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = this.firebaseStorage.getReference();

        path = storageReference.child("image").child(UUID.randomUUID().toString());


        path.putFile(pdf_uri2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    public void onSuccess(Uri uri) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM,dd,YYYY");
                        curr_date = simpleDateFormat.format(calendar.getTime());
                        Calendar calendar1 = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a");
                        curr_Time = simpleDateFormat1.format(calendar1.getTime());
                        HashMap<String, Object> hashMap = new HashMap<>();

                        String str = "image";
                        hashMap.put("message", str);
                        hashMap.put("isseen", Boolean.valueOf(false));
                        hashMap.put("date", curr_date);
                        hashMap.put("time",curr_Time);
                        hashMap.put(NotificationCompat.CATEGORY_STATUS, "send");
                        hashMap.put("uri", uri.toString());
                        databaseReference.child("Image").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            public void onComplete(Task task) {
                                if (task.isSuccessful())

                                {
                                    progressDialog.dismiss();
                                    return;
                                }
                                Toast.makeText(UploadingDataActivity.this, "file uploading error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
            public void onFailure(Exception e) {
            }
        }).addOnProgressListener((OnProgressListener) new OnProgressListener<UploadTask.TaskSnapshot>() {
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
            {


                progressDialog.setProgress((int) ((taskSnapshot.getBytesTransferred() * 100) / taskSnapshot.getTotalByteCount()));
            }
        });
    }




    public void send_msg(String msg)
    {

        String separator = ";";
        if(Build.MANUFACTURER.equalsIgnoreCase("samsung")){
            separator = ",";
        }


//        try {
//
//            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//            sendIntent.putExtra("address", "03214158008"+separator+"03324856796");
//            sendIntent.putExtra("sms_body", msg);
//            sendIntent.setType("vnd.android-dir/mms-sms");
//            startActivity(sendIntent);
//
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(),
//                    "SMS faild, please try again later!",
//                    Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }







    }



    protected void sendSMSMessage() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS))
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        10);

            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        10);
            }
        }
        else
        {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    10);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {




                    final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference()
                            .child("user_record");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren())
                            {

                                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                                {
                                    String number=dataSnapshot1.child("phone").getValue().toString();


                                    String separator = ";";
                                    if(Build.MANUFACTURER.equalsIgnoreCase("samsung")){
                                        separator = ",";
                                    }

                                    String message = To+"\n"+Data.getText().toString().trim()+"\n"+"uploaded in islamic app";
//                                          String phoneNo = "03214158008"+separator+"03324856796";
                                    String phoneNo = number+separator;


                                    StringTokenizer st=new StringTokenizer(phoneNo,separator);
                                    while (st.hasMoreElements())
                                    {
                                        String tempMobileNumber = (String)st.nextElement();
                                        if(tempMobileNumber.length()>0 && message.trim().length()>0)
                                        {
//                                SmsManager sms = SmsManager.getDefault();
//                                sms.sendTextMessage(tempMobileNumber, null, message, null, null);





                                            sendSMS(tempMobileNumber,message);
/////////////////////
                                        }


                                    }






/////////////////////////////////////////////////////////////////////
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });






                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }















    private void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        },new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }













private ArrayList<String> video(){
videos.clear();
for (int i=0; i<=120; i++) {
    if (i == 0) {

        videos.add("Select Targeted video");
    } else {
        videos.add("video " + i);
    }

}
return videos;
}

    String isamic_count= "";
    int count=0;

    public void Database_admin()
    {
        managpower();
        if (count == 0) {

            resetclockcount();count++;
        }
try {


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin_DATA");
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren()) {
                try {
                    isamic_count = dataSnapshot.child("second").getValue().toString();
                } catch (Exception e) {
                }

//isko less then one karna ha
                try {

int num=Integer.parseInt(isamic_count);
                    if (num> 1) {
                        counter.setVisibility(View.VISIBLE);
                        counter.setText("Reminder:\n" +
                                "     Currently we have an ongoing session of 'Question of the Day' \n" +
                                "     Remaining time is: " + (num+1) + " sec. \n\n " + "Please until then DO NOT TURN OFF internet, admin app or phone itself many factory can affect your counter counting like POWER SAVING MODE, DATA USAGE RESTRICTION, BACKGROUND USAGE etc.. make sure you meet all the require conditions");
                    } else {
                        counter.setVisibility(View.GONE);
                        isamic_count = "";
                    }
                }catch (Exception e){
                    counter.setVisibility(View.GONE);
                    isamic_count = "";

                }


            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


}catch (Exception e){

}
    }

private void resetclockcount(){
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("Admin_DATA");

        HashMap hashMap=new HashMap();
        hashMap.put("second",1);

        databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });

    }
}
private void managpower(){
    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");

    wl.acquire();
//    // screen and CPU will stay awake during this section
//
//wl.release();
}


}

