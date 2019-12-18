package com.example.appone;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class TaskEnterFragment extends Fragment {

    EditText numClothesInput, bucketColourInput;
    Button submitButton;
    TextView roomBlock;
    String room, bucketColour, key, sRoom, sName;
    int numBucketsInQueue, numClothes;
    static boolean isRoomB;
    StudentUser sUser;

    DocumentReference dRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_enter, container, false);

        numClothesInput = view.findViewById(R.id.NUMBER_OF_CLOTHES_INPUT);
        bucketColourInput = view.findViewById(R.id.COLOUR_OF_BUCKET_INPUT);
        submitButton = view.findViewById(R.id.SUBMIT_TASK_BUTTON);
        roomBlock = view.findViewById(R.id.LAUNDRY_ROOM_NAME_TEXT);

        room = getArguments().getString("lRoom");
        roomBlock.setText(room);
        if(room=="B"){
            isRoomB = true;

        } else {
            isRoomB = false;

        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numClothes = Integer.parseInt(numClothesInput.getText().toString().trim());
                bucketColour = bucketColourInput.getText().toString().trim();
                if(numClothes>10||numClothes<3){
                    numClothesInput.setError("Number of clothes must be between 3 and 10 (inclusive)");
                    numClothesInput.requestFocus();
                    return;
                }
                submitTask(numClothes, bucketColour);
            }
        });

        return view;
    }

    private void submitTask(final int numClothes, final String bucketColour) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dRef = FirebaseFirestore.getInstance().collection("student").document(user.getDisplayName());

        dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    sUser = documentSnapshot.toObject(StudentUser.class);
                    sUser.setBucketColor(bucketColour);
                    sUser.setNumClothes(numClothes);
                }

            }
        });

        if(isRoomB){
            dRef =  FirebaseFirestore.getInstance().collection("b_block_requests").document(sUser.getRoom());
            dRef.set(sUser);
        } else {
            dRef =  FirebaseFirestore.getInstance().collection("ax_block_requests").document(sUser.getRoom());
            dRef.set(sUser);
        }

    }

}
