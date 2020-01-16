package com.example.android.bhawanlaundry;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LaundrymanRoomSelect extends Fragment {

    laundrymanRoomSelectCI LAUNDRYMAN_ROOM_SELECT_INTERFACE;
    Button laundryman_enter_Ax, laundryman_enter_B;
    FirebaseFirestore ff;

    ProgressBar progressBar;
    FirebaseAuth mAuth;
    StudentUser studentUser;
    TextView helloLaundryman;
    static String name, room;
    Button signOut;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_laundryman_room_select, container, false);

        laundryman_enter_Ax = view.findViewById(R.id.LAUNDRYMAN_ENTER_Ax_BUTTON);
        laundryman_enter_B = view.findViewById(R.id.LAUNDRYMAN_ENTER_B_BUTTON);
        progressBar = view.findViewById(R.id.PROGRESS_CIRCULAR_LAUNDRYMAN_ROOM_SELECT);
        helloLaundryman = view.findViewById(R.id.HELLO_LAUNDRYMAN);
        signOut = view.findViewById(R.id.SIGN_OUT_BUTTON_LAUNDRYMAN);

        ff = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user ==  null){
            LAUNDRYMAN_ROOM_SELECT_INTERFACE.openLoginScreen();
            return view;
        }

        if(user.getDisplayName() == null || user.getDisplayName().isEmpty()){
            LAUNDRYMAN_ROOM_SELECT_INTERFACE.openProfileSetUp();
            return view;
        }

        DocumentReference dRef = ff.collection("students").document(user.getDisplayName());
        progressBar.setVisibility(View.VISIBLE);
        dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.GONE);
                studentUser = documentSnapshot.toObject(StudentUser.class);

                name = studentUser.getName();
                room = studentUser.getRoom();
                helloLaundryman.setText("Hello " + name + "!");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        laundryman_enter_Ax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
               DocumentReference d = ff.document("laundryrooms/Ax");

                d.update("laundrymanPresent", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        LAUNDRYMAN_ROOM_SELECT_INTERFACE.openLaundrymanRoom(false);
                    }
                });

            }
        });

        laundryman_enter_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference d = ff.document("laundryrooms/B");

                d.update("laundrymanPresent", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        LAUNDRYMAN_ROOM_SELECT_INTERFACE.openLaundrymanRoom(true);
                    }
                });

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    MainActivity.lockDrawer();
                    LAUNDRYMAN_ROOM_SELECT_INTERFACE.openLoginScreen();

                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        LAUNDRYMAN_ROOM_SELECT_INTERFACE = (laundrymanRoomSelectCI) activity;
    }

    public interface laundrymanRoomSelectCI {
            void openLaundrymanRoom(boolean isB);
            void openLoginScreen();
            void openProfileSetUp();

    }
}
