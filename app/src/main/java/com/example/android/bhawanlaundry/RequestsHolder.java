package com.example.android.bhawanlaundry;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RequestsHolder extends RecyclerView.ViewHolder {

    TextView studentNameTextView, studentRoomTextView, numClothesTextView, bucketColourTextView;
    ImageButton accept_button, reject_button;

    public RequestsHolder(View itemView){
        super(itemView);

        this.studentNameTextView = itemView.findViewById(R.id.STUDENT_NAME_REQUESTS);
        this.studentRoomTextView = itemView.findViewById(R.id.STUDENT_ROOM_REQUESTS);
        this.numClothesTextView = itemView.findViewById(R.id.NUMBER_OF_CLOTHES_REQUESTS);
        this.bucketColourTextView = itemView.findViewById(R.id.BUCKET_COLOUR_REQUESTS);
        this.accept_button = itemView.findViewById(R.id.REQUEST_ACCEPT_BUTTON);
        this.reject_button = itemView.findViewById(R.id.REQUEST_REJECT_BUTTON);

    }

}
