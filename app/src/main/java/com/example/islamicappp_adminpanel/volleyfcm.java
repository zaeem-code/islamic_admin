package com.example.islamicappp_adminpanel;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class volleyfcm {
    Context context;
    String title, messge;
    SimpleDateFormat _24HourSDF;
    SimpleDateFormat _12HourSDF;
    Date _24HourDt;
    String output2;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    private String key = "key=" + "AAAABRYqvLA:APA91bG4mH71DJs7LhE-BeE0i90BqjoiMFYX-qIIooKgyrNJWMHglnLtshSiPczbRQmJ2fCgIq7kI1jWPwQtCzQL39WpQBQZ6lBcQV3n1G7IrAhepzTmfrU0aUP31ZucMhbXq1U9oMO8";
    private String contentType = "application/json";
    JSONObject notification = new JSONObject();
    JSONObject notifcationBody = new JSONObject();
    int count=1;




    public volleyfcm(Context context, String Title, String message)
    {
        this.context = context;
        this.title = Title;
        this.messge = message;

        FirebaseMessaging.getInstance().subscribeToTopic("APP_master");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            ZoneId z = ZoneId.of("Asia/Karachi") ;
            LocalTime localTime = LocalTime.now( z ) ;
            LocalDate locale_date= LocalDate.now(z);
            Locale locale_SAU_date = Locale.forLanguageTag("PK");

            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale( locale_SAU_date ) ;
            output2 = locale_date.format( formatter ) ;



        }

        String curr_Time = new SimpleDateFormat("HH:mm").format(new Date());

        try
        {

            _24HourSDF = new SimpleDateFormat("HH:mm");
            _12HourSDF = new SimpleDateFormat("hh:mm a");
            _24HourDt = _24HourSDF.parse(curr_Time);

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }





        try {


            notifcationBody.put("title", title);
            notifcationBody.put("message", messge);
            notifcationBody.put("time", _12HourSDF.format(_24HourDt));
            notifcationBody.put("date", output2);






            notification.put("to", "/topics/APP_master");
            notification.put("data", notifcationBody);

            FCMprocess(notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    public void FCMprocess(JSONObject notification) {





        ///////////
        Toast.makeText(context, "in", Toast.LENGTH_SHORT).show();


        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL,notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "a" + response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", key);
                params.put("Content-Type", contentType);
                return params;
            }

        };

        requestQueue.add(jsonObjectRequest);


    }
}