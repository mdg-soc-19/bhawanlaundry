package com.example.android.bhawanlaundry;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class QueueHolder extends RecyclerView.ViewHolder {

    TextView studentNameTextView, studentRoomTextView, numClothesTextView, bucketColourTextView;
    ImageButton accept_button;

    String laundryroom;

    public QueueHolder(View itemView){
        super(itemView);

        this.studentNameTextView = itemView.findViewById(R.id.STUDENT_NAME_QUEUE);
        this.studentRoomTextView = itemView.findViewById(R.id.STUDENT_ROOM_QUEUE);
        this.numClothesTextView = itemView.findViewById(R.id.NUMBER_OF_CLOTHES_QUEUE);
        this.bucketColourTextView = itemView.findViewById(R.id.BUCKET_COLOUR_QUEUE);
        this.accept_button = itemView.findViewById(R.id.QUEUE_ACCEPT_BUTTON);

    }

}
