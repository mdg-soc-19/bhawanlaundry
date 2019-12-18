package com.example.appone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RoomSelectFragment extends Fragment {

    View view;
    TextView helloStudent, roomText;
    String name, room;
    DocumentReference dRef;
    StudentUser sUser;
    Button btnAxEnter, btnBEnter;
    StudentMainActivity studentMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_room_select, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dRef = FirebaseFirestore.getInstance().collection("students").document(user.getDisplayName());


        helloStudent = view.findViewById(R.id.HELLO_STUDENT);
        roomText = view.findViewById(R.id.ROOM_TEXT);
        btnAxEnter = view.findViewById(R.id.ENTER_Ax_ROOM_BUTTON);
        btnBEnter = view.findViewById(R.id.ENTER_B_ROOM_BUTTON);
        studentMainActivity = new StudentMainActivity();

        dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                   sUser = documentSnapshot.toObject(StudentUser.class);
                   name = sUser.getName();
                   room = sUser.getRoom();
                   helloStudent.setText("Hello " + name + "!");
                   roomText.setText("Room " + room);
                    }
                else{
                    helloStudent.setText("Fail");
                }
            }
        });

        //B BUTTON ONCLICK
        btnBEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentMainActivity.enterRoom(true);
            }
        });

        //Ax BUTTON ONCLICK
        btnAxEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentMainActivity.enterRoom(false);
            }
        });

        return view;
    }



}
