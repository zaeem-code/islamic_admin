package com.example.islamicappp_adminpanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ProgHolder>
{

    Context context;
    ArrayList<Model> arrayList;


    public Adapter(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ProgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.answer_design,parent,false);

        return new ProgHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgHolder holder, int position) {


        Model model=arrayList.get(position);
        holder.title.setText(model.getName());
        holder.body.setText(model.getAnswer());
        holder.time.setText(model.getPhone());
        holder.timex.setText(model.getTime());
        holder.timey.setText(model.getDate());



    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }

    public class ProgHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title,body,time,timex,timey;

        public ProgHolder(@NonNull View itemView) {
            super(itemView);

            time=itemView.findViewById(R.id.time);
            title=itemView.findViewById(R.id.title);
            body=itemView.findViewById(R.id.body);
            itemView.setOnClickListener(this);
            timex=itemView.findViewById(R.id.timex);
            timey=itemView.findViewById(R.id.timey);


        }

        @Override
        public void onClick(View v) {


        }



    }



}
