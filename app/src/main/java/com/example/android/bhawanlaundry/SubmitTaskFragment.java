package com.example.android.bhawanlaundry;

import android.app.Activity;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class SubmitTaskFragment extends Fragment {

    static  String laundryRoom, studentName, studentRoom;
    String bucketColour, numberOfClothes;
    TextView displayLaundryRoom;
    EditText numberOfClothesInput, bucketColourInput;
    Button submitRequest;
    Task task;


    submitTaskFragmentCI SUBMIT_TASK_FRAGMENT;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_submit_task, container, false);

        Bundle bundle = getArguments();
        laundryRoom = bundle.getString("Laundry_Room");
        displayLaundryRoom = view.findViewById(R.id.LAUNDRY_ROOM_NAME_TEXT);
        displayLaundryRoom.setText(laundryRoom);

        studentName = bundle.getString("nameOfStudent");
        studentRoom = bundle.getString("roomOfStudent");


        numberOfClothesInput = view.findViewById(R.id.NUMBER_OF_CLOTHES_INPUT);
        bucketColourInput = view.findViewById(R.id.COLOUR_OF_BUCKET_INPUT);
        submitRequest = view.findViewById(R.id.SUBMIT_TASK_BUTTON);

        submitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bucketColour = bucketColourInput.getText().toString().trim();
                numberOfClothes = numberOfClothesInput.getText().toString().trim();

                if(Integer.parseInt(numberOfClothes)>10){
                    numberOfClothesInput.setError("Number of clothes cannot exceed 10");
                    numberOfClothesInput.requestFocus();
                    return;
                }

                sendRequest(numberOfClothes, bucketColour);

            }
        });


        return view;
    }

    private void sendRequest(String n, String b) {



        HashMap<String, String> h = new HashMap<>();

        h.put("studentName", studentName);
        h.put("studentRoom", studentRoom);
        h.put("numberOfClothes", n);
        h.put("bucketColour", b);

        FirebaseFirestore ff = FirebaseFirestore.getInstance();
        DocumentReference dRef = ff.collection("laundryrooms").document(laundryRoom).collection("tasks").document();
        dRef.set(h).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Request sent", Toast.LENGTH_LONG).show();
            }
        });

        SUBMIT_TASK_FRAGMENT.openRequestSentFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        SUBMIT_TASK_FRAGMENT = (submitTaskFragmentCI) activity;
    }


    public interface submitTaskFragmentCI {
            void openRequestSentFragment();
    }


}
