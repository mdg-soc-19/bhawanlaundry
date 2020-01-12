package com.example.android.bhawanlaundry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsHolder> {

    Context context;
    List<Task> tasks;

    public RequestsAdapter (List<Task> t, Context c){
        this.context = c;
        this.tasks = t;
    }

    @NonNull
    @Override
    public RequestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_request, null); //to inflate our cardiew


        return new RequestsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsHolder holder, int position) {
        holder.studentNameTextView.setText(tasks.get(position).getNameOfStudent());
        holder.studentRoomTextView.setText(tasks.get(position).getRoomOfStudent());
        holder.bucketColourTextView.setText(tasks.get(position).getBucketColour());
        holder.numClothesTextView.setText("" + tasks.get(position).getNumberOfClothes());

        holder.accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(v.getContext(),"TEST", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
