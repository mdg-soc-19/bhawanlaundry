package com.example.android.bhawanlaundry;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;


public class SubmitTaskFragment extends Fragment {

    static  String laundryRoom, studentName, studentRoom;
    String bucketColour, numberOfClothes;
    TextView displayLaundryRoom, displayBucketQueue;
    EditText numberOfClothesInput, bucketColourInput;
    Button submitRequest;
    ProgressBar progressBar;
    int numBucketsInQueue;
    // Task task;


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

        numBucketsInQueue = bundle.getInt("bucketsInQueue");
        displayBucketQueue = view.findViewById(R.id.BUCKET_QUEUE_LENGTH);
        displayBucketQueue.setText("" + numBucketsInQueue);

        studentName = bundle.getString("nameOfStudent");
        studentRoom = bundle.getString("roomOfStudent");


        numberOfClothesInput = view.findViewById(R.id.NUMBER_OF_CLOTHES_INPUT);
        bucketColourInput = view.findViewById(R.id.COLOUR_OF_BUCKET_INPUT);
        submitRequest = view.findViewById(R.id.SUBMIT_TASK_BUTTON);
        progressBar = view.findViewById(R.id.PROGRESS_CIRCULAR_SUBMIT_FRAGMENT);

        submitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bucketColourInput.onEditorAction(EditorInfo.IME_ACTION_DONE);
                numberOfClothesInput.onEditorAction(EditorInfo.IME_ACTION_DONE);

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
        progressBar.setVisibility(View.VISIBLE);


        /*HashMap<String, String> h = new HashMap<>();

        h.put("studentName", studentName);
        h.put("studentRoom", studentRoom);
        h.put("numberOfClothes", n);
        h.put("bucketColour", b);*/

        int nn = Integer.parseInt(n);
        Task task = new Task (studentName, studentRoom, nn, b);
        final FirebaseFirestore ff = FirebaseFirestore.getInstance();
        DocumentReference dRef = ff.collection("laundryrooms").document(laundryRoom).collection("tasks").document();

        dRef.set(task).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                final DocumentReference dd = ff.collection("laundryrooms").document(laundryRoom);
                dd.update("numBuckets",FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        int b = Integer.parseInt(displayBucketQueue.getText().toString().trim());
                        b++;
                        displayBucketQueue.setText("" + b);

                        Toast.makeText(getContext(), "Request sent", Toast.LENGTH_SHORT).show();

                        SUBMIT_TASK_FRAGMENT.openRequestSentFragment();


                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

                /*  dd.runTransaction(new Transaction.Function<Void>(){
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {


                        return null;
                    }
                });*/

                return;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });

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
