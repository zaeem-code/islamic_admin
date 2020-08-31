package com.example.islamicappp_adminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class AnswerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Model> arrayList=new ArrayList<>();
    ImageView back;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);


        back=findViewById(R.id.back);
        recyclerView=findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setDrawingCacheEnabled(true);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        get_notifi_data();


    }



    public void get_notifi_data()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

        databaseReference .child("Admin_Ans").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren())
                {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {

                        progressDialog.dismiss();
                        Model notif_data=dataSnapshot1.getValue(Model.class);
                        arrayList.add(notif_data);

                    }

                    //////////////////remove duplicate value in array
//                    for(int i=0; i < arrayList.size(); i++){
//                        for(int j=0; j < arrayList.size(); j++){
//                            if(arrayList.get(i).equals(arrayList.get(j))){
//                                arrayList.remove(j);
//                                //j;
//                            }
//                        }
//                    }

                    adapter=new Adapter(getApplicationContext(),arrayList);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "empty Answer", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "internet error", Toast.LENGTH_SHORT).show();

            }
        });



    }




}